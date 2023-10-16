import GFG.GFG;

public class ThreadState implements Runnable {
    public static Thread t1;
    public static ThreadState o1;

    public static void main(String[] args) {
        o1 = new ThreadState();
        t1 = new Thread(o1);
        System.out.println("post-spanning, state of t1 is " + t1.getState());

        t1.start();
        System.out.println("post invoking of start() method, state of t1 is " + t1.getState());
    }

    public void run() {
        GFG g1 = new GFG();
        Thread t2 = new Thread(g1);

        t2.start();
        System.out.println("state of t2 Thread, post-calling of start() method is " + t2.getState());

        try {
            Thread.sleep(202);
        } catch (InterruptedException i2) {
            i2.printStackTrace();
        }
        System.out.println("State of Thread t2 after invoking to method sleep() is " + t2.getState());
        try {
            t2.join();
            System.out.println("State of Thread t2 after join() is " + t2.getState());
        } catch (InterruptedException i3) {
            i3.printStackTrace();
        }
        System.out.println("State of Thread t1 after completing the execution is " + t1.getState());
    }
}