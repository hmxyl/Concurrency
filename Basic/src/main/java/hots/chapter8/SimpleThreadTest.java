package hots.chapter8;

import java.util.stream.IntStream;

/**
 * 线程池测试类
 */
public class SimpleThreadTest {
    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool threadPool = new SimpleThreadPool();
        IntStream.rangeClosed(0, 40)
                .forEach(index -> {
                    threadPool.submit(() -> {
                                try {
                                    Thread.sleep(1_000L);
                                    System.out.println("Task " + index + " be serviced by " + Thread.currentThread().getName());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
                });
        // Thread.sleep(10_000);
        threadPool.shutDown();
    }
}
