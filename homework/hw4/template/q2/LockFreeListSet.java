import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeListSet<T> implements ListSet<T> {
    private Node<Integer> head;

    public LockFreeListSet() {
        // Assign sentinel nodes
        head = new Node<>(Integer.MIN_VALUE);
        head.next = new AtomicMarkableReference<>(new Node<>(Integer.MAX_VALUE), false);
    }

    public boolean add(T value) {
        int key = value.hashCode();

        while (true) {
            Window window = find(key);

            Node<T> pred = window.pred, curr = window.curr;

            if (curr.key == key) {
                return false;
            } else {
                Node<T> node = new Node<>(value);
                node.next = new AtomicMarkableReference<>(curr, false);

                if (pred.next.compareAndSet(curr, node, false, false)) {
                    return true;
                }
            }
        }

    }

    public boolean remove(T value) {
        int key = value.hashCode();

        while (true) {
            Window window = find(key);

            Node<T> pred = window.pred, curr = window.curr;

            if (curr.key != key) {
                return false;
            } else {
                // Mark this node as logically deleted according to semantics (See below)
                if (!curr.next.attemptMark(curr.next.getReference(), true)) {
                    continue;
                }

                pred.next.compareAndSet(curr, curr.next.getReference(), false, false);

                return true;
            }
        }
    }

    public boolean contains(T value) {
        boolean isMarked = false;

        int key = value.hashCode();

        Node<T> curr = (Node<T>) head;

        while (curr.key < key) {
            isMarked = curr.next.isMarked();
            curr = curr.next.getReference();
        }

        return (curr.key == key && !isMarked);
    }

    private class Window {
        public Node<T> pred, curr;
        Window(Node<T> pred, Node<T> curr) {
            this.pred = pred;
            this.curr = curr;
        }
    }

    public Window find(int key) {
        Node<T> pred = (Node<T>) head, curr;

        retry: while (true) {
            curr = pred.next.getReference();
            while (true) {
                // Help physically remove marked nodes
                while (curr.next.isMarked()) {
                    if (!pred.next.compareAndSet(curr, curr.next.getReference(), false, false)) {
                        continue retry;
                    }

                    curr = curr.next.getReference();
                }

                if (curr.key >= key) {
                    return new Window(pred, curr);
                }

                pred = curr;
                curr = curr.next.getReference();
            }
        }
    }


    private class Node<U> {
        U item;
        int key;

        // Semantics here are the current node is logically deleted if the reference is marked, not the reference node
        AtomicMarkableReference<Node<U>> next;

        public Node(U i) {
            item = i;
            key = i.hashCode();
        }
    }
}