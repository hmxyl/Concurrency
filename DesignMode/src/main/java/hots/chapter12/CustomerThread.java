package hots.chapter12;

import java.io.IOException;
import java.util.Random;


/**
 * 线程A: 主动提交保存任务
 */
public class CustomerThread extends Thread {
    private final BalkingData balkingData;

    private final Random random = new Random(System.currentTimeMillis());

    public CustomerThread(BalkingData balkingData) {
        super("Customer");
        this.balkingData = balkingData;
    }

    @Override
    public void run() {
        try {
            // 主动保存数据
            balkingData.save();
            for (int i = 0; i < 20; i++) {
                // 变更数据
                balkingData.change("No." + i);
                Thread.sleep(random.nextInt(1_000));
                // 主动保存数据
                balkingData.save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
