package hots.utils.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: DH
 * @date: 2022/3/5
 * @desc:
 */
public class ReadWriteLockExample {
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public static void main(String[] args) throws InterruptedException {
        // new Thread(ReadWriteLockExample::doWriteAction, "A1").start();
        new Thread(ReadWriteLockExample::doReadAction, "A1").start();
        new Thread(ReadWriteLockExample::doReadAction, "A2").start();
    }

    private static void doReadAction() {
        try {
            readLock.lock();
            System.out.println(Thread.currentThread().getName() + " 开始读操作");
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + " 完成读锁释放");
        }
    }

    private static void doWriteAction() {
        try {
            writeLock.lock();
            System.out.println(Thread.currentThread().getName() + " 开始写操作");
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
            System.out.println(Thread.currentThread().getName() + " 完成写锁释放");
        }
    }
}
