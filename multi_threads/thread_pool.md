# 线程池
## 简介
&emsp;&emsp;在开发中，合理的使用线程池能够带来3个好处。  
1) ***降低资源消耗***。通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
2) ***提高响应速度***。当任务到达时，任务可以不需要等到线程创建就能立即执行。
3) ***提高线程的可管理性***。线程时稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一分配、调优和监控。
## 处理流程
&emsp;&emsp;线程池的处理主要分为三步，如图所示。
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/multi_threads/thread_poll_flow_chart.png"  width="500" height="400"> 
</div>  

1) 线程池判断***核心线程池***里的线程是否都在执行任务。如果不是，则创建一个新的工作线程来执行任务。如果***核心线程池***里的线程都在执行任务，则进入下个流程。
2) 线程池判断工作队列是否已满。如果工作队列没有满，则将新提交的任务存储在这个工作队列里。如果工作队列满了，则进入下个流程。
3) 线程池判断线程池中的线程是否都处于工作状态。如果没有，则创建一个新的工作线程来执行任务。如果已经满了，则交给饱和策略来处理这个任务。  

&emsp;&emsp;ThreadPoolExecutor执行execute()方法的过程如图所示。  
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/multi_threads/ThreadPoolExecutor_flow_chart.png"  width="500" height="400">  
</div>  

1) 如果当前运行的线程少于corePoolSize，则创建新线程来执行任务（执行该步骤需要获取全局锁）。
2) 如果运行的线程大于等于corePoolSize，则将任务加入BlockingQueue。
3) 如果无法将任务加入BlockingQueue（任务队列已满），则创建新的线程来处理任务（执行该步骤同样需要获取全局锁）。
4) 如果创建新线程会导致当前运行的线程数量超过maximumPoolSize，则拒绝该任务，调用RejectedExecutionHandler.rejectedExecution()方法。  

&emsp;&emsp;为什么如此设计呢？上述步骤中，1和3会使用全局锁，是一个严重的可伸缩瓶颈。在ThreadPoolExecutor预热后（即当前运行的线程数量大于等于corePoolSize），***几乎所有的execute()方法调用都是执行步骤2***，同时步骤2不需要获取全局锁。  
***源码分析(jdk11)***  
execute()执行过程
```
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();
    /*
    * Proceed in 3 steps:
    *
    * 1. If fewer than corePoolSize threads are running, try to
    * start a new thread with the given command as its first
    * task.  The call to addWorker atomically checks runState and
    * workerCount, and so prevents false alarms that would add
    * threads when it shouldn't, by returning false.
    *
    * 2. If a task can be successfully queued, then we still need
    * to double-check whether we should have added a thread
    * (because existing ones died since last checking) or that
    * the pool shut down since entry into this method. So we
    * recheck state and if necessary roll back the enqueuing if
    * stopped, or start a new thread if there are none.
    *
    * 3. If we cannot queue task, then we try to add a new
    * thread.  If it fails, we know we are shut down or saturated
    * and so reject the task.
    */
    int c = ctl.get();
    if (workerCountOf(c) < corePoolSize) {
        if (addWorker(command, true))
            return;
        c = ctl.get();
    }
    if (isRunning(c) && workQueue.offer(command)) {
        int recheck = ctl.get();
        if (! isRunning(recheck) && remove(command))
            reject(command);
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);
    }
    else if (!addWorker(command, false))
        reject(command);
}
```
&emsp;&emsp;线程池创建后会将线程封装成工作线程（Worker类），Worker在执行完任务后，还会循环获取工作队列里的任务来执行。
&emsp;&emsp;ThreadPoolExecutor中线程执行任务的示意图如图所示。   
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/multi_threads/ThreadPoolExecutor_execute_flow_chart.png"  width="500" height="400"> 
</div>

