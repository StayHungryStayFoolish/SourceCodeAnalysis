package ArrayListAnalysis;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by bonismo@hotmail.com
 * 下午11:56 on 17/11/18.
 * <p>
 * 数组模拟 ArrayList
 * 类似 Vector，只是更改了扩容比例，没有在无参构造中加入 capacity 初始值
 * <p>
 * ArrayList
 * 1.线性数据结构，底层由数组实现，等价于动态数组。具有下标索引，默认添加元素到末尾。
 * 2.扩容，每次扩容为原容量*3/2+1。
 * 3.复制，原数组重新拷贝一份到新的数组中。操作代价比较高。如果预知保存元素多少时，尽量指定其容量，避免数组扩容发生。
 *
 * 并发处理 ---
 * 无论直接迭代，还是 JDK1.5 以后的 for-each 循环，对容器进行迭代的标准方式都是 Iterator。
 * 然后其他线程并发修改容器，即使使用迭代器也无法避免在迭代期间对容器加锁，是快速-失败 fast-failed。
 * 容器在迭代过程中被修改，会抛出 ConcurrentModificationException 异常。
 *
 * 使用 CopyOnWriterArrayList 可以解决并发问题。适合在读多-写少的场景中使用，比如缓存。
 * 该类不存在扩容，每次写操作都是加锁，拷贝一个副本，在副本基础上修改。因此过多的写入操作不建议使用该数据结构。开销很大。
 */
public class ArrayListAnalysis {

    // 数组
    private Object[] elementData;

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

    // 数组的扩容与数组拷贝,扩容时 原始容量 * 3 / 2 + 1
    private void ensureCapacity() {
        System.out.println(elementData.length);
        if (size == elementData.length) {
            Object[] newArray = new Object[(size * 3) / 2 + 1];
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