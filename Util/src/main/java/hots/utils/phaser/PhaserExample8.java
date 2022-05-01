package hots.utils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 测试 利用 {@link java.util.concurrent.Phaser#arriveAndAwaitAdvance} 监控所有party完成指定任务，才允许后续操作
 *
 * @author: DH
 * @date: 2022/3/15
 * @desc:
 */
public class PhaserExample8 {
    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(2);
        IntStream.rangeClosed(1, 2).boxed().map(i -> phaser).forEach(AwaitAdvanceTask::new);
        // 若将phaser的parties注册为3，程序会加入阻塞状态
        phaser.awaitAdvance(phaser.getPhase());
        System.out.println("【" + Thread.currentThread().getName() + "】 all part one finished.");
    }

    static class AwaitAdvanceTask extends Thread {
        private final Phaser phaser;

        public AwaitAdvanceTask(Phaser phaser) {
            this.phaser = phaser;
            this.start();
        }

        @Override
        public void run() {
            // 需要监控完成的工作
            actionDeal("part one", 2);
            phaser.arrive();
            // 完成其他工作
            actionDeal("part two", 4);
        }
    }

    static void actionDeal(String actionName, int seconds) {
        System.out.println("【" + Thread.currentThread().getName() + "】 start " + actionName + ".");
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("【" + Thread.currentThread().getName() + "】 finish  " + actionName + ".");
    }
}
