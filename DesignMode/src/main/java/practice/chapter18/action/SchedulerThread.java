package practice.chapter18.action;

/**
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
class SchedulerThread extends Thread {

    private final RequestQueue requestQueue;

    public SchedulerThread(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void invoke(MethodRequest request) {
        this.requestQueue.put(request);
    }

    @Override
    public void run() {
        while (true) {
            this.requestQueue.get().execute();
        }
    }

}
