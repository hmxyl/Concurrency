package practice.chapter18.action;

/**
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
class ActionProxy implements ActionDefinition {

    private final SchedulerThread schedulerThread;

    private final Servant servant;

    public ActionProxy(SchedulerThread schedulerThread, Servant servant) {
        this.schedulerThread = schedulerThread;
        this.servant = servant;
    }

    @Override
    public Result makeString(int count, char fillChar) {
        FutureResult futureResult = new FutureResult();
        MethodRequest request = new MakeStringRequest(servant, futureResult, count, fillChar);
        schedulerThread.invoke(request);
        return futureResult;
    }

    @Override
    public void displayString(String text) {
        schedulerThread.invoke(new DisplayStringRequest(servant, text));
    }
}
