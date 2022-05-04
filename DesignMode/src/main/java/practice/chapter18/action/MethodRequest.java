package practice.chapter18.action;

/**
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
public abstract class MethodRequest {
    protected final Servant servant;
    protected final FutureResult futureResult;

    public MethodRequest(Servant servant, FutureResult futureResult) {
        this.servant = servant;
        this.futureResult = futureResult;
    }

    public abstract void execute();
}
