package hots.utils.forkjoin;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author: DH
 * @date: 2022/3/14
 * @desc:
 */
public class RecursiveActionExample {
    public static final AtomicInteger RESULT = new AtomicInteger();
    public static final int MAX_THRESHOLD = 3;

    public static void main(String[] args) throws InterruptedException {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(new CalculateRecursiveActon(1, 100));
        forkJoinPool.awaitTermination(1, TimeUnit.SECONDS);
        Optional.of(RESULT).ifPresent(System.out::println);
        // TimeUnit.MILLISECONDS.sleep(100);
        // while (forkJoinPool.isQuiescent()) { // Returns true if all worker threads are currently idle
        //     Optional.of(RESULT).ifPresent(System.out::println);
        //     return;
        // }
    }

    private static class CalculateRecursiveActon extends RecursiveAction {
        private final int start;
        private final int end;

        public CalculateRecursiveActon(int start, int end) {
            if (end >= start) {
                this.start = start;
                this.end = end;
            } else {
                this.end = start;
                this.start = end;
            }

        }

        @Override
        protected void compute() {
            if (end - start <= MAX_THRESHOLD) {
                RESULT.getAndAdd(IntStream.rangeClosed(start, end).sum());
            } else {
                int middle = (end + start) / 2;
                // 任务拆分
                CalculateRecursiveActon left = new CalculateRecursiveActon(start, middle);
                CalculateRecursiveActon right = new CalculateRecursiveActon(middle + 1, end);
                // 任务入池
                left.fork();
                right.fork();
            }
        }
    }


}
