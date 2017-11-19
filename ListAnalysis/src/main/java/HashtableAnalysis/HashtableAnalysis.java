package HashtableAnalysis;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by bonismo@hotmail.com
 * 下午2:06 on 17/11/19.
 * <p>
 * Hashtable 与 HashMap 区别
 * 1.容器结构
 * - Hashtable 不允许 key/value 为 null，遇到 null 时，会返回 NullPointerException
 * - HashMap 允许 key/value 为 null。且只有一个 key 为 null
 * 2.线程安全
 * - Hashtable 是线程安全的，因为 Hashtable 的许多操作函数都使用 synchronized 来修饰。
 * - HashMap 不是线程安全的，具体体现在扩容时 resize()方法，会调用 transfer()方法，在扩容时，底层链表结构是逆序拷贝到新链表的。
 * 所有当同时 put 操作时，并进入 transfer() 函数时，会出现环形链表，并 next 下一个节点指向元素为 null。
 * 3.实现接口
 * - Hashtable 继承 Dictionary
 * - HashMap 继承 AbstractMap
 * 4.默认初始容量
 * - Hashtable 初始容量是11，采用质数，扩容方式采用 old*2+1 ，根据 hash 算法，使得内部 hash 表散列更加均匀，减少 hash 冲突（碰撞几率）
 * - HashMap 初始容量是16，采用 2 的 n 次方。
 * 5.Hash 散列存储位置计算方法
 * - Hashtable 采用的除留余数法计算，即 int index = (hash & 0x7FFFFFFF) % tab.length;
 * 奇数保证了散列的更均匀，减少 hash 碰撞的几率。
 * - HashMap 采用的是 hash & (length - 1) 运算，来代替 求模运算（key 的 hash 值，当前 hash 表的长途） hash值 % hash 表长度，
 * 运算效率更高
 * <p>
 * 相同点：
 * 都是0.75的负载因子。
 * 因子越高，装载空间越大，碰撞几率越高。
 * 因子越小，装载空间越小，碰撞几率越低。
 */
public class HashtableAnalysis {
    public static void main(String[] args) {
        Hashtable<String, String> table = new Hashtable<>();
        Hashtable<String, String> table1 = new Hashtable<>(16);
        Hashtable<String, String> table2 = new Hashtable<>(16, 0.75f);
        HashMap<String, String> map = new HashMap<>();
        Hashtable<String, String> table3 = new Hashtable<>(map);
        table.put("T1", "1");
        table.put("T2", "2");
//        table.put(null, "3"); // java.lang.NullPointerException Hashtable 遇到 null，抛出 NPE
        System.out.println();
        System.out.println(table.toString());

    }

    /* ---------- 源码分析 ----------   */
    /*  域和构造函数
    private transient Entry<?,?>[] table;//数组
    private transient int count;//键值对的数量
    private int threshold;//阀值
    private float loadFactor;//加载因子
    private transient int modCount = 0;//修改次数
    public Hashtable(int initialCapacity, float loadFactor) {//下面的三个构造函数都是调用这个函数，来进行相关的初始化
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal Load: "+loadFactor);

        if (initialCapacity==0)
            initialCapacity = 1;
        this.loadFactor = loadFactor;
        table = new Entry[initialCapacity];//这里是与HashMap的区别之一，HashMap中table  对 hash 表进行了初始化
        threshold = (int)Math.min(initialCapacity * loadFactor, MAX_ARRAY_SIZE + 1);
        initHashSeedAsNeeded(initialCapacity);
    }

    public Hashtable(int initialCapacity) {//指定初始数组长度
        this(initialCapacity, 0.75f);
    }

    public Hashtable() {//从这里可以看出容量的默认值为11，加载因子为0.75f.
        this(11, 0.75f);
    }

    public Hashtable(Map<? extends K, ? extends V> t) {
        this(Math.max(2*t.size(), 11), 0.75f);
        putAll(t);
    }
    */


    /* ------- put 方法 -------------- */
    /*
    public synchronized V put(K key, V value) {//这里方法修饰符为synchronized,所以是线程安全的。
        if (value == null) {
            throw new NullPointerException();//value如果为Null,抛出异常
        }
        Entry tab[] = table;
        int hash = hash(key);//hash里面的代码是hashSeed^key.hashcode（）,null.hashCode（）会抛出异常，所以这就解释了Hashtable的key和value不能为null的原因。
        int index = (hash & 0x7FFFFFFF) % tab.length;//获取数组元素下标,先对hash值取正，然后取余。
        for (Entry<K,V> e = tab[index] ; e != null ; e = e.next) {
            if ((e.hash == hash) && e.key.equals(key)) {
                V old = e.value;
                e.value = value;
                return old;
            }
        }

        modCount++;//修改次数。
        if (count >= threshold) {//键值对的总数大于其阀值
            rehash();//在rehash里进行扩容处理

            tab = table;
            hash = hash(key);
            index = (hash & 0x7FFFFFFF) % tab.length;
        }
        Entry<K,V> e = tab[index];
        tab[index] = new Entry<>(hash, key, value, e);
        count++;
        return null;
    }
private int hash(Object k) {
        // hashSeed will be zero if alternative hashing is disabled.
        return hashSeed ^ k.hashCode();//在1.8的版本中，hash就直接为k.hashCode了。
    }
protected void rehash() {
        int oldCapacity = table.length;
        Entry<K,V>[] oldMap = table;
        int newCapacity = (oldCapacity << 1) + 1;//扩容，如果默认值是11，则扩容之后，数组的长度为23
        if (newCapacity - MAX_ARRAY_SIZE > 0) {//这里的最大值和HashMap里的最大值不同，这里Max_ARRAY_SIZE的是因为有些虚拟机实现会限制数组的最大长度。
            if (oldCapacity == MAX_ARRAY_SIZE)
                // Keep running with MAX_ARRAY_SIZE buckets
                return;
            newCapacity = MAX_ARRAY_SIZE;
        }
        Entry<K,V>[] newMap = new Entry[newCapacity];

        modCount++;
        threshold = (int)Math.min(newCapacity * loadFactor, MAX_ARRAY_SIZE + 1);
        boolean rehash = initHashSeedAsNeeded(newCapacity);

        table = newMap;
        for (int i = oldCapacity ; i-- > 0 ;) {//迁移键值对
            for (Entry<K,V> old = oldMap[i] ; old != null ; ) {
                Entry<K,V> e = old;
                old = old.next;

                if (rehash) {
                    e.hash = hash(e.key);
                }
                int index = (e.hash & 0x7FFFFFFF) % newCapacity;  // e.hash & 0x7FFFFFFF 是为了保证正数
                e.next = newMap[index];
                newMap[index] = e;
            }
        }
    }

    // Hashtable 的 hash 值没有使用扰动处理。解决 hash 冲突使用的 Hashtable 的索引求值公式：
    // ((hashSeed ^ k.hashCode()) & 0x7FFFFFFF) % newCapacity
     */
}
