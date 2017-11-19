package HashtableAnalysis;

/**
 * Created by bonismo@hotmail.com
 * 下午2:06 on 17/11/19.
 *
 * Hashtable 与 HashMap 区别
 * 1.键值对
 *   - Hashtable 不允许 key/value 为 null，遇到 null 时，会返回 NullPointerException
 *   - HashMap 允许 key/value 为 null。且只有一个 key 为 null
 * 2.线程安全
 *   - Hashtable 是线程安全的，因为 Hashtable 的许多操作函数都使用 synchronized 来修饰。
 *   - HashMap 不是线程安全的，具体体现在扩容时 resize()方法，会调用 transfer()方法，在扩容时，底层链表结构是逆序拷贝到新链表的。
 *     所有当同时 put 操作时，并进入 transfer() 函数时，会出现环形链表，并 next 下一个节点指向元素为 null。
 * 3.实现接口
 *   - Hashtable 继承 Dictionary
 *   - HashMap 继承 AbstractMap
 * 4.默认初始容量
 *   - Hashtable 初始容量是11，采用质数，扩容方式采用 old*2+1 ，根据 hash 算法，使得内部 hash 表散列更加均匀，减少 hash 冲突（碰撞几率）
 *   - HashMap 初始容量是16，采用 2 的 n 次方。
 * 5.存储位置计算方法
 *   - Hashtable 采用的除留余数法计算，即 int index = (hash & 0x7FFFFFFF) % tab.length;
 *   - HashMap 采用的是 hash & (length - 1) 运算，来代替 求模运算（key 的 hash 值，当前 hash 表的长途） hash值 % hash 表长度，
 *     运算效率更高
 *
 * 相同点：
 * 都是0.75的负载因子。
 * 因子越高，装载空间越大，碰撞几率越高。
 * 因子越小，装载空间越小，碰撞几率越低。
 */
public class HashtableAnalysis {
}
