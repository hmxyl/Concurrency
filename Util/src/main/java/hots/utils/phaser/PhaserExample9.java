package hots.utils.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 测试{@link java.util.concurrent.Phaser#awaitAdvanceInterruptibly(int)}的使用
 *
 * @author: DH
 * @date: 2022/3/16
 * @desc:
 */
public class PhaserExample9 {
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(2);
        final int finishTime = 2;
        // phase未结束，可以被打断，其他的 party 仍然能够继续执行
        final int waitTimeBeforeInterrupt = 1;
        // phase已结束，不会抛出打断异常
        // final int waitTimeBeforeInterrupt = 4;

        IntStream.rangeClosed(1, 2).forEach(i -> {
            new Thread(() -> {
                actionDeal(finishTime);
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() + ": continue.");
            }).start();
        });

        Thread thread = new Thread(() -> {
            try {
                phaser.awaitAdvanceInterruptibly(phaser.getPhase());
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(Thread.currentThread().getName() + ": 未完成party数：" + phaser.getUnarrivedParties());
            }
        });
        thread.start();
        actionDeal(waitTimeBeforeInterrupt);
        thread.interrupt();
        System.out.println("=================================");
    }

    private static void actionDeal(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
