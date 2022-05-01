package hots.chapter12;


import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


/**
 * balking 设计模式核心：
 */
public class BalkingData {
    private String fileName;
    private String content;
    private boolean changed;

    public BalkingData(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        this.changed = true;
    }

    public synchronized void change(String newContent) {
        this.content = newContent;
        this.changed = true;
    }

    /**
     * 多个线程同时执行保存任务，其中一个线程完成后changed被调整，另外一个线程不会继续执行
     */
    public synchronized void save() throws IOException {
        if (!changed) {
            return;
        }
        doSave();
        this.changed = false;
    }


    private void doSave() throws IOException {
        System.out.println(Thread.currentThread().getName() + " calls do save. content = " + content);
        try (Writer writer = new FileWriter(fileName, true)) {
            writer.write(content);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }
}
