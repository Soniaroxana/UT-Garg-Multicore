import java.lang.System;

public class Main {
    public static void main (String[] args) {
        Counter counter = null;
        final MyLock lock;
        long executeTimeMS = 0;
        int numThread = 6;
        int numTotalInc = 1200000;
        Thread[] t;

        if (args.length < 3) {
            System.err.println("Provide 3 arguments");
            System.err.println("\t(1) <algorithm>: anderson/mcs");
            System.err.println("\t(2) <numThread>: the number of test thread");
            System.err.println("\t(3) <numTotalInc>: the total number of "
                    + "increment operations performed");
            System.exit(-1);
        }

        numThread = Integer.parseInt(args[1]);
        if (args[0].equals("anderson")) {
            lock = new ALock(numThread);
            counter = new LockCounter(lock);
        } else if (args[0].equals("mcs")) {
            lock = new MCSLock();
            counter = new LockCounter(lock);
        } else {
            System.err.println("ERROR: no such algorithm implemented");
            System.exit(-1);
        }

        t = new Thread[numThread];
        numTotalInc = Integer.parseInt(args[2]);

        // TODO
        // Please create numThread threads to increment the counter
        // Each thread executes numTotalInc/numThread increments
        // Please calculate the total execute time in millisecond and store the
        // result in executeTimeMS
        long startTime = System.currentTimeMillis();

        for (int i=0; i<numThread; i++){
            t[i] = new CountThread(i,counter, numTotalInc/numThread);
        }
        for (int i=0; i<numThread; i++){
            t[i].start();
        }
        for (int i=0; i<numThread; i++) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        executeTimeMS = System.currentTimeMillis() - startTime;

        // all threads finish incrementing
        // Checking if the result is correct
        if (counter == null ||
                counter.getCount() != (numTotalInc/numThread) * numThread) {
            System.err.println("Error: The counter is not equal to the number "
                    + "of total increment");
        } else {
            // print total execute time if the result is correct
            System.out.println(executeTimeMS);
        }
    }
}
