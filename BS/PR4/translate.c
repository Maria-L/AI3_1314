#include <linux/module.h>
#include <linux/moduleparam.h>
#include <linux/kernel.h>
#include <linux/sched.h>
#include <linux/slab.h>
#include <linux/fs.h>
#include <linux/proc_fs.h>
#include <linux/errno.h>
#include <linux/types.h>
#include <linux/fcntl.h>
#include <linux/poll.h>
#include <linux/cdev.h>
#include <asm/uaccess.h>

#include "translate.h"

MODULE_AUTHOR("Steffen Giersch, Maria Janna Martina Luedemann");
MODULE_LICENSE("Dual BSD/GPL");


static char *dev_name = "translate";         //Name des Geraets
static int count_of_devices = COUNT_OF_DEVS; //Anzahl der Sub-Devices

int translate_bufsize = TRANSLATE_BUFSIZE;   //Groesse des Buffers

char *translate_subst_def = TRANSLATE_SUBST; //Fixe Translate-Tabelle zum unter Umstaenden Vervollstaendigen von translate_subst
char *translate_subst = TRANSLATE_SUBST;     //Translate-Tabelle, die vom Nutzer vorgegeben werden kann

int dev_minor = 0; //Minor-Device-Number, muss noch vergeben werden
int dev_major = 0; //Major-Device-Number, muss noch vergeben werden

//Gegebenenfalls ersetzen der Parameter durch Nutzereingaben
module_param(translate_bufsize, int, 0);  
module_param(translate_subst, charp, 0);


struct translate_dev *translate_devices;  //Pointer auf alle Translate-devices

//"Verlinken" der Standart-Operations-Namen auf die passenden Translate-Operationen
struct file_operations fops = {
  .owner = THIS_MODULE,
  .read = translate_read,
  .write = translate_write,
  .open = translate_open,
  .release = translate_close
};

//########## Zugriffsfunktionen ##########
ssize_t translate_read(struct file *filp, char __user * buf, size_t count, loff_t * f_pos) {
  int err;                   //Error-Wert
  int minor;                 //Minor-Device-Number
  int i;                     //Zaehler-Variable
  struct translate_dev *dev; //Translate-Informationen
  char stringBuffer[TRANSLATE_BUFSIZE];        //Buffer fuer die Rueckgabe
  
  dev = filp->private_data;  //Nehme Translate-Informationen von der Benutzereingabe
  minor = dev->minor_number; //Speichere die Minor-Device-Number
  
  printk(KERN_ALERT "Translate: Beginne zu lesen\n");
  
  err = down_interruptible(&dev->sem);  //Warte auf den Semaphoren
  if(err) {                             //Wenn der Prozess per Signal geweckt wurde
    return -ERESTARTSYS;                //  Gib ERESTARTSYS zurueck
  }
  
  while(dev->fillcount <= ZEROBUFFER) {                             //Solange der Buffer leer ist
    printk(KERN_ALERT "Translate: Leerer Buffer, warte auf Elemente\n");

    up(&dev->sem);                                                  //  Gib den Semaphoren frei
    err = wait_event_interruptible(dev->queue, dev->fillcount > 0); //  Reihe dich in die Warteschlange ein, bis die Bedingung erfuellt ist
    if(err) {                                                       //  Wenn der Prozess per Signal geweckt wurde
      return -ERESTARTSYS;                                          //    Gib ERESTARTSYS zurueck
    }
    err = down_interruptible(&dev->sem);                            //  Warte auf den Semaphoren
    if(err) {                                                       //  Wenn der prozess per Signal geweckt wurde
      return -ERESTARTSYS;                                          //    Gib ERESTARTSYS zurueck
    }
  }

  
  if(dev->wp > dev->rp) {                             //Wenn der Write-Pointer hinter dem Read-Pointer steht
    count = min(count, (size_t) (dev->wp - dev->rp)); //  Setze count auf das minimum von count und den uebrigen Elementen vom wp bis zum rp
  } else {                                            //Sonst
    count = min(count, (size_t) (dev->end - dev->rp));//  Setze count auf das minimum von count und den uebrigen Elementen bis zum Ende des Buffers
  }
  
  if(minor == MINORONE) {                        //Wenn Decodiert werden muss
    for(i = 0; i < count; i++) {                 //  Decodiere und schreibe count mal in den stringBuffer
      stringBuffer[i] = decode_char(dev->rp[i]); 
    }
    err = copy_to_user(buf, stringBuffer, count);//Kopiere den erstellten String in den User-Buffer
    if(err) {                                    //Wenn dabei ein Fehler passiert ist
      return -EFAULT;                            //  Gib EFAULT zurueck
    }
  } else {                                       //Wenn nicht decodiert werden muss
    err = copy_to_user(buf, dev->rp, count);     //  Kopiere die Ergebnisse in den User-Speicher
    if(err) {                                    //    Wenn dabei ein Fehler passiert ist
      return -EFAULT;                            //      Gebe EFAULT zurueck
    }
  }
  
  dev->rp += count;          //Setze den Read-Pointer um count weiter
  if(dev->rp == dev->end) {  //Wenn der Read-Pointer am Ende des Buffers angekommen ist
    dev->rp = dev->buffer;   //  Setze den Read-Pointer auf den Anfang des Buffers
  }
  
  dev->fillcount -= count;   //Dekrementiere den Fill-Count
  
  printDevice(dev);
  printk(KERN_ALERT "Translate: %d Zeichen von translate%d gelesen\n", (int) count, minor);
  up(&dev->sem);             //Gib den Semaphoren wieder frei
  wake_up(&dev->queue);      //Wecke die Prozesse in der Warteschlange
  
  return count;
}


