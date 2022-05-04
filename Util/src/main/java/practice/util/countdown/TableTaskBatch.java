package practice.util.countdown;

import java.util.concurrent.CountDownLatch;

/**
 * Table验证任务完成监控
 */
class TableTaskBatch extends Watcher {
    private final Table table;

    /* 每张表存在多个验证任务 */
    private EventTaskBatch eventTaskBatch;

    TableTaskBatch(EventTaskBatch eventTaskBatch, Table table, int taskSize) {
        super(new CountDownLatch(taskSize));
        this.table = table;
        this.eventTaskBatch = eventTaskBatch;
    }

    @Override
    public void done() {
        countDownLatch.countDown();
        if (countDownLatch.getCount() == 0) {
            // Table相关所有任务完成
            System.out.println("All tasks of " + table.getTableName() + " is finished verify and update continue.");
            eventTaskBatch.done();
        }
    }
}