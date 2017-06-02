package ru.nsu.ccfit.izhitsky.factory.Warehouses;
import ru.nsu.ccfit.izhitsky.threadpool.MyBlockingQueue;
import ru.nsu.ccfit.izhitsky.factory.Parts.Engine;

public class EngineWarehouse
{
	MyBlockingQueue<Engine> theQueue;

	public EngineWarehouse(int size_)
	{
		theQueue = new MyBlockingQueue<>(size_);
	}

	public void push(Engine o) throws InterruptedException
	{
		theQueue.push(o);
	}

	public Engine pop() throws InterruptedException
	{
		return theQueue.pop();
	}

}
