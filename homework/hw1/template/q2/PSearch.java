import java.util.concurrent.*;
import java.util.*;

public class PSearch implements Callable<Integer> {
  // TODO: Declare variables
  private int x;
  private int[] A;
  private int begin;
  private int end;

  public PSearch(int x, int[] A, int begin, int end) {
    // TODO: The constructor for PSearch
    // x: the target that you want to search
    // A: the array that you want to search for the target
    // begin: the beginning index (inclusive)
    // end: the ending index (exclusive)
    this.x = x;
    this.A = A;
    this.begin = begin;
    this.end = end;
  }

  public Integer call() throws Exception {
    // TODO: your algorithm needs to use this method to get results
    // You should search for x in A within begin and end
    // Return -1 if no such target
    for (int i=begin; i<end; i++){
      if (A[i]==x){
        return i;
      }
    }
    return Integer.valueOf(-1);
  }

  public static int parallelSearch(int x, int[] A, int n) {
    // TODO: your search algorithm goes here
    // You should create a thread pool with n threads
    // Then you create PSearch objects and submit those objects to the thread
    // pool
    ExecutorService threadPool = Executors.newCachedThreadPool();
    List<Future<Integer>> t = new ArrayList<Future<Integer>>();
    int result=-1;
    for (int i=0; i<n; i++){
      t.add(threadPool.submit(new PSearch(x, A, i * A.length / n, (i+1)*A.length / n)));
    }
    for (int i=0; i<t.size(); i++){
      Future<Integer> threadresult = t.get(i);
      try {
        if (threadresult.get() != -1)
          result = threadresult.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
    threadPool.shutdown();
    return result;
    //return -1; // return -1 if the target is not found
  }
}

