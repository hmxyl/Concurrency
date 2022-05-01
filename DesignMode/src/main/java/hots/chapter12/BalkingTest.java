package hots.chapter12;


/**
 * 测试类
 */
public class BalkingTest {
    public static void main(String[] args) {
        final String fileName = "D:\\RECEIVED\\test.txt";
        final String content = "> BEGIN <";
        final BalkingData balkingData = new BalkingData(fileName, content);
        new WaiterThread(balkingData).start();
        new CustomerThread(balkingData).start();
    }
}
