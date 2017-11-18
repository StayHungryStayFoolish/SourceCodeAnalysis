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
 *
 * 底层实现原理：
 *
 * put();进行以下操作
 * 1.判断哈希表 Node<K,V>[] table 是否为空或者 null，是则执行 resize()方法进行扩容。
 *
 * 2.根据插入键值 key 的 hash 值，通过(n-1)&hash 当前元素的 hash 值 & hash 表长度 -1（实际是 hash值 % hash 表长度），计算出 table[i]
 *    如果存储位置没有元素存放，新增节点存储在当前位置 table[i]
 *
 * 3.如果元素存在，判断该位置的元素hash 值和 key 值是否和当前元素一致，一致修改 value 操作，覆盖 value。
 *
 * 4.如果不一致，证明此位置 table[i] 发生了 hash 冲突，通过判断头结点是否是 treeNode，如果是证明该结构是红黑树，以红黑树方式增加节点。
 *
 * 5.如果不是，证明是单链表，将新增节点插入链表最后位置，随后判断长度是否 >= 8，如果是，转为红黑树。遍历过程中如果发现 key 已经存在，覆盖。
 *
 * 6.插入成功后，判断阈值是否大于  threshold，是则扩容。
 *
 * remove();
 * 1.先调用  hash(key); 计算出 key 的 hash 值。
 *
 * 2.根据查找的键值 key 的 hash ,通过 hash 值对链表长度的求模运算，计算出 table[i]的位置，判断存储位置是否有元素存在。
 *      - 如果有元素存放，首先比较头结点元素，如果头结点的 key 的 hash 值和要获取的 key 的 hash 值相等，并且 key 都相等时，
 *        则该位置的头结点即为要删除的节点，记录此节点到遍历 node  中。
 *      - 如果没有元素存放，即找不到要删除的节点，返回 null
 *
 * 3.如果存储位置有元素存放，但头结点元素不是要删除元素，遍历该位置，进行查找。
 *
 * 4.先判断头节点是否是 treeNode ，如果是，以红黑树遍历查找，没有返回 null
 *
 * 5.如果不是红黑树，证明是单链表。遍历单链表，逐一比较链表节点，节点的 key 的 hash 值和获取的 key 的 hash 值相等，并 key 本身相等，
 * 则此节点即为删除的节点，记录到 node 中，遍历结束没有，返回 null
 *
 * 6.如果找到要删除节点 node，则判断是否需要比较 value 是否一致，然后执行删除节点操作。具体根据结构进行处理
 *      - 如果是红黑树节点，证明当前位置链表已经是红黑树结构，通过红黑树方式删除
 *      - 如果是不是红黑树，证明是单链表。如果要删除的是头结点，则当前存执位置 table[i] 的头结点指向删除的节点的下一个节点。
 *      - 如果删除节点不是头结点，则要删除的节点的后继节点 node.next 赋值给要删除节点的前驱节点 next 域，即 p.next = node.next;
 *
 * 7.当前存储键值对数量 -1，返回删除节点
 *
 * replace(K key,V value); 替换对应值
 * 1.先调用 hash(key)方法计算出 key 的 hash值
 *
 * 2.调用 getNode(hash(key),key) 方法，获取对应 key 的 value 值
 *
 * 3.记录旧元素值，将新值赋给元素，返回元素就只，如果没有找到元素，返回 null
 *
 * get(Object key);
 * 1.先调用 hash(key)方法计算出 key 的 hash值
 *
 * 2.根据查找的键值key的hash值，通过(n - 1) & hash当前元素的hash值  & hash表长度 - 1（实际就是 hash值 % hash表长度）
 * 计算出存储位置table[i]，判断存储位置是否有元素存在 。
 *     - 如果存储位置有元素存放，则首先比较头结点元素，如果头结点的key的hash值 和 要获取的key的hash值相等，
 *       并且 头结点的key本身 和要获取的 key 相等，则返回该位置的头结点。
 *     - 如果存储位置没有元素存放，则返回null。
 *
 * 3.如果存储位置有元素存放，但是头结点元素不是要查找的元素，则需要遍历该位置进行查找。
 *
 * 4.先判断头结点是否是treeNode，如果是treeNode则证明此位置的结构是红黑树，以红色树的方式遍历查找该结点，没有则返回null。
 *
 * 5.如果不是红黑树，则证明是单链表。遍历单链表，逐一比较链表结点，链表结点的key的hash值 和 要获取的key的hash值相等，
 * 并且 链表结点的key本身 和要获取的 key 相等，则返回该结点，遍历结束仍未找到对应key的结点，则返回null。
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
 *
 * @param <K>
 * @param <V>
 */
class HashMapCode<K, V> extends AbstractMap<K, V> implements Map<K,V>,Cloneable,Serializable{

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
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;//元素的哈希值 由final修饰可知，当hash的值确定后，就不能再修改
        final K key;// 键，由final修饰可知，当key的值确定后，就不能再修改
        V value; // 值
        Node<K,V> next; // 记录下一个元素结点(单链表结构，用于解决hash冲突)


        /**
         * Node结点构造方法
         */
        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;//元素的哈希值
            this.key = key;// 键
            this.value = value; // 值
            this.next = next;// 记录下一个元素结点
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

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
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
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