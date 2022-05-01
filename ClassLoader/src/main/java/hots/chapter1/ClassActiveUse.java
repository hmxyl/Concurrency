package hots.chapter1;

import java.util.Random;

public class ClassActiveUse {
    static {
        System.out.println("ClassActiveUse");
    }

    public static void main(String[] args) throws ClassNotFoundException {
        // final修饰的常量会在编译期间放到常量池中，不会初始化类
        System.out.println(Obj.salary);
        System.out.println("--------------------------------------");
        // final修饰的复杂类型，在编译期间无法计算得出，会初始化类
        System.out.println(Obj.x);
    }
}

class Obj {

    public static final long salary = 100000L;

    public static final int x = new Random().nextInt(100);

    static {
        System.out.println("Obj 被初始化.");
    }
}