ssize_t translate_write(struct file *filp, const char __user * buf, size_t count, loff_t *f_pos) {
  int err;                    //Error-Wert
  int minor;                  //Minor-Device-Number
  int i;                      //Zaehler-Variable
  struct translate_dev *dev;  //Translate-Informationen
  char stringBuffer[TRANSLATE_BUFSIZE];        //Buffer fuer die Rueckgabe
  
  dev = filp->private_data;   //Nehme Translate-Informationen von der Benutzereingabe
  minor = dev->minor_number;  //Speichere die Minor-Device-Number
  
  printk(KERN_ALERT "Translate: starting to write\n");
  
  err = down_interruptible(&dev->sem);  //Warte auf den Semaphoren
  if(err) {                             //Wenn der Prozess per Signal geweckt wurde
    return -ERESTARTSYS;                //  Gib ERESTARTSYS zurueck
  }
  
  while(dev->fillcount >= translate_bufsize) {                                    //Solange der Buffer voll ist
    up(&dev->sem);                                                                //  gib den Semaphoren wieder frei
    
    err = wait_event_interruptible(dev->queue, dev->fillcount < translate_bufsize);// Reihe dich in die Warteschlange ein
    if(err) {                                                                     //  Wenn der Prozess per Signal geweckt wurde
      return -ERESTARTSYS;                                                        //    Gib ERESTARTSYS zurueck
    }
    
    err = down_interruptible(&dev->sem);                                          //  Warte auf den Semaphoren
    if(err) {                                                                     //  Wenn der Prozess per Signal geweckt wurde
      return -ERESTARTSYS;                                                        //    Gib ERESTARTSYS zurueck
    }
  }
  
  count = min(count, (size_t) (translate_bufsize - dev->fillcount)); //Setze Count auf das Minimum von Count und den Uebrigen Buffer-Elementen
  if(dev->wp >= dev->rp) {                                           //Wenn der Read-Pointer einen Wrap-Around hatte
    count = min(count, (size_t) (dev->end - dev->wp));               //  Setze Count auf das Minimum von Count und den Elementen bis zum Ende der Liste
  } else {                                                           //Sonst
    count = min(count, (size_t) (dev->rp - dev->wp));                //  Setze Count auf das Minimum von Count und den Elementen bis zum Read-Pointer
  }
  
  if(minor == MINORZERO) {                          //Wenn codiert werden muss
    err = copy_from_user(stringBuffer, buf, count); //  Kopiere die zu codierenden Zeichen aus dem User-Buffer in stringBuffer
    if(err) {                                       //  Wenn dabei ein Fehler passiert ist
      return -EFAULT;                               //    Gib EFAULT zurueck
    }
    for(i = 0; i < count; i++) {                    //  Decodiere und schreibe die Zeichen ab dem Write-Pointer in den Geraete-Buffer
      dev->wp[i] = encode_char(stringBuffer[i]);
    }
  } else {                                          //Sonst muss nicht codiert werden
    err = copy_from_user(dev->wp, buf, count);      //  Kopiere count elemente aus buf ab wp
    if(err) {                                       //  Wenn nicht count elemente Kopiert wurden
      return -EFAULT;                               //    Gebe EFAULT zueueck
    }
  }
  
  dev->wp += count;          //Setze den Write-Pointer um count Elemente nach vorne
  if(dev->wp == dev->end) {  //Wenn der Write-Pointer am Ende des Buffers angekommen ist
    dev->wp = dev->buffer;   //  Setze ihn an den Anfang des Buffers
  }
  
  dev->fillcount += count;   //Erhoehe den Fillcount um count
  
  printDevice(dev);
  up(&dev->sem);             //Gib den Semaphoren wieder frei
  wake_up(&dev->queue);      //Wecke alle Elemente aus der Warteschlange
  printk(KERN_ALERT "Translate: %d Zeichen in translate%d geschrieben\n", (int) count, minor);

  return count;              //Gib count zurueck
}


