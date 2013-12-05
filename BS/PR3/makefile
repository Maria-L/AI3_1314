CC=/usr/bin/gcc
CFLAGS=-pthread -g -Wall -lpthread -O
LDFLAGS=-pthread 

vmappl: vmappl.o vmaccess.o
	$(CC) $(LDFLAGS) -o $@ $+

%.o: %.c
	$(CC) $(CFLAGS) -c $<

mmanage: mmanage.o
	$(CC) $(LDFLAGS) -o $@ $+

%.o: %.c
	$(CC) $(CFLAGS) -c $<

all: vmappl mmanage