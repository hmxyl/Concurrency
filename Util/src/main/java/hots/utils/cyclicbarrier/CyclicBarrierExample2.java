package hots.utils.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierExample2 {
    public static void main(String[] args) throws InterruptedException {

        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            System.out.println("All parties action finished");
        });

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "- enter await.");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + "- await finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "T1").start();
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + "- enter await.");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + "- await finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "T2").start();

        System.out.println(barrier.getNumberWaiting());
        TimeUnit.SECONDS.sleep(6);

        barrier.reset();
        TimeUnit.SECONDS.sleep(2);
        System.out.println(barrier.getNumberWaiting());

    }
}
