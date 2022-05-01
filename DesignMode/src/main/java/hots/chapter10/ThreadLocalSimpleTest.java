package hots.chapter10;

public class ThreadLocalSimpleTest {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "设置默认值";
        }
    };

    public static void main(String[] args) throws InterruptedException {
        // 主线程获取ThreadLocal
        System.out.println(Thread.currentThread().getName() + "--" + threadLocal.hashCode() + "---" + threadLocal.get());
        threadLocal.set("TEST");
        Thread.sleep(1_000L);
        System.out.println(Thread.currentThread().getName() + "--" + threadLocal.hashCode() + "---" + threadLocal.get());

        new Thread(() -> {
            threadLocal.set("TEST-1");
            System.out.println(Thread.currentThread().getName() + "--" + threadLocal.hashCode() + "---" + threadLocal.get());
        }).start();

        new Thread(() -> {
            threadLocal.set("TEST-2");
            System.out.println(Thread.currentThread().getName() + "--" + threadLocal.hashCode() + "---" + threadLocal.get());
        }).start();


        Thread.sleep(1_000L);
        // 主线程再次获取ThreadLocal
        System.out.println(Thread.currentThread().getName() + "--" + threadLocal.hashCode() + "---" + threadLocal.get());
    }
}
