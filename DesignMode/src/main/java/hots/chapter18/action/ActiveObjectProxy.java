package hots.chapter18.action;


class ActiveObjectProxy implements ActiveObject {
    private final SchedulerThread schedulerThread;

    private final Servant servant;

    public ActiveObjectProxy(SchedulerThread schedulerThread, Servant servant) {
        this.schedulerThread = schedulerThread;
        this.servant = servant;
    }


    /**
     * 将提交的request任务（makeString）推送到执行线程
     *
     * @return FutureResult：包装任务完成的执行结果
     */
    @Override
    public Result makeString(int count, char fillChar) {
        FutureResult future = new FutureResult();
        schedulerThread.invoke(new MakeStringRequest(servant, future, count, fillChar));
        return future;
    }

    /**
     * 将提交的request任务（displayString）推送到执行线程
     */
    @Override
    public void displayString(String text) {
        schedulerThread.invoke(new DisplayStringRequest(servant, text));
    }
}
