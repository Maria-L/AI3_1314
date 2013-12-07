/* Description: Memory Manager BSP3*/
/* Prof. Dr. Wolfgang Fohl, HAW Hamburg */
/* Winter 2010/2011
 * 
 * This is the memory manager process that
 * works together with the vmaccess process to
 * mimic virtual memory management.
 *
 * The memory manager process will be invoked
 * via a SIGUSR1 signal. It maintains the page table
 * and provides the data pages in shared memory
 *
 * This process is initiating the shared memory, so
 * it has to be started prior to the vmaccess process
 *
 * TODO:
 * currently nothing
 * */

#include "mmanage.h"

struct vmem_struct *share = NULL;
FILE *pagefile = NULL;
FILE *logfile = NULL;
int signal_number = 0;          /* Received signal */
key_t shm_key;
int shm_id;

int
main(void)
{
    int end = 0;
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "Starte mmanage.c \n");
#endif
    struct sigaction sigact;

    /* Init pagefile */
    init_pagefile(MMANAGE_PFNAME);
    if(!pagefile) {
        perror("Error creating pagefile.\n");
        exit(EXIT_FAILURE);
    }

    /* Open logfile */
    logfile = fopen(MMANAGE_LOGFNAME, "w");
    if(!logfile) {
        perror("Error creating logfile.\n");
        exit(EXIT_FAILURE);
    }

    /* Create shared memory and init vmem structure */
    vmem_init();

    /* Setup signal handler */
    /* Handler for USR1 */
    sigact.sa_handler = sighandler;
    sigemptyset(&sigact.sa_mask);
    sigact.sa_flags = 0;
    if(sigaction(SIGUSR1, &sigact, NULL) == -1) {
        perror("Error installing signal handler for USR1.\n");
        exit(EXIT_FAILURE);
    }
#ifdef DEBUG_MESSAGES
    else {
        fprintf(stderr, "USR1 handler successfully installed\n");
    }
#endif /* DEBUG_MESSAGES */

    if(sigaction(SIGUSR2, &sigact, NULL) == -1) {
        perror("Error installing signal handler for USR2. \n ");
        exit(EXIT_FAILURE);
    }
#ifdef DEBUG_MESSAGES
    else {
        fprintf(stderr, "USR2 handler successfully installed. \n");
    }
#endif /* DEBUG_MESSAGES */

    if(sigaction(SIGINT, &sigact, NULL) == -1) {
        perror("Error installing signal handler for INT. \n");
        exit(EXIT_FAILURE);
    }
#ifdef DEBUG_MESSAGES
    else {
        fprintf(stderr, "INT handler successfully installed. \n");
    }
#endif /* DEBUG_MESSAGES */
#ifdef DEBUG_MESSAGES
        fprintf(stderr, " \n");
#endif

    /* Signal processing loop */
    while(!end) {
        signal_number = 0;
#ifdef DEBUG_MESSAGES
        fprintf(stderr,"Signal-Schleife wartet auf Signal. \n");
#endif
        dump_data();
        pause();
        
        if(signal_number == SIGUSR2) {     /* PT dump */
#ifdef DEBUG_MESSAGES
            fprintf(stderr, "Processed SIGUSR2. \n");
#endif /* DEBUG_MESSAGES */
            dump_data();
        }
        else if(signal_number == SIGINT) {
#ifdef DEBUG_MESSAGES
            fprintf(stderr, "Processed SIGINT. \n");
#endif /* DEBUG_MESSAGES */
            end = 1;
            cleanup();
        }
    }

    return 0;
}

//Wird ausgeführt falls ein Seitenfehler auftritt
void
missing_page(){

#ifdef DEBUG_MESSAGES
    fprintf(stderr, "Es ist ein Seitenfehler aufgetreten.\n");
#endif
    share->adm.pf_count++;
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "- Bislang wurde(n) %d Seite(n) vermisst. \n", share->adm.pf_count);
#endif

    int index_for_new_frame = find_remove_frame();
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "- Seite wird bei Frame %d gespeichert. \n", index_for_new_frame);
#endif

    int old_page = share->pt.framepage[index_for_new_frame];
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "- Frame gehoert zu Page %d . \n", old_page);
#endif

    store_page(old_page);
    update_pt(index_for_new_frame);
    fetch_page(share->adm.req_pageno);

    //Erstellen des Log-Eintrags
    struct logevent new_entry;
    new_entry.req_pageno = share->adm.req_pageno;
    new_entry.replaced_page = old_page;
    new_entry.alloc_frame = index_for_new_frame;
    new_entry.pf_count = share->adm.pf_count;
    new_entry.g_count = share->adm.pf_count;
    logger(new_entry);
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "Seite erfolgreich nachgeladen.\n");
#endif

