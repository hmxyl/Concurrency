package practice.util.semaphore;

import practice.common.TaskFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author: DH
 * @date: 2022/5/6
 * @desc:
 */
public class SemaphoreExample {
    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(1);

        new Thread(() -> {
            try {
                // 请求执行许可证
                System.out.println("T1 ask for permits");
                semaphore.acquire();
                System.out.println("T1 got permits");
                TaskFactory.spend(10, TimeUnit.SECONDS, true);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                // 释放许可证
                semaphore.release();
            }
        }, "T1").start();

        new Thread(() -> {
            try {
                System.out.println("T2 ask for permits");
                semaphore.acquire();
                System.out.println("T2 got permits");
                TaskFactory.spend(5, TimeUnit.SECONDS, true);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                // 释放许可证
                semaphore.release();
            }
        }, "T2").start();
    }
}