线程池中的线程执行任务分两种情况，
1) 在execute()方法中创建一个线程时，会让这个线程执行当前任务。
2) 这个线程执行完上图中1的任务后，会反复从BlockingQueue获取任务来执行。
## 使用线程池
&emsp;&emsp;可以使用execute()和submit()方法提交任务。  
&emsp;&emsp;execute方法用于提交不需要返回值的任务，所以无法判断任务是否执行成功，execute()方法输入的任务是一个Runnable类的实例。
```
threadsPool.execute(new Runnable(){
    @Override
    public void run(){
        // Todo something
    }
});
```
&emsp;&emsp;submit()方法用于提交需要返回值的任务，线程池会返回一个future类型的对象，通过future可以获得返回值并且判断是否执行成功。
```
Future<Object> future = executor.submit(...);
```
### 创建线程池
&emsp;&emsp;ThreadPoolExecutor构造函数如下所示。
```
public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler);
```
1) corePoolSize：***线程池的基本大小***。当提交一个任务到线程池时，线程池会创建一个线程来执行任务（即使有空闲线程也会创建）直到需要执行的任务数大于线程池基本大小。调用prestartAllCoreThreads()方法，线程池会提前创建并启动corePoolSize个线程。
2) maximumPoolSize：***线程池最大数量***。线程池允许创建的最大线程数，如果任务队列满了，并且已创建的线程数小于最大线程数，则会创建线程执行任务直到线程数达到最大数。***（注意：若采用无界队列，则该参数没什么效果，应当避免使用无界队列，可能会导致oom。）***
3) workQueue：***任务队列***。用于保存等待执行的任务的阻塞队列。可以使用LinkedBlockingQueue、ArrayBlockingQueue、SynchronousQueue、PriorityBlockingQueue。
4) ThreadFactory：设置创建线程的工厂，可以通过工厂给线程设置有意义的名字***（debug时很有用）***。下面展示了采用guava提供的ThreadFactoryBuilder给线程池中的线程设置有意义的名字。
```
new ThreadFactoryBuilder().setNameFormate("Demo-XX-TT").build();
```
5) RejectedExecutionHandler：***饱和策略***。当队列和线程池都满了，此时线程池处于饱和状态，饱和策略在此时用来处理提交的新任务。1）AbortPolicy，直接抛出异常；2）CallerRunsPolicy：只用调用者所在线程来运行任务；3）DiscardOldestPolicy：丢弃队列里最近的一个任务，并执行当前任务；4）DiscardPolicy，不处理，直接丢弃；5）通过实现RejectedExecutionHandler接口自定义策略。
6) keepAliveTime：***线程活动保持时间***。工作线程空闲后，保持存活的时间。若任务多可以适当调大。
7) unit：***keepAliveTime的单位***。TimeUnit.DAYS、TimeUnit.HOURS、TimeUnit.MINUTES、TimeUnit.MILLISECONDS、TimeUnit.MICROSECONDS、TimeUnit.NANOSECONDS。
### 提交任务
### 关闭线程池
&emsp;&emsp;关闭线程池有两种方法：shutdown和shutdownNow。  
&emsp;&emsp;原理是遍历线程池中的工作线程，然后逐个调用线程的interrupt方法来中断线程，所以无法响应中断的任务可能永远无法终止。两个方法的区别在于：
1) shutdown将线程池的状态设置成SHUTDOWN状态，然后中断所有没有正在执行任务的线程。
2) shutdownNow首先将线程池的状态设置成STOP，然后尝试停止所有正在执行或暂停任务的线程，并返回等待执行任务的列表。

&emsp;&emsp;即shutdown的方式能保证任务执行完成，而shutdownNow的方式下任务不一定执行完，通常采用shutdown的方式关闭线程池。  
&emsp;&emsp;只要调用了上面任意方法，isShutdown方法就会返回true，所有线程关闭成功后调用isTerminated方法才会返回true。
## 合理配置线程池
### 考虑点
&emsp;&emsp;合理配置线程池需要从以下几点出发进行分析。
1) 任务的性质：CPU密集、IO密集、混合型。
2) 任务的优先级：高、中、低。
3) 任务的执行时间：长、中、短。
4) 任务的依赖性：是否依赖其他资源（如数据库连接）。
### 配置
&emsp;&emsp;CPU密集型：配置尽可能小的线程，比如 N+1（CPU核心数+1）。  
&emsp;&emsp;IO密集型：并不是一直执行任务，即等待时间较多，则可以配置尽可能多的线程，如2N。  
&emsp;&emsp;混合型：可以依据执行时间分解为CPU密集型和IO密集型。  
&emsp;&emsp;优先级：可以使用PriorityBlockingQueue处理，让优先级高的任务先执行。（此种情况可能让低优先级任务饿死）  
&emsp;&emsp;执行时间：交给不同规模的线程池，或者使用优先级队列让执行时间短的任务先执行。  
&emsp;&emsp;依赖性：假如依赖数据库连接池，因为提交SQL请求后等待时间较长，即CPU空闲时间多，则可以设置较多线程以提高CPU利用率。  
&emsp;&emsp;注意，要使用有界队列。
## 线程池监控
&emsp;&emsp;可以通过线程池提供的参数进行监控。
1) taskCount：线程池需要执行的任务数量。
2) completedTaskCount：线程池在运行过程中已完成的任务数量，小于等于taskCount。
3) largestPoolSize：线程池里曾创建过的最大线程数。可以判断线程池曾经是否满过，若lagestPoolSize=maximumPoolSize，则表示线程池曾经满过。
4) getPoolSize：线程池的线程数量。
5) getActiveCount：获取活动的线程数。