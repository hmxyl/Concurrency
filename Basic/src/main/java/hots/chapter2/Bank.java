package hots.chapter2;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class Bank {
    @Test
    public void bankTest1() {
        TicketWindow ticketWindow1 = new TicketWindow("一号");
        ticketWindow1.start();
        TicketWindow ticketWindow2 = new TicketWindow("二号");
        ticketWindow2.start();
        TicketWindow ticketWindow3 = new TicketWindow("三号");
        ticketWindow3.start();
        TicketWindow ticketWindow4 = new TicketWindow("四号");
        ticketWindow4.start();
    }

    @Test
    public void bankTest2() {
        TickWindowRunnable ticketWindowRunnable = new TickWindowRunnable();
        Thread ticketWindow1 = new Thread(ticketWindowRunnable, "一号");
        Thread ticketWindow2 = new Thread(ticketWindowRunnable, "二号");
        Thread ticketWindow3 = new Thread(ticketWindowRunnable, "三号");
        Thread ticketWindow4 = new Thread(ticketWindowRunnable, "四号");

        ticketWindow1.start();
        ticketWindow2.start();
        ticketWindow3.start();
        ticketWindow4.start();
    }

    @Test
    public void bankTest3() {
        final int MAX = 50;
        final AtomicInteger index = new AtomicInteger(0);
        final Runnable runnable = () -> {
            while (index.get() <= MAX) {
                System.out.println(Thread.currentThread().getName() + "的号码是：" + index.addAndGet(1));
            }
        };

        Thread ticketWindow1 = new Thread(runnable, "一号");
        Thread ticketWindow2 = new Thread(runnable, "二号");
        Thread ticketWindow3 = new Thread(runnable, "三号");
        Thread ticketWindow4 = new Thread(runnable, "四号");

        ticketWindow1.start();
        ticketWindow2.start();
        ticketWindow3.start();
        ticketWindow4.start();
    }

    @Test
    public void bankTest4() {
        TaxCalculator calculator = new TaxCalculator(10000d, 15000d, (s, b) -> (s * 0.1 + b * 0.15));
        System.out.println(calculator.calculate());

    }
}