int translate_open(struct inode *inode, struct file *filp) {
  struct translate_dev *dev;       //Translate-Device
  int minor;                       //Minor-Number
  
  minor = MINOR(inode->i_rdev);    //Fische die Minor-Number aus inode
  dev = &translate_devices[minor]; //Setze dev auf das Korrekte Translate-Device im Speicher
  dev->minor_number = minor;       //Speicher die ermittelte Minor-Number ab
  filp->private_data = dev;        //Speichere die Referenz auf das Device in filp->private_data ab
  
  printk(KERN_ALERT "Translate: translate_open wurde mit minor_number %d gestartet\n", minor);
  
  if(filp->f_mode & FMODE_READ) {  //Wenn gelesen werden soll
    if(dev->nreaders > 0) {        //  Wenn schon ein anderer Prozess liest
      return -EBUSY;               //    Gib EBUSY zurueck
    }
    dev->nreaders++;               //  Sonst inkrementiere die Anzahl der Reader
  }
  
  if(filp->f_mode & FMODE_WRITE) { //Wenn geschrieben werden soll
    if(dev->nwriters > 0) {        //  Wenn schon ein anderer Prozess schreibt
      return -EBUSY;               //    gib EBUSY zurueck
    }
    dev->nwriters++;               //  Sonst inkrementiere die Anzahl der Writer
  }
  nonseekable_open(inode, filp);   //Setzt LSEEK, PREAD und PWRITE in filp auf 0 um den Kernel darueber zu
                                   //Informieren, dass im Buffer nicht gesucht werden kann
  
  printk(KERN_ALERT "Translate: translate_open wurde beendet\n");
  return 0;                        //Gebe 0 zum erfolgreichen Abschliessen der Funktion zurueck
}


int translate_close(struct inode *inode, struct file *filp) {
  struct translate_dev *dev;        //Translate-Device
  
  dev = filp->private_data;         //Speichere die Referenz auf die Daten in filp zwischen
  
  printk(KERN_ALERT "Translate: translate_close wurde mit minor_number %d gestartet\n", dev->minor_number);
  
  if(filp->f_mode & FMODE_READ) {   //Wenn der Prozess Schreibrechte hatte
    dev->nreaders--;                //  Dekrementiere die Anzahl der Reader
  }
  
  if(filp->f_mode & FMODE_WRITE) {  //Wenn der Prozess Leserechte hatte
    dev->nwriters--;                //  Dekrementiere die ANzahl der Leser
  }
  
  printk(KERN_ALERT "Translate: translate_close wurde beendet\n");
  return 0;                         //Gebe 0 zum erfolgreichen Abschliessen der Funkion zurueck
}


