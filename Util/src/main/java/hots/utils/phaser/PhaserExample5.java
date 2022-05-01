package hots.utils.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author: DH
 * @date: 2022/3/15
 * @desc:
 */
public class PhaserExample5 {
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(2) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                return true;
            }
        };
        IntStream.rangeClosed(1, 2).boxed().map(i -> phaser).forEach(OnAdvanceTask::new);
    }

    static class OnAdvanceTask extends Thread {
        private final Phaser phaser;

        public OnAdvanceTask(Phaser phaser) {
            this.phaser = phaser;
            this.start();
        }

        @Override
        public void run() {
            System.out.println("【" + this.getName() + "】 start part one");
            phaser.arriveAndAwaitAdvance();
            System.out.println("【" + this.getName() + "】 end part one");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            monitor(1, "phaser.getPhase", phaser.getPhase());
            monitor(1, "phaser.getArrivedParties", phaser.getArrivedParties());
            monitor(1, "phaser.getUnarrivedParties", phaser.getUnarrivedParties());
            monitor(1, "phaser.isTerminated", phaser.isTerminated());


            if (this.getName().endsWith("1")) {
                // 非全部party能够继续执行
                // onAdvance 设置为true，arriveAndAwaitAdvance不会阻塞
                System.out.println("【" + this.getName() + "】 start part two");
                phaser.arriveAndAwaitAdvance();
                System.out.println("【" + this.getName() + "】 end part two");
            }

            monitor(2, "phaser.getPhase", phaser.getPhase());
            monitor(2, "phaser.getArrivedParties", phaser.getArrivedParties());
            monitor(2, "phaser.getUnarrivedParties", phaser.getUnarrivedParties());
            monitor(2, "phaser.isTerminated", phaser.isTerminated());


            phaser.arriveAndAwaitAdvance();

            monitor(3, "phaser.getPhase", phaser.getPhase());
            monitor(3, "phaser.isTerminated", phaser.isTerminated());

        }
    }

    static void monitor(int index, String item, Object object) {
        System.out.printf("【%s】【monitor-%d】【%30s】%s\n", Thread.currentThread().getName(), index, item, object);
    }
}
