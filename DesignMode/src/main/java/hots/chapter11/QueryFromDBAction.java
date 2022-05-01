package hots.chapter11;



public class QueryFromDBAction {
    public void execute() {
        try {
            Thread.sleep(1000L);
            String name = "Alex " + Thread.currentThread().getName();
            ActionContext.getActionContext().getContext().setName(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}