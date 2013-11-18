#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <semaphore.h>
#include <ncurses.h>

#include "monitor.h"


//Schreibt Statusaenderungen auf den Bildschirm und aktuallisiert diese im state-Array
//int n   -> Nummer des Philosophen
//char ch -> Neuer Status
void changeAndDisplayStates(int n, char ch) {
  pthread_mutex_lock(&mutexPrint);                        //Speeren auf den Print-Mutex um zu verhinden, dass zwei Prozesse Gleichzeitig drucken
  state[n] = ch;                                          //Status aktuallisieren
  int i;                                                  //Zaehlvariable
  int err;
  lineCount = lineCount + 1;                              //Den Linienzaehler aktualisieren 
  if(lineCount >= CONSOLESIZEY-1) {                       //Wenn der linienzaehler groesser als die Konsole ist
    err = clear();                                              //dann loesche die Konsole
    if(err != 0){
     perror("\n clear failed \n"); 
     exit(EXIT_FAILURE);
    } 
    lineCount = 0;                                        //und setze den Linienzaehler auf 0
  }
  
  printw("\n");                                           //Drucke einen Zeilenumbruch
  for(i = 0; i < NPHILO;i++) {                            //Drucke den Status aller Philosophen
    printw("%d %c   ", i, state[i]);
  }
  err = refresh();                                              //Aktualisiere die Ausgabe
     if(err != 0){
     perror("\n refresh failed \n"); 
     exit(EXIT_FAILURE);
    } 
    
  for(i = 0; i < NPHILO; i++) {                           //Pruefe fuer jeden Philosophenstatus
    if(state[i] == EAT && state[(i+1)%NPHILO] == EAT) {   //ob zwei Philosophen nebeneinander essen
	perror("Ungueltiger Zustand");                    //Wenn ja -> Schreibe einen Fehler und beende das Programm
        exit(EXIT_FAILURE);
      }
  }
  pthread_mutex_unlock(&mutexPrint);                      //Gebe den Print-Mutex wieder frei
}


//Wartet bis das linke und das rechte Staebchen frei sind und belegt diese
//dann fuer den aufrufenden Philosophen
//int no -> Nummer des aufrufenden Philosophen
void getSticks(int no) {
  changeAndDisplayStates(no, HUNGRY);                                 //Druckt den veraenderten Status des Philosophen
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
  
  changeAndDisplayStates(no, EAT);                                    //Druckt den veraenderten Status des Philosophen
}


//Laesst den aufrufenden Philosophen seine Staebchen wieder weglegen und
//benachrichtigt seinen linken und rechten Nachbar darueber
//int no -> Nummer des aufrufenden Philosophen
void putSticks(int no) {
  pthread_mutex_lock(&mutex);                                     //Blockiere auf dem Philosophen-Mutex
  //Kritischer Abschnitt beginnt
  changeAndDisplayStates(no, THINKING);                           //Druck den veraenderten Philosphenstatus
  stickCond[no%NPHILO] = 0;                                       //Gebe das linke Staebchen frei
  stickCond[(no+1)%NPHILO] = 0;                                   //Gebe das rechte Staebchen frei
  pthread_cond_signal(&condStick[(no-1 < 0 ? NPHILO-1 : no-1)]);  //Gib dem linken Philosophen bescheid darueber
  pthread_cond_signal(&condStick[(no+1)%NPHILO]);                 //Gib dem rechten Philosophen bescheid darueber
  //Kritischer Abschnitt endet
  pthread_mutex_unlock(&mutex);                                   //Gib den Philosophen-Mutex wieder frei
}