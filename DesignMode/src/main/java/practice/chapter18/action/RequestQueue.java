package practice.chapter18.action;

import java.util.LinkedList;

/**
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
class RequestQueue {
    private final static int MAX_METHOD_REQUEST_QUEUE_SIZE = 100;

    private final LinkedList<MethodRequest> methodQueue;

    public RequestQueue() {
        methodQueue = new LinkedList<>();
    }

    public synchronized void put(MethodRequest methodRequest) {
        while (methodQueue.size() >= MAX_METHOD_REQUEST_QUEUE_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        this.methodQueue.addLast(methodRequest);
        this.notifyAll();
    }

    public synchronized MethodRequest get() {
        while (methodQueue.size() <= 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        MethodRequest request = this.methodQueue.removeFirst();
        this.notifyAll();
        return request;
    }
}
