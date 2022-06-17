package practice.common;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author: DH
 * @date: 2022/4/1
 * @dessc:
 */
@Slf4j
public class TaskFactory {

    public static void spend(long time, TimeUnit timeUnit) {
        spend(time, timeUnit, false);
    }

    /**
     * 执行任务，打印线程名称
     *
     * @param time
     * @param timeUnit
     * @return
     */
    public static void spend(long time, TimeUnit timeUnit, boolean logActionFinish) {
        try {
            timeUnit.sleep(time);
            if (logActionFinish) {
                log.info("{} finnish task [{}}]", Thread.currentThread().getName(), System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
