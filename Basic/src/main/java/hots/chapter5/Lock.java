package hots.chapter5;

import java.util.Collection;

public interface Lock {
    void lock() throws InterruptedException;

    void lock(long mills) throws InterruptedException, TimeoutException;

    void unLock();

    Collection<Thread> getBlockedThread();

    int getBlockedSize();

    class TimeoutException extends Exception {
        public TimeoutException(String message) {
            super(message);
        }
    }
}
