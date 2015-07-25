import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Callable;
import java.util.concurrent.*;
import java.util.*;

public class LockQueue<T> implements MyQueue<T> {
  class Node<T>{
    Node<T> next;
    T value;
    public Node(T x){
      value=x;
    }
  }

  ReentrantLock enqLock;
  ReentrantLock deqLock;
  Node<T> head;
  Node<T> tail;
  AtomicInteger count;

  public LockQueue(){
    head = new Node<T>(null);
    tail = head;
    enqLock = new ReentrantLock();
    deqLock = new ReentrantLock();
    count = new AtomicInteger(0);
  }

  public boolean enq(T value) {
    if(value == null) {
      return false;
    }
    enqLock.lock();
    try{
      Node<T> e = new Node<T>(value);
      tail.next = e;
      tail = e;
      count.getAndIncrement();
      System.out.println("Enqueing "+value);
      if (count.get()==1){
        synchronized (this){
        notifyAll();}
      }
    }finally{
      enqLock.unlock();
    }
    return true;
  }

  public T deq() {
    T result;
    deqLock.lock();
    try{
      while (head.next==null){
        synchronized (this){
        try {
          System.out.println("waiting "+count.get());
          wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }}
      }
      result = head.next.value;
      head = head.next;
      count.getAndDecrement();
      System.out.println("dequeing "+result);
    } finally {
      deqLock.unlock();
    }
    return result;
  }

  public static void main(String [] args)
  {
    LockQueue<Integer> lq = new LockQueue<Integer>();
    QThread t[];
    t = new QThread[10];
    for(int i=0; i<4; i++){
      t[i] = new QThread(i,lq);
      t[i].start();
    }
  }

}
