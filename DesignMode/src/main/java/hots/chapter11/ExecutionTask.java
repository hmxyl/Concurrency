package hots.chapter11;

public class ExecutionTask implements Runnable {

    private QueryFromDBAction queryFromDBAction = new QueryFromDBAction();
    private QueryFromHttpAction queryFromHttpAction = new QueryFromHttpAction();

    @Override
    public void run() {
        // 分步骤获取
        queryFromDBAction.execute();
        System.out.println(Thread.currentThread().getName() + " The name query successful");
        queryFromHttpAction.execute();
        System.out.println(Thread.currentThread().getName() + " The card id query successful");
        // 最后，统一获取
        Context context = ActionContext.getActionContext().getContext();
        System.out.println("The Name is " + context.getName() + " and CardId " + context.getCardId());
    }
}
