CC=/usr/bin/gcc
CFLAGS=-pthread -g -Wall -lpthread -O
LDFLAGS=-pthread 

main: main.o main2.o
	$(CC) $(LDFLAGS) -o $@ $+

%.o: %.c
	$(CC) $(CFLAGS) -c $<

main3: main3.o
	$(CC) $(LDFLAGS) -o $@ $+

%.o: %.c
	$(CC) $(CFLAGS) -c $<
