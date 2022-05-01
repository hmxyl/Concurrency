package practice.chapter1;

/**
 * @author: DH
 * @date: 2022/5/1
 * @desc:
 */
public class Singleton1 {

    private Singleton1() {

    }

    private static class InstanceHolder {
        private final static Singleton1 instance = new Singleton1();
    }

    public static Singleton1 getInstance() {
        return InstanceHolder.instance;
    }

}
