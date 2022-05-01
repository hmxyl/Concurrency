package hots.chapter8;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SimpleThreadPool extends Thread {
    /* 线程池现有容量：包含FREE, RUNNING, BLOCKED 三种状态的线程 */
    private int poolSize;

    private static volatile int seq = 0;

    private static final String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";

    private final int queueSize;

    /* 默认TASK_QUEUE的阈值 */
    public static final int DEFAULT_TASK_QUEUE_SIZE = 2000;

    private final static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    private final static ThreadGroup GROUP = new ThreadGroup("Pool_Group");

    private final static List<MyThread> THREAD_QUEUE = new ArrayList<>();

    private final DiscardPolicy discardPolicy;

    // 线程池，无能力处理，策略
    public final static DiscardPolicy DEFAULT_DISCARD_POLICY = () -> {
        throw new DiscardException("Discard this task");
    };

    // 线程池，销毁标记
    private volatile boolean destroy = false;

    private final int minPoolSize;

    public static final int DEFAULT_MIN_POOL_SIZE = 4;

    private final int activePoolSize;

    public static final int DEFAULT_ACTIVE_POOL_SIZE = 8;

    private final int maxPoolSize;

    public static final int DEFAULT_MAX_POOL_SIZE = 12;

    public SimpleThreadPool() {
        this(DEFAULT_MIN_POOL_SIZE, DEFAULT_ACTIVE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE, DEFAULT_TASK_QUEUE_SIZE, DEFAULT_DISCARD_POLICY);
    }

    public SimpleThreadPool(int minPoolSize, int activePoolSize, int maxPoolSize, int queueSize, DiscardPolicy discardPolicy) {
        this.minPoolSize = minPoolSize;
        this.activePoolSize = activePoolSize;
        this.maxPoolSize = maxPoolSize;

        this.queueSize = queueSize;
        this.discardPolicy = discardPolicy;

        // 初始化，最小容量线程池
        for (int i = 0; i < minPoolSize; i++) {
            createMyThread();
        }
        // 线程池，根据工作量，自动调整容量
        this.setName(THREAD_PREFIX + "head");
        this.start();
        resetPoolSize();
    }

    private void createMyThread() {
        MyThread myThread = new MyThread(GROUP, THREAD_PREFIX + (seq++));
        THREAD_QUEUE.add(myThread);
        myThread.start();
    }

    /**
     * 获取线程池大小：每次线程池，新增/销毁线程的时候，调用
     */
    private void resetPoolSize() {
        this.poolSize = THREAD_QUEUE.size();
    }

    /**
     * 线程池，根据工作量，自动调整容量
     */
    @Override
    public void run() {
        while (!destroy) {
            // 扩展线程池
            if (TASK_QUEUE.size() > activePoolSize && poolSize < activePoolSize) {
                for (int i = poolSize; i < activePoolSize; i++) {
                    createMyThread();
                }
                System.out.println("The pool increased to activePoolSize.");
                resetPoolSize();
            }
            if (TASK_QUEUE.size() > maxPoolSize && poolSize < maxPoolSize) {
                for (int i = poolSize; i < maxPoolSize; i++) {
                    createMyThread();
                }
                System.out.println("The pool increased to maxPoolSize.");
                resetPoolSize();
            }

            // 缩减线程池
            synchronized (THREAD_QUEUE) {
                if (TASK_QUEUE.isEmpty()
                        && THREAD_QUEUE.stream().filter(e -> e.taskStatus == TaskStatus.RUNNING).count() == 0
                        && poolSize > activePoolSize) {
                    int releaseCount = poolSize - activePoolSize;
                    Iterator<MyThread> it = THREAD_QUEUE.iterator();
                    while (it.hasNext()) {
                        if (releaseCount <= 0) {
                            break;
                        }
                        MyThread myThread = it.next();
                        myThread.close();
                        myThread.interrupt();
                        it.remove();
                        --releaseCount;
                        System.out.println(myThread.getName() + " had been released");
                    }
                    resetPoolSize();
                }
            }
        }
        System.out.println(Thread.currentThread().getName() + "---- is dead");
    }

    public void shutDown() throws InterruptedException {
        // 等待现有线程池中任务执行完成
        while (!TASK_QUEUE.isEmpty() || THREAD_QUEUE.stream().filter(e -> e.taskStatus == TaskStatus.RUNNING).count() > 0) {
            Thread.sleep(50);
        }

        synchronized (THREAD_QUEUE) {
            // 进行关停销毁
            System.out.println("The pool is ready to destroy");
            Iterator<MyThread> it = THREAD_QUEUE.iterator();
            while (it.hasNext()) {
                MyThread myThread = it.next();
                if (myThread.taskStatus == TaskStatus.BLOCKED) {
                    // waiting中的线程
                    myThread.close();
                    myThread.interrupt();
                    it.remove();
                }
            }
            System.out.println("The thread pool disposed");
            resetPoolSize();
            destroy = true;
        }

        System.out.println("All threads had been destroyed");
    }

    public void submit(Runnable runnable) {
        if (destroy)
            throw new IllegalStateException("The thread pool already destroy and not allow submit task.");

        synchronized (TASK_QUEUE) {
            if (TASK_QUEUE.size() > queueSize) {
                // 处理能力之外的任务，处理措施
                discardPolicy.discard();
            }
            TASK_QUEUE.addLast(runnable);
            TASK_QUEUE.notifyAll();
        }

    }

    public interface DiscardPolicy {
        void discard() throws DiscardException;
    }

    public static class DiscardException extends RuntimeException {
        public DiscardException(String message) {
            super(message);
        }
    }

    public enum TaskStatus {
        FREE, RUNNING, BLOCKED, DEAD;
    }

    private class MyThread extends Thread {
        private volatile TaskStatus taskStatus = TaskStatus.FREE;

        public MyThread(ThreadGroup group, String name) {
            super(group, name);
        }

        @Override
        public void run() {
            OUTER:
            while (this.taskStatus != TaskStatus.DEAD) {
                Runnable runnable = null;
                synchronized (TASK_QUEUE) {
                    while (TASK_QUEUE.isEmpty()) {
                        try {
                            this.taskStatus = TaskStatus.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            break OUTER;
                        }
                    }
                    runnable = TASK_QUEUE.removeFirst();
                }

                if (runnable != null) {
                    this.taskStatus = TaskStatus.RUNNING;
                    runnable.run();
                    this.taskStatus = TaskStatus.FREE;
                }
            }
        }

        public void close() {
            this.taskStatus = TaskStatus.DEAD;
        }
    }
}

