/* Description: Memory Manager BSP3*/
/* Prof. Dr. Wolfgang Fohl, HAW Hamburg */
/* Winter 2010/2011
 * 
 * This is the memory manager process that
 * works together with the vmaccess process to
 * mimic virtual memory management.
 *
 * The memory manager process will be invoked
 * via a SIGUSR1 signal. It maintains the page table
 * and provides the data pages in shared memory
 *
 * This process is initiating the shared memory, so
 * it has to be started prior to the vmaccess process
 *
 * TODO:
 * currently nothing
 * */

/* 
 *Inhalt von pagefile.bin wie folgt gegliedert
 *',' zwischen zwei Zahlen
 * '\n' nach jeder Page
 * 
 * Beispiel:
 * 
 * "11,12,13,14\n
 * 21,22,23,24\n
 * 31,32,33,34\n
 * 41,42,43,44\n"
 *
 */

#include "mmanage.h"

struct vmem_struct *vmem = NULL;
FILE *pagefile = NULL;
FILE *logfile = NULL;
int signal_number = 0;          /* Received signal */
key_t shm_key;
int shm_id;

int
main(void)
{
    struct sigaction sigact;

    /* Init pagefile */
    init_pagefile(MMANAGE_PFNAME);

    /* Open logfile */
    logfile = fopen(MMANAGE_LOGFNAME, "w");
    if(!logfile) {
        perror("Error creating logfile");
        exit(EXIT_FAILURE);
    }

    /* Create shared memory and init vmem structure */
    vmem_init();
    if(!vmem) {
        perror("Error initialising vmem");
        exit(EXIT_FAILURE);
    }
    else {
        fprintf(stderr, "vmem successfully created\n");
    }

    /* Setup signal handler */
    /* Handler for USR1 */
    sigact.sa_handler = sighandler;
    sigemptyset(&sigact.sa_mask);
    sigact.sa_flags = 0;
    if(sigaction(SIGUSR1, &sigact, NULL) == -1) {
        perror("Error installing signal handler for USR1");
        exit(EXIT_FAILURE);
    }
    else {
        fprintf(stderr, "USR1 handler successfully installed\n");
    }

    if(sigaction(SIGUSR2, &sigact, NULL) == -1) {
        perror("Error installing signal handler for USR2");
        exit(EXIT_FAILURE);
    }
    else {
        fprintf(stderr, "USR2 handler successfully installed\n");
    }

    if(sigaction(SIGINT, &sigact, NULL) == -1) {
        perror("Error installing signal handler for INT");
        exit(EXIT_FAILURE);
    }
    else {
        fprintf(stderr, "INT handler successfully installed\n");
    }

    /* Signal processing loop */
    while(1) {
        signal_number = 0;
	fprintf(stderr, "Warte auf Signal\n");
        pause();
        if(signal_number == SIGUSR1) {              /* Page fault */
          signal_number = 0;
        }
        else if(signal_number == SIGUSR2) {         /* PT dump */
            fprintf(stderr, "Processed SIGUSR2\n");
            signal_number = 0;
        }
        else if(signal_number == SIGINT) {          /* Exit Program */
	    cleanup();
            fprintf(stderr, "Processed SIGINT\n");
	    break;
	}
    }
    return 0;
}

/* Your code goes here... */
void sighandler(int signo) {
  signal_number = signo;
  if(signo == SIGUSR1) {
    page_fault();
  } else if(signo == SIGUSR2) {
    //make_dump();
  }
  sem_post(&(vmem->adm.sema));                   //Gib vmaccess das Signal zum weiter machen
}

void page_fault(void) {
  struct logevent log;
  vmem->adm.pf_count += 1;
  log.pf_count = vmem->adm.pf_count;
  log.g_count = vmem->adm.pf_count;
  log.req_pageno = vmem->adm.req_pageno;
  log.replaced_page = NULLPAGE;
  int free_frame;
  int to_delete;
  
  free_frame = find_free_frame();                //Suche freien Platz im Physischen Speicher
	  
  if(free_frame < 0) {                           //Wenn keiner gefunden wurde
    to_delete = find_remove_clock2();              //    Suche einen Frame zum löschen
    log.replaced_page = vmem->pt.framepage[to_delete];
    store_page(to_delete);                       //    Speichere diese in die pagefile
    free_frame = to_delete;                      //    Diese Seite kann nun im physischen Speicher überschrieben werden
  }
  log.alloc_frame = free_frame;
  fetch_page(free_frame);                        //Überschreibe die alte Page mit der benötigten aus der pagef
  
  logger(log);
  fprintf(stderr, "Processed SIGUSR1 - loaded PageNr %d\n", vmem->adm.req_pageno);
}

void vmem_init(void) {
  int err;
  int i;

  shm_key = ftok(SHMKEY, 1);
  shm_id = shmget(shm_key, SHMSIZE, IPC_EXCL | 0666);
  vmem = shmat(shm_id, 0, 0);

  vmem->adm.size = VMEM_PHYSMEMSIZE;
  vmem->adm.mmanage_pid = getpid();
  vmem->adm.shm_id = shm_id;
  vmem->adm.req_pageno = -1;
  vmem->adm.next_alloc_idx = 0;
  vmem->adm.pf_count = 0;
  err = sem_init(&(vmem->adm.sema), 1, 1);
  if(err != 0) {
    perror("Semaphore initialisation failed");
  }
  //err = pthread_mutex_init(&vmem->adm.lock);
  
  for(i = 0; i < VMEM_NPAGES; i++) {
    vmem->pt.entries[i].flags = 0;
    vmem->pt.entries[i].frame = NULLPAGE;
  }
  
  /*for(i = 0; i < VMEM_BMSIZE; i++) {
    vmem->adm.bitmap[i] = 0;
  }*/
  
  for(i = 0; i < VMEM_NFRAMES; i++) {
    vmem->pt.framepage[i] = NULLPAGE;
  }
}

