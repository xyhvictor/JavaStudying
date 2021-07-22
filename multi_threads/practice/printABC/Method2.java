import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yuhui.xi
 */
public class Method2{
    private static Lock lock = new ReentrantLock();
    private static Condition conditionA = lock.newCondition();
    private static Condition conditionB = lock.newCondition();
    private static Condition conditionC = lock.newCondition();
    private static int count = 0;

    public static void main(String[] args) {
        new Thread(new ThreadA()).start();
        new Thread(new ThreadB()).start();
        new Thread(new ThreadC()).start();
    }
    static class ThreadA implements Runnable{
        @Override
        public void run(){
            lock.lock();
            try{
                for(int i = 0; i < 6; i++){
                    while (count % 3 != 0){
                        conditionA.await();
                    }
                    System.out.println("A");
                    count++;
                    conditionB.signal();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }
    static class ThreadB implements Runnable{
        @Override
        public void run(){
            lock.lock();
            try{
                for(int i = 0; i < 6; i++){
                    while (count % 3 != 1){
                        conditionB.await();
                    }
                    System.out.println("B");
                    count++;
                    conditionC.signal();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }
    static class ThreadC implements Runnable{
        @Override
        public void run(){
            lock.lock();
            try{
                for(int i = 0; i < 6; i++){
                    while (count % 3 != 2){
                        conditionC.await();
                    }
                    System.out.println("C");
                    count++;
                    conditionA.signal();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }
}