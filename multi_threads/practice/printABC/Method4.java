import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class Method4 {
    private static Lock lock = new ReentrantLock();
    private static int state = 0;
    static class ThreadA extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 6; ) {
                lock.lock();
                try {
                    // 多线程并发，不能用if，必须用循环测试等待条件，避免虚假唤醒
                    while (state % 3 == 0) {
                        System.out.println("A");
                        state++;
                        // i++操作需要上锁
                        i++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }
    static class ThreadB extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 6; ) {
                lock.lock();
                try {
                    while (state % 3 == 1) {
                        System.out.println("B");
                        state++;
                        i++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }
    static class ThreadC extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 6; ) {
                lock.lock();
                try {
                    while (state % 3 == 2) {
                        System.out.println("C");
                        state++;
                        i++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }
    public static void main(String[] args) {
        new ThreadA().start();
        new ThreadB().start();
        new ThreadC().start();
    }
}