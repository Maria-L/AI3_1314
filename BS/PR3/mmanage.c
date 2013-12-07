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
          fprintf(stderr, "Processed SIGUSR1\n");
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
  int free_frame;
  free_frame = find_free_frame();                //Suche freien Platz im Physischen Speicher
	  
  if(free_frame < 0) {                           //Wenn keiner gefunden wurde
    int to_delete;
    to_delete = find_remove_clock();             //    Suche eine Seite zum löschen
    store_page(to_delete);                       //    Speichere diese in die pagefile
    free_frame = to_delete;                      //    Diese Seite kann nun im physischen Speicher überschrieben werden
  }
  
  fetch_page(free_frame);                        //Überschreibe die alte Page mit der benötigten aus der pagef
  
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
  int frame_num = vmem->pt.entries[pt_index].frame;
  
  if(vmem->pt.entries[pt_index].flags & PTF_DIRTY) {                          //Wenn die Page verändert wurde
    fseek(pagefile, sizeof(int) * VMEM_PAGESIZE * pt_index, SEEK_SET);        //Setze den Filepointer auf die gewünschte Position
    fwrite(&vmem->data[VMEM_PAGESIZE * frame_num], sizeof(int), VMEM_PAGESIZE, pagefile); //Schreibe die Daten
    rewind(pagefile);
  }
}

void fetch_page(int pt_index) {
  /*fseek(pagefile, 0, SEEK_END);    //Setze den Filepointer an der Ende der Pagefile
  int size = ftell(pagefile);      //Speichere die Position (Filelänge) in size
  rewind(pagefile);                //Setze den Filepointer wieder an den Anfang der Pagefile
  
  char *buffer = malloc(size + 1); //Allokiere Speicher für den Inhalt der pagefile
  fread(buffer, size, 1, pagefile);
  
  char *reader = buffer;
  
  int i = 0;
  while(i < vmem->adm.req_pageno) {    //Wenn die richtige Zeile nicht gefunden ist
    while(*reader != '\n') {           //    Spule bis zur nächsten Zeile
      reader++;
    }
    reader++;
    i++;
  }
  
  for(i = 0; i < VMEM_PAGESIZE; i++) {    //Parse und schreibe aus der pagefile in data
    vmem->data[pt_index * VMEM_PAGESIZE + i] = readNum(reader);
  }
  
  free(buffer);                    //Gib den Allokierten Speicher wieder frei
  
  vmem->pt.entries[pt_index].flags |= PTF_PRESENT & PTF_USED & PTF_USED1;  //Setze das Present, Used und Used1 Flag auf 1
  vmem->pt.entries[pt_index].flags &= ~(PTF_DIRTY);                        //Setze das Dirty-Flag auf 0*/
  
  int frame_num = vmem->pt.entries[pt_index].frame;
  
  fseek(pagefile, sizeof(int) * VMEM_PAGESIZE * pt_index, SEEK_SET);       //Setze den filepointer auf die gewünschte Position
  fread(&vmem->data[VMEM_PAGESIZE * frame_num], sizeof(int), VMEM_PAGESIZE, pagefile); //Lese die Daten aus
  rewind(pagefile);
  
  vmem->pt.entries[pt_index].flags |= PTF_PRESENT | PTF_USED | PTF_USED1;  //Setze das Present, Used und Used1 Flag auf 1
  vmem->pt.entries[pt_index].flags &= ~(PTF_DIRTY);                        //Setze das Dirty-Flag auf 0
}

/*int readNum(char *reader) {
  int akku = 0;
  int potenz = 1;
  
  while(*reader != NEXTVALUE && *reader != NEXTPAGE) {
    reader++;
  }
  reader--;
  
  while(*reader != NEXTVALUE && *reader != NEXTPAGE) {
    akku += (*reader - 48) * potenz;
    potenz *= 10;
    reader--;
  }
  reader++;
  
  while(*reader != NEXTVALUE && *reader != NEXTPAGE) {
    reader++;
  }
  reader++;
  
  return akku;
}*/

int find_remove_clock(void) {
  while(1) {
    if((vmem->pt.entries[vmem->adm.next_alloc_idx].flags & PTF_USED) == 0) {     //Wenn das Used-Bit nicht gesetzt ist
      int idx = vmem->adm.next_alloc_idx;                                        //    Speichere diesn Index
      vmem->adm.next_alloc_idx = (vmem->adm.next_alloc_idx + 1) % VMEM_NFRAMES;  //    Schalte den globalen Index um einen weiter
      return idx;                                                                //    Gib den vorherigen Index zurück
    } else {                                                                     //Sonst
      vmem->pt.entries[vmem->adm.next_alloc_idx].flags &= ~(PTF_USED);           //    Setze das Used-Bit auf 0
      vmem->adm.next_alloc_idx = (vmem->adm.next_alloc_idx + 1) % VMEM_NFRAMES;  //    Schalte den Index einen weiter
    }
  }
}

void cleanup(void) {
  shmdt(vmem);

  fclose(logfile);
  fclose(pagefile);
}

void init_pagefile(const char *fileName) {
  /*int i;
  remove("MMANAGE_PFNAME");                   //Lösche die alte Pagefile
  pagefile = fopen(MMANAGE_PFNAME, "r+w");    //Öffne die neue Pagefile
  if(!pagefile) {
        perror("Error creating pagefile");
        exit(EXIT_FAILURE);
  }
  for(i = 0; i < VMEM_NPAGES; i++) {
    fputs("NEXTPAGE", pagefile);                    //Befülle die File mit \n für jede Page. Geplante Struktur im Dateikopf
  }*/
  
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

