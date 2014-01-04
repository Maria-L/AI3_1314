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

char *translate_subst_def = TRANSLATE_SUBST; //Fixe Translate-Tabelle
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
  
  dev = filp->private_data;  //Nehme Translate-Informationen von der Benutzereingabe
  minor = dev->minor_number; //Speichere die Minor-Device-Number
  
  printk(KERN_ALERT "Translate: %s starting to read\n", buf);
  
  err = down_interruptible(&dev->sem);  //Warte auf den Semaphoren
  if(err) {                             //Wenn der Prozess per Signal geweckt wurde
    return -ERESTARTSYS;                //  Gib ERESTARTSYS zurueck
  }
  
  if(dev->fillcount <= ZEROBUFFER) {                                //Wenn der Buffer leer ist
    printk(KERN_ALERT "Translate: Empty buffer, waiting for elements\n");

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
  
  if(dev->fillcount == 0) {  //FRAGWUERDIG?!?
    return 0;
  }
  
  if(dev->wp > dev->rp) {                             //Wenn der Write-Pointer hinter dem Read-Pointer steht
    count = min(count, (size_t) (dev->wp - dev->rp)); //  Setze count auf das minimum von count und den uebrigen Elementen vom wp bis zum rp
  } else {                                            //Sonst
    count = min(count, (size_t) (dev->end - dev->rp));//  Setze count auf das minimum von count und den uebrigen Elementen bis zum Ende des Buffers
  }
  
  if(minor == ONE) {                   //Wenn Decodiert werden muss
    for(i = 0; i < count; i++) {       //  Decodiere und schreibe count mal in den User-Speicher
      buf[i] = decode_char(dev->rp[i]);
    }
  } else {                             //Wenn nicht decodiert werden muss
    err = copy_to_user(buf, dev->rp, count);//Kopiere die Ergebnisse in den User-Speicher
    if(err) {                          //  Wenn dabei ein Fehler passiert ist
      up(&dev->sem);                   //    Gib den Semaphoren wieder frei           #### FRAGWUERDIG ####
      return -EFAULT;                  //    Gebe EFAULT zurueck
    }
  }
  
  dev->rp += count;        //Setze den Read-Pointer weiter
  if(dev->rp == dev->end) {   //Wenn der Read-Pointer am Ende des Buffers angekommen ist
    dev->rp = dev->buffer; //  Setze den Read-Pointer auf den Anfang des Buffers
  }
  
  dev->fillcount -= count; //Dekrementiere den Fill-Count
  
  printk(KERN_ALERT "Translate: Read %li Chars from translate%d\n", (long) count, minor);
  up(&dev->sem);           //Gib den Semaphoren wieder frei
  wake_up(&dev->queue);    //Wecke die Prozesse in der Warteschlange
  
  return count;
}


