#include "vmaccess.h"


struct vmem_struct *vmem = NULL;

void vm_init(void) {
  //####Signal an mmanage, dass der Speicher initialisiert werden muss####
  vmem = shmat(SHMID,0,0);       //Herausfinden woher wir die ID bekommen sollen | SHMPROCID?!?
}


int vmem_read(int address) {
  int pageNum = address / VMEM_PAGESIZE;
  if(vmem->pt->entries[pageNum]->flags & PTF_PRESENT)
}
