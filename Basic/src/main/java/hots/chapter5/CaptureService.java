package hots.chapter5;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class CaptureService {
    private static int MAX_RUNNING = 5;
    private static final LinkedList<ActionResult> CONTROLS = new LinkedList<>();

    public static void main(String[] args) {
        List<Thread> worker = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(index -> {
            Thread thread = getWorker(index);
            thread.start();
            worker.add(thread);
        });

        worker.stream().forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Optional.of("All of capture work finished").ifPresent(System.out::println);
    }

    private static Thread getWorker(int index) {
        return new Thread("W" + index) {
            @Override
            public void run() {
                Optional.of("The worker [" + Thread.currentThread().getName() + "] ready").ifPresent(System.out::println);

                synchronized (CONTROLS) {
                    while (CONTROLS.size() >= MAX_RUNNING) {
                        try {
                            CONTROLS.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    CONTROLS.addLast(new ActionResult());
                }

                // 并行执行
                try {
                    Optional.of("The worker [" + Thread.currentThread().getName() + "] begin action").ifPresent(System.out::println);
                    Thread.sleep(10_000);
                    Optional.of("The worker [" + Thread.currentThread().getName() + "] end action").ifPresent(System.out::println);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (CONTROLS) {
                    Optional.of("The worker [" + Thread.currentThread().getName() + "] released").ifPresent(System.out::println);
                    CONTROLS.removeFirst();
                    CONTROLS.notifyAll();
                }
            }
        };
    }

    static class ActionResult {
    }
}
