#include <stdio.h>
#include <time.h>

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

int main(void){
    int a = 1;
    int b = 1;
    int c;
    struct timespec start,end;
    for(int n = 0; n < 10 ; n++){
        clock_gettime(CLOCK_MONOTONIC,&start);
        c = a * b;
        clock_gettime(CLOCK_MONOTONIC,&end);
        struct timespec d = diff(start,end);
        long e = d.tv_sec * 1000000000 + d.tv_nsec;
        printf("%d - th : %ld \n",n+1,e);
    }
}