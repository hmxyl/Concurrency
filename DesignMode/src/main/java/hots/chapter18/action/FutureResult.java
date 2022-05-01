package hots.chapter18.action;

/**
 * 返回的任务结果，仅包可见
 */
class FutureResult implements Result {

    private Result result;
    private boolean ready = false;

    public synchronized void setResult(Result result) {
        this.result = result;
        this.ready = true;
        // 通知等待提取任务结果的线程，任务结束
        this.notifyAll();
    }

    @Override
    public synchronized Object getResultValue() {
        while (!ready) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.result.getResultValue();
    }
}