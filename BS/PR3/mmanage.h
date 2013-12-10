/** Definitions for virtual memory management model
 * File: mmanage.h
 *
 * Prof. Dr. Wolfgang Fohl, HAW Hamburg
 * 2013
 */
#ifndef MMANAGE_H
#define MMANAGE_H
#include "vmem.h"
#include <limits.h>
#include <signal.h>
#include <unistd.h>
#include <sys/mman.h>


#define CLOCK

/** Event struct for logging */
struct logevent {
  int req_pageno;
  int replaced_page;
  int alloc_frame;
  int pf_count;
  int g_count;
};

/** Prototypes */
void sighandler(int signo);

void vmem_init(void);

void fetch_page(int pt_idx);

void store_page(int pt_idx);

void page_fault(void);

int find_remove_fifo(void);

int find_remove_clock(void);

int find_remove_clock2(void);

int find_free_frame(void);

void init_pagefile(const char *pfname);

void cleanup(void);

void logger(struct logevent le);

void make_dump(void);

/** Misc */
#define MMANAGE_PFNAME "./pagefile.bin"    /**< pagefile name */
#define MMANAGE_LOGFNAME "./logfile.txt"   /**< logfile name */

#define NEXTVALUE ','
#define NEXTPAGE  '\n'

#define VMEM_ALGO_FIFO  0
#define VMEM_ALGO_LRU   1
#define VMEM_ALGO_CLOCK 2
#define VMEM_ALGO_CLOCK2 3

#define SEED_PF 290913        /**< Get reproducable pseudo-random numbers for
                           init_pagefile */

#endif /* MMANAGE_H */
