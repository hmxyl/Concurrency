package hots.chapter18.action;


class DisplayStringRequest extends MethodRequest {
    private final String text;

    public DisplayStringRequest(Servant servant, final String text) {
        super(servant, null);
        this.text = text;
    }

    /**
     * 任务执行的主体
     */
    @Override
    public void execute() {
        this.servant.displayString(text);
    }
}
