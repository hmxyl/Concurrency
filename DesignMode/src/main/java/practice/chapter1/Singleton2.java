package practice.chapter1;

/**
 * @author: DH
 * @date: 2022/5/1
 * @desc:
 */
public class Singleton2 {

    private Singleton2() {

    }

    private enum InstanceHolder {
        INSTANCE;

        private Singleton2 instance;

        InstanceHolder() {
            instance = new Singleton2();
        }

        Singleton2 getInstance() {
            return instance;
        }

    }

    public Singleton2 getInstance() {
        return InstanceHolder.INSTANCE.getInstance();
    }
}
