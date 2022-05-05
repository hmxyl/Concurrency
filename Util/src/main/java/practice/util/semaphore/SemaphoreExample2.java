package practice.util.semaphore;

import practice.common.TaskFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author: DH
 * @date: 2022/5/6
 * @desc:
 */
public class SemaphoreExample2 {
    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(1);
        new Thread(new TaskRunnable(semaphore), "T1").start();
        new Thread(new TaskRunnable(semaphore), "T2").start();
    }

    static class TaskRunnable implements Runnable {
        private final Semaphore semaphore;
        TaskRunnable(Semaphore semaphore) {
            this.semaphore = semaphore;
        }
        @Override
        public void run() {
            try {
                // 请求执行许可证
                System.out.println(Thread.currentThread().getName() +  " ask for permits");
                boolean tryResult = semaphore.tryAcquire();
                System.out.println(Thread.currentThread().getName() +  (tryResult ? " got permits" : " ignore permits and continue"));
                TaskFactory.spend(2, TimeUnit.SECONDS, true);
            } finally {
                // 释放许可证
                semaphore.release();
            }
        }
    }
}
