package HashMapAnalysis;

import java.io.Serializable;
import java.util.*;

/**
 * Created by bonismo@hotmail.com
 * 下午3:07 on 17/11/18.
 * <p>
 * JDK1.8 分析 HashMap
 * <p>
 * HashMap 是基于 Map 接口实现的一种键值对 （ <key,value> ）存储结构.
 * 允许 null 值，无序，非同步（线程不安全）。
 * HashMap 只允许一条记录的 key 为 null
 * <p>
 * 底层实现结构：
 * 数组、单链表、TreeMap（JDK1.8新增）
 * 单链表的插入为头插法。(具体看线性表)
 * <p>
 * 存储和查找数据：
 * 根据 key 的 hashcode 值计算出具体的存储位置。
 * <p>
 * 底层实现原理：
 * <p>
 * put();进行以下操作
 * 1.判断哈希表 Node<K,V>[] table 是否为空或者 null，是则执行 resize()方法进行扩容。
 * <p>
 * 2.根据插入键值 key 的 hash 值，通过(n-1)&hash 当前元素的 hash 值 & hash 表长度 -1（实际是 hash值 % hash 表长度），计算出 table[i]
 * 如果存储位置没有元素存放，新增节点存储在当前位置 table[i]
 * <p>
 * 3.如果元素存在，判断该位置的元素hash 值和 key 值是否和当前元素一致，一致修改 value 操作，覆盖 value。
 * <p>
 * 4.如果不一致，证明此位置 table[i] 发生了 hash 冲突，通过判断头结点是否是 treeNode，如果是证明该结构是红黑树，以红黑树方式增加节点。
 * <p>
 * 5.如果不是，证明是单链表，将新增节点插入链表最后位置，随后判断长度是否 >= 8，如果是，转为红黑树。遍历过程中如果发现 key 已经存在，覆盖。
 * <p>
 * 6.插入成功后，判断阈值是否大于  threshold，是则扩容。
 * <p>
 * remove();
 * 1.先调用  hash(key); 计算出 key 的 hash 值。
 * <p>
 * 2.根据查找的键值 key 的 hash ,通过 hash 值对链表长度的求模运算，计算出 table[i]的位置，判断存储位置是否有元素存在。
 *      - 如果有元素存放，首先比较头结点元素，如果头结点的 key 的 hash 值和要获取的 key 的 hash 值相等，并且 key 都相等时，
 *        则该位置的头结点即为要删除的节点，记录此节点到变量 node  中。
 *      - 如果没有元素存放，即找不到要删除的节点，返回 null
 * <p>
 * 3.如果存储位置有元素存放，但头结点元素不是要删除元素，遍历该位置，进行查找。
 * <p>
 * 4.先判断头节点是否是 treeNode ，如果是，以红黑树遍历查找，没有返回 null
 * <p>
 * 5.如果不是红黑树，证明是单链表。遍历单链表，逐一比较链表节点，节点的 key 的 hash 值和获取的 key 的 hash 值相等，并 key 本身相等，
 * 则此节点即为删除的节点，记录到 node 中，遍历结束没有，返回 null
 * <p>
 * 6.如果找到要删除节点 node，则判断是否需要比较 value 是否一致，然后执行删除节点操作。具体根据结构进行处理
 * - 如果是红黑树节点，证明当前位置链表已经是红黑树结构，通过红黑树方式删除
 * - 如果是不是红黑树，证明是单链表。如果要删除的是头结点，则当前存执位置 table[i] 的头结点指向删除的节点的下一个节点。
 * - 如果删除节点不是头结点，则要删除的节点的后继节点 node.next 赋值给要删除节点的前驱节点 next 域，即 p.next = node.next;
 * <p>
 * 7.当前存储键值对数量 -1，返回删除节点
 * <p>
 * replace(K key,V value); 替换对应值
 * 1.先调用 hash(key)方法计算出 key 的 hash值
 * <p>
 * 2.调用 getNode(hash(key),key) 方法，获取对应 key 的 value 值
 * <p>
 * 3.记录旧元素值，将新值赋给元素，返回元素就只，如果没有找到元素，返回 null
 * <p>
 * get(Object key);
 * 1.先调用 hash(key)方法计算出 key 的 hash值
 * <p>
 * 2.根据查找的键值key的hash值，通过(n - 1) & hash当前元素的hash值  & hash表长度 - 1（实际就是 hash值 % hash表长度）
 * 计算出存储位置table[i]，判断存储位置是否有元素存在 。
 * - 如果存储位置有元素存放，则首先比较头结点元素，如果头结点的key的hash值 和 要获取的key的hash值相等，
 * 并且 头结点的key本身 和要获取的 key 相等，则返回该位置的头结点。
 * - 如果存储位置没有元素存放，则返回null。
 * <p>
 * 3.如果存储位置有元素存放，但是头结点元素不是要查找的元素，则需要遍历该位置进行查找。
 * <p>
 * 4.先判断头结点是否是treeNode，如果是treeNode则证明此位置的结构是红黑树，以红色树的方式遍历查找该结点，没有则返回null。
 * <p>
 * 5.如果不是红黑树，则证明是单链表。遍历单链表，逐一比较链表结点，链表结点的key的hash值 和 要获取的key的hash值相等，
 * 并且 链表结点的key本身 和要获取的 key 相等，则返回该结点，遍历结束仍未找到对应key的结点，则返回null。
 * <p>
 * hash 冲突:
 * 当调用 put(K key,V value) 操作添加 k-v 键值对是，存储位置是通过扰动函数计算的。
 * 扰动函数  (key == null)?0:(h = key.hashCode())^(h>>>16)
 * 当计算出相同存放位置时，此现象就是 hash 冲突或者 hash 碰撞
 *
 * hash 冲突解决四种办法：
 * 1.开发定址法：
 *      当前位置容不下冲突元素，再找一个空的位置存储冲突值，当前 Index 冲突了，将冲突元素放在 Index + 1
 * 2.再散列法：
 *      换一个 hash 算法，再计算一个 hash 值，如果不冲突就存储值（例如：计算第一个算法的名字首字母的 hash 值，如果冲突了，计算第二个）
 * 3.链地址法： HashMap 采用此方法
 *      每个数组中存有一个单链表，发生 hash 冲突时，将冲突的 value 当做新节点插入链表。
 * 4.公共溢出区法：
 *      将冲突的 value 都存到另外一个顺序表中，查找时如果当前表没有对应值，则去溢出区进行顺序查找。
 *
 *
 *
 * <p>
 * HashMap 的容量必须是 2 的 n 次方
 * 1.调用 put(); 元素位置时 key 的 hash 值 % 哈希表 Node<K,V>[] table 计算的。通过委员算 h&(length-1)获取位置。
 * 只有 length 长度是 2 的 n 次方时，h&(length - 1) 等价于 h % length
 * <p>
 * 2.数组长度为2的 n 次幂是，不同 key 计算出 index 相同几率较小。数组分布比较均匀，也就是碰撞几率小。
 * <p>
 * 负载因子
 * 1.负载因子表示哈希表空间的使用程度。
 * 2.因子越大，装在程度越高，容纳更多元素，hash 碰撞几率会加大，链表加长，查询效率降低。
 * 3.因子越小，链表数据量稀疏，空间会浪费，但是查询效率高。
 * <p>
 * HashMap 和 Hashtable 区别
 * 1）容器整体结构：
 * <p>
 * HashMap的key和value都允许为null，HashMap遇到key为null的时候，调用putForNullKey方法进行处理，而对value没有处理。
 * <p>
 * Hashtable的key和value都不允许为null。Hashtable遇到null，直接返回NullPointerException。
 * <p>
 * <p>
 * 2） 容量设定与扩容机制：
 * <p>
 * HashMap默认初始化容量为 16，并且容器容量一定是2的n次方，扩容时，是以原容量 2倍 的方式 进行扩容。
 * <p>
 * Hashtable默认初始化容量为 11，扩容时，是以原容量 2倍 再加 1的方式进行扩容。即int newCapacity = (oldCapacity << 1) + 1;。
 * <p>
 * <p>
 * 3） 散列分布方式（计算存储位置）：
 * <p>
 * HashMap是先将key键的hashCode经过扰动函数扰动后得到hash值，然后再利用 hash & (length - 1)的方式代替取模，得到元素的存储位置。
 * <p>
 * Hashtable则是除留余数法进行计算存储位置的（因为其默认容量也不是2的n次方。所以也无法用位运算替代模运算），int index = (hash & 0x7FFFFFFF) % tab.length;。
 * <p>
 * 由于HashMap的容器容量一定是2的n次方，所以能使用hash & (length - 1)的方式代替取模的方式计算元素的位置提高运算效率，但Hashtable的容器容量不一定是2的n次方，所以不能使用此运算方式代替。
 * <p>
 * <p>
 * 4）线程安全（最重要）：
 * <p>
 * HashMap 不是线程安全，如果想线程安全，可以通过调用synchronizedMap(Map<K,V> m)使其线程安全。但是使用时的运行效率会下降，所以建议使用ConcurrentHashMap容器以此达到线程安全。
 * <p>
 * Hashtable则是线程安全的，每个操作方法前都有synchronized修饰使其同步，但运行效率也不高，所以还是建议使用ConcurrentHashMap容器以此达到线程安全。
 * <p>
 * 因此，Hashtable是一个遗留容器，如果我们不需要线程同步，则建议使用HashMap，如果需要线程同步，则建议使用ConcurrentHashMap。
 * 此处不再对Hashtable的源码进行逐一分析了，如果想深入了解的同学，可以参考此文章Hashtable源码剖析
 *
 * HashMap 在多线程下是不安全的，可以考虑使用 ConcurrentHashMap 代替
 * 由于 HashMap 的扩容机制，当 HashMap 调用 resize() 进行自动扩容时，可能导致死循环。
 *
 * resize 死循环
 * 超过 threshold ，需要扩容。Hash 表中所有元素都需要重新算一遍。这叫 rehash。
 * transfer
 * 1. 对索引数组元素遍历
 * 2. 对链表每个节点遍历：用 next 取得转移元素的下一个，将 e 转移到新  hash 表的头部，使用头插法插入节点
 * 3. 循环2，直到链表节点全部转移
 * 4. 循环1，知道所有数组的索引全部转移
 *
 * 原因： transfer() 函数
 * 转移过程是逆序的，如：链表 1->2->3，转移后是3->2>->1 。思索问题就是 1->2 的同时，2->1 造成的。

 void resize(int newCapacity) {
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable, initHashSeedAsNeeded(newCapacity));
        table = newTable;
        threshold = (int)Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
 }

 void transfer(Entry[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Entry<K,V> e : table) {
            while(null != e) {
                Entry<K,V> next = e.next;
                if (rehash) {
                    e.hash = null == e.key ? 0 : hash(e.key);
                }
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];  // 逆序
                newTable[i] = e;
                e = next;
            }
        }
 }

 关键 diamante： while 循环
 1. Entry<K,V> next = e.next;
    是单链表，如果转移头指针，一点要保存下一个节点，不然转移后链表就丢失了。
 2. e.next = newTable[i];
    元素 e 要插入链表头部，所以先用 e.next 指向 hash 表的第一个元素。
 3. newTable[i] = e;
    新 hash 表的头指针仍然指向 e 没转移前的第一个元素，所以需要新 hash 表头指针指向 e
 4. e = next;
    转移 e 的下一个节点

 假设两个线程通知对当前 HashMap 进行 put 操作，并进入 transfer() 环节，会出现环形链表。

 *
 * HashMap 的 Key 尽量使用不可变对象，如果选用可变对象作为 Key 是，可能会造成数据丢失，因为 hash&(length - 1)运算时，位置可能已经发生改变。
 */
