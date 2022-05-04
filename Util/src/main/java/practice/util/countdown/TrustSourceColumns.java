package practice.util.countdown;

import practice.common.TaskFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 验证2：验证表结构
 */
class TrustSourceColumns extends TableVerify {
    TrustSourceColumns(Table table, TableTaskBatch tableTaskBatch) {
        super(table, tableTaskBatch);
    }

    @Override
    public void run() {
        TaskFactory.spend(ThreadLocalRandom.current().nextInt(10), TimeUnit.SECONDS);
        table.setTargetColumnSchema(table.getSourceColumnSchema());
        // 完成一次一张表的验证完成计数
        tableTaskBatch.done();
    }
}
