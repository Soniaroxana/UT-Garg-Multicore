// TODO
// Use MyLock to protect the count
// This counter can use either BakeryLock or FastMutexLock to protect the count

public class LockCounter extends Counter {
    private MyLock myLock;

    public LockCounter(MyLock lock) {
        myLock = lock;
    }

    @Override
    public void increment() {
            CountThread t = (CountThread) Thread.currentThread();
            int pid = t.getPid();
            myLock.lock(pid);
            super.count++;
            myLock.unlock(pid);
    }
}