ssize_t translate_write(struct file *filp, const char __user * buf, size_t count, loff_t *f_pos) {
  int err;                    //Error-Wert
  int minor;                  //Minor-Device-Number
  int i;                      //Zaehler-Variable
  struct translate_dev *dev;  //Translate-Informationen
  
  dev = filp->private_data;   //Nehme Translate-Informationen von der Benutzereingabe
  minor = dev->minor_number;  //Speichere die Minor-Device-Number
  
  printk(KERN_ALERT "Translate: %s starting to write\n", buf);
  
  err = down_interruptible(&dev->sem);  //Warte auf den Semaphoren
  if(err) {                             //Wenn der Prozess per Signal geweckt wurde
    return -ERESTARTSYS;                //  Gib ERESTARTSYS zurueck
  }
  
  if(dev->fillcount >= translate_bufsize) {                                       //Wenn der Buffer voll ist
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
  if(dev->wp >= dev->rp) {                                           //Wenn der Readpointer einen Wrap-Around hatte
    count = min(count, (size_t) (dev->end - dev->wp));               //  Setze Count auf das Minimum von Count und den Elementen bis zum Ende der Liste
  } else {                                                           //Sonst
    count = min(count, (size_t) (dev->rp - dev->wp - 1));            //  Setze Count auf das Minimum von Count und den Elementen bis zum Read-Pointer
  }
  
  if(minor == ONE) {                           //Wenn nicht codiert werden muss
    err = copy_from_user(dev->wp, buf, count); //  Kopiere count elemente aus buf ab wp
    if(err) {                                  //  Wenn nicht count elemente Kopiert wurden
      up(&dev->sem);                           //    Gib den Semaphoren frei
      return -EFAULT;                          //    Gebe EFAULT zueueck
    }
  } else {                                     //Sonst muss codiert werden
    for(i = 0; i < count; i++) {               //  Kopiere und codiere count Elemente aus buf in wp
      dev->wp[i] = encode_char(buf[i]);
    }
  }
  
  dev->wp += count;          //Setze den Write-Pointer um count Elemente nach vorne
  if(dev->wp == dev->end) {  //Wenn der Write-Pointer am Ende des Buffers angekommen ist
    dev->wp = dev->buffer;   //  Setze ihn an den Anfang des Buffers
  }
  
  dev->fillcount += count;   //Erhoehe den Fillcount um count
  wake_up(&dev->queue);      //Wecke alle Elemente aus der Warteschlange
  up(&dev->sem);             //Gib den Semaphoren wieder frei
  return count;              //Gib count zurueck
}


int translate_open(struct inode *inode, struct file *filp) {
  struct translate_dev *dev;       //Translate-Device
  int minor;                       //Minor-Number
  
  minor = MINOR(inode->i_rdev);    //Fische die Minor-Number aus inode
  dev = &translate_devices[minor]; //Setze dev auf das Korrekte Translate-Device im Speicher
  dev->minor_number = minor;       //Speicher die ermittelte Minor-Number ab
  filp->private_data = dev;        //Speichere die Referenz auf das Device in filp->private_data ab
  //############Obere zwei Zeilen unter Umstaenden vertauschen?
  
  printk(KERN_ALERT "Translate: translate_open wurde mit minor_number %d gestartet\n", minor);
  
  if(filp->f_mode & FMODE_READ) {  //Wenn gelesen werden soll
    if(dev->nreaders > ZERO) {     //  Wenn schon ein anderer Prozess liest
      return -EBUSY;               //    Gib EBUSY zurueck
    }
    dev->nreaders++;               //  Sonst inkrementiere die Anzahl der Reader
  }
  
  if(filp->f_mode & FMODE_WRITE) { //Wenn geschrieben werden soll
    if(dev->nwriters > ZERO) {     //  Wenn schon ein anderer Prozess schreibt
      return -EBUSY;               //    gib EBUSY zurueck
    }
    dev->nwriters++;               //  Sonst inkrementiere die Anzahl der Writer
  }
  nonseekable_open(inode, filp);   //Setzt LSEEK, PREAD und PWRITE in filp auf 0 um den Kernel darueber zu
                                   //Informieren, dass im Buffer nicht gesucht werden kann
  
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
  
  return 0;                         //Gebe 0 zum erfolgreichen Abschliessen der Funkion zurueck
}


int init_module(void) {
  int i;                        //Zaehler-Variable
  int err;                      //Error-Variable
  struct translate_dev *device; //Translate-Device
  dev_t dev;                    //Standart-Device                    ##########Notwendig?!?##########
  
  dev = 0;                      //Setze dev auf 0                    ##########Notwendig?!?##########
  
  printk(KERN_ALERT "Translate: Starte Initialisierung...\n");
  
  if(strlen(translate_subst) > TRANSLATE_SUBST_LENGTH) {  //Wenn der vom Nutzer eingegebene String laenger als die Translate-Tabelle ist
    return -1;                                            //  Gib unverichteter Dinge -1 zurueck
  }
  
  if(strlen(translate_subst) < TRANSLATE_SUBST_LENGTH) {                 //Wenn der eingegebene String kuerzer als die Translate-Tabelle ist
    for(i = strlen(translate_subst); i <= TRANSLATE_SUBST_LENGTH; i++) { //  Haenge an den eingegebenen String das Ende der original-Tabelle an
      translate_subst[i] = translate_subst_def[i];                       //  Hierbei wird bis <= gezahelt, weil so die terminierende null mitkopiert wird
    }
  }
  
  if(translate_bufsize <= ZERO) {  //Wenn der Buffer <= 0 ist
  dev_major = err;
  printk(KERN_ALERT "Translate: Die zuge                                                                                                                                                                                                                                                           
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
          æſł@€€łł@ſæſðŋŋħæſðæſð
          
                  æſł@€€łł@ſæſðŋŋħæſðæſð
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
sssssssssssssssssssSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSsssssSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS












































  
                                                                                                            
//########## Hilfsfunktionen ##########