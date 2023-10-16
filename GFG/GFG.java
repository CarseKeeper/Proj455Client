package GFG;

import java.util.*;
import java.io.*;

public class GFG implements Runnable {
    public void run() {
        try {
            Thread.sleep(102);
        } catch (InterruptedException i1) {
            i1.printStackTrace();
        }
        System.out.println("state for t1 after it invoked join method() on Thread t2 " + ThreadState.t1.getState());

        try {
            Thread.sleep(202);
        } catch (InterruptedException i2) {
            i2.printStackTrace();
        }
    }
}