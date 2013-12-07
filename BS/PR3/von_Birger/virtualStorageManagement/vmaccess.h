/* Header file for vmappl.c
 * File: vmappl.h
 * Prof. Dr. Wolfgang Fohl, HAW Hamburg
 * 2010
 */
#include "vmem.h"
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <signal.h>
#include <sys/types.h>
#include <pthread.h>
#ifndef VMACCESS_H
#define VMACCESS_H

/** Connect to shared memory (key from vmem.h) */
void vm_init(void);

//Lesen der Daten einer Page
int get_page(int page, int offset);

//Setzen er Used-Bits für die CLOCK-Algorithmen
void set_used_bit(int page);

//Setzen von Bits
void set_bit(int *need_to_set,int bits);

/** Read from "virtual" address */
int vmem_read(int address);

/** Write data to "virtual" address */
void vmem_write(int address, int data);

//Tatsaechliches Schreiben von Daten zu einer Page
void write_page(int page, int offset, int data);

//Berechnen des Indizes durch Page-Nummer und Offset
int get_index_by_page_offset(int page, int offset);

//Check ob der Shared Memory schon verbunden ist
void check_shared_memory();

#endif
