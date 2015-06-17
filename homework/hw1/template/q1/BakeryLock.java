// TODO
// Implement the bakery algorithm

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BakeryLock implements MyLock {
    private int N;
    private AtomicBoolean[] choosing;
    private AtomicInteger[] number;

    public BakeryLock(int numThread) {
        N = numThread;
        choosing = new AtomicBoolean[N];
        number = new AtomicInteger[N];
        for (int j=0; j<N; j++){
            choosing[j] = new AtomicBoolean();
            choosing[j].set(false);
            number[j] = new AtomicInteger();
            number[j].set(0);
        }

    }

    @Override
    public void lock(int myId) {
        choosing[myId].set(true);
        for(int j=0; j<N; j++){
            if (number[j].get()>number[myId].get()){
                number[myId].set(number[j].get());
            }
        }
        number[myId].set(number[myId].get()+1);
        choosing[myId].set(false);

        for (int j=0; j<N; j++) {
            if (j != myId) {
                while (choosing[j].get()) ;
                while ((number[j].get() != 0) && ((number[j].get() < number[myId].get()) || ((number[j].get() == number[myId].get()) && j < myId))) ;
            }
        }
    }

    @Override
    public void unlock(int myId) {
        number[myId].set(0);
    }
}
