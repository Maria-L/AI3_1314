CC=/usr/bin/gcc
FLAG=-lncurses -pthread -g -Wall -lpthread -O

philosophen: main.c philo.c monitor.c
	$(CC) $(FLAG) -o $@ $+

%.o: %.c
	$(CC) $(FLAG) -c $<