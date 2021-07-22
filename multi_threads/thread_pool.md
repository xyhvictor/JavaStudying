# 线程池
## 简介
&emsp;&emsp;在开发中，合理的使用线程池能够带来3个好处。  
1) ***降低资源消耗***。通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
2) ***提高响应速度***。当任务到达时，任务可以不需要等到线程创建就能立即执行。
3) ***提高线程的可管理性***。线程时稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一分配、调优和监控。
## 处理流程
&emsp;&emsp;线程池的处理主要分为三步，如图所示。
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/multi_threads/thread_poll_flow_chart.png"  style="zoom:10%"> 
</div>  

1) 线程池判断***核心线程池***里的线程是否都在执行任务。如果不是，则创建一个新的工作线程来执行任务。如果***核心线程池***里的线程都在执行任务，则进入下个流程。
2) 线程池判断工作队列是否已满。如果工作队列没有满，则将新提交的任务存储在这个工作队列里。如果工作队列满了，则进入下个流程。
3) 线程池判断线程池中的线程是否都处于工作状态。如果没有，则创建一个新的工作线程来执行任务。如果已经满了，则交给饱和策略来处理这个任务。  

&emsp;&emsp;ThreadPoolExecutor执行execute()方法的过程如图所示。  
<div align="center">  
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/multi_threads/ThreadPoolExecutor_flow_chart.png"  style="zoom:10%">  
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
<img src="https://github.com/xyhvictor/JavaStudying/blob/main/pic/multi_threads/ThreadPoolExecutor_execute_flow_chart.png"  style="zoom:10%"> 
</div>

线程池中的线程执行任务分两种情况，
1) 在execute()方法中创建一个线程时，会让这个线程执行当前任务。
2) 这个线程执行完上图中1的任务后，会反复从BlockingQueue获取任务来执行。