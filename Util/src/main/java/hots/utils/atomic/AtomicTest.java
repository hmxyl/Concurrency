package hots.utils.atomic;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicTest {

    @Test
    public void testAtomicInteger() {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.compareAndSet(1, 2);
        System.out.println(atomicInteger.get());
        try {
            Thread.sleep(20_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAtomicBoolean() throws InterruptedException {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        AtomicInteger atomicInteger = new AtomicInteger();

        Thread t1 = new Thread(() -> {
            while (atomicInteger.incrementAndGet() < 10) {
                System.out.println(Thread.currentThread().getName() + ": " + atomicBoolean.getAndSet(false) + " " + atomicBoolean.get());
            }
        });


        Thread t2 = new Thread(() -> {
            while (atomicInteger.incrementAndGet() < 10) {
                System.out.println(Thread.currentThread().getName() + ": " + atomicBoolean.getAndSet(true) + "-" + atomicBoolean.get());
            }
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    @Test
    public void testAtomicReference() throws InterruptedException {
        final Integer moneyTotal = 100;
        AtomicInteger money = new AtomicInteger(moneyTotal);

        Thread t1 = new Thread(() -> {
            money.getAndAdd(-50);
            System.out.printf(Thread.currentThread().getName() + "-更新成功（%d->%d）.\n", moneyTotal, money.get());
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            money.getAndAdd(-50);
            System.out.printf(Thread.currentThread().getName() + "-更新成功（%d->%d）.\n", moneyTotal, money.get());
        });
        t2.start();

        Thread t3 = new Thread(() -> {
            money.getAndAdd(50);
            System.out.printf(Thread.currentThread().getName() + "-更新成功（%d->%d）.\n", moneyTotal, money.get());
        });
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        // 输出50，钱数错误
        System.out.println(money.get());
    }


    @Test
    public void testAtomicStampedReference() throws InterruptedException {

        final Integer moneyTotal = 100;
        AtomicStampedReference money = new AtomicStampedReference(moneyTotal, 0);

        // 1. ATM 取用50（两个线程同步进行）
        final Integer moneyStep1 = moneyTotal - 50;
        Thread t1 = new Thread(() -> {
            if (money.compareAndSet(moneyTotal, moneyStep1, 0, money.getStamp() + 1)) {
                System.out.printf(Thread.currentThread().getName() + "-更新成功（%d->%d）:%d.\n", moneyTotal, money.getReference(), money.getStamp());
            } else {
                System.out.printf(Thread.currentThread().getName() + "-更新失败（%d->%d）:%d.\n", moneyTotal, money.getReference(), money.getStamp());
            }
        }, "T1");
        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (money.compareAndSet(moneyTotal, moneyStep1, 0, money.getStamp() + 1)) {
                System.out.printf(Thread.currentThread().getName() + "-更新成功（%d->%d）:%d.\n", moneyTotal, money.getReference(), money.getStamp());
            } else {
                System.out.printf(Thread.currentThread().getName() + "-更新失败（%d->%d）:%d.\n", moneyTotal, money.getReference(), money.getStamp());
            }
        }, "T2");
        t2.start();

        // 2. 他人转入50
        final Integer moneyStep2 = moneyStep1 + 50;
        Thread t3 = new Thread(() -> {
            if (money.compareAndSet(moneyStep1, moneyStep2, 1, money.getStamp() + 1)) {
                System.out.printf(Thread.currentThread().getName() + "-更新成功（%d->%d）:%d.\n", moneyTotal, money.getReference(), money.getStamp());
            } else {
                System.out.printf(Thread.currentThread().getName() + "-更新成功（%d->%d）:%d.\n", moneyTotal, money.getReference(), money.getStamp());
            }
        }, "T3");
        t3.start();


        t1.join();
        t2.join();
        t3.join();
        System.out.printf(Thread.currentThread().getName() + "-最终（%d）:%d.\n", money.getReference(), money.getStamp());
    }
}

