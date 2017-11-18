package VectorAnalysis;

import java.util.Arrays;

/**
 * Created by bonismo@hotmail.com
 * 下午11:13 on 17/11/18.
 * <p>
 * 用数组来模拟向量
 * <p>
 * 思路:
 * 1、设置初始容量 10 、数组、容量、向量大小
 * 2、创建有参、无参构造器。 无参默认容量、有参设置容量
 * 3、添加元素，利用 System.arraycopy();
 * System.arrcopy(Object src,int srcPos, object dest, int destPos,int length);
 * src 源数组， srcPos 起始位置， dest 目标数组，destPos 目标位置，length 复制的长度
 * 复制数组。从指定源数组，复制一个
 * 4、删除元素，remove ，System.arraycopy(); 复制数组
 */
public class VectorAnalysis {


    public static void main(String[] args) {
        VectorAnalysis vectorAnalysis = new VectorAnalysis();
        vectorAnalysis.add("hello");
        vectorAnalysis.add("world");
        vectorAnalysis.add("hi");
        vectorAnalysis.add("earth");

//        System.out.println(vectorAnalysis.remove(1)); // 移除后，返回移除元素
        System.out.println(vectorAnalysis.size());
        for (int i = 0; i < vectorAnalysis.size(); i++) {
            String s = vectorAnalysis.get(i);
            System.out.println("数组元素 >>>" + s);
        }

        String[] strings = {"hello", "world", "hi", "earth"};
        String[] strings1 = new String[5];
        System.arraycopy(strings, 0, strings1, 0, 5-1);
        System.out.println(Arrays.toString(strings1));
    }

    private static final int DEFAULT_CAPACITY = 10;
    private String[] strings;
    private int capacity;
    private int size;

    public VectorAnalysis() {
        strings = new String[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    public VectorAnalysis(int initialCapacity) {
        strings = new String[initialCapacity];
        capacity = initialCapacity;
    }

    public void add(String string) {
        // 当 大小 = 容量时，双倍扩容
        if (size == capacity) {
            String[] newStrings = new String[capacity * 2];
            capacity *= 2;

            // System.arrcopy(Object src,int srcPos, object dest, int destPos,int length);
            // src 源数组， srcPos 起始位置， dest 目标数组，destPos 目标位置，length 复制的长度
            // 复制数组。从指定源数组，复制一个
            System.arraycopy(strings, 0, newStrings, 0, strings.length);
            strings = newStrings;
        }
        strings[size] = string;
        size++;
    }


    public String get(int index) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return strings[index];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public String remove(int index) {
        if (index >= size) {
            System.out.println("error");
            System.exit(0);
        }

        String s = strings[index];

        // 删除数组，index + 1 开始复制，
        System.arraycopy(strings, index + 1, strings, index, size - index - 1);
        strings[size - 1] = null;
        size--;
        return s;
    }

    public String set(int index, String element) {
        if (index >= size) {
            System.out.println("error");
            System.exit(0);
        }

        String s = strings[index];
        strings[index] = element;
        return s;
    }
}

