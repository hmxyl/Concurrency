package hots.utils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 测试 Phaser 重复使用 计数器
 *
 * @author: DH
 * @date: 2022/3/15
 * @desc:
 */
public class PhaserExample2 {
    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        final Phaser phaser = new Phaser(5);
        IntStream.rangeClosed(1, 5).boxed().map(i -> phaser).forEach(Athlete::new);
    }

    static class Athlete extends Thread {
        private final Phaser phaser;

        public Athlete(Phaser phaser) {
            this.phaser = phaser;
            this.start();
        }

        @Override
        public void run() {
            // 输出：0
            monitor(0, "phaser.getPhase", phaser.getPhase());
            actionDeal("running");
            // 第一轮等待：所有运动员跑步全部完成
            phaser.arriveAndAwaitAdvance();

            // 输出：1（所有parties 在 arriveAndAwaitAdvance之后，Phase 自增1）
            monitor(1, "phaser.getPhase", phaser.getPhase());
            actionDeal("bicycle");
            // 第二轮等待：所有运动员自行车全部完成
            phaser.arriveAndAwaitAdvance();

            // 输出：2
            monitor(2, "phaser.getPhase", phaser.getPhase());
            actionDeal("swimming");
            // 第三轮等待：所有运动员游泳完成
            phaser.arriveAndAwaitAdvance();
        }
    }

    static void monitor(int index, String item, Object object) {
        System.out.printf("【%s】【monitor-%d】【%30s】%s\n", Thread.currentThread().getName(), index, item, object);
    }


    static void actionDeal(String taskName) {
        System.out.println("【" + Thread.currentThread().getName() + "】 start " + taskName + ".");

        try {
            TimeUnit.SECONDS.sleep(random.nextInt(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("【" + Thread.currentThread().getName() + "】 finish  " + taskName + ".");
    }
}
