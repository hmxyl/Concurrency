package practice.util.countdown;

import lombok.Getter;
import lombok.Setter;

/**
 * 统计表
 */
@Getter
@Setter
class Table {
    // 表名
    private String tableName;
    // 原始记录条数
    private long sourceRecordCount = 10;
    // 传输完成后的记录条数：验证1
    private long targetCount;
    // 原始schema
    private String sourceColumnSchema = "<table name='a'><column name='c1' type='varchar'></column></table>";
    // 传输完成之后的schema：验证2
    private String targetColumnSchema = "";

    public Table(String tableName, long sourceRecordCount) {
        this.tableName = tableName;
        this.sourceRecordCount = sourceRecordCount;
    }
}
