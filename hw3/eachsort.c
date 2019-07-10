#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void quicksort(int*,int,int);
void quickswap(int*,int,int);
void heap(int*,int,int);
void heapsort(int*,int,int);
void heapswap(int*,int,int);
struct timespec diff(struct timespec start, struct timespec end);

long quickcomp = 0;
long quickex = 0;
long heapcomp = 0;
long heapex = 0;

int main(int argc, char const *argv[]){
    int v = 1;
    for(int SIZE = 2;SIZE > 0;SIZE *= 2,v++){
        printf("-------------------------------------------\nWhen size is = %d(2^%d)\n",SIZE,v);
        srand(time(NULL));
        struct timespec start,end;
        // int toquick[SIZE];
        // int toheap[SIZE];
        int* toquick = (int *)malloc(sizeof(int)*SIZE);
        int* toheap = (int *)malloc(sizeof(int)*SIZE);
        for(int i = 0;i<SIZE;i++){
            toquick[i] = rand()%SIZE;
            toheap[i] = toquick[i];
        }
        clock_gettime(CLOCK_MONOTONIC,&start);
        quicksort(toquick,0,SIZE-1);
        clock_gettime(CLOCK_MONOTONIC,&end);
        struct timespec quick_time = diff(start,end);
        long quick_diff = quick_time.tv_sec * 1000000000 + quick_time.tv_nsec;
        printf("\nquicksort comparison: %ld\nquicksort exchange: %ld\n",quickcomp,quickex);
        printf("quicksort time : %lu (ns.)\n",quick_diff);
        clock_gettime(CLOCK_MONOTONIC,&start);
        heapsort(toheap,0,SIZE-1);
        clock_gettime(CLOCK_MONOTONIC,&end);
        struct timespec heap_time = diff(start,end);
        long heap_diff = heap_time.tv_sec * 1000000000 + heap_time.tv_nsec;
        printf("\nheapsort comparison: %ld\nheapsort exchange: %ld\n",heapcomp,heapex);
        printf("heapsort time: %lu (ns.)\n",heap_diff);
        free(toquick);
        free(toheap);
    }
    return 0;
}

void quicksort(int* arr,int left,int right){
    int pivotitem = left;
    int low = left;
    int high = right;

    while (quickcomp++ > -1 && low < high){
        while (quickcomp++ && arr[low] <= arr[pivotitem] && quickcomp++ && low < right){
            low += 1;
        }
        while (quickcomp++ && arr[high] >= arr[pivotitem] && quickcomp++ && high > left){
            high -= 1;
        }
        if(quickcomp++ && low < high){
            quickswap(arr,low,high);

            continue;
        }
        quickswap(arr,pivotitem,high);
        quicksort(arr,left,high-1);
        quicksort(arr,high+1,right);
    }
    
}

void quickswap(int* arr,int one,int other){
    quickex++;
    int temp = arr[one];
    arr[one] = arr[other];
    arr[other] = temp;
}

void heap(int* arr, int num,int length){
    for (int i = (num << 1) | 1; i < length; num = i, i = i << 1 | 1, heapcomp++){
        if (heapcomp++ && i + 1 < length && heapcomp++ && arr[i + 1] > arr[i]) ++i;
        if (heapcomp++ && arr[i] <= arr[num]) return;
        heapswap(arr,i,num);
    }
}

void heapsort(int* arr,int low,int high){
    int* h = arr + low;
    int length = high - low + 1;
    for(int i = length / 2 - 1;i >= 0;i--,heapcomp++) heap(h, i, length);
    while(heapcomp++ > -1 && --length >= 1){
        heapswap(h, 0, length);
        heap(h, 0, length);
    }
}

void heapswap(int* arr,int one,int other){
    heapex++;
    int temp = arr[one];
    arr[one] = arr[other];
    arr[other] = temp;
}

struct timespec diff(struct timespec start, struct timespec end)
{
	struct timespec temp;
	if ((end.tv_nsec-start.tv_nsec)<0) {
		temp.tv_sec = end.tv_sec-start.tv_sec-1;
		temp.tv_nsec = 1000000000+end.tv_nsec-start.tv_nsec;
	} else {
		temp.tv_sec = end.tv_sec-start.tv_sec;
		temp.tv_nsec = end.tv_nsec-start.tv_nsec;
	}
	return temp;
}