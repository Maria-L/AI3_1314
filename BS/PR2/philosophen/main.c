#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <semaphore.h>
#include <ncurses.h>

#define MSGLEN 128
#define NPHILO 5
#define THINK_LOOP 1000000000
#define EAT_LOOP 500000000
#define CONSOLESIZEX 50
#define CONSOLESIZEY 40
#define ASCIICHARTOINTOFFSET 48

void *philo(void *arg);            //Pointer auf die Philosophenfunktion
int stickCond[NPHILO];             //Zustandsarray der Staebchen 0->frei 1->belegt
sem_t block[NPHILO];               //Semaphoren zum benutzergesteuerten Blockieren
pthread_mutex_t mutex;             //Mutex fuer das Probieren  die Staebchen aufzunehmen
pthread_mutex_t mutexPrint;        //Mutex fuer das Drucken von Ausgaben
pthread_cond_t condStick[NPHILO];  //Condition-Array fuer Staebchen-Blockieren
pthread_barrier_t barrierSingle;   //Barriere fuer das sichere Erstellen der einzelnen Philosophen
pthread_barrier_t barrierAll;      //Barriere fuer das sichere Erstellen aller Philosophen.
char command[NPHILO];
char state[NPHILO];
int err;
int lineCount;

void printStates(int n, char ch) {
  pthread_mutex_lock(&mutexPrint);
  state[n] = ch;
  int i;
  lineCount = lineCount + 1;
  if(lineCount >= CONSOLESIZEY) {
    clear();
    lineCount = 0;
  }
  
  printw("\n");
  for(i = 0; i < NPHILO;i++) {
    printw("%d %c   ", i, state[i]);
  }
  refresh();
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
  state[philono] = 'T';
  pthread_barrier_wait(&barrierAll);

  while(command[philono] != 'q') {
    //Thinkloop
    for(i = 0; i < THINK_LOOP;i++) {
      if(command[philono] == 'b') {
        sem_wait(&block[philono]);
      } else if(command[philono] == 'p') {
        command[philono] = ' ';
        break;
      }
    }
    //Eatloop
    getSticks(philono);
    for(i = 0; i < EAT_LOOP;i++) {
      if(command[philono] == 'b') {
        sem_wait(&block[philono]);
      }
    };
    putSticks(philono);
  }

  pthread_exit(NULL);
}

int main(void) {
  pthread_t tid[NPHILO] = {0};        //Array der Thread-IDs
  void *thread_result[NPHILO] = {0};  //Array der Thread-Ergebnisse
  int i;                              //Laufzeilenindex
  char ch;
  lineCount = 0;

  initscr();
  raw();
  noecho();
  resize_term(CONSOLESIZEY, CONSOLESIZEX);
  refresh();

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

  for(i = 0; i < NPHILO;i++) {
    err = sem_init(&block[i], 0, 0);
    if(err != 0) {
      perror("Semaphor initalisation failed");
      exit(EXIT_FAILURE);
    }
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
    ch = getch();
    if(ch == 'q') {
      for(i = 0; i < NPHILO; i++) {
        command[i] = ch;
      }
    } else if(ch == 'b') {
      i = getch() - ASCIICHARTOINTOFFSET;
      command[i] = 'b';
    } else if(ch == 'u') {
      i = getch() - ASCIICHARTOINTOFFSET;
      command[i] = 'u';
      sem_post(&block[i]);
    } else if(ch == 'p') {
      i = getch() - ASCIICHARTOINTOFFSET;
      command[i] = 'p';
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
  }

  for(i = 0; i < NPHILO; i++) {
    sem_destroy(&block[i]);
  }
  //sem_destroy(&condStick);
  endwin();
  return 0;
}
