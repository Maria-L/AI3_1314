#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <semaphore.h>
#include <ncurses.h>

#include "philo.h"
#include "monitor.h"


//Philosophenthread.
//Dieser denkt, ist hungrig und isst.
// void *arg -> Soll der Nummer des Philosophen entsprechen
void *philo(void *arg) {
  int philono = *((int *) arg);             //Speichere die Nummer des Philosophen als philono ab
  int i;                                    //Zaehlvariable fuer den Philosophen
  printf("\nPhilosoph %d ready to think!", philono);
  pthread_barrier_wait(&barrierSingle);     //Wartet darauf, dass der main-thread fertig mit dem Erstellen ist
  state[philono] = THINKING;                //Setzt seinen Status auf 'T' -> Thinking
  pthread_barrier_wait(&barrierAll);        //Wartet darauf, dass alle Philosphen erstellt wurden

  while(command[philono] != QUIT) {          //Solange dein Kommando nicht gleich 'q' -> Quit ist
    //Thinkloop
    for(i = 0; i < THINK_LOOP;i++) {            //  Fuer die Groesse des Think-Loops
      if(command[philono] == BLOCK) {           //    Wenn das Kommando = 'b' ist
        sem_wait(&block[philono]);              //      Mache ein sem_wait auf deiner Semaphore
      } else if(command[philono] == PROCEED) {  //    Wenn das Kommando = 'p' ist
        command[philono] = ' ';                 //      Setze das Kommando auf ' '
        break;                                  //      Springe aus dem Think-Loop
      }
    }
	
    //Eatloop
    getSticks(philono);                         //  Rufe getSticks auf um die Sticks fuer sich zu belegen
    for(i = 0; i < EAT_LOOP;i++) {              //  Fuer die Groesse des Think-Loops
      if(command[philono] == BLOCK) {           //    Wenn das Kommando = 'b' ist
        sem_wait(&block[philono]);              //      Mache ein sem_wait auf deiner Semaphore
      } else if(command[philono] == PROCEED) {  //    Wenn das Kommando = 'p' ist
        command[philono] = ' ';                 //      Setze das Kommando auf ' '
        break;                                  //      Springe aus dem Think-Loop
      }
    }
    putSticks(philono);                         //  Rufe putSticks auf um die Sticks wieder weg zu legen
  }

  pthread_exit(NULL);
}