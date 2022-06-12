package practice.util.lock.stamp;

import practice.common.TaskFactory;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author: DH
 * @date: 2022/6/12
 * @desc:
 */
public class StampedLockTest {

    private static final StampedLock STAMPED_LOCK = new StampedLock();

    private static final LinkedList<Long> DATA = new LinkedList<>();

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        IntStream.rangeClosed(1, 10).forEach(index -> {
            if (index % 8 == 0) {
                // 写数据
                executorService.submit(() -> {
                    while (true) {
                        write();
                    }
                });
            } else {
                // 读数据
                executorService.submit(() -> {
                    while (true) {
                        read();
                    }
                });
            }
        });
    }

    public static void read() {
        long stamp = -1;
        try {
            // 获取锁，并获取时间戳
            stamp = STAMPED_LOCK.readLock();
            Optional.of(DATA.stream().map(String::valueOf).collect(Collectors.joining("、", "R-", "")))
                    .ifPresent(System.out::println);

            TaskFactory.spend(1, TimeUnit.SECONDS);
        } finally {
            // 按照时间戳释放锁
            STAMPED_LOCK.unlockRead(stamp);
        }
    }

    public static void write() {
        long stamp = -1;
        try {
            stamp = STAMPED_LOCK.writeLock();
            long value = System.currentTimeMillis();
            DATA.addLast(value);
            System.out.println("C:" + value);
        } finally {
            STAMPED_LOCK.unlockWrite(stamp);
        }
    }
}
