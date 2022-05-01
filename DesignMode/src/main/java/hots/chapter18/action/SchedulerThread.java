package hots.chapter18.action;


import java.util.LinkedList;

public class SchedulerThread extends Thread {

    private final ActivationQueue activationQueue;

    public SchedulerThread(ActivationQueue activationQueue) {
        this.activationQueue = activationQueue;
    }

    /**
     * 提交任务到任务队列
     */
    public void invoke(MethodRequest request) {
        this.activationQueue.put(request);
    }

    /**
     * 执行任务队列中的任务
     */
    @Override
    public void run() {
        while (true) {
            // 执行提交的任务的任务主体
            // execute的内容，存在于提交的request中
            activationQueue.take().execute();
        }
    }
}


/**
 * 由SchedulerThread管理的任务队列
 */
class ActivationQueue {

    private final static int MAX_METHOD_REQUEST_QUEUE_SIZE = 100;

    private LinkedList<MethodRequest> methodQueue;

    public ActivationQueue() {
        methodQueue = new LinkedList<>();
    }

    /**
     * 提交任务到任务队列
     */
    public synchronized void put(MethodRequest request) {
        while (methodQueue.size() >= MAX_METHOD_REQUEST_QUEUE_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        methodQueue.addFirst(request);
        this.notifyAll();
    }


    /**
     * 从任务队列提取任务
     */
    public synchronized MethodRequest take() {
        while (methodQueue.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        MethodRequest methodRequest = methodQueue.removeFirst();
        this.notifyAll();
        return methodRequest;
    }
}

