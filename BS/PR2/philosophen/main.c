#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <semaphore.h>

#define NPHILO 5
#define THINK_LOOP 1000000000
#define EAT_LOOP 500000000

void *philo(void *arg);            //Pointer auf die Philosophenfunktion
int stickCond[NPHILO];             //Zustandsarray der Staebchen 0->frei 1->belegt
pthread_mutex_t stickSem[NPHILO];  //Array der Staebchen-Mutexe
pthread_cond_t cond[NPHILO];       //Condition-Array
pthread_barrier_t barrier;         //Barriere für das sichere Erstellen der Philosophen
int err;

int main(void) {
  pthread_t tid[NPHILO] = {0};
  void *thread_result[NPHILO] = {0};
  int i;

  for(i = 0; i < NPHILO;i++) {
    err = pthread_mutex_init(&(stickSem[i]), NULL);
    if(err != 0) {
      perror("Semaphore initialisation failed");
      exit(EXIT_FAILURE);
    }
  }

  err = pthread_barrier_init(&barrier, NULL, 2);
  for(i = 0; i < NPHILO;i++) {
    err = pthread_create(&tid[i], NULL, philo###################)
  }

  printf("Hello World\n");
  sem_destroy(&stickSem);
  return 0;
}
