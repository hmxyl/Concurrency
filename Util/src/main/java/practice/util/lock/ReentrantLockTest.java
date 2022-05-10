package practice.util.lock;

import practice.common.TaskFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author: DH
 * @date: 2022/5/8
 * @desc:
 */
public class ReentrantLockTest {

    public static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        IntStream.rangeClosed(1, 2).forEach(i -> new Thread(() -> needLock()).start());
        System.out.println("-----------------------------------");
        TaskFactory.spend(10, TimeUnit.SECONDS);
        System.out.println("-----------------------------------");
        IntStream.rangeClosed(1, 2).forEach(i -> new Thread(() -> tryLock()).start());
    }

    static void needLock() {
        // 不允许打断
        lock.lock();
        try {
            TaskFactory.spend(2, TimeUnit.SECONDS, true);
            System.out.println(Thread.currentThread().getName() + " - 取得锁 ：" + lock.isHeldByCurrentThread());
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + " - 释放锁资源：" + !lock.isLocked());
        }
    }

    static void tryLock() {
        if (lock.tryLock()) {
            // got the lock
            try {
                TaskFactory.spend(5, TimeUnit.SECONDS, true);
                System.out.println(Thread.currentThread().getName() + " - 取得锁 ：" + lock.isHeldByCurrentThread());
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " - 释放锁资源：" + !lock.isLocked());
            }
        } else {
            // do other things
            System.out.println(Thread.currentThread().getName() + " - 未取得锁.");
        }
    }
}
