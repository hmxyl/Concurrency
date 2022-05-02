package practice.chapter8;

import example.chapter8.Future;

import java.util.function.Consumer;

/**
 * @author: DH
 * @date: 2022/5/2
 * @desc:
 */
public class FutureService {

    public <T> Future<T> submit(final FutureTask<T> futureTask, final Consumer<T> consumer) {
        AsyncFuture<T> asyncFuture = new AsyncFuture<>();
        new Thread(() -> {
            T result = futureTask.call();
            asyncFuture.done(result);
            consumer.accept(result);
        }).start();
        return asyncFuture;
    }
}
