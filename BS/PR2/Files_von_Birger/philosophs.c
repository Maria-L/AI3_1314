/*
 * eatingPhilosophs.c
 *
 *  Created on: 07.11.2013
 *      Author: Birger Kamp, Jan Spijker
 */
#include "philosophs.h"

int philo_states[NPHILO] = { '-' };       //Für die derzeitigen Stati jedes Philosophen
int philo_states_restore[NPHILO] = { 0 };
int sticks[NPHILO] = { 0 };             //States of all sticks
pthread_cond_t philo_conditions[NPHILO];//Conditionsvariables of the philosophs
pthread_mutex_t stick_mutex;     //Mutex der den Zugriff auf die Sticks regelt
pthread_mutex_t print_mutex;    //Mutex der den Zugriff auf den Printstream regelt
pthread_barrier_t barrierSingle;
pthread_barrier_t barrierAll;
char philo_commands[NPHILO] = { COMMAND_EMPTY };
sem_t philo_sem[NPHILO];
int DEBUGGING = TRUE;

void *philo(void *arg){
    int key = *((int *) arg);
    pthread_barrier_wait(&barrierSingle);
    debugging_message("Philosoph# %i is in da hood!",key);
    pthread_barrier_wait(&barrierAll);
    while(TRUE){
        think(key);
        get_sticks(key);
        eat(key);
        put_sticks(key);
    }
}

void think(int key){
    debugging_message("Philosoph# %i starts to think.",key);
    philo_states[key] = THINK; //Zustand 1 steht für das Denken
    disp_philo_states();
    int i;
    for( i = 0; i <= THINK_LOOP ; i++ ){
        //Einfach nur warten, warten, warten, warten,....
        //Oh guck mal ein Eichhörnchen....
        check_commands(key);
        if(philo_commands[key] == COMMAND_TO_PROCEED){
            debugging_message("Loop of Thread# %i was proceeded.",key);
            philo_commands[key] = COMMAND_EMPTY;
        }
    }
    debugging_message("Philosoph# %i ends to think.",key);
}

void get_sticks(int key){
    debugging_message("Philosoph# %i gets hungry, take care of your fingers.",key);
    philo_states[key] = HUNGRY;  //State 2 means that he is hungry, poor guy
    disp_philo_states();
    pthread_mutex_lock(&stick_mutex);
    while(sticks[left_stick(key)] != FREE || sticks[right_stick(key)] != FREE ){
        debugging_message("Thread will be blocked, because of at least one stick is in use.",NULLINT);
        pthread_cond_wait(&(philo_conditions[key]),&stick_mutex);
        debugging_message("Thread will be unblocked, sticks are now free.",NULLINT);
    }
    sticks[left_stick(key)] = IN_USE;
    sticks[right_stick(key)] = IN_USE;
    pthread_mutex_unlock(&stick_mutex);
}

void eat(int key){
    debugging_message("Philosoph# %i starts to eat.",key);
    philo_states[key] = EAT;  //State 3 means he is eating, lucky guy, enjoy
    disp_philo_states();
    int i;
    for( i = 0 ; i < EAT_LOOP ; i++ ){
        check_commands(key);
        if(philo_commands[key] == COMMAND_TO_PROCEED){
            debugging_message("Loop of Thread# %i was proceeded.",key);
            philo_commands[key] = COMMAND_EMPTY;
        }
    }
    debugging_message("Philosoph# %i ends eating.",key);
}

void put_sticks(int key){
    debugging_message("Philosoph# %i wants to put sticks down.",key);
    pthread_mutex_lock(&stick_mutex);
    sticks[left_stick(key)] = FREE;
    sticks[right_stick(key)] = FREE;
    debugging_message("Philosoph# %i put sticks down.\n",key);
    debugging_message("Informing his neighbours about the free sticks.",NULLINT);
    pthread_cond_signal(&philo_conditions[left_neighbor(key)]);
    pthread_cond_signal(&philo_conditions[right_neighbor(key)]);
    philo_states[key] = THINK;
    pthread_mutex_unlock(&stick_mutex);
}

void disp_philo_states(){
    pthread_mutex_lock(&print_mutex);
    int i;
    for( i = 0 ; i < NPHILO ; i++ ){
        printf("%i %c\t",i,philo_states[i]);
    }
    if(neighbors_are_eating() == TRUE){
        printf("\n TAKE CARE!!! There are two neighbor philosophs eating. Check it out!\n");
        exit(EXIT_FAILURE);
    }
    printf("\n");
    pthread_mutex_unlock(&print_mutex);
}
void check_commands(int key){
    if(philo_commands[key] == COMMAND_FOR_QUIT1){
        debugging_message("Philosoph# %i is going to leave the table.",key);
        pthread_exit(0);
        debugging_message("Philosoph# %i left the table.",key);
    }
    if(philo_commands[key] == COMMAND_TO_BLOCK){
        philo_commands[key] = COMMAND_EMPTY;
        sem_wait(&philo_sem[key]);
    }
}

int left_stick(int key){
    return key % NPHILO;
}
int right_stick(int key){
    return (( key + DISTANCE_TO_NEXT_STICK ) % NPHILO);
}
int left_neighbor(int key){
    return (key-1 < 0 ? NPHILO-1 : key-1);
}
int right_neighbor(int key){
    return ((key + DISTANCE_TO_NEXT_NEIGHBOR) % NPHILO);
}
int neighbors_are_eating(){
    int i;
    for( i = 0 ; i < NPHILO ; i++ ){
        if( philo_states[i] == EAT && philo_states[right_neighbor(i)] == EAT ){
            return TRUE;
        }
    }
    return FALSE;
}
void debugging_message(char message[],int key){
    if(DEBUGGING == TRUE){
        pthread_mutex_lock(&print_mutex);
        printf(message,key);
        printf("\n");
        pthread_mutex_unlock(&print_mutex);
    }
}