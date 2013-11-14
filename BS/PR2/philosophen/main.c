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
char command[NPHILO];              //Commandoarray
char state[NPHILO];                //Statusarray der Philosophen
int lineCount;                     //Linienzähler für die Ausgabe via NCurses

//Schreibt Statusaenderungen auf den Bildschirm und aktuallisiert diese im state-Array
//int n   -> Nummer des Philosophen
//char ch -> Neuer Status
void printStates(int n, char ch) {
  pthread_mutex_lock(&mutexPrint);                        //Speeren auf den Print-Mutex um zu verhinden, dass zwei Prozesse Gleichzeitig drucken
  state[n] = ch;                                          //Status aktuallisieren
  int i;                                                  //Zaehlvariable
  lineCount = lineCount + 1;                              //Den Linienzaehler aktualisieren 
  if(lineCount >= CONSOLESIZEY) {                         //Wenn der linienzaehler größer als die Konsole ist
    clear();                                              //dann loesche die Konsole
    lineCount = 0;                                        //und setze den Linienzaehler auf 0
  }
  
  printw("\n");                                           //Drucke einen Zeilenumbruch
  for(i = 0; i < NPHILO;i++) {                            //Drucke den Status aller Philosophen
    printw("%d %c   ", i, state[i]);
  }
  refresh();                                              //Aktualisiere die Ausgabe
  for(i = 0; i < NPHILO; i++) {                           //Pruefe für jeden Philosophenstatus
    if(state[i] == 'E' && state[(i+1)%NPHILO] == 'E') {   //ob zwei Philosophen nebeneinander essen
	perror("Ungueltiger Zustand");                        //Wenn ja -> Schreibe einen Fehler und beende das Programm
        exit(EXIT_FAILURE);
      }
  }
  pthread_mutex_unlock(&mutexPrint);                      //Gebe den Print-Mutex wieder frei
}

//Wartet bis das linke und das rechte Staebchen frei sind und belegt diese
//dann fuer den aufrufenden Philosophen
//int no -> Nummer des aufrufenden Philosophen
void getSticks(int no) {
  printStates(no, 'H');                                               //Druckt den veraenderten Status des Philosophen
                                                                      //in die Konsole
  pthread_mutex_lock(&mutex);                                         //Blockiere auf dem Philosophen-Mutex
  while(stickCond[no%NPHILO] != 0 || stickCond[(no+1)%NPHILO] != 0) { //Wenn das linke oder das rechte Staebchen nicht frei sind
    pthread_cond_wait(&condStick[no], &mutex);                        //  dann gib den Mutex wieder frei und blockiere auf der Philosophen-Semaphore
  }                                                                   //  diese Blockierung endet wenn der linke oder rechte Philosoph ein Staebchen weglegt
  //Kritischer Abschnitt beginnt
  stickCond[no%NPHILO] = 1;                                           //Belege das linke Staebchen
  stickCond[(no+1)%NPHILO] = 1;                                       //Belege das rechte Staebchen
  //Kritischer Abschnit endet
  pthread_mutex_unlock(&mutex);                                       //Gib den Mutex wieder frei
  
  printStates(no, 'E');                                               //Druckt den veraenderten Status des Philosophen
}

//Laesst den aufrufenden Philosophen seine Staebchen wieder weglegen und
//benachrichtigt seinen linken und rechten Nachbar darueber
//int no -> Nummer des aufrufenden Philosophen
void putSticks(int no) {
  pthread_mutex_lock(&mutex);                                     //Blockiere auf dem Philosophen-Mutex

  stickCond[no%NPHILO] = 0;                                       //Gebe das linke Staebchen frei
  stickCond[(no+1)%NPHILO] = 0;                                   //Gebe das rechte Staebchen frei
  pthread_cond_signal(&condStick[(no-1 < 0 ? NPHILO-1 : no-1)]);  //Gib dem linken Philosophen bescheid darueber
  pthread_cond_signal(&condStick[(no+1)%NPHILO]);                 //Gib dem rechten Philosophen bescheid darueber

  printStates(no, 'T');                                           //Druck den veraenderten Philosphenstatus
  pthread_mutex_unlock(&mutex);                                   //Gib den Philosophen-Mutex wieder frei
}

