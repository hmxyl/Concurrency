package hots.chapter6;

/**
 * 读写分离锁
 */
public class ReadWriteLock {
    private int readingReaders = 0;
    private int waitingReaders = 0;
    private int writingWriters = 0;
    private int waitingWriters = 0;

    // 控制倾向性，有写操作等待时，优先执行
    private final boolean preferWriter;

    public ReadWriteLock() {
        this.preferWriter = true;
    }

    public ReadWriteLock(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }


    public synchronized void readLock() throws InterruptedException {
        try {
            this.waitingReaders++;
            while (writingWriters > 0 || (preferWriter && waitingWriters > 0)) {
                this.wait();
            }
            this.readingReaders++;
        } finally {
            this.waitingReaders--;
        }
    }

    public synchronized void readUnlock() {
        this.readingReaders--;
        this.notifyAll();
    }

    public synchronized void writeLock() throws InterruptedException {
        try {
            this.waitingWriters++;
            while (readingReaders > 0 || writingWriters > 0) {
                this.wait();
            }
            writingWriters++;
        } finally {
            waitingWriters--;
        }
    }

    public synchronized void writeUnlock() {
        writingWriters--;
        notifyAll();
    }
}
