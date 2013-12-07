CC:=/usr/bin/gcc
FLAG:=-g -Wall -lpthread -O -pthread 
MMFLAG:= -DDEBUG_MESSAGES

MMFLAG += -DFIFO
#MMFLAG += -DCLOCK
#MMFLAG += -DCLOCK2

vmappl: vmaccess.o vmappl.o
	$(CC) $(FLAG) -o vmappl vmappl.o vmaccess.o

mmanage: mmanage.o
	$(CC) $(FLAG) -o mmanage mmanage.o

mmanage.o: mmanage.c mmanage.h
	$(CC) $(FLAG) $(MMFLAG) -c -o mmanage.o mmanage.c

vmappl.o: vmappl.c vmappl.h
	$(CC) $(FLAG) -c -o vmappl.o vmappl.c

vmaccess.o: vmaccess.c vmaccess.h
	$(CC) $(FLAG) -c -o vmaccess.o vmaccess.c

clean:
	rm -f mmanage vmappl *.o logfile.txt pagefile.bin