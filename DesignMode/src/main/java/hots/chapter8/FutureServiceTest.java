package hots.chapter8;

public class FutureServiceTest {
    public static void main(String[] args) throws InterruptedException {
        FutureService futureService = new FutureService();
        FutureService.Future<String> future = futureService.submit(() -> {
            try {
                Thread.sleep(10000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "FINISH";
        }, System.out::println);

        System.out.println(" MAIN ");

        System.out.println(" RESULT " + future.get());
    }
}