int find_free_frame(void) {
  int i;
  for(i = 0; i < VMEM_NFRAMES; i++) {
    if(vmem->pt.framepage[i] == NULLPAGE) {
      return i;
    }
  }
  return -1;
}

void store_page(int pt_index) {
  int old_page = vmem->pt.framepage[pt_index];
  vmem->pt.entries[old_page].flags &= ~(PTF_PRESENT);
  
  if(vmem->pt.entries[old_page].flags & PTF_DIRTY) {                          //Wenn die Page verändert wurde
    fseek(pagefile, sizeof(int) * VMEM_PAGESIZE * old_page, SEEK_SET);          //Setze den Filepointer auf die gewünschte Position
    fwrite(&vmem->data[VMEM_PAGESIZE * pt_index], sizeof(int), VMEM_PAGESIZE, pagefile); //Schreibe die Daten
    rewind(pagefile);
  }
}

void fetch_page(int pt_index) { 
  int old_page = vmem->pt.framepage[pt_index];
  vmem->pt.entries[old_page].flags &= ~(PTF_PRESENT);
  
  fseek(pagefile, sizeof(int) * VMEM_PAGESIZE * vmem->adm.req_pageno, SEEK_SET);      //Setze den filepointer auf die gewünschte Position
  fread(&vmem->data[VMEM_PAGESIZE * pt_index], sizeof(int), VMEM_PAGESIZE, pagefile); //Lese die Daten aus
  rewind(pagefile);
  
  vmem->pt.entries[vmem->adm.req_pageno].flags |= PTF_PRESENT | PTF_USED | PTF_USED1;  //Setze das Present, Used und Used1 Flag auf 1
  vmem->pt.entries[vmem->adm.req_pageno].flags &= ~(PTF_DIRTY);                        //Setze das Dirty-Flag auf 0
  vmem->pt.entries[vmem->adm.req_pageno].frame = pt_index;                             //Aktualisiere den Speicherort
  vmem->pt.framepage[pt_index] = vmem->adm.req_pageno;
}

int find_remove_fifo(void) {
  int idx = vmem->adm.next_alloc_idx;
  vmem->adm.next_alloc_idx = (vmem->adm.next_alloc_idx + 1) % VMEM_NFRAMES;
  return idx;
}

int find_remove_clock(void) {
  while(1) {
    if((vmem->pt.entries[vmem->pt.framepage[vmem->adm.next_alloc_idx]].flags & PTF_USED) == 0) {     //Wenn das Used-Bit nicht gesetzt ist
      int idx = vmem->adm.next_alloc_idx;                                        //    Speichere diesen Index
      vmem->adm.next_alloc_idx = (vmem->adm.next_alloc_idx + 1) % VMEM_NFRAMES;  //    Schalte den globalen Index um einen weiter
      return idx;                                                                //    Gib den vorherigen Index zurück
    } else {                                                                     //Sonst
      vmem->pt.entries[vmem->pt.framepage[vmem->adm.next_alloc_idx]].flags &= ~(PTF_USED);           //    Setze das Used-Bit auf 0
      vmem->adm.next_alloc_idx = (vmem->adm.next_alloc_idx + 1) % VMEM_NFRAMES;  //    Schalte den Index einen weiter
    }
  }
}

int find_remove_clock2(void) {
  while(1) {
    if((vmem->pt.entries[vmem->pt.framepage[vmem->adm.next_alloc_idx]].flags & PTF_USED) == 0) {
      int idx = vmem->adm.next_alloc_idx;                                        //    Speichere diesen Index
      vmem->adm.next_alloc_idx = (vmem->adm.next_alloc_idx + 1) % VMEM_NFRAMES;  //    Schalte den globalen Index um einen weiter
      return idx;
    } else if((vmem->pt.entries[vmem->pt.framepage[vmem->adm.next_alloc_idx]].flags & PTF_USED1) == 1){ //Wenn PTF_USED1 == 1
      vmem->pt.entries[vmem->pt.framepage[vmem->adm.next_alloc_idx]].flags &= ~(PTF_USED1);             //    Setze PTF_USED1 = 0
      vmem->adm.next_alloc_idx = (vmem->adm.next_alloc_idx + 1) % VMEM_NFRAMES;                         //    Schalte den globalen Index um einen weiter
    } else {                                                                                            //Ansonsten
      vmem->pt.entries[vmem->pt.framepage[vmem->adm.next_alloc_idx]].flags &= ~(PTF_USED);              //    Setze PTF_USED1 = 0
      vmem->adm.next_alloc_idx = (vmem->adm.next_alloc_idx + 1) % VMEM_NFRAMES;                         //    Schalte den globalen Index um einen weiter
    }
  }
}

void cleanup(void) {
  shmdt(vmem);

  fclose(logfile);
  fclose(pagefile);
}

void init_pagefile(const char *fileName) {
  
  int array_size = VMEM_NPAGES * VMEM_PAGESIZE;
  int data[array_size];
  
  int i;
  for(i = 0; i < array_size; i++) {
    data[i] = 0;
  }
  printf("\nuntil the remove");
  //remove(MMANAGE_PFNAME);
  pagefile = fopen(MMANAGE_PFNAME, "r+w");
  fwrite(data, sizeof(int), array_size, pagefile);
}


/* Do not change!  */
void
logger(struct logevent le)
{
    fprintf(logfile, "Page fault %10d, Global count %10d:\n"
            "Removed: %10d, Allocated: %10d, Frame: %10d\n",
            le.pf_count, le.g_count,
            le.replaced_page, le.req_pageno, le.alloc_frame);
    fflush(logfile);
}

