#include "omp.h"
#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <time.h>
using namespace std;

bool MatrixMult(int rowA, int colA, double* A, int rowB, int colB, double* B,
  double* C, int T) {
  // TODO: Implement your parallel multiplication for two matrices of doubles
  // by using OpenMP
  // parameter rowA: indicates the number of rows of the matrix A
  // parameter colA: indicates the number of columns of the matrix A
  // parameter A: indicates the matrix A
  // parameter rowB: indicates the number of rows of the matrix B
  // parameter colB: indicates the number of columns of the matrix B
  // parameter B: indicates the matrix B
  // parameter C: indicates the matrix C, which is the results of A x B
  // parameter T: indicates the number of threads
  // return true if A and B can be multiplied; otherwise, return false
  if (colA != rowB){
    return false;
  }
  double dtime = omp_get_wtime();
  omp_set_dynamic(0);     // Explicitly disable dynamic teams
  omp_set_num_threads(T);
  #pragma omp parallel for
  for (int i=0; i<rowA; i++){
      for(int j=0; j<colB; j++){
        double term=0.0;
        #pragma omp parallel for reduction (+:term)
        for (int k=0; k<colA; k++){
          term+=A[i*colA+k]*B[k*colB+j];
        }
        C[i*colB+j]=term;
      }
  }
  dtime = omp_get_wtime() - dtime;
  return true;
}

double* ReadMatrixFromFile(const char* file, int &rowA, int &colA){
  ifstream myfile;
  double* A;
  myfile.open(file);
  if (!myfile){
      cout << "File cannot be open for read \n";
      return NULL;
  }
  myfile >> rowA;
  myfile >> colA;
  A = (double *)malloc(rowA*colA*sizeof(double));
  for(int i=0; i<rowA; i++){
    for (int j=0; j<colA; j++){
      myfile >> A[i*colA+j];
    }
  }
  myfile.close();
  return A;
}

int main(int argc, const char *argv[]) {
  int ROWA;
  int COLA;
  int ROWB;
  int COLB;
  int T;
  int test100=0;

  double* A;
  double* B;
  double* C;

  const char* fileMatrixA;
  const char* fileMatrixB;

  // TODO: Initialize the necessary data
  if (argc < 4){
    cout << "Usage: matrix_mult <fileMatrixA> <fileMatrixB> <numThreads> \n";
    return 0;
  }

  fileMatrixA = argv[1];
  fileMatrixB = argv[2];
  T = atoi(argv[3]);

  A = ReadMatrixFromFile(fileMatrixA,ROWA,COLA);
  if (A==NULL){
      return 0;
  }

  B = ReadMatrixFromFile(fileMatrixB,ROWB,COLB);
  if (B==NULL){
    return 0;
  }

  C = (double *) malloc(ROWA*COLB*sizeof(double));
  cout << ROWA << " " << COLB << endl;
  if(MatrixMult(ROWA, COLA, A, ROWB, COLB, B, C, T)) {
    // TODO: Output the results
    for (int i=0; i<ROWA; i++){
              for (int j=0; j<COLB; j++){
                cout << C[i*COLB+j] << " ";
              }
              cout << "\n";
            }
  } else {
    cout << "the colA != rowB MatrixMult return false" << endl;
  }


  return 0;
}