int init_module(void) {
  int i;                        //Zaehler-Variable
  int err;                      //Error-Variable
  struct translate_dev *device; //Translate-Device
  dev_t dev;
  
  dev = 0;
  
  printk(KERN_ALERT "Translate: Starte Initialisierung...\n");
  
  if(strlen(translate_subst) > TRANSLATE_SUBST_LENGTH) {  //Wenn der vom Nutzer eingegebene String laenger als die Translate-Tabelle ist
    printk(KERN_ALERT "Translate: Eingegebener Translate_String ist zu lang - dieser dart maximal %d Zeichen haben - Abbruch\n", ALPHABET_LENGTH * 2);
    return -1;                                            //  Gib unverichteter Dinge -1 zurueck
  }
  
  if(strlen(translate_subst) < TRANSLATE_SUBST_LENGTH) {                 //Wenn der eingegebene String kuerzer als die Translate-Tabelle ist
    printk(KERN_ALERT "Translate: Unvollstaendige Tabelle - ergaenze automatisch\n");
    for(i = strlen(translate_subst); i <= TRANSLATE_SUBST_LENGTH; i++) { //  Haenge an den eingegebenen String das Ende der original-Tabelle an
      translate_subst[i] = translate_subst_def[i];                       //  Hierbei wird bis <= gezahelt, weil so die terminierende null mitkopiert wird
    }
    translate_subst[TRANSLATE_SUBST_LENGTH] = 0;
  }
  
  if(translate_bufsize <= 0) {                         //Wenn der Buffer <= 0 ist
    printk(KERN_ALERT "Translate: Der Buffer ist zu klein\n");
    return -1;                                         //  Abbruch
  }
  
  err = register_chrdev(dev_major, dev_name, &fops);   //Fordere dynamische Major-Number vom Kernel an
  if(err < 0) {                                        //Wenn keine Nummer zugewiesen werden konnte
    printk(KERN_ALERT "Translate: Es konnte keine Major-Nummer zugewiesen werden - abbruch\n");
    return -1;                                         //  Abbruch
  }
  
  dev_major = err;                                     //Speichere Major-Number
  printk(KERN_ALERT "Translate: Die zugewiesene Major-Nummer ist %d\n", dev_major);
  
  translate_devices = kmalloc(count_of_devices * sizeof(struct translate_dev), GFP_KERNEL);//Fordere Speicher fuer das Geraet vom Kernel an
  if(!translate_devices) {                                                                 //Wenn nicht genug Speicher im Kernel zur Verfuegung steht
    printk(KERN_ALERT "Translate: Es konnte kein Kernel-Speicher zugewiesen werden - abbruch\n");
    err = -ENOMEM;                                                                         //  Abbruch und zur fail-Sequenz mit ENOMEM
    goto fail;
  }
  
  memset(translate_devices, 0, count_of_devices * sizeof(struct translate_dev));              //Setze den Inhalt des erhaltenen Speichers auf 0
  for(i = 0; i < count_of_devices; i++) {                                                     //Fuer jedes Minor-Geraet
    printk(KERN_ALERT "Translate: Allozierung fuer den Buffer von Geraet-Nummer %d\n", i);
    device = &translate_devices[i];                                                        //  Speicher eine lokale Referenz auf dieses Geraet
    sema_init(&device->sem, 1);                                                            //  Initialisiere den Semaphoren mit 1
    init_waitqueue_head(&device->queue);
    device->buffersize = translate_bufsize;                                                //  Setzte die Buffergroesse auf translate_bufsize

    device->buffer = kmalloc(translate_bufsize * sizeof(char), GFP_KERNEL);                //  Forder Speicher fuer den Buffer des Geraets an
    if(!device->buffer) {                                                                  //    Wenn nicht genug Speicher zur Verfuegung stand
      err = -ENOMEM;                                                                       //      Abbruch und zur fail-Sequenz mit ENOMEM
      goto fail;
    }

    device->end = device->buffer + device->buffersize;                                     //  Setze das Ende des Buffers
    device->rp = device->wp = device->buffer;                                              //  Setze den Read- und Write-Pointer an dern Anfang des Buffers
    device->fillcount = 0;                                                                 //  Setze den Fillcount auf 0
  }
  
  printk(KERN_ALERT "Translate: Initialisierung abgeschlossen\n");
  return 0;                                                                                //Gebe 0 zum erfolgreichen Abschliessen der Funktion zurueck
  
  
  fail:                //Fail-Region
    cleanup_module();  //  Rufe Cleanup auf
    return err;        //  Brich mit dem eingegebenen Fehler ab
}

