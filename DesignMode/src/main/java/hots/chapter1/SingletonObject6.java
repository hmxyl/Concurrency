package hots.chapter1;

public class SingletonObject6 {
    private SingletonObject6() {
    }

    // 在静态内部类中持有SingletonObject6的实例，并且可被直接初始化
    private static class InstanceHolder {
        private final static SingletonObject6 instance = new SingletonObject6();
    }

    //
    public static SingletonObject6 getInstance() {
        return InstanceHolder.instance;
    }
}
