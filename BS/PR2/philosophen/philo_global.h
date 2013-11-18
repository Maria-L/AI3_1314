#define MSGLEN 128
#define NPHILO 5
#define THINK_LOOP 1000000000
#define EAT_LOOP 500000000
#define CONSOLESIZEX 70
#define CONSOLESIZEY 40
#define ASCIICHARTOINTOFFSET 48
#define QUIT 'q'
#define BLOCK 'b'
#define UNBLOCK 'u'
#define PROCEED 'p'
#define THINKING 'T'
#define EAT 'E'
#define HUNGRY 'H'


int stickCond[NPHILO];             //Zustandsarray der Staebchen 0->frei 1->belegt
sem_t block[NPHILO];               //Semaphoren zum benutzergesteuerten Blockieren
pthread_mutex_t mutex;             //Mutex fuer das Probieren  die Staebchen aufzunehmen
pthread_mutex_t mutexPrint;        //Mutex fuer das Drucken von Ausgaben
pthread_cond_t condStick[NPHILO];  //Condition-Array fuer Staebchen-Blockieren
pthread_barrier_t barrierSingle;   //Barriere fuer das sichere Erstellen der einzelnen Philosophen
pthread_barrier_t barrierAll;      //Barriere fuer das sichere Erstellen aller Philosophen.
char command[NPHILO];              //Commandoarray
char state[NPHILO];                //Statusarray der Philosophen
int lineCount;                     //Linienzähler für die Ausgabe via NCurses
