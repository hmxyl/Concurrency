package hots.utils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 测试phaser的arrive方法
 * @author: DH
 * @date: 2022/3/15
 * @desc:
 */
public class PhaserExample6 {
    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        final Phaser phaser = new Phaser(3);
        IntStream.rangeClosed(1, 2).boxed().map(i -> phaser).forEach(ArriveTask::new);
        phaser.arriveAndAwaitAdvance();// 此处，会阻塞，等待part one 全部完成
        System.out.println("【" + Thread.currentThread().getName() + "】 part one all done");
    }


    static class ArriveTask extends Thread {
        private final Phaser phaser;

        public ArriveTask(Phaser phaser) {
            this.phaser = phaser;
            this.start();
        }

        @Override
        public void run() {
            actionDeal("part one", 3);
            phaser.arrive();// 不阻塞
            actionDeal("part two", 6);
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