#ifdef DEBUG_MESSAGES
    fprintf(stderr, "Semaphore wurde von mmanage freigegeben. \n");
#endif
    dump_data();
}

//Frame zur Page speichern
void
store_page(int page){
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "- Alte Seite wird vom RAM wieder auf HDD geschrieben.\n");
#endif
    int frame = share->pt.entries[page].frame;
    //Suche nach Position wo Frame hingespeichert werden soll
    fseek(pagefile,sizeof(int)*VMEM_PAGESIZE*page, SEEK_SET);
    int written_data = fwrite(&share->data[VMEM_PAGESIZE*frame],sizeof(int), VMEM_PAGESIZE,pagefile);
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "-- Geschrieben Daten: %d.\n", share->data[VMEM_PAGESIZE*frame]);
#endif
    if(written_data != VMEM_PAGESIZE){
        perror("Es konnten nicht alle Page-Daten ins Pagefile geschrieben werden.\n");
        exit(EXIT_FAILURE);
    }
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "- Alte Seite wurde erfolgreich wieder auf HDD gespeichert.\n");
#endif
}

//Page aus Pagefile auslesen
void
fetch_page(int page){
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "- Neue Seite wird von HDD gelesen. \n");
#endif
    int frame = share->pt.entries[page].frame;
    //Suche nach der auszulesenden Page
    fseek(pagefile, sizeof(int)*VMEM_PAGESIZE*page, SEEK_SET);
    int read_data = fread(&share->data[VMEM_PAGESIZE*frame], sizeof(int), VMEM_PAGESIZE, pagefile);
    if(read_data != VMEM_PAGESIZE){
        perror("Es konnten nicht alle Page-Daten aus dem Pagefile gelesen werden.");
        exit(EXIT_FAILURE);
    }
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "- Neue Seite wurde erfolgreich von HDD gelesen und in RAM gespeichert. \n");
#endif
}

//Suche nach einer Position wo ein Frame gespeichert werden kann
int
find_remove_frame(){
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "- Es wird nach einer Stelle fuer ein Frame gesucht. \n");
#endif
    int frame = VOID_IDX;
    //Falls RAM bereits voll ist
    if(share->adm.size < VMEM_NFRAMES){
        share->adm.size++;
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "-- adm.size ist %d gross. \n", share->adm.size);
#endif
        frame = share->adm.size;
#ifdef DEBUG_MESSAGES
        fprintf(stderr, "-- Daten koennen an freier Stelle %d abgelegt werden. \n", frame );
#endif
    } else {
#ifdef FIFO
        frame = find_remove_fifo();
#endif
#ifdef CLOCK
        frame = find_remove_clock();
#endif
#ifdef CLOCK2
        frame = find_remove_clock2();
#endif
#ifdef DEBUG_MESSAGES
        fprintf(stderr, "-- Daten koennen an bereinigter Stelle %d abgelegt werden. \n", frame );
#endif
    }
    return frame;   //return erst hier damit Debugging ausgegeben werden kann
}

//FIFO-Algorithmus
int
find_remove_fifo(){
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "-- Frame wird gemaess FIFO gesucht. \n");
#endif
    int frame = share->adm.next_alloc_idx;
    increment_alloc_idx(frame);
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "-- Zu benutzendes Frame ist %d . \n", frame);
#endif
    return frame;
}

//Dient zum Rotieren der Frame-Indizes
void
increment_alloc_idx(int alloc_idx){
    if(alloc_idx == (VMEM_NFRAMES -1)){
        share->adm.next_alloc_idx = 0; //Faengt wieder vorne an
    } else {
        share->adm.next_alloc_idx++; //Springt einen weiter
    }
}

