package practice.util.lock.condition;

import practice.common.TaskFactory;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * 多线程生产、多线程消费
 *
 * @author: DH
 * @date: 2022/6/12
 * @desc:
 */
public class ConditionExample3 {

    private static Lock lock = new ReentrantLock();

    private static final Condition PRODUCE_COND = lock.newCondition();

    private static final Condition CONSUMER_COND = lock.newCondition();

    private static final LinkedList<Long> TIMESTAMP_POOL = new LinkedList<>();

    private static final int MAX_SIZE = 100;

    public static void main(String[] args) {
        // 包装多名生产者
        IntStream.rangeClosed(1, 5).boxed().forEach(ConditionExample3::doBuildData);
        // 包装多名消费者
        IntStream.rangeClosed(1, 8).boxed().forEach(ConditionExample3::doConsumeData);
    }

    private static void doBuildData(int index) {
        // 生产者不间断生产数据
        new Thread(() -> {
            while (true) {
                buildData();
                TaskFactory.spend(1, TimeUnit.SECONDS);
            }
        }, "P(" + index + ")").start();
    }

    private static void doConsumeData(int index) {
        // 消费者不间断消费数据
        new Thread(() -> {
            while (true) {
                useData();
                TaskFactory.spend(1, TimeUnit.SECONDS);
            }
        }, "C(" + index + ")").start();
    }

    private static void buildData() {
        try {
            lock.lock();
            while (TIMESTAMP_POOL.size() > MAX_SIZE) {
                PRODUCE_COND.await();
            }
            TaskFactory.spend(1, TimeUnit.SECONDS);
            long value = System.currentTimeMillis();
            TIMESTAMP_POOL.addLast(value);
            System.out.println(Thread.currentThread() + "->" + value);
            CONSUMER_COND.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private static void useData() {
        try {
            lock.lock();
            while (TIMESTAMP_POOL.isEmpty()) {
                CONSUMER_COND.await();
            }
            TaskFactory.spend(1, TimeUnit.SECONDS);
            long value = TIMESTAMP_POOL.removeFirst();
            System.out.println(Thread.currentThread() + "->" + value);
            PRODUCE_COND.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
