package hots.chapter5;

import java.util.Arrays;

public class BooleanLockTest {
    public static void main(String[] args) {
        final BooleanLock booleanLock = new BooleanLock();

        Arrays.asList("W1", "W2", "W3").stream()
                .forEach(name -> {
                    new Thread(() -> {
                        try {
                            booleanLock.lock(5_000);
                            System.out.println(Thread.currentThread().getName() + " got the lock");
                            work();
                        } catch (InterruptedException | Lock.TimeoutException e) {
                            e.printStackTrace();
                        } finally {
                            booleanLock.unLock();
                        }
                    }, name).start();
                });

    }

    public static void work() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is working...");
        Thread.sleep(10_000);
    }
}
