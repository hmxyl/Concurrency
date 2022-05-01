package hots.utils.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * 指定范围内数据，求和
 *
 * @author: DH
 * @date: 2022/3/14
 * @desc:
 */
public class RecursiveTaskExample {

    /* 拆分到每个任务的阈值 */
    private static final int MAX_THRESHOLD = 3;

    public static void main(String[] args) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> future = forkJoinPool.submit(new CalculateRecursiveTask(1, 100));
        System.out.println("==================other tasks：====================");
        try {
            System.out.println("================== action results：" + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class CalculateRecursiveTask extends RecursiveTask<Integer> {
        private final int start;
        private final int end;

        public CalculateRecursiveTask(int start, int end) {
            if (end >= start) {
                this.start = start;
                this.end = end;
            } else {
                this.end = start;
                this.start = end;
            }

        }

        @Override
        protected Integer compute() {
            if (end - start <= MAX_THRESHOLD) {
                return IntStream.rangeClosed(start, end).sum();
            } else {
                int middle = (end + start) / 2;

                // 任务拆分
                CalculateRecursiveTask left = new CalculateRecursiveTask(start, middle);
                CalculateRecursiveTask right = new CalculateRecursiveTask(middle + 1, end);

                // 任务入池
                left.fork();
                right.fork();

                // 任务完成：输出，无异常
                return left.join() + right.join();
            }
        }
    }
}
