package hots.utils.phaser;

import java.util.concurrent.Phaser;

/**
 * @author: DH
 * @date: 2022/3/15
 * @desc:
 */
public class PhaserExample4 {
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(5);
        phaser.bulkRegister(5);
        monitor(1, "phaser.getRegisteredParties", phaser.getRegisteredParties());
        monitor(1, "phaser.getArrivedParties", phaser.getArrivedParties());
        new Thread(phaser::arriveAndAwaitAdvance).start();
        monitor(2, "phaser.getArrivedParties", phaser.getArrivedParties());
        monitor(2, "phaser.getUnarrivedParties", phaser.getUnarrivedParties());
    }

    static void monitor(int index, String item, Object object) {
        System.out.printf("【%s】【monitor-%d】【%30s】%s\n", Thread.currentThread().getName(), index, item, object);
    }
}
