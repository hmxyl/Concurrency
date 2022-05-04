package practice.util.countdown;

import practice.common.TaskFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 验证1：验证数据量
 */
class TrustSourceRecordCount extends TableVerify{
    TrustSourceRecordCount(Table table, TableTaskBatch tableTaskBatch) {
        super(table, tableTaskBatch);
    }

    @Override
    public void run() {
        TaskFactory.spend(ThreadLocalRandom.current().nextInt(10), TimeUnit.SECONDS);
        // 设置传输完成之后的数据量
        table.setTargetCount(table.getSourceRecordCount());
        // 完成一次一张表的验证完成计数
        tableTaskBatch.done();
    }
}