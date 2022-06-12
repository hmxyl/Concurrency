package practice.util.lock.condition;

import practice.common.TaskFactory;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: DH
 * @date: 2022/5/29
 * @desc:
 */
public class ConditionExample {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    private static int data = 1;
    private static volatile boolean used = true;

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                buildData();
            }
        }).start();

        new Thread(() -> {
            while (true) {
                useData();
            }
        }).start();
    }

    private static void buildData() {
        try {
            lock.lock();
            while (used) {
                condition.await();
            }
            //  used 为 false 生产数据
            ++data;
            Optional.of("P: " + data).ifPresent(System.out::println);
            TaskFactory.spend(1, TimeUnit.SECONDS);
            used = true;
            condition.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private static void useData() {
        try {
            lock.lock();
            while (!used) {
                condition.await();
            }
            //  used 为 true 消费数据
            --data;
            Optional.of("C: " + data).ifPresent(System.out::println);
            TaskFactory.spend(1, TimeUnit.SECONDS);
            used = false;
            condition.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
