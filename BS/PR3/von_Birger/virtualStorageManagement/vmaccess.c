#include "vmaccess.h"
key_t shm_key;
int shm_id;

struct vmem_struct* share;

void
vm_init(void){
	shm_key = ftok(SHMKEY,1);
	shm_id = shmget(shm_key,SHMSIZE,IPC_CREAT | 0666);
	share = shmat(shm_id,0,0);
	if(share == NULL){
		perror("Speicher konnte nicht eingebunden werden.\n");
	}
#ifdef DEBUG_MESSAGES
	fprintf(stderr,"Speicher erfolgreich initialisiert.\n");
#endif
}

int
vmem_read(int address){
	int accu;
	int index_of_pt = address / VMEM_PAGESIZE;	//Page berechnen
	int offset = address % VMEM_PAGESIZE; //Offset berechnen

	share->adm.req_pageno = index_of_pt; /* im shared memory bekannt
								 machen welche Page benoetigt wird */

	int flags = share->pt.entries[index_of_pt].flags; /* Flags der benoetigten
														 Page auslesen */
	if(!((flags & PTF_PRESENT) == PTF_PRESENT)){	//Falls Frame nicht vorhanden ist
#ifdef DEBUG_MESSAGES
		fprintf(stderr,"Read - Page %d ist nicht als Frame vorhanden, wird nachgeladen.\n", index_of_pt);
#endif
		kill((share->adm.mmanage_pid),SIGUSR1);
		sem_wait(&share->adm.sema);
#ifdef DEBUG_MESSAGES
		fprintf(stderr,"- Read - Page %d ist nun im RAM.\n",index_of_pt);
#endif
#ifdef DEBUG_MESSAGES
		fprintf(stderr,"Read - Page erfolgreich in die Frames geladen.\n");
#endif
		return vmem_read(address);
	} 
		accu = get_page(index_of_pt,offset);
#ifdef DEBUG_MESSAGES
		fprintf(stderr,"Read - Beendet.\n");
#endif
		return accu;
}

//Helfer-Funktion zum Erhalten der Daten einer Page
int
get_page(int page, int offset){
	set_used_bit(page);
	int index = get_index_by_page_offset(page,offset); /*Index durch den Offset
													 berechnen */
	return share->data[index];
}

//Helfer-Funktion zum Setzen der Used-Bits für die CLOCK-Algorithmen
void
set_used_bit(int page){
	int firstUsedBit = share->pt.entries[page].flags & PTF_USED;
	if(firstUsedBit){ /* Falls erstes Used-Bit bereits gesetzt ist;
						 ist wichtig für CLOCK2 */
		set_bit(&(share->pt.entries[page].flags),PTF_USED1);
	}
	//Es wird in jedem Fall das erste Used-Bit gesetzt
	set_bit(&(share->pt.entries[page].flags),PTF_USED);
}

//Helfer-Funktion zum Setzen eines Bits
void set_bit(int *need_to_set, int bits){
	*(need_to_set) |= bits;
}

void
vmem_write(int address, int data){
	int err;
	check_shared_memory();
#ifdef DEBUG_MESSAGES
		fprintf(stderr,"Write wird gestartet.\n");
#endif
	int index_of_pt = address / VMEM_PAGESIZE;	//Page berechnen
	int offset = address % VMEM_PAGESIZE;	//Offset berechnen

	share->adm.req_pageno = index_of_pt; /* im shared memory bekannt
								 machen welche Page benoetigt wird */

	int flags = share->pt.entries[index_of_pt].flags;

	if(!((flags & PTF_PRESENT) == PTF_PRESENT)){	//Falls Frame nicht vorhanden ist
#ifdef DEBUG_MESSAGES
		fprintf(stderr,"Read - Page %d ist nicht als Frame vorhanden, wird nachgeladen.\n", index_of_pt);
#endif
		kill((share->adm.mmanage_pid),SIGUSR1);
#ifdef DEBUG_MESSAGES
	    fprintf(stderr, "Versuche auf Semaphore zu warten.\n");
#endif
		err = sem_wait(&share->adm.sema);
		if(err != 0){
			perror("Semaphore konnte nicht blockiert werden.\n");
			exit(EXIT_FAILURE);
		}
#ifdef DEBUG_MESSAGES
		fprintf(stderr, "Semaphore ist wieder freigegeben.\n");
		fprintf(stderr,"- Write - Page %d ist nun im RAM.\n",index_of_pt);
#endif
#ifdef DEBUG_MESSAGES
		fprintf(stderr,"Write - Page erfolgreich in die Frames geladen.\n");
#endif
	}
	write_page(index_of_pt,offset,data);
#ifdef DEBUG_MESSAGES
		fprintf(stderr,"Write - Beendet.\n");
#endif
}

//Helfer-Funktion zum tatsaechlichen Schreiben einer Page
void
write_page(int page, int offset, int data){
	set_used_bit(page);
	//Schreiben der Daten in den Hauptspeicher
	share->data[get_index_by_page_offset(page,offset)] = data;
#ifdef DEBUG_MESSAGES
		fprintf(stderr,"- Write - Page wurde erfolgreich in den RAM geschrieben.\n");
#endif
}

//Helfer-Funktion zum Berechnen des Indizes durch Page-Nummer und Offset
int
get_index_by_page_offset(int page, int offset){
	return (share->pt.entries[page].frame*VMEM_PAGESIZE) + offset;
}

//Helfer-Funktion zum Initialisieren des Shared Memory falls noch nicht erfolgt
void
check_shared_memory(){
	if(share == NULL){
		vm_init();
	}
}