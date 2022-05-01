package hots.chapter9;

import java.util.Random;


/**
 * 处理任务服务端
 */
public class ServerThread extends Thread {
    private RequestQueue queue;
    private Random random;

    private volatile boolean closed = false;


    public ServerThread(RequestQueue queue) {
        this.queue = queue;
        this.random = new Random(System.currentTimeMillis());
    }


    @Override
    public void run() {
        while (!closed) {
            Request request = queue.getRequest();
            if (null == request) {
                // queue从wait中被打断
                System.out.println("Received the empty request.");
                continue;
            }
            System.out.println("Server ->" + request.getValue());
            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                System.out.println("Server -> Wake up from sleeping");
                return;
            }
        }
    }

    /**
     * 关闭服务端线程
     */
    public void close() {
        this.closed = true;
        this.interrupt();
    }
}
