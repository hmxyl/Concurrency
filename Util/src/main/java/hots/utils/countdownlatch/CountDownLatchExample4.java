package hots.utils.countdownlatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchExample4 {

    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 模拟两次批处理数据
        Event[] events = {new Event("Event-1"), new Event("Event-2")};
        for (Event event : events) {
            // 每次批处理，提交10张表数据
            List<Table> tables = capture(event);
            EventTaskBatch eventTaskBatch = new EventTaskBatch(event, tables.size());
            for (Table table : tables) {
                // 与Table相关的任务技术监控。
                TableTaskBatch tableTaskBatch = new TableTaskBatch(eventTaskBatch, table, 2);
                executorService.submit(new TrustSourceRecordCount(table, tableTaskBatch));
                executorService.submit(new TrustSourceColumns(table, tableTaskBatch));
            }
        }

        executorService.shutdown();

    }

    private static class Event {
        private String eventName;

        public Event(String eventName) {
            this.eventName = eventName;
        }
    }


    private static class Table {
        // 表名
        String tableName;
        // 原始记录条数
        long sourceRecordCount = 10;
        // 传输完成后的记录条数：验证1
        long targetCount;
        // 原始schema
        String sourceColumnSchema = "<table name='a'><column name='c1' type='varchar'></column></table>";
        // 传输完成之后的schema：验证2
        String targetColumnSchema = "";


        public Table(String tableName, long sourceRecordCount) {
            this.tableName = tableName;
            this.sourceRecordCount = sourceRecordCount;
        }
    }

    private static abstract class Watcher {
        final CountDownLatch countDownLatch;

        Watcher(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        abstract void done();
    }

    /**
     * Table验证任务完成监控
     */
    private static class TableTaskBatch extends Watcher {
        private final Table table;

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
                System.out.println("All tasks of " + table.tableName + " is finished verify and update continue.");
                eventTaskBatch.done();
            }
        }
    }

    private static class EventTaskBatch extends Watcher {
        private final Event event;

        EventTaskBatch(Event event, int taskSize) {
            super(new CountDownLatch(taskSize));
            this.event = event;
        }

        @Override
        public void done() {
            countDownLatch.countDown();
            if (countDownLatch.getCount() == 0) {
                // Event涉及到的所有Table任务完成
                System.out.println("All table of event " + event.eventName + " is finished verify and update continue.");
            }
        }

    }

    private static List<Table> capture(Event event) {
        List<Table> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Table(event.eventName + "-Table-" + i, i * 1000));
        }
        return list;
    }

    /**
     * Table 验证表
     */
    private static abstract class TableVerify {
        protected final Table table;

        protected final TableTaskBatch tableTaskBatch;

        public TableVerify(Table table, TableTaskBatch tableTaskBatch) {
            this.table = table;
            this.tableTaskBatch = tableTaskBatch;
        }
    }

    /**
     * 验证1：验证数据量
     */
    private static class TrustSourceRecordCount extends TableVerify implements Runnable {
        TrustSourceRecordCount(Table table, TableTaskBatch tableTaskBatch) {
            super(table, tableTaskBatch);
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(10_000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            table.targetCount = table.sourceRecordCount;
            // Table的验证任务1完成，通知一下
            tableTaskBatch.done();
        }
    }

    /**
     * 验证2：验证表结构
     */
    private static class TrustSourceColumns extends TableVerify implements Runnable {
        TrustSourceColumns(Table table, TableTaskBatch tableTaskBatch) {
            super(table, tableTaskBatch);
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(10_000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            table.targetColumnSchema = table.sourceColumnSchema;
            // Table的验证任务2完成，通知一下
            tableTaskBatch.done();
        }
    }


}
