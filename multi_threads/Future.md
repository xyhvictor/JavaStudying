# Future
## 应用场景  
&emsp;&emsp;解决问题：Runnable、Callable、Thread都不能保证获取到线程执行的结果，而Future可以。通过实现Callback接口，并用Future可以接收到多线程的执行结果。  
&emsp;&emsp;场景：Future表示一个可能还没有完成的异步任务的结果，针对这个结果可以添加Callback以便在任务执行成功或失败后作出相应的操作。
## 接口方法与类图
&emsp;&emsp;Future接口中包括了5个方法，分别是cancel()、isCancelled()、isDone()、get()、get(long, TimeUnit)，下面分别对其进行详细介绍。
1) cancel()：停止一个任务，若可以停止则返回true（通过mayInterruptIfRunning判断），若已经完成、已经停止、无法停止则返回false。
2) isCancelled()：判断当前方法是否取消。
3) isDone()：判断当前方法是否完成。
4) get()：当任务结束后返回一个结果，若调用时工作还没有结束，则会阻塞线程直到任务执行完毕。
5) get(long, TimeUnit)：在get()基础上等待一定的时间，有可能等不到。  

&emsp;&emsp;Future接口类图如图所示。其中主要包含了RunnableFuture接口、SchedualedFuture接口、CompletableFuture类和ForkJoinFuture类。  
![image](https://github.com/xyhvictor/JavaStudying/blob/main/pic/multi_threads/Future.png)  
## RunnableFuture
&emsp;&emsp;RunnableFuture接口同时继承了Future接口和Runnable接口，如下所示。在成功执行run方法后，可以通过Future获得执行结果。
```
public interface RunnableFuture<V> extends Runnable, Future<V>{
    void run();
}
```
## SchedualFuture
## ForkJoinTask
## CompetableFuture
