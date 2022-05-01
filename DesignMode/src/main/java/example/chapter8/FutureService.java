package example.chapter8;

import java.util.function.Consumer;

/***************************************
 * @author:Alex Wang
 * @Date:2017/3/22 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class FutureService {

    public <T> Future<T> submit(final FutureTask<T> task) {
        AsyncFuture<T> asynFuture = new AsyncFuture<>();
        new Thread(() -> {
            T result = task.call();
            asynFuture.done(result);
        }).start();
        return asynFuture;
    }

    public <T> Future<T> submit(final FutureTask<T> task, final Consumer<T> consumer) {
        AsyncFuture<T> asyncFuture = new AsyncFuture<>();
        new Thread(() -> {
            T result = task.call();
            asyncFuture.done(result);
            consumer.accept(result);
        }).start();
        return asyncFuture;
    }
}