//Philosophenthread.
//Dieser denkt, ist hungrig und isst.
// void *arg -> Soll der Nummer des Philosophen entsprechen
void *philo(void *arg) {
  int philono = *((int *) arg);             //Speichere die Nummer des Philosophen als philono ab
  int i;                                    //Zaehlvariable fuer den Philosophen
  pthread_barrier_wait(&barrierSingle);     //Wartet darauf, dass der main-thread fertig mit dem Erstellen ist
  state[philono] = 'T';                     //Setzt seinen Status auf 'T' -> Thinking
  pthread_barrier_wait(&barrierAll);        //Wartet darauf, dass alle Philosphen erstellt wurden

  while(command[philono] != 'q') {          //Solange dein Kommando nicht gleich 'q' -> Quit ist
    //Thinkloop
    for(i = 0; i < THINK_LOOP;i++) {        //  Fuer die Groesse des Think-Loops
      if(command[philono] == 'b') {         //    Wenn das Kommando = 'b' ist
        sem_wait(&block[philono]);          //      Mache ein sem_wait auf deiner Semaphore
      } else if(command[philono] == 'p') {  //    Wenn das Kommando = 'p' ist
        command[philono] = ' ';             //      Setze das Kommando auf ' '
        break;                              //      Springe aus dem Think-Loop
      }
    }
	
    //Eatloop
    getSticks(philono);                     //  Rufe getSticks auf um die Sticks fuer sich zu belegen
    for(i = 0; i < EAT_LOOP;i++) {          //  Fuer die Groesse des Think-Loops
      if(command[philono] == 'b') {         //    Wenn das Kommando = 'b' ist
        sem_wait(&block[philono]);          //      Mache ein sem_wait auf deiner Semaphore
      } else if(command[philono] == 'p') {  //    Wenn das Kommando = 'p' ist
        command[philono] = ' ';             //      Setze das Kommando auf ' '
        break;                              //      Springe aus dem Think-Loop
      }
    }
    putSticks(philono);                     //  Rufe putSticks auf um die Sticks wieder weg zu legen
  }

  pthread_exit(NULL);
}

int main(void) {
  pthread_t tid[NPHILO] = {0};        //Array der Thread-IDs
  void *thread_result[NPHILO] = {0};  //Array der Thread-Ergebnisse
  int i;                              //Laufzeilenindex
  char ch;                            //Character 
  int err;                            //Speicher für Ausgabewerte
  lineCount = 0;                      //Initialisiere den lineCount mit 0;

  initscr();                               //Initialisiere die Konsole für NCurses
  //raw();
  noecho();                                //Deaktiviere das automatische Ausgeben von Eingaben
  resize_term(CONSOLESIZEY, CONSOLESIZEX); //Definiere den Druckbereich der Konsole
  refresh();                               //Aktualisiere die Konsole

  err = pthread_mutex_init(&mutex, NULL);          //Initialisiere den Mutex für die Philosophen
  if(err != 0) {                                   //Pruefe auf einen Fehler bei der Initialisierung
    perror("\n Semaphore initialisation failed\n");
    exit(EXIT_FAILURE);
  }

  err = pthread_mutex_init(&mutexPrint, NULL);     //Initialisiere den Mutex für die Konsolenausgabe
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

  err = pthread_barrier_init(&barrierSingle, NULL, 2);  //Initialisiere die Barriere für das einzelne Erstellen von Philosophen
  if(err != 0) {
    perror("\n Barrier initialisation failed\n");       //Pruefe auf einen Fehler bei der Initialisierung
    exit(EXIT_FAILURE);
  }

  err = pthread_barrier_init(&barrierAll, NULL, NPHILO+1);  //Initialisiere die Barriere für das Gesamtheitliche Erstellen von Philosophen
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
  
  while(ch != 'q'){                        //Solange der Prozess nicht beendet werden soll
    ch = getch();                          //  Lese die Benutzereingabe ein. q -> Quit, b -> block, u -> unblock
    if(ch == 'q') {                        //  Wenn ch = q ist
      for(i = 0; i < NPHILO; i++) {        //    Dann schreibe bei jedem Philosophen das Kommando auf 'q'
        command[i] = ch;
      }
    } else if(ch == 'b') {                 //  Wenn ch = b ist
      i = getch() - ASCIICHARTOINTOFFSET;  //    Warte auf eine Zweite Benutzereingabe, die die Nummer des Philosophen sein muss
      command[i] = 'b';                    //    Schreibe dem jeweiligen Philosophen ein 'b' in die Kommandozeile
    } else if(ch == 'u') {                 //  wenn ch = u ist
      i = getch() - ASCIICHARTOINTOFFSET;  //    Warte auf eine Zweite Benutzereingabe, die die Nummer des Philosophen sein muss
      command[i] = 'u';                    //    Schreibe dem jeweiligen Philosophen ein 'u' in die Kommandozeile
      sem_post(&block[i]);                 //    Fuehre ein sem_post auf der Semaphore des geblockten Philosophen aus
    } else if(ch == 'p') {                 //  wenn ch = p ist
      i = getch() - ASCIICHARTOINTOFFSET;  //    Warte auf eine Zweite Benutzereingabe, die die Nummer des Philosophen sein muss
      command[i] = 'p';                    //    Schreibe dem jeweiligen Philosophen ein 'p' in die Kommandozeile
    }                                      //Prozess soll beendet werden
  }

  for(i = 0; i < NPHILO;i++) {                     //Fuer jeden Philosophen
    err = pthread_join(tid[i], &thread_result[i]); //warte darauf, dass der Philosph beendet
    if(err != 0) {                                 //Pruefe auf einen Fehler bei der Initialisierung
      char msg[MSGLEN] = "";
      sprintf(msg, "\n Thread join for thread %d failed\n", i);
      perror(msg);
      exit(EXIT_FAILURE);
    }
  }

  for(i = 0; i < NPHILO; i++) { //Zerstoere alle Synchronisationsobjekte
    sem_destroy(&block[i]);
	sem_destroy(&condStick[i]);
  }
  
  endwin();  //Beende die NCurses-Console
  return 0;
}
