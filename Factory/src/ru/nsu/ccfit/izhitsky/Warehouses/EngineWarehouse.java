package ru.nsu.ccfit.izhitsky.Warehouses;
import ru.nsu.ccfit.izhitsky.MyBlockingQueue;
import ru.nsu.ccfit.izhitsky.Parts.Engine;

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

	Engine pop() throws InterruptedException
	{
		return theQueue.pop();
	}

}
