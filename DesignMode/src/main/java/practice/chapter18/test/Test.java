package practice.chapter18.test;

import practice.chapter18.action.ActionDefinition;
import practice.chapter18.action.ActionProxyFactory;
import practice.chapter18.action.Result;

public class Test {
    public static void main(String[] args) {
        ActionDefinition actionProxy = ActionProxyFactory.getActionProxy();
        Result<String> result = actionProxy.makeString(10, 'a');
        System.out.println(result.getResultValue());

        actionProxy.displayString("test");
    }
}