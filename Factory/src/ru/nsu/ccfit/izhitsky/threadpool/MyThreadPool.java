package ru.nsu.ccfit.izhitsky.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MyThreadPool
{
	private MyBlockingQueue<Runnable> queueOfTasks;
	private Thread[] poolOfThreads;
	private static final Logger theLogger = LogManager.getLogger(MyThreadPool.class); //log4j

	public Thread[] getPoolOfThreads()
	{
		return poolOfThreads;
	}

	public MyThreadPool(int threadPoolSize_, int taskQueueSize_)
	{
		poolOfThreads = new Thread[threadPoolSize_];
		queueOfTasks = new MyBlockingQueue<>(taskQueueSize_);

		for (int i = 0; i < threadPoolSize_; ++i)
		{
			ThreadPoolRunnable runnable = new ThreadPoolRunnable();
			poolOfThreads[i] = new Thread(runnable);
			poolOfThreads[i].setName("Thread #" + i);
		}
	}

	class ThreadPoolRunnable implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				Runnable theTask = queueOfTasks.pop();
				theTask.run();
			}
			catch (InterruptedException e)
			{
				theLogger.info("ThreadPool was interrupted");
			}
		}
	}

	public void addTask(Runnable task) throws InterruptedException
	{
		queueOfTasks.push(task);
	}


	public void interrupt()
	{
		for (Thread t : poolOfThreads)
		{
			t.interrupt();
		}
	}

}
