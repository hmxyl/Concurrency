package practice.chapter8;

import example.chapter8.Future;

import java.util.concurrent.TimeUnit;

/**
 * @author: DH
 * @date: 2022/5/2
 * @desc:
 */
public class SyncInvoker {
    public static void main(String[] args) throws InterruptedException {
        FutureTask<String> futureTask = () -> {
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "finish jobs";
        };

        FutureService futureService = new FutureService();
        Future<String> result = futureService.submit(futureTask, System.out::println);

        System.out.println("===========");
        System.out.println("do other thing.");
        TimeUnit.SECONDS.sleep(10);
        System.out.println("===========");

        System.out.println("results from job：" + result.get());

    }
}
