package hots.chapter6;

import java.util.Random;

/**
 * 线程-写资源
 */
public class WriterWorker extends Thread {
    private static final Random random = new Random(System.currentTimeMillis());

    private final SharedData sharedData;

    private int index = 0;
    private final String filler;

    public WriterWorker(SharedData sharedData, String filler) {
        this.sharedData = sharedData;
        this.filler = filler;
    }

    @Override
    public void run() {
        try {
            while (true) {
                char c = nextChar();
                sharedData.write(c);
                System.out.println(Thread.currentThread().getName() + " write " + c);
                Thread.sleep(random.nextInt(1000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private char nextChar() {
        char c = filler.charAt(index);
        index++;
        if (index >= filler.length())
            index = 0;
        return c;
    }
}
