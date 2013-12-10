/* Accessfile for vmappl
 * File: vmaccess.h
 * Steffen Giersch, Maria Janna Martina Luedemann
 * 2013
 */

#include "vmaccess.h"


struct vmem_struct *vmem = NULL;

void vm_init(void) {
  key_t shm_key;
  int shm_id;

  shm_key = ftok(SHMKEY, 1);                   //Generiere Key
  shm_id = shmget(shm_key, SHMSIZE, 0666);     //Finde den geteilten Speicher
  if(shm_id == -1) {
    perror("Error finding the Shared Memory");
    exit(EXIT_FAILURE);
    }
  vmem = shmat(shm_id, NULL, 0);               //Verbinde mit dem geteilten Speicher
  if(vmem == (void *) -1) {
    perror("Error attatching to shared Memory");
    exit(EXIT_FAILURE);
  } else {
    fprintf(stderr, "\nSuccessfully attatched to shared Memory");
  }
}


int vmem_read(int address) {
  if(vmem == NULL) {                                       //Wenn der geteilte Speicher noch nicht initialisiert ist
    vm_init();                                             //initialisiere ihn
  }

  int pageNum = address / VMEM_PAGESIZE;                   //Nummer der Page
  vmem->adm.req_pageno = pageNum;

  if(!(vmem->pt.entries[pageNum].flags & PTF_PRESENT)) {   //Wenn die Page nicht Present ist
    kill(vmem->adm.mmanage_pid, SIGUSR1);                  //Signal, dass mmanage eine Seite laden muss
    sem_wait(&(vmem->adm.sema));                           //Warte auf Signal von mmanage
  }
                                                           //Berechne die physische Adresse
  int physAddr = vmem->pt.entries[pageNum].frame * VMEM_PAGESIZE + (address % VMEM_PAGESIZE);
  if((vmem->pt.entries[pageNum].flags & PTF_USED) == 0) {  //Setze das passende Used-Flag
    vmem->pt.entries[pageNum].flags |= PTF_USED;
  } else {
    vmem->pt.entries[pageNum].flags |= PTF_USED1;
  }
  return vmem->data[physAddr];
}


void vmem_write(int address, int data) {
  if(vmem == NULL) {                                      //Wenn der geteilte Seicher noch nicht inisialisiert ist
    vm_init();                                            //initialisiere ihn
  }

  int pageNum = address / VMEM_PAGESIZE;
  vmem->adm.req_pageno = pageNum;                         //Nummer der Page
  
  if(!(vmem->pt.entries[pageNum].flags & PTF_PRESENT)) {  //Wenn die Page nicht Present ist
    kill(vmem->adm.mmanage_pid, SIGUSR1);                 //  Signal, dass mmanage diese Page laden muss
    sem_wait(&vmem->adm.sema);                            //  Warte auf Signal von mmanage zum weiter machen
     usleep(10000);                                        //  Unerklaerliche 10 ms Wartezeit damit das Programm
  }                                                       //    richtig funktioniert - Begruendung in Punkt xxx

  vmem->pt.entries[pageNum].flags |= PTF_DIRTY;           //Setze das Used, Used1 und/oder Dirty-Flag
  if((vmem->pt.entries[pageNum].flags & PTF_USED) == 0) {
    vmem->pt.entries[pageNum].flags |= PTF_USED;
  } else {
    vmem->pt.entries[pageNum].flags |= PTF_USED1;
  }                                                       //Berechne die physische Adresse
  int physAddr = vmem->pt.entries[pageNum].frame * VMEM_PAGESIZE + (address % VMEM_PAGESIZE);
  vmem->data[physAddr] = data;                            //Schreibe data in das Daten-Array
}
