import java.util.concurrent.atomic.AtomicStampedReference;

public class LockFreeQueue<T> implements MyQueue<T> {
  class Node<T>{
    T value;
    AtomicStampedReference<Node<T>> next;

    public Node(T x){
      value=x;
    }
  }

  AtomicStampedReference<Node<T>> head;
  AtomicStampedReference<Node<T>> tail;

  public LockFreeQueue(){
    Node<T> node = new Node<T>(null);
    node.next = new AtomicStampedReference<>(null,0);
    this.head = this.tail = new AtomicStampedReference<>(node, 0);
  }

  public boolean enq(T value) {
    if(value == null) {
      return false;
    }
    Node<T> node = new Node<T>(value);
    node.next = new AtomicStampedReference<>(null,0);
    AtomicStampedReference<Node<T>> qtail;
    while(true){
      qtail = this.tail;
      Node<T> qnode = qtail.getReference();
      int qstamp = qtail.getStamp();
      AtomicStampedReference<Node<T>> qnext = qnode.next;
      Node<T> qnodenext = qnext.getReference();
      int qstampnext = qnext.getStamp();
      if (qtail==this.tail){
        if (qnodenext == null){
          if (qnext.compareAndSet(qnodenext, node, qstampnext, qstampnext+1)){
            break;
          }
        } else {
          this.tail.compareAndSet(qnode,qnodenext,qstamp,qstamp+1);
        }
      }
    }
    boolean result = this.tail.compareAndSet(qtail.getReference(), node, qtail.getStamp(), qtail.getStamp()+1);
    return result;
  }

  public T deq() {
    AtomicStampedReference<Node<T>> qtail,qhead;
    T result = null;
    while(true){
      qtail = this.tail;
      qhead = this.head;
      Node<T> qnode = qhead.getReference();
      int qstamp = qhead.getStamp();
      AtomicStampedReference<Node<T>> qnext = qnode.next;
      Node<T> qnodenext = qnext.getReference();
      Node<T> qnodetail = qtail.getReference();
      int qstamptail = qtail.getStamp();
      if (qhead == this.head){
        if (qnode == qnodetail){
          System.out.println("Head equals tail");
          if (qnodenext==null){
            System.out.println("Dequeued null");
            return null;
          }
          this.tail.compareAndSet(qnodetail,qnodenext,qstamptail, qstamptail+1);
        }
        else {
          System.out.println("Q not empty");
          result = qnode.value;
          if(this.head.compareAndSet(qnode, qnodenext,qstamp, qstamp+1)){
            break;
          }
        }
      }
    }
    System.out.println("Dequeued "+result);
    return result;
  }

  public static void main(String [] args)
  {
    LockFreeQueue<Integer> lq = new LockFreeQueue<Integer>();
    QThread t[];
    t = new QThread[10];
    for(int i=0; i<10; i++){
      t[i] = new QThread(i,lq);
      t[i].start();
    }
  }
}
