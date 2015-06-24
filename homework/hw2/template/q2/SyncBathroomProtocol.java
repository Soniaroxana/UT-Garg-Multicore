// TODO
// Use synchronized, wait(), notify(), and notifyAll() to implement this
// bathroom protocol

public class SyncBathroomProtocol implements BathroomProtocol {
  private int maleCount=0;
  private int femaleCount=0;
  private int waitingMales=0;
  private int waitingFemales=0;

  synchronized public void enterMale() {
    System.out.println("Male Trying to enter"+maleCount);
    waitingMales++;
    while(femaleCount>0 || (maleCount>0 && waitingFemales>0)){
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("Enter male"+maleCount);
    waitingMales--;
    maleCount++;
    if(waitingMales<=0){
      notifyAll();
    }
  }

  synchronized public void leaveMale() {
    maleCount--;
    System.out.println("Leave male"+maleCount);
    if (maleCount<=0){
      notifyAll();
    }
  }

  synchronized public void enterFemale() {
    waitingFemales++;
    System.out.println("Female Trying to enter"+femaleCount);
    while(maleCount>0 || (femaleCount>0 && waitingMales>0)){
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("Enter female"+femaleCount);
    waitingFemales--;
    femaleCount++;
    if(waitingFemales<=0){
      notifyAll();
    }
  }

  synchronized public void leaveFemale() {
    femaleCount--;
    System.out.println("Leave female"+femaleCount);
    if (femaleCount<=0){
      notifyAll();
    }
  }


}
