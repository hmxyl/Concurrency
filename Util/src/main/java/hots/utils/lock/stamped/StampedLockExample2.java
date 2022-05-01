package hots.utils.lock.stamped;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * 乐观读：锁
 *
 * @author: DH
 * @date: 2022/3/10
 * @desc:
 */
class StampedLockExample2 {
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
                            readOptimistic();
                            sleep(1);
                        }
                    });
                    break;

            }
        }
    }


    /**
     * 乐观读操作模板
     * <p>
     * 乐观读锁在保证数据一致性上需要拷贝一份要操作的变量到方法栈，并且在操作数据时候可能其他写线程已经修改了数据，
     * 而我们操作的是方法栈里面的数据，也就是一个快照，所以最多返回的不是最新的数据，但是一致性还是得到保障的
     * </p>
     */
    private static double readOptimistic() {
        long stamp = stampedLock.tryOptimisticRead();
        // 拷贝共享资源到本地方法栈中
        double currentX = x, currentY = y;
        // 确认非写锁-独占操作
        if (!stampedLock.validate(stamp)) {
            // 如果有写锁被占用，可能造成数据不一致，所以要切换到普通读锁模式
            stamp = stampedLock.readLock();
            // 开始读操作
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        double result = Math.sqrt(currentX * currentX + currentY * currentY);
        Optional.of(Thread.currentThread().getName() + ": 读数据 - " + currentX + ", " + currentY + " = " + result).ifPresent(System.out::println);
        return result;
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
