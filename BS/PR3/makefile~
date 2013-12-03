CC=/usr/bin/gcc
CFLAGS=-pthread -g -Wall -lpthread -O
LDFLAGS=-pthread 

philosophen: main.o philo.o monitor.o
	$(CC) $(LDFLAGS) -o $@ $+

%.o: %.c
	$(CC) $(CFLAGS) -c $<