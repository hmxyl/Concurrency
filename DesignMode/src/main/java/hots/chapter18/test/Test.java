package hots.chapter18.test;


import hots.chapter18.action.ActiveObject;
import hots.chapter18.action.ActiveObjectFactory;

public class Test {
    public static void main(String[] args) {
        ActiveObject activeObject = ActiveObjectFactory.createActiveObject();
        new MakerClientThread(activeObject, "Alice").start();
        new MakerClientThread(activeObject, "Bobby").start();

        new DisplayClientThread("Chris", activeObject).start();
    }
}
