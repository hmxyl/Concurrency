package practice.chapter18.action;

/**
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
class RealResult<T> implements Result<T> {

    private final T resultValue;

    public RealResult(T resultValue) {
        this.resultValue = resultValue;
    }

    @Override
    public T getResultValue() {
        return this.resultValue;
    }
}
