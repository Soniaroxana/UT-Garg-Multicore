    #include <stdio.h>
    
 main ()  {
    
    int   i, j, k, n;
    float a[400][400], result;
    
    /* Some initializations */
    n = 400;
    result = 0.0;
    for (i=0; i < n; i++)
     for (j=0; j < n; j++)
      {
      a[i][j] = i * 1.0 + j;
      }
    
    for (k=0; k < 1000; k++) {
       result = k * 1.0;
       // printf("initial result= %f\n",result);

       for (i=0; i < n; i++)
     	for (j=0; j < n; j++)
       	    result = result + a[i][j];

    }
       printf("Final result= %f\n",result);
    
    
 }
