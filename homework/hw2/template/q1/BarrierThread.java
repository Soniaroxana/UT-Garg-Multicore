/**
 * Created by soniamarginean on 6/22/15.
 */
public class BarrierThread extends Thread {
    final CyclicBarrier barrier;

    public BarrierThread(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public void run(){
        try {
            int index = barrier.await();
            System.out.println("Returning"+index);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        int threads = 10;
        CyclicBarrier barrier = new CyclicBarrier(threads);
        for (int i=0; i<threads; i++){
            Thread t = new BarrierThread(barrier);
            t.start();
        }
    }
}
