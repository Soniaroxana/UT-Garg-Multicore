class CountThread extends Thread {
    private int pid;
    private Counter counter;
    private int countNumber;

    public CountThread(int pid, Counter counter, int countNumber){
        this.pid = pid;
        this.counter = counter;
        this.countNumber = countNumber;
    }

    public int getPid(){
        return this.pid;
    }

    public void run(){
        for (int i=0; i<countNumber; i++) {
            counter.increment();
        }
    }
}