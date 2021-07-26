# Executor
## Introduction
&emsp;&emsp;在HotSpot VM线程模型中，java线程（java.lang.Thread）被一对一映射到本地操作系统线程，即通过操作系统调度java线程。因此为了将这些重复的工作封装起来，java提供了用户级的调度器Executor，将应用分解为一个个任务，然后由Executor将这些任务映射到固定数量的线程上去完成。这种两级调度模型（Executor级调度任务和操作系统级调度线程）如图所示。

<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/multi_threads/Executor_Framework.png"  width="500" height="400"> 
</div> 

## Executor框架简介 
&emsp;&emsp;Executor包括3大部分：  
1) 任务。即工作单元，包括被执行任务需要实现的接口（***即Runnable或Callable接口***）。
2) 任务的执行。即将任务分配给多个线程的执行机制，包括***Executor接口***以及继承自Executor接口的***ExecutorService接口***。
3) 异步计算的结果。即***Future接口***及实现了Future接口的***FutureTask类***。

&emsp;&emsp;Executor框架的成员及其关系如图所示  
  
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/multi_threads/Executor.png"  width="500" height="400"> 
</div> 

&emsp;&emsp;Executor框架的使用示意图如图所示  
 
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/multi_threads/Executor_usage.png"  width="500" height="400"> 
</div> 

使用步骤大致分为三步，如下所示
1) 实现Runnable并重写run()方法 或 实现Callable并重写call()方法：
```
class CallableTest implements Callable<String> {
    @Override
    public String call(){
        try {
            return "ok";
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
```
2) 创建Executor接口的实现类ThreadPoolExecutor类（或者ScheduledThreadPoolExecutor类）的对象，然后调用其submit()（有返回值使用submit(),没有可以使用execute()方法）将工作任务添加到线程中，如果有返回值则返回Future对象。
```
ThreadPoolExecutor tpe = new ThreadPoolExecutor(5,10,100,TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5));
Future<String> future = tpe.submit(new CallableTest());
```
3) 调用Future对象的get()方法获得返回值，或者调用cancel()方法取消当前线程的执行。
```
try{
    System.out.println(future.get());
}catch (Exception e){
    e.printStackTrace();
}finally {
    tpe.shutdown();
}
```
## ExecutorService
### newFixedThreadPool  
&emsp;&emsp;创建一个定长的线程池，可控制线程最大并发数，超出的线程会在队列中等待。
### newCachedThreadPool  
&emsp;&emsp;创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，那么就会回收部分空闲的线程；当任务数增加时，线程池自动的添加新线程来处理任务。此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说JVM）能够创建的最大线程数量。
### ScheduledExecutorService
