package hots.chapter1;

import java.net.Socket;
import java.sql.Connection;

public class SingletonObject5 {
    // 实例变量
    private byte[] data = new byte[1024];
    private Connection connection;
    private Socket socket;

    private volatile static SingletonObject5 instance = null;

    private SingletonObject5() {
        // 初始化connection
        //this.connection
        // 初始化socket
        //this.socket;

    }

    public static SingletonObject5 getInstance() {
        // 当instance为null时，进入同步代码块，可避免每次读取都进入
        if (instance == null) {
            // 只有一个线程能获取到SingletonObject4.classg关联的monitor
            synchronized (SingletonObject5.class) {
                // 判断如果instance为null时重建
                if (instance == null)
                    instance = new SingletonObject5();
            }
        }

        return instance;
    }
}