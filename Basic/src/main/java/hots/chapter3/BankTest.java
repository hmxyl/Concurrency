package hots.chapter3;

public class BankTest {
    public static void main(String[] args) {
        TickWindow ticketWindow = new TickWindow();
        Thread ticketWindow1 = new Thread(ticketWindow, "一号");
        Thread ticketWindow2 = new Thread(ticketWindow, "二号");
        Thread ticketWindow3 = new Thread(ticketWindow, "三号");
        Thread ticketWindow4 = new Thread(ticketWindow, "四号");

        ticketWindow1.start();
        ticketWindow2.start();
        ticketWindow3.start();
        ticketWindow4.start();
    }
}

class TickWindow implements Runnable {
    private int index = 1;
    private final int MAX = 500;
    public final Object LOCK = new Object();

    @Override
    public void run() {
        while (true) {
            synchronized (LOCK) {
                if (index > MAX) {
                    break;
                }
                try {
                    Thread.sleep(500_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "的号码是：" + index++);
            }
        }
    }
}