package ArrayListAnalysis;

/**
 * Created by bonismo@hotmail.com
 * 下午11:56 on 17/11/18.
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
        elementData = new Object[DEFAULT_CAPACITY];
        size = DEFAULT_CAPACITY;
    }

    // 有参构造，设置容量
    public ArrayListAnalysis(int initialCapacity) {
        if (initialCapactiy < 0) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        elementData = new Object[initialCapacity];
    }

    // 返回 ArrayList 大小
    public int size() {
        return size;
    }

    // 判断当前数组是否为空
    public boolean isEmpty() {
        return size == 0;
    }
}
