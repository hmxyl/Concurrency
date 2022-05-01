package hots.chapter5;

import java.util.stream.Stream;

public class ProducerAndConsumerVersion2 {

    private int i = 0;

    private volatile boolean isProduced = false;

    private final Object LOCK = new Object();

    private void produce() {
        synchronized (LOCK) {
            if (isProduced) {
                try {
                    LOCK.wait();
                    System.out.println(Thread.currentThread().getName() + "：wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                ++i;
                LOCK.notify();
                isProduced = true;
                System.out.println(Thread.currentThread().getName() + "：produced->" + i);
            }
        }
    }

    private void consume() {
        synchronized (LOCK) {
            if (isProduced) {
                LOCK.notify();
                isProduced = false;
                System.out.println(Thread.currentThread().getName() + "：consumed->" + i);
            } else {
                try {
                    LOCK.wait();
                    System.out.println(Thread.currentThread().getName() + "：wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProducerAndConsumerVersion2 producerAndConsumerVersion1 = new ProducerAndConsumerVersion2();

        Stream.of("P1", "P2").forEach(name -> {
            new Thread(name) {
                @Override
                public void run() {
                    while (true) {
                        producerAndConsumerVersion1.produce();
                    }
                }
            }.start();
        });

        Stream.of("C1", "C2").forEach(name -> {
            new Thread(name) {
                @Override
                public void run() {
                    while (true) {
                        producerAndConsumerVersion1.consume();
                    }
                }
            }.start();
        });
    }
}
