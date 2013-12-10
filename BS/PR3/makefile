CC=/usr/bin/gcc
CFLAGS=-pthread -g -Wall -lpthread
LDFLAGS=-pthread 

all: vmappl mmanage

vmappl: vmappl.o vmaccess.o
	$(CC) $(LDFLAGS) -o $@ $+

mmanage: mmanage.o
	$(CC) $(LDFLAGS) -o $@ $+

%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

clean:
	rm *.o vmappl mmanage logfile.txt pagefile.bin
