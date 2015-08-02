/**
 * Created by neelshah on 8/2/15.
 */

public class LSThread<T> extends Thread {
    int id;
    T value;
    ListSet<T> listSet;
    Type type;

    public LSThread(int id, T value, ListSet<T> listSet, Type type) {
        this.id = id;
        this.value = value;
        this.listSet = listSet;
        this.type = type;
    }

    public void run() {
        System.out.println(type + " thread " + id + " started");

        switch (type) {
            case ADD:
                if (listSet.add(value)) {
                    System.out.println(type + " thread " + id + " added " + value);

                } else {
                    System.out.println(type + " thread " + id + " did not add " + value);
                }
                break;
            case REMOVE:
                if (listSet.remove(value)) {
                    System.out.println(type + " thread " + id + " removed " + value);

                } else {
                    System.out.println(type + " thread " + id + " did not remove " + value);
                }
                break;
            case CONTAINS:
                if (listSet.contains(value)) {
                    System.out.println(type + " thread " + id + " found " + value);

                } else {
                    System.out.println(type + " thread " + id + " did not find " + value);
                }
                break;
            default:
                break;
        }

        System.out.println(type + " thread " + id + " ended");
    }

    public enum Type {
        ADD,
        REMOVE,
        CONTAINS
    }
}
