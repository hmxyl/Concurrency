package practice.util.lock.stamp;

import practice.common.TaskFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 乐观读，需要遵循必须遵循以下模式
 *<p>
 *   long stamp = lock.tryOptimisticRead();  // 非阻塞获取版本信息
 *   copyVaraibale2ThreadMemory();           // 拷贝变量到线程本地堆栈
 *   if(!lock.validate(stamp)){              // 校验
 *       long stamp = lock.readLock();       // 获取读锁
 *       try {
 *           copyVaraibale2ThreadMemory();   // 拷贝变量到线程本地堆栈
 *        } finally {
 *          lock.unlock(stamp);              // 释放悲观锁
 *       }
 *   }
 *   useThreadMemoryVarables();              // 使用线程本地堆栈里面的数据进行操作
 *</p>
 */
public class StampedLockOptimisticTest {
    private static final StampedLock stampedLock = new StampedLock();

    private static final List<Long> DATA = new ArrayList<>();

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
                        optimisticRead();
                    }
                });
            }
        });
    }

    public static void optimisticRead() {
        // 获取锁，并获取时间戳
        long stamp = stampedLock.tryOptimisticRead();
        // 乐观读，必须先拷贝一份数据到在方法中
        List<Long> local = new ArrayList<>();
        local.addAll(DATA);
        // 检查在拷贝过程中有没有排他锁抢占，如果有则悲观读
        if (!stampedLock.validate(stamp)) {
            stamp = stampedLock.readLock();
            try {
                System.out.println(">>>>>>>> 重新读取数据到本地 >>>>>>>>");
                local.clear();
                local.addAll(DATA);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }

        // 使用数据
        Optional.of(local.stream().map(String::valueOf).collect(Collectors.joining("、", "R-", "")))
                .ifPresent(System.out::println);
        TaskFactory.spend(1, TimeUnit.SECONDS);
    }

    public static void write() {
        long stamp = stampedLock.writeLock();
        try {
            long value = System.currentTimeMillis();
            System.out.println("W-" + value);
            System.out.println();
            DATA.add(value);
        } finally {
            stampedLock.unlockWrite(stamp);
        }

    }
}
