import java.util.concurrent.atomic.AtomicStampedReference;

public class LockFreeQueue<T> implements MyQueue<T> {
    class Node<T> {
        T value;
        AtomicStampedReference<Node<T>> next;

        public Node(T x) {
            value = x;
        }
    }

    AtomicStampedReference<Node<T>> head;
    AtomicStampedReference<Node<T>> tail;

    public LockFreeQueue() {
        Node<T> node = new Node<T>(null);
        node.next = new AtomicStampedReference<>(null, 0);
        this.head = new AtomicStampedReference<>(node, 0);
        this.tail = new AtomicStampedReference<>(node, 0);
    }

    public boolean enq(T value) {
        if (value == null) {
            return false;
        }

        Node<T> node = new Node<T>(value);
        node.next = new AtomicStampedReference<>(null, 0);
        AtomicStampedReference<Node<T>> qtail;
        AtomicStampedReference<Node<T>> qnext;

        while (true) {
            qtail = this.tail;
            qnext = qtail.getReference().next;

            if (qtail == this.tail) {
                if (qnext.getReference() == null) {
                    if (qnext.compareAndSet(qnext.getReference(), node, qnext.getStamp(), qnext.getStamp() + 1)) {
                        break;
                    }
                } else {
                    this.tail.compareAndSet(qtail.getReference(), qnext.getReference(), qtail.getStamp(), qtail.getStamp() + 1);
                }
            }
        }

        return this.tail.compareAndSet(qtail.getReference(), node, qtail.getStamp(), qtail.getStamp() + 1);
    }

    public T deq() {
        AtomicStampedReference<Node<T>> qtail, qhead;
        T result;

        while (true) {
            qhead = this.head;
            qtail = this.tail;

            AtomicStampedReference<Node<T>> qnext = qhead.getReference().next;

            if (qhead == this.head) {
                if (qhead.getReference() == qtail.getReference()) {
                    if (qnext.getReference() == null) {
                        return null;
                    }
                    this.tail.compareAndSet(qtail.getReference(), qnext.getReference(), qtail.getStamp(), qtail.getStamp() + 1);
                } else {
                    result = qnext.getReference().value;
                    if (this.head.compareAndSet(qhead.getReference(), qnext.getReference(), qhead.getStamp(), qhead.getStamp() + 1)) {
                        break;
                    }
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        LockFreeQueue<Integer> lq = new LockFreeQueue<>();
        QThread t[];
        t = new QThread[10];
        for (int i = 0; i < 10; i++) {
            t[i] = new QThread(i, lq);
            t[i].start();
        }
    }
}
