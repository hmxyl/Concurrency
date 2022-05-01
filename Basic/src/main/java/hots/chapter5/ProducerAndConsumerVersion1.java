package hots.chapter5;

public class ProducerAndConsumerVersion1 {

    private int i = 0;

    private volatile boolean isProduced = false;

    private final Object LOCK = new Object();

    private void produce() {
        synchronized (LOCK) {
            if (isProduced) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                ++i;
                System.out.println(Thread.currentThread().getName() + "：produced->" + i);
                LOCK.notify();
                isProduced = true;
            }
        }
    }

    private void consume() {
        synchronized (LOCK) {
            if (isProduced) {
                System.out.println(Thread.currentThread().getName() + "：consumed->" + i);
                LOCK.notify();
                isProduced = false;
            } else {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProducerAndConsumerVersion1 producerAndConsumerVersion1 = new ProducerAndConsumerVersion1();
        new Thread("P1") {
            @Override
            public void run() {
                while (true) {
                    producerAndConsumerVersion1.produce();
                }
            }
        }.start();

        new Thread("C1") {
            @Override
            public void run() {
                while (true) {
                    producerAndConsumerVersion1.consume();
                }
            }
        }.start();
    }
}
