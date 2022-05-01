package hots.utils.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * 测试{@link java.util.concurrent.Phaser#awaitAdvanceInterruptibly(int, long, TimeUnit)}的使用
 *
 * @author: DH
 * @date: 2022/3/16
 * @desc:
 */
public class PhaserExample10 {
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(2);
        final int finishTime = 2;
        // 超时，抛出TimeoutException
        final int waitTime = 1;
        // 未超时，无异常抛出
        // final int waitTime = 4;
        
        IntStream.rangeClosed(1, 2).forEach(i -> {
            new Thread(() -> {
                actionDeal(finishTime);
                phaser.arrive();
                System.out.println(Thread.currentThread().getName() + ": continue.");
            }).start();
        });

        new Thread(() -> {
            try {
                phaser.awaitAdvanceInterruptibly(phaser.getPhase(), waitTime, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
                System.out.println(Thread.currentThread().getName() + ": 未完成party数：" + phaser.getUnarrivedParties());
            }
        }).start();
    }

    private static void actionDeal(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
