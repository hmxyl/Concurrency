package practice.chapter18.action;

/**
 * 行为一的请求
 *
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
class MakeStringRequest extends MethodRequest {

    private final int count;
    private final char fillChar;

    public MakeStringRequest(Servant servant, FutureResult futureResult, int count, char fillChar) {
        super(servant, futureResult);
        this.fillChar = fillChar;
        this.count = count;
    }

    @Override
    public void execute() {
        Result result = this.servant.makeString(count, fillChar);
        futureResult.setResult(result);
    }
}
