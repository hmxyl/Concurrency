package hots.chapter18.action;


/**
 * 任务定义接口
 */
public interface ActiveObject {

    Result makeString(int count, char fillChar);

    void displayString(String text);
}

