package hots.chapter18.action;

abstract class MethodRequest {
    protected final Servant servant;

    protected final FutureResult futureResult;

    public MethodRequest(Servant servant, FutureResult futureResult) {
        this.servant = servant;
        this.futureResult = futureResult;
    }

    /**
     * 提交任务的抽象方法。具体执行内容由子类定义。
     * 执行者交给servant，执行结果包装到futureResult中
     */
    public abstract void execute();
}
