package ThreadLoaclAnalysis;

/**
 * Created by bonismo@hotmail.com
 * 上午3:44 on 17/11/19.
 *
 * ThreadLocal
 *
 * 使用场景：
 * - 解决并发问题：
 *      使用 ThreadLocal 代替 synchronized 来保证线程安全。同步机制采用以 "时间换空间" 方式， ThreadLocal 采用了 "空间换时间" 方式。
 *      同步机制值提供一份变量，让不同线程访问。空间消耗不大，但消耗时间多。
 *      ThreadLocal 为每一个线程提供变量拷贝，占用了空间，但是让不同线程可以同时访问变量拷贝，而互不影响。
 * - 解决数据存储问题：
 *      ThreadLocal 为每个线程中都创建了一个变量副本，所以每个线程都可以访问自己的变量副本，不同线程之间不会干扰。如果一个参数对象数据需要
 *      在多个模块中使用，如果采用参数传递的方式，显然会增加模块之间的耦合度。所以采用 ThreadLocal 来解决。
 *
 * Spring 使用 ThreadLocal 解决线程安全问题
 * - 一般情况下，只有无状态的 Bean 才可以在多线程下共享。在 Spring 中，绝大多数 Bean 都可以声明为 singleton 作用域。
 *   就是因为 Spring 对一些 Bean (如RequestContextHolder、TransactionSynchronizationManager、LocaleContextHolder等）
 *   中非线程安全状态采用了 ThreadLocal 进行处理，使他们成为线程安全。
 *
 * - 一般的 WEB 应用划分为 展现层、服务层、持久层三个层次，在不同层编写对应的饿逻辑。下层通过接口向上层开放功能调用。在一般情况下，
 *   从接收请求到返回响应经过的所有程序都同属于一个线程。 ThreadLocal 是解决线程安全的一个很好的思路。通过为每个线程提供独立的变量副本
 *   解决了变量并发访问的冲突问题。
 *
 * ThreadLocal 和 synchronized 区别
 * 1. ThreadLocal 是一个 Java 类，通过对当前线程中的局部变量拷贝来解决不同线程变量访问的冲突问题。ThreadLocal 提供了线程安全的共享机制，
 *    每个线程都拥有其副本
 * 2. synchronized 是 Java 的一个保留字，依靠 JVM 的锁机制来实现临界区的函数或者变量访问的原子性。在同步机制中，通过对象的锁机制
 *    保证同一时间只有一个线程访问变量。此时，用用作 "锁机制" 的变量是多个线程共享的。但是线程访问不能同时进行。
 * 3. ThreadLocal 是 "空间换时间"，拷贝变量给每个线程。同时间多线程访问。
 *    synchronized 是 "时间换空间"，通过锁机制，同一时间只能有一个线程访问。
 */
public class ThreadLocalAnalysis {
}
