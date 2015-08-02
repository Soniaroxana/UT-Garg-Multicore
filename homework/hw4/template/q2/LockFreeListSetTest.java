/**
 * Created by neelshah on 8/2/15.
 */
public class LockFreeListSetTest {
    public static void main(String [] args) {
        int numThreads = 20;

        ListSet<Integer> listSet = new CoarseGrainedListSet<>();

        LSThread[] addThreads = new LSThread[numThreads];
        LSThread[] containsThreads = new LSThread[numThreads];
        LSThread[] removeThreads = new LSThread[numThreads];

        for(int i = 0; i < numThreads; i++){
            addThreads[i] = new LSThread<>(i, i, listSet, LSThread.Type.ADD);
            containsThreads[i] = new LSThread<>(i, i, listSet, LSThread.Type.CONTAINS);
            removeThreads[i] = new LSThread<>(i, i, listSet, LSThread.Type.REMOVE);
        }

        for(int i = 0; i < numThreads; i++){
            addThreads[i].start();
            containsThreads[i].start();
            removeThreads[i].start();
        }
    }
}
