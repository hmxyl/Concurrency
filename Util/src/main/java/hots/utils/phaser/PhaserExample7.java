package hots.utils.phaser;

import org.junit.Test;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 测试awaitAdvance 不参与 party 的数据量统计中
 *
 * @author: DH
 * @date: 2022/3/15
 * @desc:
 */
public class PhaserExample7 {
    @Test
    public void testAwaitAdvance1() throws InterruptedException {
        final Phaser phaser = new Phaser(2);
        new Thread(() -> phaser.arriveAndAwaitAdvance()).start();
        TimeUnit.SECONDS.sleep(1);

        int testResult = phaser.getArrivedParties();
        assert testResult == 1;
    }

    @Test
    public void testAwaitAdvance2() throws InterruptedException {
        final Phaser phaser = new Phaser(2);
        new Thread(() -> phaser.awaitAdvance(phaser.getPhase())).start();
        TimeUnit.SECONDS.sleep(1);
        int testResult = phaser.getArrivedParties();
        assert testResult == 0;
    }
}
