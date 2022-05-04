package practice.util.atomic;

import org.junit.Test;
import practice.common.TaskFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
public class AtomicTest {

    @Test
    public void testAtomicStampedReference() throws InterruptedException {
        final AtomicInteger stamp = new AtomicInteger();
        final Integer moneyTotal = 100;
        AtomicStampedReference money = new AtomicStampedReference(moneyTotal, 0);
        // step1：取款50
        final Integer moneyStep1 = moneyTotal - 50;
        Thread t1 = new Thread(() -> {
            if (money.compareAndSet(moneyTotal, moneyStep1, stamp.get(), stamp.incrementAndGet())) {
                System.out.printf(Thread.currentThread().getName() + "-更新成功（%d->%d）:%d.\n", moneyTotal,
                        money.getReference(), money.getStamp());
            } else {
                System.out.printf(Thread.currentThread().getName() + "-更新失败（%d->%d）:%d.\n", moneyTotal,
                        money.getReference(), money.getStamp());
            }
        }, "T1");
        t1.start();

        Thread t2 = new Thread(() -> {

            TaskFactory.spend(2, TimeUnit.SECONDS);

            if (money.compareAndSet(moneyTotal, moneyStep1, stamp.get(), stamp.incrementAndGet())) {
                System.out.printf(Thread.currentThread().getName() + "-更新成功（%d->%d）:%d.\n", moneyTotal,
                        money.getReference(), money.getStamp());
            } else {
                System.out.printf(Thread.currentThread().getName() + "-更新失败（%d->%d）:%d.\n", moneyTotal,
                        money.getReference(), money.getStamp());
            }
        }, "T2");
        t2.start();
        // step2. 他人转入50
        final Integer moneyStep2 = moneyStep1 + 50;
        Thread t3 = new Thread(() -> {
            if (money.compareAndSet(moneyStep1, moneyStep2, 1, 2)) {
                System.out.printf(Thread.currentThread().getName() + "-更新成功（%d->%d）:%d.\n", moneyTotal,
                        money.getReference(), money.getStamp());
            } else {
                System.out.printf(Thread.currentThread().getName() + "-更新成功（%d->%d）:%d.\n", moneyTotal,
                        money.getReference(), money.getStamp());
            }
        }, "T3");
        t3.start();

        t1.join();
        t2.join();
        t3.join();
        System.out.printf(Thread.currentThread().getName() + "-最终（%d）:%d.\n", money.getReference(), money.getStamp());

    }
}