//CLOCK-Algorithmus
int
find_remove_clock(){
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "-- Frame wird gemaess CLOCK gesucht. \n");
#endif
    int frame;
    int found = 0;
    int alloc_idx;
    int frame_of_alloc_idx;
    int flags;
    int used_bit;

    while(!found){
        alloc_idx = share->adm.next_alloc_idx;
        frame_of_alloc_idx = share->pt.framepage[alloc_idx];
        flags = share->pt.entries[frame_of_alloc_idx].flags;
        used_bit = (flags & PTF_USED) == PTF_USED;

        if(used_bit){ //Falls Used-Bit gesetzt ist, wird es negiert
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "--- Flags vor Veraenderung: %d. \n",share->pt.entries[frame_of_alloc_idx].flags);
#endif
            share->pt.entries[frame_of_alloc_idx].flags &= ~PTF_USED;
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "--- Flags nach Veraenderung: %d. \n",share->pt.entries[frame_of_alloc_idx].flags);
#endif
            increment_alloc_idx(alloc_idx); //Um nächste Position zu pruefen
        } else {
            frame = alloc_idx;
            found = 1; //Um Suchschleife zu beenden
        }
    }
    increment_alloc_idx(share->adm.next_alloc_idx);
    return frame;
}

//CLOCK2-Algorithmus
int
find_remove_clock2(){
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "-- Frame wird gemaess CLOCK2 gesucht. \n");
#endif
    int frame;
    int found = 0;
    int alloc_idx;
    int frame_of_alloc_idx;
    int flags;
    int first_used_bit;
    int second_used_bit;

    while(!found){
        alloc_idx = share->adm.next_alloc_idx;
        frame_of_alloc_idx = share->pt.framepage[alloc_idx];
        flags = share->pt.entries[frame_of_alloc_idx].flags;
        first_used_bit = (flags & PTF_USED) == PTF_USED;

        if(first_used_bit){ 
            second_used_bit = (flags & PTF_USED1) == PTF_USED1;
            if(second_used_bit){ //Falls zweites Used-Bit gesetzt ist, wird es negiert
                share->pt.entries[frame_of_alloc_idx].flags &= ~PTF_USED1;
            } else { //Ansonsten wird erstes Used-Bit negiert
                share->pt.entries[frame_of_alloc_idx].flags &= ~PTF_USED;
            }
            increment_alloc_idx(alloc_idx); //Um nächste Position zu pruefen
        } else {
            frame = alloc_idx;
            found = 1; //Um Suchschleife zu beenden
        }
    }
    increment_alloc_idx(share->adm.next_alloc_idx);
    return frame;
}

//Pagetable aktualisieren
void
update_pt(int frame){
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "- Pagetable wird aktualisiert. \n");
#endif
    int old_page = share->pt.framepage[frame];
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "-- Auszulagerndes Page ist %d . \n", old_page);
#endif
    int req_page = share->adm.req_pageno;
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "-- In den RAM zu ladende Page ist %d . \n", req_page);
#endif
    //Reset von allen Flags
    share->pt.entries[old_page].flags = 0;
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "-- Flags des Frames wurden zurueckgesetzt. \n");
#endif
    //Frame-Verlinkung entfernen
    share->pt.entries[old_page].frame = VOID_IDX;
    //Verlinken des Frames mit der neuen Seite
    share->pt.framepage[frame] = req_page;
    share->pt.entries[req_page].frame = frame;
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "-- Verlinkungen wurden korrigiert. \n");
#endif
    //Flag auf "Verfuegbar" setzen
    share->pt.entries[req_page].flags |= PTF_PRESENT;
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "-- Neue Page wird als verfuegbar markiert. \n");
#endif
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "- Pagetable wurde erfolgreich aktualisiert. \n");
#endif
}

void
sighandler(int signo){
    signal_number = signo;
    if(signo == SIGUSR1) {  /* Page fault */
#ifdef DEBUG_MESSAGES
            fprintf(stderr, "Processed SIGUSR1. \n");
#endif /* DEBUG_MESSAGES */
            missing_page();
#ifdef DEBUG_MESSAGES
            fprintf(stderr, "Versuche Semaphore freizugeben. \n");
#endif
    //Anderen Prozess per Semaphore weiterlaufen lassen
            int err;
            err = sem_post(&share->adm.sema);
            if(err != 0){
                perror("Semaphore konnte nicht freigegeben werden.\n");
                exit(EXIT_FAILURE);
            }
        }
}

