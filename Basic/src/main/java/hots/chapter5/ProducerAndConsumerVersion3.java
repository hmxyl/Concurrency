package hots.chapter5;

import java.util.stream.Stream;

public class ProducerAndConsumerVersion3 {

    private int i = 0;

    private volatile boolean isProduced = false;

    private final Object LOCK = new Object();

    private void produce() {
        synchronized (LOCK) {
            while (isProduced) {
                try {
                    LOCK.wait();
                    System.out.println(Thread.currentThread().getName() + "：wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ++i;
            System.out.println(Thread.currentThread().getName() + "：produced->" + i);
            isProduced = true;
            LOCK.notifyAll();
        }
    }

    private void consume() {
        synchronized (LOCK) {
            while (!isProduced) {
                try {
                    LOCK.wait();
                    System.out.println(Thread.currentThread().getName() + "：wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(Thread.currentThread().getName() + "：consumed->" + i);
            isProduced = false;
            LOCK.notifyAll();
        }
    }

    /**
     *
     */
    public static void main(String[] args) {
        ProducerAndConsumerVersion3 producerAndConsumerVersion1 = new ProducerAndConsumerVersion3();
        Stream.of("P1", "P2").forEach(name -> {
            new Thread(name) {
                @Override
                public void run() {
                    while (true) {
                        producerAndConsumerVersion1.produce();
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        });

        Stream.of("C1", "C2", "C3", "C4").forEach(name -> {
            new Thread(name) {
                @Override
                public void run() {
                    while (true) {
                        producerAndConsumerVersion1.consume();
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        });
    }
}
