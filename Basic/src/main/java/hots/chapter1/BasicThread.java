package hots.chapter1;

import org.junit.Test;

public class BasicThread {

    @Test
    public void test1() throws Exception {
        Thread test = new Thread(() -> {
            try {

                Thread ttt = new Thread(() -> {
                    try {
                        System.out.println(Thread.currentThread().getName() + "---> " + Thread.currentThread().isDaemon());
                        Thread.sleep(20_000);
                        System.out.println(Thread.currentThread().getName() + "---> " + Thread.currentThread().isDaemon());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                System.out.println(Thread.currentThread().getName() + "---> " + Thread.currentThread().isDaemon());
                Thread.sleep(10_000);
                System.out.println(Thread.currentThread().getName() + "---> " + Thread.currentThread().isDaemon());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        test.setDaemon(true);
        test.start();
        Thread.sleep(5_000);
        System.out.println(Thread.currentThread().getName() + "---> " + Thread.currentThread().isDaemon());
    }

    private static Object monitor = new Object();

    public static void main(String[] args) {
        Thread test = new Thread(() -> {
            while (true) {
//                synchronized (monitor) {
//                    try {
//                        monitor.wait(10);
//                    } catch (InterruptedException e) {
//                        Thread.interrupted();
//                        e.printStackTrace();
//                    }
//                }
            }
        });
        Thread main = Thread.currentThread();
        test.start();
        Thread test2 = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            main.interrupt();
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>");
        });
        test2.start();
        try {
            test.join();
        } catch (InterruptedException e) {
            System.out.println("......");
            System.out.println(Thread.currentThread().getName());
            e.printStackTrace();
        }
    }

}
