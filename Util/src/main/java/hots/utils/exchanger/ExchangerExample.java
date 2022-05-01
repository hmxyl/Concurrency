package hots.utils.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class ExchangerExample {
    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " start.");
            try {
                TimeUnit.SECONDS.sleep(3);
                // 交换点，成对的线程同时达到这个交换点才会交换数据
                String msg = exchanger.exchange("（message from " + Thread.currentThread().getName() + ".）");
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + " got " + msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " end.");
        }, "T-A").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " start.");
            try {
                // 交换点，成对的线程同时达到这个交换点才会交换数据
                String msg = exchanger.exchange("（message from " + Thread.currentThread().getName() + ".）");
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + " got " + msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " end.");
        }, "T-B").start();
    }
}
