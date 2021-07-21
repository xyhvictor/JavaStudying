# Thread类
&emsp;&emsp;Thread类的详细使用方法如下所示，用户通过继承Thread类并重写run方法来实现多线程。该方法存在一个明显的缺点，即在Java中仅允许单继承，具有一定的局限性，不利于扩展。
```
public class ThreadSubClassDemo{
    private static class ThreadDemo extends Thread{
        @Override
        public void run(){
            for(int i = 0; i < 10; i++){
                System.out.println(i);
            }
        }
    }
    public static void main(String[] args){
        for(int i = 0; i < 5; i++){
            ThreadDemo threadDemo = new ThreadDemo();
            threadDemo.start();
        }
    }
}
```
&emsp;&emsp;在Java中，线程分为守护线程（Daemon Thread）和用户线程（User Thread），守护线程通过Thread类的setDaemon()方法设置，用户线程完成工作之前不会自动结束生命周期，守护线程具备自动结束生命周期的能力。
在Thread中提供了多种方法，包括sleep()、join()、interrupt()等。下面对其分别进行介绍。
1) sleep：线程睡眠；
2) join：让一个线程等待另一个线程完成工作，例如thread.join()表示当前线程等待thread代表的线程工作结束；
3) interrupt：将线程的中断状态位设置为true，该方法不会中断一个正在运行的线程，其继续执行、中断或是死亡取决于程序本身。
# Runnable接口
&emsp;&emsp;Runnable与Callable的区别主要在于运行机制、返回值和异常处理三个方面，因此在Runnable与Callable小节中分别从以上三个方面进行介绍。
1) 运行机制  
   a.实现Runnable接口；  
   b.重写run()方法。
2) 返回值  
   无
3) 异常处理  
   无没有抛出任何异常，在run()中自行处理
# Callable接口
1) 运行机制  
   a.实现Callable接口；  
   b.重写call()方法。
2) 返回值  
   有
3) 异常处理  
   可以抛出异常