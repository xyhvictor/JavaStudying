# Java多线程
## Introduction
Java中线程生命周期如图所示，共五个状态，分别为：
1) 新建状态（New）；
2) 就绪状态（Runnable）；
3) 运行状态（Running）；
4) 阻塞状态Blocked；
5) 死亡状态（Dead）。  
![image](https://github.com/xyhvictor/JavaStudying/blob/main/pic/thread_status.png)

&emsp;&emsp;当线程被创建后即进入了新建状态（如使用new操作创建Thread类）；线程被创建后，该线程的start()方法被调用，则该线程进入就绪状态，等待CPU分配时间片执行；线程获得CPU的时间片后开始运行，线程进入运行状态；当线程因为某种原因暂停执行（如等待IO），让出CPU，则线程进入阻塞状态；线程执行完成或者因为异常退出run()方法后，该线程进入死亡状态，结束其生命周期。  
&emsp;&emsp;其中阻塞状态共分为三种，分别是：1）等待阻塞（如通过wait()方法，让线程等待某工作的完成）；2）同步阻塞（线程获取如synchronized同步锁失败）；3）其他阻塞（通过调用线程的sleep()、join()或发出了I/O请求）。
## Thread、Runnable、Callable、Future
### Thread类
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
### Runnable接口
&emsp;&emsp;Runnable与Callable的区别主要在于运行机制、返回值和异常处理三个方面，因此在Runnable与Callable小节中分别从以上三个方面进行介绍。
1) 运行机制  
   a.实现Runnable接口；  
   b.重写run()方法。
2) 返回值  
   无
3) 异常处理  
   无没有抛出任何异常，在run()中自行处理
### Callable接口
1) 运行机制  
   a.实现Callable接口；  
   b.重写call()方法。
2) 返回值  
   有
3) 异常处理  
   可以抛出异常
### Future
#### 应用场景  
&emsp;&emsp;解决问题：上面提到的三种方法都不能保证获取到线程执行的结果，而Future可以。通过实现Callback接口，并用Future可以接收到多线程的执行结果。  
&emsp;&emsp;场景：Future表示一个可能还没有完成的异步任务的结果，针对这个结果可以添加Callback以便在任务执行成功或失败后作出相应的操作。
#### 接口方法与类图
&emsp;&emsp;Future接口中包括了5个方法，分别是cancel()、isCancelled()、isDone()、get()、get(long, TimeUnit)，下面分别对其进行详细介绍。
1) cancel()：停止一个任务，若可以停止则返回true（通过mayInterruptIfRunning判断），若已经完成、已经停止、无法停止则返回false。
2) isCancelled()：判断当前方法是否取消。
3) isDone()：判断当前方法是否完成。
4) get()：当任务结束后返回一个结果，若调用时工作还没有结束，则会阻塞线程直到任务执行完毕。
5) get(long, TimeUnit)：在get()基础上等待一定的时间，有可能等不到。  

&emsp;&emsp;Future接口类图如图所示。其中主要包含了RunnableFuture接口、SchedualedFuture接口、CompletableFuture类和ForkJoinFuture类。  
![image](https://github.com/xyhvictor/JavaStudying/blob/main/pic/Future.png)  
1) RunnableFuture  
&emsp;&emsp;RunnableFuture接口同时继承了Future接口和Runnable接口，如下所示。在成功执行run方法后，可以通过Future获得执行结果。
```
public interface RunnableFuture<V> extends Runnable, Future<V>{
    void run();
}
```
2) SchedualFuture
3) ForkJoinTask
4) CompetableFuture

## Executor
&emsp;&emsp;线程池在java.util.concurrent包中，除此之外，线程池是为了复用线程，将线程的创建维护和任务进行分离，避免了创建和启动线程的消耗。  
&emsp;&emsp;Executor包括3大部分：  
1) 任务。即工作单元，包括被执行任务需要实现的接口（***即Runnable或Callable接口***）。
2) 任务的执行。即将任务分配给多个线程的执行机制，包括***Executor接口***以及继承自Executor接口的***ExecutorService接口***。
3) 异步计算的结果。即***Future接口***及实现了Future接口的***FutureTask类***。

Executor框架的成员及其关系如图所示
### ExecutorService
1) newFixedThreadPool  
&emsp;&emsp;创建一个定长的线程池，可控制线程最大并发数，超出的线程会在队列中等待。
2) newCachedThreadPool  
&emsp;&emsp;创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，那么就会回收部分空闲的线程；当任务数增加时，线程池自动的添加新线程来处理任务。此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说JVM）能够创建的最大线程数量。
3) ScheduledExecutorService
## Copy On Write机制
&emsp;&emsp;COW（Copy On Write）适用于读写模式。当多个线程在读取同一内存时，若有线程需要写该内存则会触发此机制。  
&emsp;&emsp;内存状态分为读和写。当线程读取内存块时，内存块为只读状态，若此时有线程写此内存块，该内存块的状态并不会更改，操作系统会创建一个副本供线程写，线程写完后会修改读状态为写状态，此时会触发中断，执行中断处理程序将副本覆盖在原内存块上。