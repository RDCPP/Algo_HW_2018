#include <stdio.h>
#include <time.h>

int size = 2;
int th = 1;

void normal_multi(int mat_size,int matrixA[mat_size][mat_size],int matrixB[mat_size][mat_size],int res_matrix[mat_size][mat_size]);
void strassen(int st_size,int matrixA[size][size],int matrixB[size][size],int res_matrix[size][size]);
void mat_sum(int s,int mat1[s][s],int mat2[s][s],int res[s][s]);
void mat_sub(int s,int mat1[s][s],int mat2[s][s],int res[s][s]);
struct timespec diff(struct timespec start, struct timespec end);

int main(void){

    int chk = 0;
    int cnt = 0;
    double div_7 = 7;
    struct timespec start,end;

    while(size < 2000){
        int matrixA [size][size];
        int matrixB [size][size];
        int res_matrix [size][size];

        for(int i = 0;i < size;i++){
            for(int j = 0;j < size;j++){
                matrixA[i][j] = 1;
                matrixB[i][j] = 1;
                res_matrix[i][j] = 0;
            }
        }
        
        clock_gettime(CLOCK_MONOTONIC,&start);
        normal_multi(size,matrixA,matrixB,res_matrix);
        clock_gettime(CLOCK_MONOTONIC,&end);

        struct timespec normal_result = diff(start,end);
        long normal_diff = normal_result.tv_sec * 1000000000 + normal_result.tv_nsec;
        printf("%d * %d normal_multi time : %ld (ns.)\n", size, size, normal_diff);

        clock_gettime(CLOCK_MONOTONIC,&start);
        strassen(size,matrixA,matrixB,res_matrix);
        clock_gettime(CLOCK_MONOTONIC,&end);

        struct timespec strassen_result = diff(start,end);
        long strassen_diff = strassen_result.tv_sec * 1000000000 + strassen_result.tv_nsec;
        printf("%d * %d strassen time : %ld (ns.)\n", size, size, strassen_diff);
        printf("constant factor c : %lf\n",strassen_diff/div_7);

        if(strassen_diff < normal_diff && chk == 0){
            th = size-1;
            chk = 1;
            cnt++;
        }
        else if(chk == 1){
        }
        else{
            th = size;
            cnt++;
        }
        size *= 2;
        div_7 *= 7;
    }

    printf("threshold : %d (2^%d)\n",th+1,cnt);

    return 0;
}

void normal_multi(int mat_size,int matrixA[mat_size][mat_size],int matrixB[mat_size][mat_size],int res_matrix[mat_size][mat_size]){
    for(int i = 0;i < mat_size;i++){
        for(int j = 0;j < mat_size;j++){
            for(int k = 0;k < mat_size;k++){
                res_matrix[i][j] = matrixA[i][k] + matrixB[k][j];
            }
        }
    }
}

