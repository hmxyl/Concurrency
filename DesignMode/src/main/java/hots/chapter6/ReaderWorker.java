package hots.chapter6;


/**
 * 线程-读资源
 */
public class ReaderWorker extends Thread {
    private final SharedData data;

    public ReaderWorker(SharedData data) {
        this.data = data;
    }

    @Override
    public void run() {
        try {
            while (true) {
                char[] buf = data.read();
                System.out.println(Thread.currentThread() + " read " + String.valueOf(buf));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
