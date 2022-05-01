package hots.chapter1;

public class SingletonObject1 {

    private static SingletonObject1 instance = new SingletonObject1();

    private SingletonObject1() {

    }

    public static SingletonObject1 getInstance() {
        return instance;
    }
}
