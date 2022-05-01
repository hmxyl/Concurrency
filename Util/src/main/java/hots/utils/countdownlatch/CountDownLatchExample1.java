package hots.utils.countdownlatch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class CountDownLatchExample1 {
    private final static AtomicInteger count = new AtomicInteger();
    private final static Random random = new Random();

    private static CountDownLatch latch;
    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws InterruptedException {
        // step1: 获取查询数据
        int[] data = IntStream.rangeClosed(1, 5).map(e -> random.nextInt(1_000)).toArray();
        latch = new CountDownLatch(data.length);

        // step2：根据查询数据分配多个线程执行
        for (int i = 0; i < data.length; i++) {
            executorService.submit(new SimpleRunnable(latch, count, i, data[i]));
        }
        System.out.printf("All works submitted.\n");
        latch.await();

        executorService.shutdown();
        // step3
        System.out.printf("All works finished. Support with [%d] threads.\n", count.get());

    }

    private static class SimpleRunnable implements Runnable {
        private final int index;
        private final int param;
        private final CountDownLatch latch;
        private final AtomicInteger count;

        SimpleRunnable(CountDownLatch latch, AtomicInteger count, int index, int param) {
            this.index = index;
            this.param = param;
            this.count = count;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(100);
                System.out.printf("%s deal with [%d]-[%d] \n", Thread.currentThread().getName(), index, param);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                count.incrementAndGet();
                latch.countDown();
            }
        }
    }
}
