package hots.utils.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author: DH
 * @date: 2022/3/4
 * @desc:
 */
public class ReentrantLockExample {
    private final static ReentrantLock lock = new ReentrantLock();


    public static void main(String[] args) {
        testLock();
        testTryLock();
    }

    private static void testTryLock() {
        IntStream.rangeClosed(1, 2).forEach(index -> {
            new Thread(() -> {
                if (lock.tryLock()) {
                    // got the lock
                    try {
                        doAction(5);
                        System.out.println(Thread.currentThread().getName() + " - 取得锁 ：" + lock.isHeldByCurrentThread());
                    } finally {
                        lock.unlock();
                        System.out.println(Thread.currentThread().getName() + " - 释放锁资源：" + !lock.isLocked());
                    }
                } else {
                    // do other things
                    System.out.println(Thread.currentThread().getName() + " - 未取得锁.");
                }
            }, "A" + index).start();
        });
    }

    private static void testLock() {
        IntStream.rangeClosed(1, 2).forEach(index -> {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " - 开始抢锁.");
                    lock.lock();
                    doAction(5);
                    System.out.println(Thread.currentThread().getName() + " - 取得锁 ：" + lock.isHeldByCurrentThread());
                } finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + " - 释放锁资源：" + !lock.isLocked());

                }

            }, "A" + index).start();
        });
    }

    private static void doAction(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
