// TODO
// Implement the bakery algorithm

public class BakeryLock implements MyLock {
    int N;
    volatile boolean[] choosing;
    volatile int[] number;

    public BakeryLock(int numThread) {
        N = numThread;
        choosing = new boolean[N];
        number = new int[N];
        for (int j=0; j<N; j++){
            choosing[j] = false;
            number[j] = 0;
        }

    }

    @Override
    public void lock(int myId) {
        choosing[myId]=true;
        for(int j=0; j<N; j++){
            if (number[j]>number[myId]){
                number[myId] = number[j];
            }
        }
        number[myId] =number[myId]+1;
        choosing[myId] = false;

        for (int j=0; j<N; j++) {
            if (j != myId) {
                while (choosing[j]) ;
                while ((number[j] != 0) && ((number[j] < number[myId]) || ((number[j] == number[myId]) && j < myId))) ;
            }
        }
    }

    @Override
    public void unlock(int myId) {
        number[myId] = 0;
    }
}