void cleanup_module(void) {
  int i;                                            //Zaehl-Variable
  
  if(!translate_devices) {                          //Wenn kein Geraet gefunden wurde
    return;                                         //  Brich ab
  }
  
  for(i = 0; i < count_of_devices; i++) {           //Fuer jede Minor-Number
    kfree(translate_devices[i].buffer);             //  Gib den Buffer des jeweiligen Geraets frei
  }
  
  kfree(translate_devices);                         //Gib den Speicher der Geraete frei
  unregister_chrdev(dev_major, dev_name);           //Deregistrieren des Treibers
  translate_devices = NULL;                         //Nulle den Pointer aus
}
  
//########## Hilfsfunktionen ##########
char encode_char(char c) {
  if(c >= 'A' && c <= 'Z') {                             //Wenn c zwischen 'A' und 'Z' liegt
    return translate_subst[(c - 'A') + ALPHABET_LENGTH]; //  Gib den dazu passenden Kleinbuchstaben zurueck
  } else if (c >= 'a' && c <= 'z') {                     //Sonst wenn c zwischen 'a' und 'z' liegt
    return translate_subst[(c - 'a')];                   //  Gib den dazu passenden Grossbuchstaben zurueck
  }
  return c;                                              //Wenn keiner dieser Faelle eingetreten ist gib das Original zurueck
}

char decode_char(char c) {
  int index;                                               //Index zum Zwischenspeichern
  
  if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {   //Wenn c ein Buchstabe des Alphabets ist
    index = indexOf(c);                                    //  Suche den Index dieses Buchstaben im Substitutionsstring heraus
	if(index < 0) {                                    //  Wenn der Buchstabe nicht gefunden wurde
	  printk(KERN_ALERT "Translate: Character konnte nicht gefunden werden, Substitutionsstring moeglicherweise Fahlerhaft\n"); 
	                                                   //   Gib einen Fehler aus
	} else if(index < ALPHABET_LENGTH) {               //  Wenn der Buchstabe in der ersten Haelfte des Substitutionsstrings vorkam
	  return 'a' + index;                              //    Gib den passenden Kleinbuchstaben zurueck
	} else {                                           //  Sonst
	  return 'A' + (index - ALPHABET_LENGTH);          //    Gib den passenden Grossbuchstaben zurueck
	}
  }
  return c;                                                //Wenn c kein Buchstabe des Alphabets war, gib ihn unveraendert zurueck
}

int indexOf(char c) {
  char *p;                        //Char zum Speichern
  p = strchr(translate_subst, c); //Suche das erste Vorkommen von c im Substitutionsstring heraus
  
  if(p == NULL) {                 //  Wenn c nicht gefunden werden konnte
    printk(KERN_ALERT "Translate: Coult not find char\n");
	return -1;                //    Gib -1 als Fehlerwert zurueck
  }
  
  return (int) (p - translate_subst); //Gib den Pointer auf das erste Vorkommen - den Anfang des Substitutionsstrings als Integer aus
}

void printDevice(struct translate_dev *dev) {
  printk(KERN_ALERT "Translate: ----------BEGIN PRINT DEVICE---------\n");
  printk(KERN_ALERT "Translate: Device translate%d\n", dev->minor_number);
  printk(KERN_ALERT "Translate: Buffersize: %d fillcount: %d\n", dev->buffersize, dev->fillcount);
  printk(KERN_ALERT "Translate: Anzahl der Reader: %d\n", dev->nreaders);
  printk(KERN_ALERT "Translate: Anzahl der Writer: %d\n", dev->nwriters);
  printk(KERN_ALERT "Translate: ---------- END PRINT DEVICE ---------\n");
}