package hots.chapter7;

/**
 * 不可变对象
 * 1. 确保类是final 的，不允许被其他类继承。（final修饰的类，被继承的提示：There is no default constructor available in 'com.hots.part2.chapter7.Person'）
 * 2. 确保所有的成员变量（字段）是final 的，这样的话，它们就只能在构造方法中初始化值，并且不会在随后被修改。
 * 3. 不要提供任何setter 方法。
 * 4. 如果要修改类的状态，必须返回一个新的对象。
 */
final public class Person {
    private final String name;
    private final String address;

    public Person(final String name, final String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}