package IteratorAnalysis;

import java.util.PrimitiveIterator;

/**
 * Created by bonismo@hotmail.com
 * 上午2:31 on 17/11/19.
 * <p>
 * Iterator 的模拟，其实就是工程方法的设计模式
 *
 * Iterator 使用工程方法设计模式，将不同类型的集合类的方法抽象出来（hasNext, next）
 * 从而避免了向客户端暴露内部结构。
 *
 * 快速失败 fast-fail 机制
 * 出现场景：
 * 当使用 Iterator 进行迭代操作的同时，又去调用了 ArrayList/LinkedList 等集合时，比如使用 remove()、set()、方法，此时
 * elementData 数组的元素会发生变化，而迭代器此时正在通过了 hasNext 方法判断，正在调用 next 方法，最后去到的值却已经被修改过了，
 * 不是最初的期望值（原值）。从而造成数据污染（数据不一致）。 List 内部有一个 int transient modCount 变量，用来记录结构修改次数，
 * 初始值为0，Iterator 内部有一个 int transient expectedModCount 用来和 List 的 modCount 比较，当相等时，继续迭代。
 * 如果 List 自身结构发生变化，modCount 值改变，不等于 expectedModCount 。也就是迭代过程中检测到 modCount！=expectedModCount，说明
 * 结构变化，不需要继续迭代元素，抛出 ConcurrentModificationException 异常，终止迭代操作。这就是快速失败机制 fast-fail 。
 *
 * 是 Java 的一种错误检测机制。这种机制有可能触发，并不是一定。因为只有通过 hasNext，正在调用 next 时，才会触发。
 *
 */
public class IteratorAnalysis {

    public static void main(String[] args) {
        // 因为当前类自定义的 List，所有以 Package 形式引入 JDK 的 List
        // 比较 for 循环和 Iterator 的区别
        java.util.List<String> list1 =  new java.util.ArrayList<String>();
        for(int i = 0 ; i < list1.size() ;  i++){
            String s1 = list1.get(i);
            //do something....
        }

        java.util.List<String> list2 = new java.util.LinkedList<String>();
        for(int i = 0 ; i < list2.size() ;  i++){
            String s2 = list2.get(i);
            //do something....
        }

        // Iterator 迭代器
        java.util.Iterator iterator = list1.iterator();
        //判断是否有下一个元素
        while(iterator.hasNext()){
            //获取迭代器的值
            iterator.next();
            //do something......
        }

        /* 结论： 使用 Iterator 不需要了解数据内部结构，直接使用 next()方法即可获得。        */
    }
}

/*** Product 抽象产品，遍历数据 *****/
interface Iterator<T> {

    boolean hasNext();

    T next();
}

/*** Factory 抽象工厂  构造者 *****/
interface List<T> {

    // 返回一个便利器
    Iterator<T> iterator();

    // 添加元素到列表
    boolean add(T t);
}


/******** ConcreteFactory 具体工厂类 **********/
class ArrayList<T> implements List<T> {

    // 存放元素个数
    private int size;

    // 使用数组来存放元素
    private Object[] defaultList;

    // 数组初始长度
    private static final int DEFAULT_LENGTH = 10;

    // 无参构造，注入数组初始长度
    public ArrayList() {
        defaultList = new Object[DEFAULT_LENGTH];
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    // 添加元素
    @Override
    public boolean add(T t) {
        if (size <= DEFAULT_LENGTH) {
            defaultList[size++] = t;
            return true;
        }
        return false;
    }

    // 私有内部类，具体产品
    private class MyIterator implements Iterator<T> {

        private int next;

        @Override
        public boolean hasNext() {
            return next < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            return (T) defaultList[next++];
        }
    }
}

/******** ConcreteFactory 具体工厂类 **********/
class LinkedList<T> implements List<T> {

    private int size;

    private Node<T> first;

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    @Override
    public boolean add(T t) {
        if (size == 0) {
            first = new Node<T>(t, null);
            size++;
            return true;
        }
        Node<T> node = first;
        while (node.next != null) {
            node = node.next;
        }
        node.next = new Node<T>(t, null);
        size++;
        return true;
    }

    // 链表节点
    private static class Node<T> {

        T data;
        Node<T> next;

        Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }

    private class MyIterator implements Iterator<T> {

        private Node<T> next;

        public MyIterator() {
            next = first;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            T data = next.data;
            next = next.next;
            return data;
        }
    }

}