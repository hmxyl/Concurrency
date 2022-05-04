package practice.chapter18.action;

/**
 * 对外可见
 *
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
class FutureResult<T> implements Result<T> {

    private Result<T> result;

    private boolean ready;

    public synchronized void setResult(Result<T> result) {
        this.result = result;
        this.ready = true;
        this.notifyAll();
    }

    @Override
    public synchronized T getResultValue() {
        while (!ready) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return this.result.getResultValue();
    }
}
