#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <semaphore.h>

#define MSGLEN 128
#define NPHILO 5
#define THINK_LOOP 100000000
#define EAT_LOOP 50000000

void *philo(void *arg);            //Pointer auf die Philosophenfunktion
int stickCond[NPHILO];             //Zustandsarray der Staebchen 0->frei 1->belegt
pthread_mutex_t mutex;             //Mutex fuer das Probieren  die Staebchen aufzunehmen
pthread_mutex_t mutexPrint;        //Mutex fuer das Drucken von Ausgaben
pthread_cont_t condBlock;          //Condition für das benutzergesteuerte Blockieren
pthread_cond_t condStick[NPHILO];  //Condition-Array fuer Staebchen-Blockieren
pthread_barrier_t barrierSingle;   //Barriere fuer das sichere Erstellen der einzelnen Philosophen
pthread_barrier_t barrierAll;      //Barriere fuer das sichere Erstellen aller Philosophen.
char command[NPHILO];
char state[NPHILO];
int err;

void printStates(int n, char ch) {
  pthread_mutex_lock(&mutexPrint);
  state[n] = ch;
  int i;
  printf("\n");
  for(i = 0; i < NPHILO;i++) {
    printf("%d %c   ", i, state[i]);
  }
  for(i = 0; i < NPHILO; i++) {
    if(state[i] == 'E' && state[(i+1)%NPHILO] == 'E') {
	perror("Ungueltiger Zustand");
        exit(EXIT_FAILURE);
      }
  }
  pthread_mutex_unlock(&mutexPrint);
}

void getSticks(int no) {
  printStates(no, 'H');

  pthread_mutex_lock(&mutex);
  while(stickCond[no%NPHILO] != 0 || stickCond[(no+1)%NPHILO] != 0) {
    pthread_cond_wait(&condStick[no], &mutex);
  }
  //Kritischer Abschnitt beginnt
  stickCond[no%NPHILO] = 1;
  stickCond[(no+1)%NPHILO] = 1;
  //Kritischer Abschnit endet
  pthread_mutex_unlock(&mutex);
  
  printStates(no, 'E');
}

void putSticks(int no) {
  pthread_mutex_lock(&mutex);

  stickCond[no%NPHILO] = 0;
  stickCond[(no+1)%NPHILO] = 0;
  pthread_cond_signal(&condStick[(no-1 < 0 ? NPHILO-1 : no-1)]);
  pthread_cond_signal(&condStick[(no+1)%NPHILO]);

  printStates(no, 'T');
  pthread_mutex_unlock(&mutex);

}


void *philo(void *arg) {
  int philono = *((int *) arg);
  int i;
  pthread_barrier_wait(&barrierSingle);
  printf("\nHello, I am Philosoph no %d\n", philono);
  state[philono] = 'T';
  pthread_barrier_wait(&barrierAll);

  while(command[philono] != 'q') {
    //Thinkloop
    for(i = 0; i < THINK_LOOP;i++) {
      if(command[philono] == 'b') {
        pthread_cond_wait(&condBlock[philono]);
      }
    };
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
  char ch;

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

  err = pthread_barrier_init(&barrierSingle, NULL, 2);
  if(err != 0) {
    perror("\n Barrier initialisation failed\n");
    exit(EXIT_FAILURE);
  }

  err = pthread_barrier_init(&barrierAll, NULL, NPHILO+1);
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
    pthread_barrier_wait(&barrierSingle);
  }
  pthread_barrier_wait(&barrierAll);

  while(ch != 'q'){
    ch = _getch();
    if(ch == 'q') {
      for(i = 0; i < NPHILO; i++) {
        command[i] = ch;
      }
    }
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

  sem_destroy(&condBlock);
  sem_destroy(&condStick);
  return 0;
}
