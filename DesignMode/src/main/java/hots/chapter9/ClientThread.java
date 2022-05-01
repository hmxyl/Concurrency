package hots.chapter9;

import java.util.Random;

/**
 * 发送任务客户端
 */
public class ClientThread extends Thread {
    private final RequestQueue queue;

    private Random random;

    private final String sendValue;


    public ClientThread(RequestQueue queue, String sendValue) {
        this.queue = queue;
        this.sendValue = sendValue;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Client -> request " + sendValue);
            queue.putRequest(new Request(sendValue));
            try {
                Thread.sleep(random.nextInt(50));
            } catch (InterruptedException e) {
                System.out.println("Client -> Wake up from sleeping");
                e.printStackTrace();
            }
        }
    }
}
