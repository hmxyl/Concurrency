package hots.chapter9;

/**
 * 测试类
 */
public class SuspensionClient {
    public static void main(String[] args) throws InterruptedException {

        final RequestQueue queue = new RequestQueue();

        ClientThread clientThread = new ClientThread(queue, "test");
        clientThread.start();

        ServerThread serverThread = new ServerThread(queue);
        serverThread.start();

        Thread.sleep(10000);

        System.out.println("Server ready to close.");
        serverThread.close();
    }
}
