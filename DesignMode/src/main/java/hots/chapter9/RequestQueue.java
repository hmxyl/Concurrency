package hots.chapter9;

import java.util.LinkedList;

public class RequestQueue {

    // 任务队列
    private final LinkedList<Request> queue = new LinkedList<>();

    /**
     * 服务端处理任务
     *
     * @return
     */
    public Request getRequest() {
        synchronized (queue) {
            while (queue.size() <= 0) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    System.out.println("queue wait interrupted");
                    return null;
                }
            }

            return queue.removeFirst();
        }
    }

    /**
     * 客户端推送任务
     *
     * @param request
     */
    public void putRequest(Request request) {
        synchronized (queue) {
            queue.add(request);
            queue.notifyAll();
        }
    }
}
