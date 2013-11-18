#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <semaphore.h>
#include <ncurses.h>

#include "philo_global.h"
#include "monitor.h"
#include "philo.h"


int main(void) {
  pthread_t tid[NPHILO] = {0};        //Array der Thread-IDs
  void *thread_result[NPHILO] = {0};  //Array der Thread-Ergebnisse
  int i;                              //Laufzeilenindex
  char ch;                            //Character 
  int err;                            //Speicher fuer Ausgabewerte
  lineCount = 0;                      //Initialisiere den lineCount mit 0;

  err = initscr();                               //Initialisiere die Konsole fuer NCurses
  if(err != 0) {                                 //Pruefe auf einen Fehler bei der Initialisierung
    perror("\n NCruses initialisation failed\n");
    exit(EXIT_FAILURE);
  }
  
  err = noecho();                                //Deaktiviere das automatische Ausgeben von Eingaben
  if(err != 0) {                                   //Pruefe auf einen Fehler bei der Initialisierung
    perror("\n noecho failed\n");
    exit(EXIT_FAILURE);
  }
  
  err = resize_term(CONSOLESIZEY, CONSOLESIZEX); //Definiere den Druckbereich der Konsole
  if(err != 0) {                                   //Pruefe auf einen Fehler bei der Initialisierung
    perror("\n resice failed\n");
    exit(EXIT_FAILURE);
  }
  
  err = refresh();                               //Aktualisiere die Konsole
  if(err != 0) {                                   //Pruefe auf einen Fehler bei der Initialisierung
    perror("\n refresh failed\n");
    exit(EXIT_FAILURE);
  }
  
  err = pthread_mutex_init(&mutex, NULL);          //Initialisiere den Mutex fuer die Philosophen
  if(err != 0) {                                   //Pruefe auf einen Fehler bei der Initialisierung
    perror("\n Semaphore initialisation failed\n");
    exit(EXIT_FAILURE);
  }

  err = pthread_mutex_init(&mutexPrint, NULL);     //Initialisiere den Mutex fuer die Konsolenausgabe
  if(err != 0) {                                   //Pruefe auf einen Fehler bei der Initialisierung
    perror("\n Semaphore initialisation failed\n");
    exit(EXIT_FAILURE);
  }

  for(i = 0; i < NPHILO;i++) {                     //Initialisiere jede Philosophen-Semaphore zum Blockieren per Benutzersteuerung
    err = sem_init(&block[i], 0, 0);               
    if(err != 0) {                                 //Pruefe auf einen Fehler bei der Initialisierung
      perror("Semaphor initalisation failed");
      exit(EXIT_FAILURE);
    }
  }

  err = pthread_barrier_init(&barrierSingle, NULL, 2);  //Initialisiere die Barriere fuer das einzelne Erstellen von Philosophen
  if(err != 0) {
    perror("\n Barrier initialisation failed\n");       //Pruefe auf einen Fehler bei der Initialisierung
    exit(EXIT_FAILURE);
  }

  err = pthread_barrier_init(&barrierAll, NULL, NPHILO+1);  //Initialisiere die Barriere fuer das Gesamtheitliche Erstellen von Philosophen
  if(err != 0) {                                            //um zu verhindern, dass Gedruckt wird, bevor alle Philosophen einen Status haben
    perror("\n Barrier initialisation failed\n");           //Pruefe auf einen Fehler bei der Initialisierung
    exit(EXIT_FAILURE);
  }
  
  for(i = 0; i < NPHILO;i++) {                                //Initialisieren der einzelnen Philosophen
    err = pthread_create(&tid[i], NULL, philo, (void *) &i);
    if(err != 0) {                                            //Pruefe auf einen Fehler bei der Initialisierung
      perror("\n Thread creation failed\n ");
      exit(EXIT_FAILURE);
    }
    pthread_barrier_wait(&barrierSingle);                     //Lasse den Erstellten Philosophen und die main aufeinander warten
  }
  pthread_barrier_wait(&barrierAll);                          //Lasse alle Philosophen und die main gleichzeitig weiter machen
  
  while(ch != QUIT){                        //Solange der Prozess nicht beendet werden soll
    ch = getch();                           //  Lese die Benutzereingabe ein. q -> Quit, b -> block, u -> unblock
    if(ch == QUIT) {                        //  Wenn ch = q ist
      for(i = 0; i < NPHILO; i++) {         //    Dann schreibe bei jedem Philosophen das Kommando auf 'q'
        command[i] = ch;
	sem_post(&block[i]);
      }
    } else if(ch == BLOCK) {                 //  Wenn ch = b ist
      i = getch() - ASCIICHARTOINTOFFSET;    //    Warte auf eine Zweite Benutzereingabe, die die Nummer des Philosophen sein muss
      command[i] = BLOCK;                    //    Schreibe dem jeweiligen Philosophen ein 'b' in die Kommandozeile
    } else if(ch == UNBLOCK) {               //  wenn ch = u ist
      i = getch() - ASCIICHARTOINTOFFSET;    //    Warte auf eine Zweite Benutzereingabe, die die Nummer des Philosophen sein muss
      command[i] = UNBLOCK;                  //    Schreibe dem jeweiligen Philosophen ein 'u' in die Kommandozeile
      sem_post(&block[i]);                   //    Fuehre ein sem_post auf der Semaphore des geblockten Philosophen aus
    } else if(ch == PROCEED) {               //  wenn ch = p ist
      i = getch() - ASCIICHARTOINTOFFSET;    //    Warte auf eine Zweite Benutzereingabe, die die Nummer des Philosophen sein muss
      command[i] = PROCEED;                  //    Schreibe dem jeweiligen Philosophen ein 'p' in die Kommandozeile
    }
  }                                          //Prozess soll beendet werden

  for(i = 0; i < NPHILO;i++) {                     //Fuer jeden Philosophen
    err = pthread_join(tid[i], &thread_result[i]); //warte darauf, dass der Philosph beendet
    if(err != 0) {                                 //Pruefe auf einen Fehler bei der Initialisierung
      char msg[MSGLEN] = "";
      sprintf(msg, "\n Thread join for thread %d failed\n", i);
      perror(msg);
      exit(EXIT_FAILURE);
    }
  }
  
  //Zerstören aller Synchronisationsobjekte
  pthread_mutex_destroy(&mutex);
  pthread_mutex_destroy(&mutexPrint);
  pthread_barrier_destroy(&barrierSingle);
  pthread_barrier_destroy(&barrierAll);
  for(i = 0; i < NPHILO; i++) {
    sem_destroy(&block[i]);
    pthread_cond_destroy(&condStick[i]);
  }

  printw("\nAll threads have successfully terminated - ready to quit");
  getch();
  endwin();  //Beende die NCurses-Console
  return 0;
}
