package practice.util.countdown;

import java.util.concurrent.CountDownLatch;

/**
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
public class EventTaskBatch extends Watcher {
    private final Event event;

    EventTaskBatch(Event event, int taskSize) {
        super(new CountDownLatch(taskSize));
        this.event = event;
    }

    @Override
    void done() {
        countDownLatch.countDown();
        if (countDownLatch.getCount() == 0) {
            // Event涉及到的所有Table任务完成
            System.out.println("All table of event " + event.getEventName() + " is finished verify and update continue.");
            System.out.println();
        }
    }
}
