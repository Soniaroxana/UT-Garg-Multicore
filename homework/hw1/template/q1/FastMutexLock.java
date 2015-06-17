// TODO 
// Implement Fast Mutex Algorithm

public class FastMutexLock implements MyLock {
    private enum flagEnum{
        DOWN,
        UP
    }

    private volatile int X;
    private volatile int Y;
    private volatile flagEnum[] flag;
    private int N;


    public FastMutexLock(int numThread) {
        // TODO: initialize your algorithm
        X = -1;
        Y = -1;
        N = numThread;
        flag = new flagEnum[numThread];
        for (int i=0; i<numThread; i++){
            flag[i] = flagEnum.DOWN;
        }
    }

    @Override
    public void lock(int myId) {
        // TODO: the locking algorithm
        while (true){
            flag[myId] = flagEnum.UP;
            X = myId;
            if (Y != -1){
                flag[myId] = flagEnum.DOWN;
                while(Y != -1);
                continue;
            } else {
                Y = myId;
                if (X==myId){
                    return;
                } else {
                    flag[myId]=flagEnum.DOWN;
                    for (int j=0; j<N; j++) {
                        while (flag[j] != flagEnum.DOWN) ;
                    }
                        if (Y == myId){
                            return;
                        } else {
                            while(Y != -1);
                            continue;
                        }
                }
            }
        }
    }

    @Override
    public void unlock(int myId) {
        // TODO: the unlocking algorithm
        Y = -1;
        flag[myId]=flagEnum.DOWN;
    }
}
