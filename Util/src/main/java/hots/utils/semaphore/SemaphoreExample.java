package hots.utils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreExample {
    public static void main(String[] args) {
        // 设置许可证容量为1，则第一个线程获得许可证之后，另外一个线程需要等待中
        // Semaphore semaphore = new Semaphore(1);

        // 设置许可证容量为2，则两个线程均能获得许可证
        Semaphore semaphore = new Semaphore(2);
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " begin.");
                try {
                    semaphore.acquire();
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                System.out.println(Thread.currentThread().getName() + " end.");
            }, "Thread " + i).start();
        }
    }
}
