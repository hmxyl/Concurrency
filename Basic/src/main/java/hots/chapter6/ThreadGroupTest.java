package hots.chapter6;

public class ThreadGroupTest {
    public static void main(String[] args) {
        ThreadGroup tg1 = new ThreadGroup("TG1");
        //Thread.currentThread().getThreadGroup().parentOf(tg1);
        Thread tt1 = new Thread(tg1, "tt1") {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getThreadGroup().getName() + " --> " + Thread.currentThread().getName());
            }
        };
        tt1.start();
        System.out.println(Thread.currentThread().getThreadGroup().getName() + " --> " + Thread.currentThread().getName());
        System.out.println(tg1.getParent().getName());
        tg1.checkAccess();
        tt1.checkAccess();
    }
}
