package hots.chapter5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class BooleanLock implements Lock {
    private boolean initValue = false;

    private Thread currentThread;

    private Collection<Thread> blockedThreadCollection = new ArrayList<>();

    @Override
    public synchronized void lock() throws InterruptedException {
        while (initValue) {
            blockedThreadCollection.add(Thread.currentThread());
            this.wait();
        }
        initValue = true;
        currentThread = Thread.currentThread();
        blockedThreadCollection.remove(Thread.currentThread());
    }

    @Override
    public synchronized void lock(long mills) throws InterruptedException, TimeoutException {
        if (mills <= 0) {
            lock();
        }
        long waitMills = mills;
        long endTime = System.currentTimeMillis() + waitMills;
        while (initValue) {
            if (waitMills <= 0) {
                throw new TimeoutException(Thread.currentThread().getName() + " waiting timeout");
            }
            this.wait(waitMills);
            waitMills = endTime - System.currentTimeMillis();
        }

        this.initValue = true;
        this.currentThread = Thread.currentThread();
    }

    @Override
    public synchronized void unLock() {
        if (Thread.currentThread() == currentThread) {
            initValue = false;
            System.out.println(Thread.currentThread().getName() + " release the monitor");
            this.notifyAll();
        }
    }

    @Override
    public Collection<Thread> getBlockedThread() {
        return Collections.unmodifiableCollection(blockedThreadCollection);
    }

    @Override
    public int getBlockedSize() {
        return blockedThreadCollection.size();
    }
}
