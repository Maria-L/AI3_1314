#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <semaphore.h>

#define MSGLEN 128
#define NPHILO 5
#define THINK_LOOP 1000000000
#define EAT_LOOP 500000000

void *philo(void *arg);            //Pointer auf die Philosophenfunktion
int stickCond[NPHILO];             //Zustandsarray der Staebchen 0->frei 1->belegt
pthread_mutex_t mutex;             //Mutex für das Probieren  die Staebchen aufzunehmen
pthread_mutex_t mutexPrint;        //Mutex für das Drucken von Ausgaben
pthread_cond_t cond[NPHILO];       //Condition-Array
pthread_barrier_t barrier;         //Barriere für das sichere Erstellen der Philosophen
char* state[NPHILO];
int err;


void printStates() {
  pthread_mutex_lock(&mutexPrint);
  int i;
  printf("\n");
  for(i = 0; i < NPHILO;i++) {
    printf("%d %c   ", i, state[i]);
  }
  pthread_mutex_unlock(&mutexPrint);
}

void getSticks(int no) {
  state[no] = "HUNGRY";
  printStates();

  pthread_mutex_lock(&mutex);
  if(!(stickCond[no%NPHILO] == 0 && stickCond[(no+1)%NPHILO] == 0)) {
    pthread_cond_wait(&cond[no], &mutex);
  }
  //Kritischer Abschnitt beginnt
  stickCond[no%NPHILO] = 1;
  stickCond[(no+1)%NPHILO] = 1;
  //Kritischer Abschnit endet
  pthread_mutex_unlock(&mutex);
  state[no] = "EAT";
  printStates();
}

void putSticks(int no) {
  pthread_mutex_lock(&mutex);

  stickCond[no%NPHILO] = 0;
  stickCond[(no+1)%NPHILO] = 0;
  pthread_cond_signal(&cond[(no-1)%NPHILO]);
  pthread_cond_signal(&cond[(no+1)%NPHILO]);

  pthread_mutex_unlock(&mutex);
}


void *philo(void *arg) {
  int philono = *((int *) arg);
  int i;
  pthread_barrier_wait(&barrier);

  printf("\nHello, I am Philosoph no %d\n", philono);

  while(1) {
    //Thinkloop
    state[philono] = "THINK";
    printStates();
    for(i = 0; i < THINK_LOOP;i++) {};
    //Eatloop
    getSticks(philono);
    for(i = 0; i < EAT_LOOP;i++) {};
    putSticks(philono);
  }

  pthread_exit(NULL);
}

int main(void) {
  pthread_t tid[NPHILO] = {0};        //Array der Thread-IDs
  void *thread_result[NPHILO] = {0};  //Array der Thread-Ergebnisse
  int i;                              //Laufzeilenindex

  err = pthread_mutex_init(&mutex, NULL);
  if(err != 0) {
    perror("\n Semaphore initialisation failed\n");
    exit(EXIT_FAILURE);
  }

  err = pthread_mutex_init(&mutexPrint, NULL);
  if(err != 0) {
    perror("\n Semaphore initialisation failed\n");
    exit(EXIT_FAILURE);
  }

  err = pthread_barrier_init(&barrier, NULL, 2);
  if(err != 0) {
    perror("\n Barrier initialisation failed\n");
    exit(EXIT_FAILURE);
  }

  for(i = 0; i < NPHILO;i++) {
    err = pthread_create(&tid[i], NULL, philo, (void *) &i);
    if(err != 0) {
      perror("\n Thread creation failed\n ");
      exit(EXIT_FAILURE);
    }
    pthread_barrier_wait(&barrier);
  }

  for(i = 0; i < NPHILO;i++) {
    err = pthread_join(tid[i], &thread_result[i]);
    if(err != 0) {
      char msg[MSGLEN] = "";
      sprintf(msg, "\n Thread join for thread %d failed\n", i);
      perror(msg);
      exit(EXIT_FAILURE);
    }
    printf("\nThread %d joined\n", i);
  }

  sem_destroy(&cond);
  return 0;
}




