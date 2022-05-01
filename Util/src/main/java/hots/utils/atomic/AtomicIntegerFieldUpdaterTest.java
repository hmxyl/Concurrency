package hots.utils.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldUpdaterTest {
    @Test
    public void test() {
        AtomicIntegerFieldUpdater<TestBean> updater = AtomicIntegerFieldUpdater.newUpdater(TestBean.class, "param");
        TestBean test = new TestBean();
        updater.incrementAndGet(test);
        System.out.println(updater.get(test));
    }

    class TestBean {
       private  volatile int param;
    }
}



