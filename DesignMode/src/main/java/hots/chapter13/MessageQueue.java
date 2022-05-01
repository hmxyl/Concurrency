package hots.chapter13;

import java.util.LinkedList;


/**
 * 消费队列
 */
public class MessageQueue {

    private final static int DEFAULT_MAX_LIMIT = 100;

    private final LinkedList<Message> queue;

    private final int limit;

    public MessageQueue() {
        this(DEFAULT_MAX_LIMIT);
    }

    public MessageQueue(final int limit) {
        this.limit = limit;
        this.queue = new LinkedList<>();
    }


    public void put(Message message) throws InterruptedException {
        synchronized (queue) {
            while (queue.size() > limit) {
                queue.wait();
            }

            queue.addLast(message);
            queue.notifyAll();
        }
    }

    public Message take() throws InterruptedException {
        synchronized (queue) {
            while (queue.size() == 0) {
                queue.wait();
            }

            Message message = queue.removeFirst();
            queue.notifyAll();
            return message;
        }
    }
}
