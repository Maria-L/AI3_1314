CC=/usr/bin/gcc
CFLAGS=-pthread -g -Wall -lpthread -O
LDFLAGS=-pthread 

main: main.o
	$(CC) $(LDFLAGS) -o $@ $+

%.o: %.c
	$(CC) $(CFLAGS) -c $<
