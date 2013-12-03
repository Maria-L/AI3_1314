/* Accessfile for vmappl
 * File: vmaccess.h
 * Steffen Giersch, Maria Janna Martina Luedemann
 * 2013
 */

#include "vmaccess.h"


struct vmem_struct *vmem = NULL;

void vm_init(void) {
  kill(SIGUSR1, vmem->adm.mmanage_pid);
  sem_wait(&(vmem->adm.sema));
  vmem = shmat(SHMPROCID,0,0);
}


int vmem_read(int address) {
  if(vmem == NULL) {
    vm_init();
  }

  int pageNum = address / VMEM_PAGESIZE;                   //Nummer der Page

  if(!(vmem->pt.entries[pageNum].flags & PTF_PRESENT)) {   //Wenn die Page nicht Present ist
    vmem->adm.req_pageno = pageNum;                        //Speicher die aktuelle Seite in req_pageno
    kill(SIGUSR2, vmem->adm.mmanage_pid);                  //Signal, dass mmanage eine Seite laden muss
    sem_wait(&(vmem->adm.sema));                           //Warte auf Signal von mmanage
  }
                                                           //Berechne die Physische Adresse
  int physAddr = vmem->pt.entries[pageNum].frame * VMEM_PAGESIZE + (address % VMEM_PAGESIZE);
  return vmem->data[physAddr];
}


void vmem_write(int address, int data) {
  if(vmem == NULL) {
    vm_init();
  }

  int pageNum = address / VMEM_PAGESIZE;

  if(!(vmem->pt.entries[pageNum].flags & PTF_PRESENT)) {
    vmem->adm.req_pageno = pageNum;
    kill(SIGUSR2, vmem->adm.mmanage_pid);
    sem_wait(&(vmem->adm.sema));
  }

  vmem->pt.entries[pageNum].flags |= PTF_DIRTY;
  int physAddr = vmem->pt.entries[pageNum].frame * VMEM_PAGESIZE + (address % VMEM_PAGESIZE);
  vmem->data[physAddr] = data;
}
