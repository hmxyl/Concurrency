package practice.chapter18.action;

/**
 * 请求处理行为定义接口
 * @author: DH
 * @date: 2022/5/4
 * @desc:
 */
public interface ActionDefinition {
    Result makeString(int count, char fillChar);

    void displayString(String text);
}
