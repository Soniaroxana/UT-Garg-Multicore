/**
 * Created by soniamarginean on 6/23/15.
 */
public class TestSyncBathroom {

    public static void main(String[] args) {
        System.out.println("Using synch");
        final SyncBathroomProtocol p = new SyncBathroomProtocol();
        for (int i = 0; i < 10; i++) {
            new BathroomUser(i, true, p);
        }
        for (int i = 0; i < 5; i++) {
            new BathroomUser(i, false, p);
        }
        for (int i = 0; i < 10; i++) {
            new BathroomUser(i, true, p);
        }
        for (int i = 0; i < 5; i++) {
            new BathroomUser(i, true, p);
        }
    }
}
