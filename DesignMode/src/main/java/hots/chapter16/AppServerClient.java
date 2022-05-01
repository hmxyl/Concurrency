package hots.chapter16;

public class AppServerClient {
    public static void main(String[] args) throws InterruptedException {
        AppServer appServer = new AppServer(12312);
        appServer.start();

        Thread.sleep(20_000L);
        appServer.shutDown();
    }
}
