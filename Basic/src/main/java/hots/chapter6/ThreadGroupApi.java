package hots.chapter6;

public class ThreadGroupApi {
    public static void main(String[] args) throws InterruptedException {
        ThreadGroup tg1 = new ThreadGroup("group1");
        new Thread(tg1, () -> {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "group1-t1").start();

        ThreadGroup tg2 = new ThreadGroup("group2");
        new Thread(tg2, () -> {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "group2-t1").start();
        tg2.setDaemon(true);

        Thread.sleep(1_000);

        System.out.println(tg1.getName() + " -- " + tg1.isDestroyed());
        System.out.println(tg2.getName() + " -- " + tg2.isDestroyed());

        tg1.destroy();

        System.out.println(tg1.getName() + " -- " + tg1.isDestroyed());
        System.out.println(tg2.getName() + " -- " + tg2.isDestroyed());
    }
}
