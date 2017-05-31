package ru.nsu.ccfit.izhitsky;

public class MyThreadPool
{
	private MyBlockingQueue<Runnable> queueOfTasks;
	private Thread[] poolOfThreads;
	//final static Logger logger = Logger.getLogger(ThreadPool.class); //log4j


	MyThreadPool(int threadCount)
	{
		poolOfThreads = new Thread[threadCount];
		queueOfTasks = new MyBlockingQueue<>(100);
	}

	public void addTask(Runnable task) throws InterruptedException
	{
		queueOfTasks.push(task);
	}

	public void run()
	{
		for (Runnable task : queueOfTasks)
		{
			poolOfThreads[1].add(new Thread(task)); //TODO wut
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
