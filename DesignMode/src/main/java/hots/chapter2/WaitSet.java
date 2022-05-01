package hots.chapter2;

import java.util.stream.IntStream;

public class WaitSet {
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        IntStream.rangeClosed(1, 1).forEach(i ->
                new Thread(String.valueOf(i)) {
                    @Override
                    public void run() {
                        synchronized (LOCK) {
                            try {
                                long time1 = System.currentTimeMillis();
                                System.out.println(Thread.currentThread().getName() + " will come to wait set.");
                                LOCK.wait();
                                System.out.println(Thread.currentThread().getName() + " will leave to wait set." + (System.currentTimeMillis() - time1));

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start()
        );

        Thread.sleep(3_000);

        IntStream.rangeClosed(1, 10).forEach(i ->
                {
                    synchronized (LOCK) {
                        LOCK.notify();
                        try {
                            Thread.sleep(1_000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
