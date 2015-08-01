/**
 * Created by soniamarginean on 7/25/15.
 */

import java.util.concurrent.Callable;
import java.util.concurrent.*;
import java.util.*;

public class QThread extends Thread {
    int myID;
    MyQueue<Integer> lockQ;

    public QThread(int id, MyQueue<Integer> q) {
        myID = id;
        lockQ = q;
    }

    public void run() {
            System.out.println("Entering thread " + myID);
            if (myID%2==0) {
                System.out.println("Thread " + myID + " dequeues " + lockQ.deq());
            } else {
                System.out.println("Thread " + myID + " enqueues " + myID);
                        lockQ.enq(myID);
            }
            //lockQ.deq();
            //lockQ.enq(myID);
            System.out.println("Leaving thread " + myID);
    }
}
