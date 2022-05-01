package hots.chapter2;

import java.util.concurrent.TimeUnit;

public class StopThread {

    static class MyThread extends Thread {
        private volatile boolean shutdown = false;

        @Override
        public void run() {
            System.out.println("I will start work.");
            while (!shutdown) {
                // working
            }
            System.out.println("I will be exiting.");
        }

        public void shutdown() {
            this.shutdown = true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread t1 = new MyThread();
        t1.start();
        TimeUnit.SECONDS.sleep(10);
        System.out.println("System will be shutdown");
        t1.shutdown();
    }
}

