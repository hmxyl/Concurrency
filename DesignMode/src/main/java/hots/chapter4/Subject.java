package hots.chapter4;


import java.util.ArrayList;
import java.util.List;

public class Subject {

    private List<Observer> observers = new ArrayList<>();

    private int state;

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        notifyAllObserver();
    }

    private void notifyAllObserver() {
        observers.stream().forEach(Observer::update);
    }

    public void attach(Observer observer) {
        this.observers.add(observer);
    }
}
