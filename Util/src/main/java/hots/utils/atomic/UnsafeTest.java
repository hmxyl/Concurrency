package hots.utils.atomic;


import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class UnsafeTest {
    private static final int THREAD_COUNT = 1000;
    private static final int MAX_NUM = 10000;

    public static void main(String[] args) {
        doAction(getAction(), new VolatileCounter(), "Volatile");
        doAction(getAction(), new AtomicCounter(), "Executors");
        doAction(getAction(), new SynCounter(), "Sync");
        doAction(getAction(), new LockCounter(), "Lock");
        doAction(getAction(), new CasCounter(), "Cas");

    }

    private static Consumer<Counter> getAction() {
        Consumer<Counter> action = param -> {
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
            for (int i = 0; i < THREAD_COUNT; i++) {
                executorService.submit(new CounterRunnable(param, MAX_NUM));
            }
            executorService.shutdown();
            try {
                // 不可省略，需要等待执行线程运行结束
                // 一般情况下awaitTermination和shutdown配合使用，shutdown之后调用awaitTermination
                // 如果注释掉shutdown方法，则awaitTermination不会监视到线程池关闭的信息 所以在这个地方代码会堵塞，
                // 如果注释掉awaitTermination方法，则后面的代码不会得到线程执行过的结果
                executorService.awaitTermination(1, TimeUnit.HOURS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        return action;
    }

    /**
     * 统计运行时长
     *
     * @param action
     * @param counter
     */
    private static void doAction(Consumer<Counter> action, Counter counter, String tag) {
        long begin = System.currentTimeMillis();
        // 任务执行
        action.accept(counter);
        long end = System.currentTimeMillis();
        System.out.println(tag + " counter result: " + counter.getCounter() + " and time passed in ms: " + (end - begin));
    }

    interface Counter {
        void increment();

        long getCounter();
    }


    static class VolatileCounter implements Counter {
        private volatile int counter;

        @Override
        public void increment() {
            ++counter;
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class AtomicCounter implements Counter {
        private AtomicInteger counter = new AtomicInteger(0);

        @Override
        public void increment() {
            counter.incrementAndGet();
        }

        @Override
        public long getCounter() {
            return counter.get();
        }
    }

    static class SynCounter implements Counter {
        private int counter = 0;

        @Override
        public synchronized void increment() {
            ++counter;
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }


    static class LockCounter implements Counter {
        private int counter = 0;
        private Lock lock = new ReentrantLock();

        @Override
        public void increment() {
            try {
                lock.lock();
                ++counter;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class CasCounter implements Counter {
        private int counter = 0;
        private static final Unsafe unsafe = getUnsafe();
        private static final long valueOffset;

        static {
            try {
                valueOffset = unsafe.objectFieldOffset(CasCounter.class.getDeclaredField("counter"));
            } catch (Exception ex) {
                throw new Error(ex);
            }
        }


        @Override
        public void increment() {
            int expect = counter;
            while (!unsafe.compareAndSwapInt(this, valueOffset, expect, expect + 1)) {
                expect = counter;
            }
        }

        @Override
        public long getCounter() {
            return counter;
        }

        private static Unsafe getUnsafe() {
            try {
                Field unsafe = Unsafe.class.getDeclaredField("theUnsafe");
                unsafe.setAccessible(true);
                return (Unsafe) unsafe.get(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class CounterRunnable implements Runnable {
        private final Counter counter;
        private final int num;

        CounterRunnable(Counter counter, int num) {
            this.counter = counter;
            this.num = num;
        }

        @Override
        public void run() {
            synchronized (counter) {
                for (int i = 0; i < num; i++) {
                    counter.increment();
                }
            }
        }
    }
}
