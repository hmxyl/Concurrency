package hots.utils.lock.stamped;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * 悲观读：锁
 *
 * @author: DH
 * @date: 2022/3/10
 * @desc:
 */
class StampedLockExample1 {
    private static double x, y;
    private static final StampedLock stampedLock = new StampedLock();

    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            switch (i % 5) {
                case 0:
                    executorService.submit(() -> {
                        while (true) {
                            write();
                            sleep(1);
                        }
                    });
                    break;
                default:
                    executorService.submit(() -> {
                        while (true) {
                            read();
                            sleep(1);
                        }
                    });
                    break;

            }
        }
    }


    /**
     * 悲观读
     *
     * @return
     */
    private static double read() {
        long stamp = -1;
        try {
            stamp = stampedLock.readLock();
            double result = Math.sqrt(x * x + y * y);
            Optional.of(Thread.currentThread().getName() + ": 读数据 - " + x + ", " + y + " = " + result).ifPresent(System.out::println);
            return result;
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }


    private static void write() {
        long stamp = -1;
        try {
            stamp = stampedLock.writeLock();
            x = random.nextInt(50);
            y = random.nextInt(50);
            Optional.of(Thread.currentThread().getName() + ": 写数据 - " + x + ", " + y).ifPresent(System.out::println);
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    private static void sleep(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
