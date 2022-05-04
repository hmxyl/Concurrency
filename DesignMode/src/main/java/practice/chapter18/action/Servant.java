package practice.chapter18.action;


import java.util.concurrent.TimeUnit;

/**
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
class Servant implements ActionDefinition {

    @Override
    public Result makeString(int count, char fillChar) {
        char[] buf = new char[count];
        for (int i = 0; i < count; i++) {
            buf[i] = fillChar;
            spend(10);
        }
        return new RealResult(new String(buf));
    }

    @Override
    public void displayString(String text) {
        System.out.println("Display:" + text);
        spend(10);
    }

    private void spend(long millis){
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
