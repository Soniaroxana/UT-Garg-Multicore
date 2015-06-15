import java.lang.System;
import java.util.*;
import java.sql.Timestamp;

public class TestPSearch{
    public static void main(String[] args){
        int[] Array = new int[100000];

        for (int i = 0; i<100000; i++){
            Array[i]=100000-2*i;
        }

        System.out.println("Sequential Begin");
        long startTime = System.currentTimeMillis();
        try {
            int answer = PSearch.parallelSearch(4000,Array,1);
            System.out.println("The answer is " + answer);
        }
        catch (Exception e) { System.err.println(e);}
        System.out.println("Sequential End");
        System.out.println("Exec time = "+(System.currentTimeMillis()-startTime));

        System.out.println("Parallel Begin");
        startTime = System.currentTimeMillis();
        try {
            int answer = PSearch.parallelSearch(4000,Array,8);
            System.out.println("The answer is " + answer);
        }
        catch (Exception e) { System.err.println(e);}
        System.out.println("Parallel End");
        System.out.println("Exec time = "+(System.currentTimeMillis()-startTime));
    }

}