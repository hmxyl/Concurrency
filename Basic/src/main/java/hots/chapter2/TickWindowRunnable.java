package hots.chapter2;

public class TickWindowRunnable implements Runnable {
    private final int MAX = 50;
    private int index = 1;

    @Override
    public void run() {
        while (index <= MAX) {
            System.out.println(Thread.currentThread().getName() + "的号码是：" + index++);
        }
    }
}
