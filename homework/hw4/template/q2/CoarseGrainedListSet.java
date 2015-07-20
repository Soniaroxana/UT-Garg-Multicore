import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseGrainedListSet<T> implements ListSet<T> {
    private Node<Integer> head;
    private Lock lock = new ReentrantLock();

    public CoarseGrainedListSet() {
        // Assign sentinel nodes
        head = new Node<>(Integer.MIN_VALUE);
        head.next = new Node<>(Integer.MAX_VALUE);
    }

    public boolean add(T value) {
        Node pred = head, curr = head.next;

        int key = value.hashCode();

        lock.lock();

        try {
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            if (key == curr.key) {
                return false;
            } else {
                Node<T> node = new Node<>(value);
                node.next = curr;
                pred.next = node;
                return true;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean remove(T value) {
        Node pred = head, curr = head.next;

        int key = value.hashCode();

        lock.lock();

        try {
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            if (key == curr.key) {
                pred.next = curr.next;
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean contains(T value) {
        Node curr = head.next;

        int key = value.hashCode();

        lock.lock();

        try {
            while (curr.key < key) {
                curr = curr.next;
            }

            return key == curr.key;
        } finally {
            lock.unlock();
        }
    }

    private class Node<U> {
        U item;
        int key;
        Node next;

        public Node(U i) {
            item = i;
            key = i.hashCode();
        }
    }
}