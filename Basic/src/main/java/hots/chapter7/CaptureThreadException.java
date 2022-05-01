package hots.chapter7;

public class CaptureThreadException {
    public static void main(String[] args) {

        // 1. 设置回调接口
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println(t.getName() + " ----- occur exception：" + e.getMessage());
            e.printStackTrace();
        });

        new Thread(() -> {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 2. 抛出运行时异常
            System.out.println(1 / 0);
        }).start();
    }
}
