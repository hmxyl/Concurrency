package hots.chapter3;

public class ThreadService {
    private Thread executeThead;

    private boolean finished = false;

    public void execute(Runnable task) {
        executeThead = new Thread() {
            @Override
            public void run() {
                Thread runner = new Thread(task);
                runner.setDaemon(true);
                runner.start();
                try {
                    runner.join();
                    finished = true;
                } catch (InterruptedException e) {
                    // 执行被打断
                    System.out.println("执行任务的守护线程被打断");
                    e.printStackTrace();
                }
            }
        };
        executeThead.start();
    }

    public void shutdown(long miles) {
        long currentTime = System.currentTimeMillis();
        while (!finished) {
            if (System.currentTimeMillis() - currentTime >= miles) {
                System.out.println("执行任务超时");
                executeThead.interrupt();
                break;
            }

            try {
                // 短暂休眠，减少执行次数
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("执行线程被打断");
                e.printStackTrace();
            }
        }
    }
}
