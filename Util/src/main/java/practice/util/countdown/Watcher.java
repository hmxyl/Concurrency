package practice.util.countdown;

import java.util.concurrent.CountDownLatch;

/**
 * 监控工具
 */
abstract class Watcher {
    final CountDownLatch countDownLatch;

    Watcher(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    abstract void done();
}
