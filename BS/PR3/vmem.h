/* File: vmem.h
 * Global Definitions for BSP3 sample solution
 * Model of virtual memory management
 */

/*SIGUSR1: Signalisiert, dass mmanage 
 *
 */

#ifndef VMEM_H
#define VMEM_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>
#include <semaphore.h>
#include <sys/stat.h>
#include <sys/fcntl.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <pthread.h>

#define SHMKEY          "/vmem.h"
#define SHMPROCID       'C'

typedef unsigned int Bmword;    /* Frame bitmap */

/* Sizes */
#define VMEM_VIRTMEMSIZE 1024   /* Process address space / items */
#define VMEM_PHYSMEMSIZE  128   /* Physical memory / items */
#define VMEM_PAGESIZE       8   /* Items per page */
#define VMEM_NPAGES     (VMEM_VIRTMEMSIZE / VMEM_PAGESIZE)      /* Total 
                                                                   number 
                                                                   of
                                                                   pages 
                                                                 */
#define VMEM_NFRAMES (VMEM_PHYSMEMSIZE / VMEM_PAGESIZE) /* Number of
                                                           available
                                                           frames */
#define VMEM_LASTBMMASK (~0U << (VMEM_NFRAMES % (sizeof(Bmword) * 8)))
#define VMEM_BITS_PER_BMWORD     (sizeof(Bmword) * 8)
#define VMEM_BMSIZE     ((VMEM_NFRAMES - 1) / VMEM_BITS_PER_BMWORD + 1) //Wie gross ist die Anzahl bvenötigter BMWords um den Speicher zu verwalten | 1 bit pro Frame

/* Page Table */
#define PTF_PRESENT     1
#define PTF_DIRTY       2       /* store: need to write */
#define PTF_USED        4       /* For clock algo only */
#define PTF_USED1       8       /* For clock2 algo only */

struct pt_entry {
    int flags;                  /* see defines above */
    int frame;                  /* Frame index */
};

struct vmem_adm_struct {
    int size;           //Groesse unserer Speicherstruktur im Speicher
    pid_t mmanage_pid;  //Prozessid
    int shm_id;         //ID des geteilten Speichers
    sem_t sema;                 /* Coordinate acces to shm */
    int req_pageno;             /* Number of requested page */
    int next_alloc_idx;         /* Next frame to allocate (FIFO, CLOCK) 
                                 */
    int pf_count;               /* Page fault counter */
    Bmword bitmap[VMEM_BMSIZE]; /* 0 = free */
};

struct pt_struct {
    struct pt_entry entries[VMEM_NPAGES];    //Informationen über jede Page des Prozesses
    int framepage[VMEM_NFRAMES];        /* pages on frame */ // Nummern der Pages in den Frames
};

/* This is to be located in shared memory */
struct vmem_struct {
    struct vmem_adm_struct adm;                //Administrierungsdaten
    struct pt_struct pt;                       //Pagetable
    int data[VMEM_NFRAMES * VMEM_PAGESIZE];    //Daten
};

#define SHMSIZE (sizeof(struct vmem_struct))

#endif /* VMEM_H */


/*
Frame: Speicherseite im Physikalische Speicher
Page:  Speicherseite im Logischen Speicher

Wir lesen Daten aus:
- Wir geben eine logische Adresse logAdr an vmaccess. Dieser Prozess geht mit der dazu passenden PageNummer 
    (logAdr / VMEM_PAGESIZE) an das Array entries und prueft ob die Page im Physikalischen Speicher liegt 
    pt_entry.flags == present
- Wenn ja, dann nehmen wir pt_entry.frame * VMEM_PAGESIZE + Offset (logAdr % VMEM_PAGESIZE) aus data. So 
    erhalten wir den gewuenschten Wert
- Sonst muss mmanage die page in vmem schubsen, sodass vmaccess nun auslesen kann

Wir schreiben Daten:
- Wir erhalten num (zu speichernde Zahl) und index (index der Zahl)
- Wir pruefen, ob sich die Page (index / VMEM_PAGESIZE) im Physikalischen Speicher befindet
- Wenn ja: Wir setzen das dirty-Flag der fraglichen Page in der Pagetable
- Dann schreiben wir num in data[pt_entry.frame * VMEM_PAGESIZE + Offset]
 */
