#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
 
static void catch_function(int signo) {
    puts("Interactive attention signal caught.");
}
 
int main(void) {
    if (signal(SIGUSR1, catch_function) == SIG_ERR) {
        fputs("An error occurred while setting a signal handler.\n", stderr);
        return EXIT_FAILURE;
    }
    puts("Raising the interactive attention signal.");
    if (raise(SIGUSR1) != 0) {
        fputs("Error raising the signal.\n", stderr);
        return EXIT_FAILURE;
    }
    puts("Exiting.");
    return 0;
}
