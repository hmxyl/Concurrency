package hots.chapter12;

import java.io.IOException;

/**
 * 线程B：后台自动保存
 */
public class WaiterThread extends Thread {
    private final BalkingData balkingData;

    public WaiterThread(BalkingData balkingData) {
        super("Waiter");
        this.balkingData = balkingData;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                // 后台自动保存
                balkingData.save();
                Thread.sleep(200L);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
