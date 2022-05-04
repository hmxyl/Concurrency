package practice.chapter18.action;

/**
 * 行为二
 *
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
class DisplayStringRequest extends MethodRequest {
    private final String text;

    public DisplayStringRequest(Servant servant, String text) {
        super(servant, null);
        this.text = text;
    }

    @Override
    public void execute() {
        this.servant.displayString(text);
    }
}