public class HashMapAnalysis {
    public static void main(String[] args) {
        // 创建一个HashMap，如果没有指定初始大小，默认底层hash表数组的大小为16
        HashMap<String, String> hashMap = new HashMap<String, String>();
        // 往容器里面添加元素
        hashMap.put("小明", "好帅");
        hashMap.put("老王", "坑爹货");
        hashMap.put("老铁", "没毛病");
        hashMap.put("掘金", "好地方");
        hashMap.put("王五", "别搞事");
        // 获取key为小明的元素 好帅
        String element = hashMap.get("小明");
        // value : 好帅
        System.out.println(element);
        // 移除key为王五的元素
        String removeElement = hashMap.remove("王五");
        // value : 别搞事
        System.out.println(removeElement);
        // 修改key为小明的元素的值value 为 其实有点丑
        hashMap.replace("小明", "其实有点丑");
        // {老铁=没毛病, 小明=其实有点丑, 老王=坑爹货, 掘金=好地方}
        System.out.println(hashMap);
        // 通过put方法也可以达到修改对应元素的值的效果
        hashMap.put("小明", "其实还可以啦,开玩笑的");
        // {老铁=没毛病, 小明=其实还可以啦,开玩笑的, 老王=坑爹货, 掘金=好地方}
        System.out.println(hashMap);
        // 判断key为老王的元素是否存在(捉奸老王)
        boolean isExist = hashMap.containsKey("老王");
        // true , 老王竟然来搞事
        System.out.println(isExist);
        // 判断是否有 value = "坑爹货" 的人
        boolean isHasSomeOne = hashMap.containsValue("坑爹货");
        // true 老王是坑爹货
        System.out.println(isHasSomeOne);
        // 查看这个容器里面还有几个家伙 value : 4
        System.out.println(hashMap.size());

    }
}

