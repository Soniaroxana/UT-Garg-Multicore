#include "omp.h"
#include <iostream>
#include <fstream>
#include <stdlib.h>
using namespace std;

double MonteCarloPi(int s) {
  // TODO: Implement your Monte Carlo Method
  // parameter s: number of points you randomly choose
  // return the value of pi
  double R = 1.0;
  double circleArea=0.0;
  double squareArea=0.0;
  double pi;
  double x,y;

  double dtime = omp_get_wtime();
  #pragma omp parallel for private(x,y) reduction(+:circleArea)
  for(int i=0; i<s; i++)
  {
    double x = (double)rand()/(double)RAND_MAX;
    double y = (double)rand()/(double)RAND_MAX;
    if (x*x+y*y <= R*R){
      circleArea++;
    }
  }
  pi = 4.0*circleArea/s;
  dtime = omp_get_wtime() - dtime;
  return pi;
}