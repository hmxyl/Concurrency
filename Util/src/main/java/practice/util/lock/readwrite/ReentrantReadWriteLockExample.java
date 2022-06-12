package practice.util.lock.readwrite;

import practice.common.TaskFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: DH
 * @date: 2022/5/8
 * @desc:
 */
public class ReentrantReadWriteLockExample {
    public static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public static final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    public static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public static void main(String[] args) {
        // new Thread(ReadWriteLockExample::doWriteAction, "A1").start();
        // 同时读，不会排他
        new Thread(ReentrantReadWriteLockExample::readFiles, "A1").start();
        new Thread(ReentrantReadWriteLockExample::readFiles, "A2").start();
    }

    static void readFiles() {
        try {
            readLock.lock();
            TaskFactory.spend(5, TimeUnit.SECONDS, true);
            System.out.println(Thread.currentThread().getName() + " 开始读操作");
            TaskFactory.spend(3, TimeUnit.SECONDS);
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + " 完成读操作");
        }
    }

    static void writeFiles() {
        try {
            writeLock.lock();
            System.out.println(Thread.currentThread().getName() + " 开始写操作");
            TaskFactory.spend(3, TimeUnit.SECONDS);
        } finally {
            writeLock.unlock();
            System.out.println(Thread.currentThread().getName() + " 完成写操作");
        }
    }
}
