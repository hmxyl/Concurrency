package hots.utils.lock.condition;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author: DH
 * @date: 2022/3/10
 * @desc:
 */
public class ConditionExample2 {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition CONSUMER_CONDITION = lock.newCondition();

    private static final Condition PRODUCT_CONDITION = lock.newCondition();
    /* 数据最大容量 */
    private static final int MAX_POOL_SIZE = 10;
    private static LinkedList<Long> SOURCE_POOL = new LinkedList<>();

    public static void main(String[] args) {
        IntStream.rangeClosed(1, 5).forEach(index -> doProduct(index));
        IntStream.rangeClosed(1, 10).forEach(index -> doConsume(index));
    }

    private static void doProduct(int index) {
        new Thread(() -> {
            while (true) {
                productData();
                sleep(1);
            }
        }, "P" + index).start();
    }

    private static void doConsume(int index) {
        new Thread(() -> {
            while (true) {
                consumeData();
                sleep(1);
            }
        }, "C" + index).start();
    }

    private static void productData() {
        try {
            lock.lock();
            while (SOURCE_POOL.size() >= MAX_POOL_SIZE) {
                PRODUCT_CONDITION.await();
            }
            long currentTimeMillis = System.currentTimeMillis();
            SOURCE_POOL.addLast(currentTimeMillis);
            System.out.println(Thread.currentThread().getName() + "：" + currentTimeMillis);

            monitor("PRODUCT_CONDITION", PRODUCT_CONDITION);
            monitor("CONSUMER_CONDITION", CONSUMER_CONDITION);

            CONSUMER_CONDITION.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private static void consumeData() {
        try {
            lock.lock();
            while (SOURCE_POOL.isEmpty()) {
                CONSUMER_CONDITION.await();
            }
            long currentTimeMillis = SOURCE_POOL.removeFirst();
            System.out.println(Thread.currentThread().getName() + "：" + currentTimeMillis);
            PRODUCT_CONDITION.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private static void sleep(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监控
     *
     * @param tag
     * @param condition
     */
    private static void monitor(String tag, Condition condition) {
        System.out.println(tag + "---" + lock.hasWaiters(condition) + "---" + lock.getWaitQueueLength(condition));
    }
}
