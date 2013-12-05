//main1

#include <signal.h>
#include <stdio.h>
#include <stdlib.h>

int signal_number = 0;
 
static void catch_function(int signo) {
    puts("Interactive attention signal caught.");
}
 
int main(void) {

  struct sigaction sicact;

  sigact.sa_handler = sighandler;
  sigemptyset(&sigact.sa_mask);
  sigact.sa_flags = 0;
  if(sigaction(SIGUSR1, &sigact, NULL) == -1) {
      perror("Error installing signal handler for USR1");
      exit(EXIT_FAILURE);
  }

  while(1) {
    signal_number = 0;
    pause();
    if(signal_number == SIGUSR1) {
      fprintf("SIGUSR1 bekommen");
    }
  }
    return 0;
}
