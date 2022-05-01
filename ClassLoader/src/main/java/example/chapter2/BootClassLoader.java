package example.chapter2;

/***************************************
 * @author:Alex Wang
 * @Date:2017/4/2 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class BootClassLoader {

    public static void main(String[] args) throws ClassNotFoundException {
        // 根（Bootstrap）类加载器加载的内容
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println("------------------");
        // 扩展（Extension）类加载器加载的内容
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println("------------------");

        Class<?> klass = Class.forName("example.chapter2.SimpleObject");
        // 系统类加载器
        // sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(klass.getClassLoader());
        // 扩展类加载器
        // sun.misc.Launcher$ExtClassLoader@6d6f6e28
        System.out.println(klass.getClassLoader().getParent());
        // 根加载器是由C++写的，输出为null。
        System.out.println(klass.getClassLoader().getParent().getParent());

        // 无法获取到自定义的String类
        // 原因：父加载器中存在，优先返回父加载器
        Class<?> clazz = Class.forName("java.lang.String");
        System.out.println(clazz);
        System.out.println(clazz.getClassLoader());

    }
}
