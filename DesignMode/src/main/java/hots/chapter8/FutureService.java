package hots.chapter8;

import java.util.function.Consumer;

public class FutureService {


    public interface FutureTask<T> {
        T call();
    }

    public interface Future<T> {
        T get() throws InterruptedException;
    }

    public class AsyncFuture<T> implements Future<T> {

        private volatile boolean done = false;

        private T result;

        public void done(T result) {
            synchronized (this) {
                this.result = result;
                this.done = true;
                this.notifyAll();
            }
        }

        @Override
        public T get() throws InterruptedException {
            synchronized (this) {
                while (!done) {
                    this.wait();
                }
            }
            return result;
        }
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
