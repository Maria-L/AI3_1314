/*Headerdatei für die Philosophen
*Autoren: Birger Kamp, Jan Spijker
*/
#include <pthread.h>
#include <assert.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <semaphore.h>

#define NULLINT 0
#define NPHILO 15
#define THINK_LOOP 1000000
#define EAT_LOOP 500000
#define IN_USE 1
#define FREE 0
#define TRUE 1
#define FALSE 0
#define THINK 'T'
#define HUNGRY 'H'
#define EAT 'E'
#define DISTANCE_TO_NEXT_NEIGHBOR 1
#define DISTANCE_TO_NEXT_STICK 1
#define COMMAND_FOR_QUIT1 'q'
#define COMMAND_FOR_QUIT2 'Q'
#define COMMAND_TO_BLOCK 'b'
#define COMMAND_TO_UNBLOCK 'u'
#define COMMAND_TO_PROCEED 'p'
#define COMMAND_EMPTY 'a'
#define COMMAND_SWITCH_DEBUGGING 'd'

void *philo(void *arg);			//Pointer auf die philo-Funktion
void get_sticks(int philo_key);  //Aufnehmen der Stäbchen
void think(int key);
void eat(int philo_key);         //Philosoph isst
void put_sticks(int philo_key);  //Weglegen der Stäbchen
void disp_philo_states();        //Zeigt den Status aller Philosophen an
int left_stick(int key);
int right_stick(int key);
int left_neighbor(int key);
int right_neighbor(int key);
int neighbors_are_eating();
void check_commands(int key);
void debugging_message(char message[],int key);