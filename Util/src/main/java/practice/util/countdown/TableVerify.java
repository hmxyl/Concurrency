package practice.util.countdown;

/**
 * 表验证
 */
abstract class TableVerify implements Runnable {

    protected final Table table;

    protected final TableTaskBatch tableTaskBatch;

    public TableVerify(Table table, TableTaskBatch tableTaskBatch) {
        this.table = table;
        this.tableTaskBatch = tableTaskBatch;
    }
}