import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineGrainedListSet<T> implements ListSet<T> {
    private Node<Integer> head;

    public FineGrainedListSet() {
        // Assign sentinel nodes
        head = new Node<>(Integer.MIN_VALUE);
        head.next = new Node<>(Integer.MAX_VALUE);
    }

    public boolean add(T value) {
        Node pred  = head, curr = head.next;

        int key = value.hashCode();

        head.lock();

        try{
            curr.lock();

            try{
                while(curr.key < key){
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }

                if(curr.key == key){
                    return false;
                }

                Node<T> node = new Node<>(value);
                node.next = curr;
                pred.next = node;

                return true;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    public boolean remove(T value) {
        Node pred = head, curr = head.next;

        int key = value.hashCode();

        head.lock();

        try{
            curr.lock();
            try{
                while(curr.key < key){
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }

                if(curr.key == key){
                    pred.next = curr.next;
                    return true;
                }

                return false;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    public boolean contains(T value) {
        Node pred = head, curr = head.next;

        int key = value.hashCode();

        head.lock();

        try{
            curr.lock();

            try{
                while(curr.key < key){
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }

                return curr.key == key;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    private class Node<U> {
        U item;
        int key;
        Node next;
        private Lock lock;

        public Node(U i) {
            item = i;
            key = i.hashCode();
            lock = new ReentrantLock();
        }

        public void lock(){
            lock.lock();
        }
        public void unlock(){
            lock.unlock();
        }
    }
}