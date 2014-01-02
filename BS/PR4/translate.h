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

#ifdef TRANSLATE_SUBST_LENGTH
#define TRANSLATE_SUBST_LENGTH (ALPHABET_LENGTH * 2)
#endif

struct translate_dev {
  char *buffer, *end;
  int buffersize, fillcount;
  char *rp, *wp;
  int nreaders, nwriters;
  int minor_number;
  struct semaphore sem;
  struct cdev cdev;
  wait_queue_head_t queue;
};

int init_module(void);
void cleanup_module(void);
int translate_open(struct inode *inode, struct file *filp);
int translate_close(struct inode *inode, struct file *filp);
ssize_t translate_read(struct file *filp, char __user * buf, size_t count, loff_t * f_pos);
ssize_t translate_write(struct file *filp, const char __user * buf, size_t count, loff_t * f_pos);

char encode_char(char inputChar);
char decode_char(char inputChar);
int indexOf(char inputChar);
void printDevice(struct translate_dev *dev);

#endif