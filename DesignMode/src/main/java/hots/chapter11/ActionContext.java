package hots.chapter11;

public class ActionContext {
    private ActionContext() {

    }

    private static final ThreadLocal<Context> threadLocal = new ThreadLocal<Context>() {
        @Override
        protected Context initialValue() {
            return new Context();
        }
    };

    private static class ContextHolder {
        private final static ActionContext actionContext = new ActionContext();
    }

    public static ActionContext getActionContext() {
        return ActionContext.ContextHolder.actionContext;
    }

    public Context getContext() {
        return threadLocal.get();
    }
}
