package hots.utils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 测试 Phaser 减少计数器（动态销户）
 *
 * @author: DH
 * @date: 2022/3/15
 * @desc:
 */
public class PhaserExample3 {
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
            actionDeal("running");
            // 第一轮等待：所有运动员跑步全部完成
            phaser.arriveAndAwaitAdvance();

            // 5
            monitor(1, "phaser.getRegisteredParties", phaser.getRegisteredParties());

            if (Thread.currentThread().getName().endsWith("2")) {
                // 任务执行失败
                actionFailed("bicycle");
                // 运动员退出比赛（退出计数）
                phaser.arriveAndDeregister();
                // 退出计数之后，后续流程无法重新参与计数。
                return;
            } else {
                // 正常完成
                actionDeal("bicycle");
                // 第二轮等待：所有运动员自行车全部完成
                phaser.arriveAndAwaitAdvance();
            }

            // 4（party 通过arriveAndDeregister注销了一个）
            monitor(2, "phaser.getRegisteredParties", phaser.getRegisteredParties());

            actionDeal("swimming");
            // 第三轮等待：所有运动员游泳完成
            phaser.arriveAndAwaitAdvance();
        }
    }

    static void monitor(int index, String item, Object object) {
        System.out.printf("【%s】【monitor-%d】【%30s】%s\n", Thread.currentThread().getName(), index, item, object);
    }

    static void actionFailed(String sportName) {
        System.out.println("【" + Thread.currentThread().getName() + "】 start " + sportName + " and failed. ");
    }

    static void actionDeal(String sportName) {
        System.out.println("【" + Thread.currentThread().getName() + "】 start " + sportName + ".");

        try {
            TimeUnit.SECONDS.sleep(random.nextInt(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("【" + Thread.currentThread().getName() + "】 finish  " + sportName + ".");
    }
}
