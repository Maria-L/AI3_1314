#ifndef _TRANSLATE_H_
#define _TRANSLATE_H_

#ifndef init_MUTEX
#define init_MUTEX(mutex) sema_init(mutex, 1)
#endif

#define PDEBUG(fmt, args...) printk( KERN_DEBUG "translate: " fmt, ## args)

#undef PDEBUGG
#define PDEBUGG(fmt, args...)

#ifndef COUNT_OF_DEVS
#define COUNT_OF_DEVS 2
#endif

#ifndef TRANSLATE_BUFSIZE
#define TRANSLATE_BUFSIZE 40
#endif

#ifndef TRANSLATE_SUBST
#define TRANSLATE_SUBST "ABCDEFGHIJKLMNOPQRSTUFVXYZabcdefghijklmnopqrstuvwxyz"
#endif

#ifndef ALPHABET_LENGTH
#define ALPHABET_LENGTH ('Z'-'A'+1)
#endif

#ifndef TRANSLATE_SUBST_LENGTH
#define TRANSLATE_SUBST_LENGTH (ALPHABET_LENGTH * 2)
#endif

#ifndef ZEROBUFFER
#define ZEROBUFFER 0
#endif

#ifndef ZERO
#define ZERO 0
#endif

#ifndef ONE
#define ONE 1
#endif

#ifndef DYNAMIC_MAJOR
#define DYNAMIC_MAJOR 0
#endif

// Geraete-Struct fuer translate
struct translate_dev {
  char *buffer, *end;        //Buffer-Anfangs und End-adresse
  int buffersize, fillcount; //Groesse des Buffers und die Anzahl der beschriebenen Stellen im Buffer
  char *rp, *wp;             //Position des Read und des Write-Pointers
  int nreaders, nwriters;    //Anzahl der lesenden und schreibenden Prozesse
  int minor_number;          //Minor-Number des Geraetes: 0 -> Encode | 1 -> Decode
  struct semaphore sem;      //Semaphore um gleichzeitige Zugriffe zu vermeiden
  struct cdev cdev;          //Von der Inode-Struktur benoetigtes Element zur Repraesentation von Char-Devices
  wait_queue_head_t queue;   //Warteschlange fuer Prozesse
};

//########## Zugriffsfunktionen ##########
int init_module(void);      //Methode zum Initialisieren des Moduls
void cleanup_module(void);  //Methode zum Freigeben des Moduls
int translate_open(struct inode *inode, struct file *filp);  //Gewaehrt Zugriff auf ein Translate-Device
int translate_close(struct inode *inode, struct file *filp); //Loescht den Zugriff auf das Translate-Device wieder
ssize_t translate_read(struct file *filp, char __user * buf, size_t count, loff_t * f_pos);        //Liest aus dem Buffer des Geraetes
ssize_t translate_write(struct file *filp, const char __user * buf, size_t count, loff_t * f_pos); //Schreibt in den Buffer des Geraetes

//########## Hilfsfunktionen ##########
char encode_char(char c);           //Verschluesselt den eingegebenen char nach der Kodierungstabelle
char decode_char(char c);           //Entschluesselt den eingegebenen char nach der Kodierungstabelle
int indexOf(char c);                //Sucht den Index des eingegebenen char im Buffer
void printDevice(struct translate_dev *dev);//Schreibt den aktuellen Zustand des Geraets in das Kernellog

#endif