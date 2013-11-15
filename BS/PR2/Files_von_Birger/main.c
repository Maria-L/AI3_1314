/*
*
*
*/
#include "philosophs.h"
#include "philosophs.c"

void check_error(char message[],int error);

int main()
{
    int error;
    int i;  //counter for any loops, nothing else!
    int amount_of_single_wall = 2;
    int amount_of_big_wall = NPHILO+1;
    int size_of_input = 10;
    printf("Start\n");
    pthread_t threads[NPHILO] = {0};
    void *thread_results[NPHILO] = {0};

    // INITIALIZE SYNCHRONIZING OBJECTS
    error = pthread_mutex_init(&stick_mutex,NULL);
    check_error("Mutex cant been initialised.",error);
    error = pthread_mutex_init(&print_mutex,NULL);
    check_error("Mutex cant been initialised.",error);
    error = pthread_barrier_init(&barrierSingle,NULL,amount_of_single_wall);
    check_error("Small wall cant be initialised.",error);
    error = pthread_barrier_init(&barrierAll,NULL,amount_of_big_wall);
    check_error("Big wall cant be initialised.",error);

    //iterate over number of philosophs...
    for( i = 0 ; i < NPHILO ; i++ ){
        //...and initialize that synchronizing objects which are arrays
        error = sem_init(&philo_sem[i],0,0);
        check_error("Semaphore cant been initialised.",error);

        debugging_message("ConditionVariable# %i will be created.",i);
        error = pthread_cond_init(&philo_conditions[i],NULL);
        check_error("Cant initialise Conditionvariable.",error);

        // CREATE PHILOSOPH-THREADS
        debugging_message("Thread# %i will be created.",i);
        error = pthread_create(&threads[i],NULL,philo,(void *) &i);
        check_error("Konnte Thread nicht erzeugen.",error);
        debugging_message("Thread# %i was been created.",i);
        //waiting until every thread is started and the next can be created
        pthread_barrier_wait(&barrierSingle);
    }
    //waiting for all thread until they cant start together their way
    pthread_barrier_wait(&barrierAll);

    // CHECK FOR INPUT ON CMD-LINE

    int condition = FALSE;  //Condition for accepting commands
    long int philo_to_change = NULLINT; //the philonumber who should be changed
    char input[size_of_input];  //the input command
    char *last_command = NULL;  //the filtered command
    while(condition == FALSE){
        fgets(input,10,stdin);
        if(input[0] == COMMAND_FOR_QUIT1 || input[0] == COMMAND_FOR_QUIT2){
            for( i = 0 ; i < NPHILO ; i++ ){
                philo_commands[i] = COMMAND_FOR_QUIT1;
            }
            condition = TRUE;
        }
        if(input[0] == COMMAND_SWITCH_DEBUGGING){
            if(DEBUGGING == TRUE){
                DEBUGGING = FALSE;
            }
            else {
                DEBUGGING = TRUE;
            }
        }
        philo_to_change = strtol(input,&last_command,10);
        if(philo_to_change < NPHILO){
            if(*last_command == COMMAND_TO_BLOCK ){
                debugging_message("Philosoph# %i will be blocked.\n",philo_to_change);
                philo_states_restore[philo_to_change] = philo_states[philo_to_change];
                philo_states[philo_to_change] = '-';
                philo_commands[philo_to_change] = COMMAND_TO_BLOCK;
                debugging_message("Philosoph# %i is blocked.\n",philo_to_change);
            }
            if(*last_command == COMMAND_TO_UNBLOCK){
                debugging_message("Philosoph# %i will be unblocked.\n",philo_to_change);
                philo_states[philo_to_change] = philo_states_restore[philo_to_change];
                sem_post(&philo_sem[philo_to_change]);
                debugging_message("Philosoph# %i is unblocked.\n",philo_to_change);
            }
            if(*last_command == COMMAND_TO_PROCEED){
                debugging_message("Loop of Thread# %i will be proceeded.\n",philo_to_change);
                philo_commands[philo_to_change] = COMMAND_TO_PROCEED;
            }
        }
    }



    // DESTROY ALL PHILOSOPHS

    for( i = 0 ; i < NPHILO ; i++ ){
        debugging_message("Thread %i will be unblocked if blocked.\n",i);
        error = sem_post(&philo_sem[i]);
        if(error != 0){
            printf("Cant reactivate semaphore.");
            exit(EXIT_FAILURE);
        }
        debugging_message("Thread# %i will be joined.\n",i);
        error = pthread_join(threads[i],&thread_results[i]);
        if(error != 0){
            printf("Konnte Thread nicht starten.");
            exit(EXIT_FAILURE);
        }
        debugging_message("Thread# %i was been joined.\n",i);
    }
    debugging_message("All Philosophs left the table.\n",NULLINT);

    // DESTROY ALL OBJECTS

    error = pthread_mutex_destroy(&stick_mutex);
    if(error != 0){
        printf("Konnte Mutex nicht vernichten.");
        exit(EXIT_FAILURE);
    }
    error = pthread_mutex_destroy(&print_mutex);
    if(error != 0){
        printf("Konnte Mutex nicht vernichten.");
        exit(EXIT_FAILURE);
    }
    for( i = 0 ; i < NPHILO ; i++){
        error = pthread_cond_destroy(&philo_conditions[i]);
        if(error != 0){
            printf("Konnte Cond nicht vernichten.");
            exit(EXIT_FAILURE);
        }
    }
    error = pthread_barrier_destroy(&barrierSingle);
    if(error != 0){
        printf("Konnte Barrier nicht vernichten.");
        exit(EXIT_FAILURE);
    }
    error = pthread_barrier_destroy(&barrierAll);
    if(error != 0){
        printf("Konnte Barrier nicht vernichten.");
        exit(EXIT_FAILURE);
    }
    return (EXIT_SUCCESS);
}

void check_error(char message[],int error){
    if(error != 0){
            printf(message);
            printf("\n");
            exit(EXIT_FAILURE);
        }
}