/**
 * Created by soniamarginean on 6/23/15.
 */
public class BathroomUser implements Runnable {
    int id = 0;
    boolean male = false;
    BathroomProtocol r = null;
    public BathroomUser(int initId, boolean male, BathroomProtocol protocol) {
        id = initId;
        r = protocol;
        this.male = male;
        new Thread(this).start();
    }
    public void run() {
        //while (true) {
            try {
                Thread.sleep(3000);
                if (male){
                    r.enterMale();
                    Thread.sleep(100);
                } else {
                    r.enterFemale();
                    Thread.sleep(3000);
                }
                if (male){
                    r.leaveMale();
                } else {
                    r.leaveFemale();
                }
            } catch (InterruptedException e) {
                return;
            }
        //}
    }
}
