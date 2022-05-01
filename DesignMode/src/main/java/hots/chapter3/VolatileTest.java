package hots.chapter3;

public class VolatileTest {

    private volatile static int INIT_VALUE = 0;
    private final static int MAX_VALUE = 5;

    public static void main(String[] args) {

        new Thread(() -> {
            int localValue = INIT_VALUE;
            while (localValue < MAX_VALUE) {
                if (localValue != INIT_VALUE) {
                    System.out.printf("The value updated from [%d] to [%d]\n", localValue, INIT_VALUE);
                    localValue = INIT_VALUE;
                }
            }
        }, "READER").start();

        new Thread(() -> {
            int localValue = INIT_VALUE;
            while (localValue < MAX_VALUE) {
                INIT_VALUE = ++localValue;
                System.out.printf("Update the value to [%d]\n", INIT_VALUE);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "WRITER").start();
    }
}