void
cleanup(){
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "Prozess mmanage soll beendet werden, es wird aufgeraeumt. \n");
#endif
    shmdt(share);

    fclose(logfile);
    fclose(pagefile);
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "Alles erfolgreich aufgeraeumt. Ciao! \n");
#endif
}

//Initialisieren des pagefiles mit ein paar Werten
void
init_pagefile(const char *pagefile_path){
    int noOfElements = VMEM_NPAGES*VMEM_PAGESIZE;
    int data[noOfElements];
    int i;
    // Mit irgendwelchen Daten vorbefuellen
    for(i = 0; i < noOfElements; i++) {
        data[i] = rand() % RNDMOD;
    } 
    
    pagefile = fopen(pagefile_path, "w+b");
    if(!pagefile) {
        perror("Error creating pagefile. \n");
        exit(EXIT_FAILURE);
    }
    
    int writing_result = fwrite(data, sizeof(int), noOfElements, pagefile);
    if(!writing_result) {
        perror("Error creating pagefile. \n");
        exit(EXIT_FAILURE);
    }
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "Pagefile erfolgreich erstellt.\n");
#endif
}

//Initialisieren des Shared Memory
void
vmem_init(){
#ifdef DEBUG_MESSAGES
    fprintf(stderr,"Aufbau des SharedMemory. \n");
#endif
    int i;
    shm_key = ftok(SHMKEY,1);
    shm_id = shmget(shm_key,SHMSIZE,IPC_CREAT | 0666);
    share = shmat(shm_id,0,0);
    if(share == NULL){
        perror("Speicher konnte nicht eingebunden werden. \n");
        exit(EXIT_FAILURE);
    }
    //Initialisiere ADM im Shared Memory
    share->adm.size = VOID_IDX;
    share->adm.mmanage_pid = getpid();
    share->adm.shm_id = VOID_IDX;
    share->adm.req_pageno = VOID_IDX;
    share->adm.next_alloc_idx = 0;
    share->adm.pf_count = 0;

    //Initialisiere Semaphoren
    int sem_err = sem_init(&share->adm.sema,1,1);
    if(sem_err != 0){
        perror("Semaphoren konnten nicht initialisiert werden. \n");
        exit(EXIT_FAILURE);
    }

    //Pagetable-Daten im Shared-Memory initialisieren
    for(i = 0; i < VMEM_NPAGES; i++){
        share->pt.entries[i].flags = 0;
        share->pt.entries[i].frame = VOID_IDX;
    }

    //Die Framepage im Shared-Memory initialisieren
    for(i = 0; i < VMEM_NFRAMES; i++){
        share->pt.framepage[i] = VOID_IDX;
    }

    //RAM im Shared-Memor initialisieren
    for(i = 0; i < (VMEM_NFRAMES * VMEM_PAGESIZE); i++ ){
        share->data[i] = VOID_IDX;
    }
    dump_data();
#ifdef DEBUG_MESSAGES
    fprintf(stderr, "Shared-Memory erfolgreich eingebunden und initialisiert. \n");
#endif
}

void
dump_data(){
#ifdef DEBUG_MESSAGES
    int i;
    fprintf(stderr, "--> DUMP <--\n");
    fprintf(stderr, "ADM: \n");
    fprintf(stderr, "Groesse: %d, Naechste Seite: %d, Seitenfehlerzaehler: %d\n", share->adm.size, share->adm.req_pageno, share->adm.pf_count);
    fprintf(stderr, "DATEN: \n");
    fprintf(stderr, "[ Frame , Page ]\n");
    for(i = 0 ; i < VMEM_NFRAMES; i++){
        fprintf(stderr, "[ %d , %d ]\n",i,share->pt.framepage[i]);
    }
#endif
}

/* Do not change!  */
void
logger(struct logevent le)
{
    fprintf(logfile, "Page fault %10d, Global count %10d:\n"
            "Removed: %10d, Allocated: %10d, Frame: %10d\n",
            le.pf_count, le.g_count,
            le.replaced_page, le.req_pageno, le.alloc_frame);
    fflush(logfile);
}