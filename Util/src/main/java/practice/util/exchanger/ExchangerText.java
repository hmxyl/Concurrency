package practice.util.exchanger;

import practice.common.TaskFactory;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * @author: DH
 * @date: 2022/5/5
 * @desc:
 */
public class ExchangerText {
    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            TaskFactory.spend(3, TimeUnit.SECONDS, true);
            try {
                // 交换点，成对的线程同时达到这个交换点才会交换数据
                String msg = exchanger.exchange("（message from " + Thread.currentThread().getName() + ".）");
                System.out.println(Thread.currentThread().getName() + " got " + msg + "[" + System.currentTimeMillis() + "]");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "T-A").start();

        new Thread(() -> {
            TaskFactory.spend(10, TimeUnit.SECONDS, true);
            try {
                // 交换点，成对的线程同时达到这个交换点才会交换数据
                String msg = exchanger.exchange("（message from " + Thread.currentThread().getName() + ".）");
                System.out.println(Thread.currentThread().getName() + " got " + msg + "[" + System.currentTimeMillis() + "]");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "T-B").start();
    }
}
