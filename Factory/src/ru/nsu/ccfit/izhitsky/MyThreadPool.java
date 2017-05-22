package ru.nsu.ccfit.izhitsky;

public class MyThreadPool
{
	MyBlockingQueue<Runnable> queueOfTasks;
	Thread[] poolOfThreads;
	//final static Logger logger = Logger.getLogger(ThreadPool.class); //log4j


	MyThreadPool(int threadCount)
	{
		poolOfThreads = new Thread[threadCount];
		queueOfTasks = new MyBlockingQueue<>(3);
	}

	public void addTask(Runnable task) throws InterruptedException
	{
		queueOfTasks.push(task);
	}

	public void run()
	{
		for (Runnable task : queueOfTasks)
		{
			poolOfThreads.add(new Thread(task));
		}
		for (Thread thread: poolOfThreads)
		{
			thread.start();
		}
	}


	public void interrupt()
	{
		for (Thread t : poolOfThreads)
		{
			t.interrupt();
		}
	}

}
