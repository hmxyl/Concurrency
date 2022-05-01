package hots.chapter18.action;

/**
 * 仅包可见
 */
class RealResult implements Result {

    private final Object resultValue;


    public RealResult(Object resultValue) {
        this.resultValue = resultValue;
    }

    @Override
    public Object getResultValue() {
        return resultValue;
    }
}
