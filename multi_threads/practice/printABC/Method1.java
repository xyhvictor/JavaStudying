/**
 * @author yuhui.xi
 */
public class Method1 {
    public static void main(String[] args) {
        ThreadAbc1 a = new ThreadAbc1();
        ThreadAbc1 b = new ThreadAbc1();
        ThreadAbc1 c = new ThreadAbc1();
        a.init("A", c, a);
        b.init("B", a, b);
        c.init("C", b, c);
        try {
            // Thread.sleep用于保证 A B C 启动顺序
            new Thread(a).start();
            Thread.sleep(10);
            new Thread(b).start();
            Thread.sleep(10);
            new Thread(c).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class ThreadAbc1 implements Runnable{
    private String name;
    private ThreadAbc1 prev;
    private ThreadAbc1 self;
    public void init(String name, ThreadAbc1 prev, ThreadAbc1 self){
        this.name = name;
        this.prev = prev;
        this.self = self;
    }
    @Override
    public void run() {
        int count = 6;
        // 多线程并发，必须使用while，不能用if
        while(count > 0) {
            // 首先获取prev锁
            synchronized (prev) {
                // 再获取self锁
                synchronized (self) {
                    System.out.println(this.name);
                    count--;
                    // 唤醒其他线程竞争self锁
                    // notifyAll和notify并不会立刻释放锁
                    self.notifyAll();
                }
                try {
                    // wait会立即释放锁，当前线程休眠，等待唤醒
                    prev.wait();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}