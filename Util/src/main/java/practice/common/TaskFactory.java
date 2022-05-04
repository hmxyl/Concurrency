package practice.common;

import java.util.concurrent.TimeUnit;

/**
 * @author: DH
 * @date: 2022/4/1
 * @dessc:
 */
public final class TaskFactory {

    public static boolean spendSeconds(long time) {
        return spend(time, TimeUnit.SECONDS, false, false);
    }


    /**
     * 执行任务，不打印线程名称
     *
     * @param time
     * @param timeUnit
     * @return
     */
    public static boolean spend(long time, TimeUnit timeUnit) {
        return spend(time, timeUnit, false, false);
    }

    /**
     * 执行任务，打印线程结束
     *
     * @param time
     * @param timeUnit
     * @return
     */
    public static boolean spend(long time, TimeUnit timeUnit, boolean logActionFinish) {
        return spend(time, timeUnit, false, logActionFinish);
    }

    /**
     * 执行任务，打印线程名称
     *
     * @param time
     * @param timeUnit
     * @return
     */
    public static boolean spend(long time, TimeUnit timeUnit, boolean logActionBegin, boolean logActionFinish) {
        try {
            if (logActionBegin) {
                System.out.printf("%s start task（%s）\n", Thread.currentThread().getName(), System.currentTimeMillis());
            }

            timeUnit.sleep(time);

            if (logActionFinish) {
                System.out.printf("%s finnish task（%s）\n", Thread.currentThread().getName(),
                        System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
