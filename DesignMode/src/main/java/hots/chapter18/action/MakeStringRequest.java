package hots.chapter18.action;

public class MakeStringRequest extends MethodRequest {
    private final int count;
    private final char fillChar;

    public MakeStringRequest(Servant servant, FutureResult futureResult, int count, char fillChar) {
        super(servant, futureResult);
        this.fillChar = fillChar;
        this.count = count;
    }

    /**
     * 任务执行的主体
     */
    @Override
    public void execute() {
        futureResult.setResult(servant.makeString(count, fillChar));
    }
}