/**
 * @param <K>
 * @param <V>
 */
class HashMapCode<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {

    // 哈希表，第一次使用时，初始化，重置大小
    // 分配容量 1>>4
    transient Node<K, V>[] table;

    // 实际存储的 key - value 键值对个数
    transient int size;

    // 下一次扩容的阈值 阈值 threshold = 容器容量 capacity * 负载因子（0.75） loadFactor
    int threshold;

    // 哈希表的负载因子
    final float loadFactor;

    HashMapCode(float loadFactor) {
        this.loadFactor = loadFactor;
    }

    /**
     * 定义HashMap存储元素结点的底层实现
     */
    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;//元素的哈希值 由final修饰可知，当hash的值确定后，就不能再修改
        final K key;// 键，由final修饰可知，当key的值确定后，就不能再修改
        V value; // 值
        Node<K, V> next; // 记录下一个元素结点(单链表结构，用于解决hash冲突)


        /**
         * Node结点构造方法
         */
        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;//元素的哈希值
            this.key = key;// 键
            this.value = value; // 值
            this.next = next;// 记录下一个元素结点
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        /**
         * 为Node重写hashCode方法，值为：key的hashCode 异或 value的hashCode
         * 运算作用就是将2个hashCode的二进制中，同一位置相同的值为0，不同的为1。
         */
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        /**
         * 修改某一元素的值
         */
        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        /**
         * 为Node重写equals方法
         */
        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }


    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}