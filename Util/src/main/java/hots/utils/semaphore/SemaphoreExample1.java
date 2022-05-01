package hots.utils.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreExample1 {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " begin.");
                System.out.println(Thread.currentThread().getName() + " - " + semaphore.tryAcquire());
                System.out.println(Thread.currentThread().getName() + " end.");
            }, "Thread " + i).start();
        }
    }
}
