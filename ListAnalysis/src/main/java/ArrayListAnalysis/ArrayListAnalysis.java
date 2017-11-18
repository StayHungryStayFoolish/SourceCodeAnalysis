package ArrayListAnalysis;

import java.util.ArrayList;

/**
 * Created by bonismo@hotmail.com
 * 下午11:56 on 17/11/18.
 */
public class ArrayListAnalysis {

    // 数组
    private Object[] elementData;

    // 容量
    private int capacity;

    // 长度
    private int size;

    // 初始容量
    private final static int DEFAULT_CAPACITY = 10;

    // 默认无参构造，内部初始化容量
    public ArrayListAnalysis() {
        this(DEFAULT_CAPACITY);
    }

    // 有参构造，设置容量
    public ArrayListAnalysis(int initialCapacity) {
        if (initialCapacity < 0) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        elementData = new Object[initialCapacity];
    }

    /* add Method , 默认添加到列表末尾 */
    public void add(Object o) {
        // 检查是否需要扩容
        ensureCapacity();
        // 将元素添加到末尾
        elementData[size++] = o;
    }

    /* add Method at index position */
    public void add(int index, Object o) {
        // 检查下标是否越界
        rangeCheck(index);
        // 数组扩容与拷贝
        ensureCapacity();
        // 复制数组 elementData 从 index 开始，复制到 elementData，从 index+1 开始，复制 index 后边的数组长度（size - index）
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        // 将当前元素复制到索引位置
        elementData[index] = o;
        size++;
    }

    // 删除该索引位置的对象，并且大小减一
    public void remove(int index) {
        rangeCheck(index);
        int numMove = size - index - 1;
        if (numMove > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMove);
            elementData[--size] = null;
        } else if (numMove == 0) { // 如果是最后一个索引位置，直接删除元素
            elementData[--size] = null;
        }
    }

    // 移除元素
    public void remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (get(i).equals(o)) {
                remove(i);
            }
        }
    }

    // 更改索引处元素值，返回旧元素
    public Object set(int index, Object o) {
        rangeCheck(index);
        Object oldValue = elementData[index];
        elementData[index] = 0;
        return oldValue;
    }

    // 数组的扩容与数组拷贝
    private void ensureCapacity() {
        System.out.println(elementData.length);
        if (size == elementData.length) {
            Object[] newArray = new Object[size * 2 + 1];
            System.arraycopy(elementData, 0, newArray, 0, elementData.length);
            elementData = newArray;
        }
    }

    // 检测数组下标越界
    private void rangeCheck(int index) {
        if (index < 0 || index > size) {
            try {
                throw new Exception("ArrayIndexOfBondsException !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 返回 ArrayList 大小
    public int size() {
        return size;
    }

    // 判断当前数组是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    // 返回当前索引处的值
    public Object get(int index) {
        rangeCheck(index);
        return elementData[index];
    }
}

class Client {
    public static void main(String[] args) {
        ArrayListAnalysis analysis = new ArrayListAnalysis();
        analysis.add("1");
        analysis.add("1");
        analysis.add("1");
        analysis.add("1");
        System.out.println("size >>>" + analysis.size());
        analysis.remove(1);
        System.out.println("size >>>" + analysis.size());
    }
}