package hots.chapter2;

import java.util.concurrent.atomic.AtomicInteger;

public class TicketWindow extends Thread {
    private final int MAX = 50;
    private static AtomicInteger index = new AtomicInteger(0);

    private String name;

    public TicketWindow(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        while (index.get() <= MAX) {
            System.out.println("柜台：" + name + "当前的号码是：" + index.addAndGet(1));
        }
    }
}
