import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// TODO
// Use locks and condition variables to implement this bathroom protocol
public class LockBathroomProtocol implements BathroomProtocol {
  // declare the lock and conditions here
  final ReentrantLock lock = new ReentrantLock();
  final Condition maleOccupied = lock.newCondition();
  final Condition femaleOccupied = lock.newCondition();
  int maleCount = 0;
  int femaleCount = 0;
  int waitingMales = 0;
  int waitingFemales = 0;

  public void enterMale() {
    lock.lock();
    waitingMales++;
    System.out.println("Male Trying to enter"+maleCount);
    try {
      while (femaleCount>0 || (maleCount>0 && waitingFemales>0)) {
        femaleOccupied.await();
      }
      System.out.println("Enter male"+maleCount);
      waitingMales--;
      maleCount++;
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  public void leaveMale() {
    lock.lock();
    maleCount--;
    if(maleCount <=0){
      maleOccupied.signalAll();
    }
    System.out.println("Leave male"+maleCount);
    lock.unlock();
  }

  public void enterFemale() {
    lock.lock();
    waitingFemales++;
    System.out.println("Female Trying to enter"+femaleCount);
    try {
      while (maleCount>0 || (femaleCount>0 && waitingMales>0)) {
        maleOccupied.await();
      }
      System.out.println("Enter female"+femaleCount);
      waitingFemales--;
      femaleCount++;
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  public void leaveFemale() {
    lock.lock();
    femaleCount--;
    if (femaleCount<= 0){
      femaleOccupied.signalAll();
    }
    System.out.println("Leave female"+femaleCount);
    lock.unlock();
  }
}
