package hots.chapter3;

public class ThreadCloseForce {
    public static void main(String[] args) {
        ThreadService threadService = new ThreadService();
        long start = System.currentTimeMillis();
        // 启动执行线程
        threadService.execute(() -> {
            while (true) {
                // 模拟线程阻塞
            }
        });
        // 超时验证
        threadService.shutdown(10_000);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
