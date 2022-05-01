package hots.chapter16;

import java.util.Random;

public class CounterIncrement extends Thread {

    private volatile boolean terminated = false;

    private int counter = 0;

    private Random random = new Random(System.currentTimeMillis());

    @Override
    public void run() {
        try {
            while (!terminated) {
                System.out.println(Thread.currentThread().getName() + " " + counter++);
                Thread.sleep(random.nextInt(1000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.clean();
        }
    }

    /**
     * 执行程序结束后的任务
     */
    private void clean() {
        System.out.println("do some clean work for the second phase,current counter " + counter);
    }


    /**
     * 结束线程
     */
    public void close() {
        this.terminated = true;
    }
}
