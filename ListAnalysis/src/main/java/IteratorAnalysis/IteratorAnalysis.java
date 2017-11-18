package IteratorAnalysis;

import javax.sound.midi.Soundbank;

/**
 * Created by bonismo@hotmail.com
 * 上午2:31 on 17/11/19.
 * <p>
 * Iterator 的模拟，其实就是工程方法的设计模式
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