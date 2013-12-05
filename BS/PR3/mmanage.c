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
	  int free_space;    //Freie Seite im Phyischen Speicher
	  free_space = find_free_bit(vmem->adm.bitmap);  //Suche freien Platz im Physischen Speicher
	  
	  if(free_space < 0) {                           //Wenn keiner gefunden wurde
	    int to_delete;
	    to_delete = find_remove_clock();             //Suche eine Seite zum löschen
	    store_page(to_delete);                       //Speichere diese in die pagefile
	    free_space = to_delete;                      //Diese Seite kann nun im physischen Speicher überschrieben werden
	  }
	  
	  fetch_page(free_space);
	  
	  pthread_cond_signal(&(vmem->adm.sema));
	  
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
}

void vmem_init(void) {
  int err;
  int i;

  shm_key = ftok(SHMKEY, 1);
  shm_id = shmget(shm_key, SHMSIZE, IPC_CREAT | 0666);
  vmem = shmat(shm_id, 0, 0);

  vmem->adm.size = VMEM_PHYSMEMSIZE;
  vmem->adm.mmanage_pid = getpid();
  vmem->adm.shm_id = shm_id;
  vmem->adm.req_pageno = -1;
  vmem->adm.next_alloc_idx = -1;
  vmem->adm.pf_count = 0;
  err = sem_init(&(vmem->adm.sema), 0, 0);
  if(err != 0) {
    perror("Semaphore initialisation failed");
  }
  
  for(i = 0; i < VMEM_NPAGES; i++) {
    vmem->pt.entries[i].flags = 0;
    vmem->pt.entries[i].frame = -1;
  }
  
  for(i = 0; i < VMEM_BMSIZE; i++) {
    vmem->adm.bitmap[i] = 0;
  }
  
  for(i = 0; i < VMEM_NFRAMES; i++) {
    vmem->pt.framepage[i] = -1;
  }
}

void cleanup(void) {
  shmdt(vmem);

  fclose(logfile);
  fclose(pagefile);
}

void init_pagefile(const char *fileName) {
  pagefile = fopen(MMANAGE_PFNAME, "r+w");
  if(!pagefile) {
        perror("Error creating pagefile");
        exit(EXIT_FAILURE);
  }
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

