package practice.chapter18.action;

/**
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
public final class ActionProxyFactory {

    private ActionProxyFactory() {

    }

    public static ActionDefinition getActionProxy() {
        RequestQueue requestQueue = new RequestQueue();
        SchedulerThread schedulerThread = new SchedulerThread(requestQueue);
        Servant servant = new Servant();
        ActionProxy actionProxy = new ActionProxy(schedulerThread, servant);
        schedulerThread.start();
        return actionProxy;
    }

}
