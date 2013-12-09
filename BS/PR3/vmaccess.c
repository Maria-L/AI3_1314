/* Accessfile for vmappl
 * File: vmaccess.h
 * Steffen Giersch, Maria Janna Martina Luedemann
 * 2013
 */

#include "vmaccess.h"
key_t shm_key;
int shm_id;

struct vmem_struct *vmem = NULL;

void vm_init(void) {
  shm_key = ftok(SHMKEY, 1);
  shm_id = shmget(shm_key, SHMSIZE, 0666);
  if(shm_id == -1) {
    perror("Error finding the Shared Memory");
    exit(EXIT_FAILURE);
    }
  vmem = shmat(shm_id, NULL, 0);
  if(vmem == (void *) -1) {
    perror("Error attatching to shared Memory");
    exit(EXIT_FAILURE);
  } else {
    fprintf(stderr, "\nSuccessfully attatched to shared Memory");
  }
}


int vmem_read(int address) {
  if(vmem == NULL) {
    vm_init();
  }

  int pageNum = address / VMEM_PAGESIZE;                   //Nummer der Page
  vmem->adm.req_pageno = pageNum;

  if(!(vmem->pt.entries[pageNum].flags & PTF_PRESENT)) {   //Wenn die Page nicht Present ist
    kill(vmem->adm.mmanage_pid, SIGUSR1);                  //Signal, dass mmanage eine Seite laden muss
    sem_wait(&(vmem->adm.sema));                           //Warte auf Signal von mmanage
  }
                                                   //Berechne die Physische Adresse
  int physAddr = vmem->pt.entries[pageNum].frame * VMEM_PAGESIZE + (address % VMEM_PAGESIZE);
  if((vmem->pt.entries[pageNum].flags & PTF_USED) == 0) {
    vmem->pt.entries[pageNum].flags |= PTF_USED;
  } else {
    vmem->pt.entries[pageNum].flags |= PTF_USED1;
  }
  return vmem->data[physAddr];
}


void vmem_write(int address, int data) {
  if(vmem == NULL) {
    vm_init();
  }

  int pageNum = address / VMEM_PAGESIZE;
  vmem->adm.req_pageno = pageNum;
  
  if(!(vmem->pt.entries[pageNum].flags & PTF_PRESENT)) {
    kill(vmem->adm.mmanage_pid, SIGUSR1);
    sem_wait(&vmem->adm.sema);
    usleep(10000);
  }

  vmem->pt.entries[pageNum].flags |= PTF_DIRTY;           //Setze das Used, Used1 und Dirty-Flag
  if((vmem->pt.entries[pageNum].flags & PTF_USED) == 0) {
    vmem->pt.entries[pageNum].flags |= PTF_USED;
  } else {
    vmem->pt.entries[pageNum].flags |= PTF_USED1;
  }
  int physAddr = vmem->pt.entries[pageNum].frame * VMEM_PAGESIZE + (address % VMEM_PAGESIZE);
  vmem->data[physAddr] = data;                            //Schreibe data in das Daten-Array
}
