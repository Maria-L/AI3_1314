/* Header file for vmappl.c
 * File: vmappl.h
 * Prof. Dr. Wolfgang Fohl, HAW Hamburg
 * 2013
 */

#ifndef VMAPPL_H
#define VMAPPL_H

#include <stdio.h>
#include <stdlib.h>
#include "vmaccess.h"


#define LENGTH 550


void init_data(int length);

void quicksort(int l, int r);

void sort(int length);

void swap(int addr1, int addr2);

#define NDISPLAYCOLS 8
void display_data(int length);
#endif
