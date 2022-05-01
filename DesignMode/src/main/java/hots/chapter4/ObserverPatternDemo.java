package hots.chapter4;

public class ObserverPatternDemo {
    public static void main(String[] args) {
        final Subject subject = new Subject();
        new BinaryObserver(subject);
        new OctalObserver(subject);
        System.out.println("===============1==========");
        subject.setState(10);
        System.out.println("===============2==========");
        subject.setState(10);
        System.out.println("===============3==========");
        subject.setState(20);
    }
}
