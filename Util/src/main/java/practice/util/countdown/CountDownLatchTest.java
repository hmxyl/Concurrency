package practice.util.countdown;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
public class CountDownLatchTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            // 不同数据源的数据，批处理
            Event[] events = {new Event("Event-1"), new Event("Event-2")};
            for (Event event : events) {
                // 获取数据源表资源概况
                List<Table> tables = capture(event);
                EventTaskBatch eventTaskBatch = new EventTaskBatch(event, tables.size());
                for (Table table : tables) {
                    // 与Table相关的任务技术监控。
                    TableTaskBatch tableTaskBatch = new TableTaskBatch(eventTaskBatch, table, 2);
                    executorService.submit(new TrustSourceRecordCount(table, tableTaskBatch));
                    executorService.submit(new TrustSourceColumns(table, tableTaskBatch));
                }
            }
        } finally {
            executorService.shutdown();
        }

    }

    private static List<Table> capture(Event event) {
        List<Table> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Table(event.getEventName() + "-Table-" + i, i * 1000));
        }
        return list;
    }

}
