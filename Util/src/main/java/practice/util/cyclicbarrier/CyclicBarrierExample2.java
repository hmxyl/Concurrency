package practice.util.cyclicbarrier;

import practice.common.TaskFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierExample2 {

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            System.out.println("All parties action finished");
        });
        new Thread(new ActionRunnable(barrier), "T1").start();
        new Thread(new ActionRunnable(barrier), "T2").start();

        TaskFactory.spendSeconds(6);
        System.out.println(barrier.getNumberWaiting());

        barrier.reset();
        TaskFactory.spendSeconds(2);
        System.out.println(barrier.getNumberWaiting());
    }

    private static class ActionRunnable implements Runnable {
        private final CyclicBarrier barrier;

        public ActionRunnable(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                TaskFactory.spend(ThreadLocalRandom.current().nextInt(5), TimeUnit.SECONDS, false, true);
                barrier.await();
                System.out.println(Thread.currentThread().getName() + "- await finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}