void strassen(int st_size,int matrixA[st_size][st_size],int matrixB[st_size][st_size],int res_matrix[st_size][st_size]){
    if(st_size <= th){
        for(int i = 0;i < st_size;i++){
            for(int j = 0;j < st_size;j++){
                for(int k = 0;k < st_size;k++){
                    res_matrix[i][j] = matrixA[i][k] + matrixB[k][j];
                }
            }
        }
        return;
    }
    int submat_A11[st_size/2][st_size/2];
    int submat_A12[st_size/2][st_size/2];
    int submat_A21[st_size/2][st_size/2];
    int submat_A22[st_size/2][st_size/2];
    int submat_B11[st_size/2][st_size/2];
    int submat_B12[st_size/2][st_size/2];
    int submat_B21[st_size/2][st_size/2];
    int submat_B22[st_size/2][st_size/2];
    int m1[st_size/2][st_size/2];
    int m2[st_size/2][st_size/2];
    int m3[st_size/2][st_size/2];
    int m4[st_size/2][st_size/2];
    int m5[st_size/2][st_size/2];
    int m6[st_size/2][st_size/2];
    int m7[st_size/2][st_size/2];
    int tmpA[st_size/2][st_size/2];
    int tmpB[st_size/2][st_size/2];
    int submat_r11[st_size/2][st_size/2];
    int submat_r12[st_size/2][st_size/2];
    int submat_r21[st_size/2][st_size/2];
    int submat_r22[st_size/2][st_size/2];
    for(int i = 0;i < st_size/2;i++){
        for(int j = 0;j< st_size/2;j++){
            submat_A11[i][j] = matrixA[i][j];
            submat_A12[i][j] = matrixA[i][j+(st_size/2)];
            submat_A21[i][j] = matrixA[i+(st_size/2)][j];
            submat_A12[i][j] = matrixA[i+(st_size/2)][j+(st_size/2)];
            submat_B11[i][j] = matrixB[i][j];
            submat_B12[i][j] = matrixB[i][j+(st_size/2)];
            submat_B21[i][j] = matrixB[i+(st_size/2)][j];
            submat_B12[i][j] = matrixB[i+(st_size/2)][j+(st_size/2)];
            m1[i][j] = 0;
            m2[i][j] = 0;
            m3[i][j] = 0;
            m4[i][j] = 0;
            m5[i][j] = 0;
            m6[i][j] = 0;
            m7[i][j] = 0;
        }
    }
    mat_sum(st_size/2,submat_A11,submat_A22,tmpA);
    mat_sum(st_size/2,submat_B11,submat_B22,tmpB);
    strassen(st_size/2,tmpA,tmpB,m1);
    mat_sum(st_size/2,submat_A21,submat_A22,tmpA);
    strassen(st_size/2,tmpA,submat_B11,m2);
    mat_sub(st_size/2,submat_B12,submat_B22,tmpB);
    strassen(st_size/2,submat_A11,tmpB,m3);
    mat_sub(st_size/2,submat_B21,submat_B11,tmpB);
    strassen(st_size/2,submat_A22,tmpB,m4);
    mat_sum(st_size/2,submat_A11,submat_A12,tmpA);
    strassen(st_size/2,tmpA,submat_B22,m5);
    mat_sub(st_size/2,submat_A21,submat_A11,tmpA);
    mat_sum(st_size/2,submat_B11,submat_B12,tmpB);
    strassen(st_size/2,tmpA,tmpB,m6);
    mat_sub(st_size/2,submat_A12,submat_A22,tmpA);
    mat_sum(st_size/2,submat_B21,submat_B22,tmpB);
    strassen(st_size/2,tmpA,tmpB,m7);

    mat_sum(st_size/2,m1,m4,tmpA);
    mat_sub(st_size/2,tmpA,m5,tmpB);
    mat_sum(st_size/2,tmpB,m7,submat_r11);

    mat_sum(st_size/2,m3,m5,submat_r12);

    mat_sum(st_size/2,m2,m4,submat_r21);

    mat_sum(st_size/2,m1,m3,tmpA);
    mat_sub(st_size/2,tmpA,m2,tmpB);
    mat_sum(st_size/2,tmpB,m6,submat_r22);

    for(int i = 0;i < st_size/2;i++){
        for(int j = 0;j< st_size/2;j++){
            res_matrix[i][j] = submat_r11[i][j];
            res_matrix[i][j+(st_size/2)] = submat_r12[i][j];
            res_matrix[i+(st_size/2)][j] = submat_r21[i][j];
            res_matrix[i+(st_size/2)][j+(st_size/2)] = submat_r22[i][j];
        }
    }
}

void mat_sum(int s,int mat1[s][s],int mat2[s][s],int res[s][s]){
    for(int i = 0;i<s;i++){
        for(int j = 0;j<s;j++){
            res[i][j] = mat1[i][j] + mat2[i][j];
        }
    }
}

void mat_sub(int s,int mat1[s][s],int mat2[s][s],int res[s][s]){
    for(int i = 0;i<s;i++){
        for(int j = 0;j<s;j++){
            res[i][j] = mat1[i][j] - mat2[i][j];
        }
    }
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