package hots.utils.lock.condition;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: DH
 * @date: 2022/3/5
 * @desc:
 */
public class ConditionExample1 {
    private final static ReentrantLock sourceLock = new ReentrantLock();

    private final static Condition condition = sourceLock.newCondition();

    private static int data = 0;

    private static boolean isUsed = false;


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
            sourceLock.lock();
            while (!isUsed) {
                condition.await();
            }

            TimeUnit.SECONDS.sleep(1);
            data++;
            Optional.of("P：" + data).ifPresent(System.out::println);
            isUsed = false;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sourceLock.unlock();
        }
    }


    private static void useData() {
        try {
            sourceLock.lock();
            while (isUsed) {
                condition.await();
            }
            TimeUnit.SECONDS.sleep(1);
            Optional.of("C：" + data).ifPresent(System.out::println);
            isUsed = true;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sourceLock.unlock();
        }
    }
}
