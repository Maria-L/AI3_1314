#include <stdio.h>
#include <sys/ipc.h>
#include <sys/shm.h>

#define MAXMEM 30

int main(int argc, char **argv) {
  int shmID = shmget("/vmem.h", MAXMEM, 0);
  int *shmPTR = shmat(shmID, 0, 0);
  printf("\nID des Speichers: %c\n", shmID);
  shmdt(shmPTR);

  return 0;
}
