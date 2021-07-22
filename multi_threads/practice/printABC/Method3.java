import java.util.concurrent.Semaphore;

/**
 * @author yuhui.xi
 */
public class Method3 {
    private static Semaphore A = new Semaphore(1);
    private static Semaphore B = new Semaphore(0);
    private static Semaphore C = new Semaphore(0);

    public static void main(String[] args) {
        new Thread(new ThreadA()).start();
        new Thread(new ThreadB()).start();
        new Thread(new ThreadC()).start();
    }
    static class ThreadA implements Runnable{
        @Override
        public void run(){
            try {
                for(int i = 0; i < 6; i++){
                    A.acquire();
                    System.out.println("A");
                    B.release();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    static class ThreadB implements Runnable{
        @Override
        public void run(){
            try {
                for(int i = 0; i < 6; i++){
                    B.acquire();
                    System.out.println("B");
                    C.release();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    static class ThreadC implements Runnable{
        @Override
        public void run(){
            try {
                for(int i = 0; i < 6; i++){
                    C.acquire();
                    System.out.println("C");
                    A.release();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
