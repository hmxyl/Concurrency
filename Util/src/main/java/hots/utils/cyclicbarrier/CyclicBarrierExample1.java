package hots.utils.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierExample1 {
    public static void main(String[] args) {
//        CyclicBarrier barrier = new CyclicBarrier(4);
        CyclicBarrier barrier = new CyclicBarrier(4, () -> {
            System.out.println(Thread.currentThread().getName() + "- await finished");
        });

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "- begin.");
                TimeUnit.SECONDS.sleep(20);
                System.out.println(Thread.currentThread().getName() + "- end.");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + "- await finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "T1").start();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "- begin.");
                TimeUnit.SECONDS.sleep(10);
                System.out.println(Thread.currentThread().getName() + "- end.");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + "- await finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "T2").start();


        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "- begin.");
                TimeUnit.SECONDS.sleep(15);
                System.out.println(Thread.currentThread().getName() + "- end.");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + "- await finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "T3").start();


        try {
            // 主线程知晓拆分的parties执行结束
            // 将主线程加入到CyclicBarrier的parties
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
