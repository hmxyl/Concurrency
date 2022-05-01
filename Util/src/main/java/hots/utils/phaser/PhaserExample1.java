package hots.utils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 测试Phaser动态注册party
 *
 * @author: DH
 * @date: 2022/3/15
 * @desc:
 */
public class PhaserExample1 {
    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        final Phaser phaser = new Phaser();
        IntStream.rangeClosed(1, 5).boxed().map(i -> phaser).forEach(TestTask::new);
        System.out.println("【1】【phaser.getRegisteredParties 】" + phaser.getRegisteredParties());
        // 注册main线程
        phaser.register();
        // 类似CyclicBarrier的await
        phaser.arriveAndAwaitAdvance();
        // 等待所有线程全部到达隔离点之后执行
        System.out.println("【" + Thread.currentThread().getName() + " 】 all threads finished the work.");
    }

    static class TestTask extends Thread {
        private final Phaser phaser;

        public TestTask(Phaser phaser) {
            this.phaser = phaser;
            // 动态追加party
            this.phaser.register();
            // 任务启动
            this.start();
        }

        @Override
        public void run() {
            System.out.println("【" + Thread.currentThread().getName() + " 】 start working.");
            try {
                TimeUnit.SECONDS.sleep(random.nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndAwaitAdvance();
            System.out.println("【" + Thread.currentThread().getName() + " 】 end working.");
        }
    }
}
