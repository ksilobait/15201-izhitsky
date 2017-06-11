package ru.nsu.ccfit.izhitsky.factory.Warehouses;
import ru.nsu.ccfit.izhitsky.threadpool.MyBlockingQueue;
import ru.nsu.ccfit.izhitsky.factory.Parts.Engine;

import java.util.ArrayList;
import java.util.List;

public class EngineWarehouse
{
	MyBlockingQueue<Engine> theQueue;

	public EngineWarehouse(int size_)
	{
		theQueue = new MyBlockingQueue<>(size_);
		notifyMaximumCapacityListener(size_);
	}

	public void push(Engine o) throws InterruptedException
	{
		theQueue.push(o);
		notifyCurrentCapacityListener(theQueue.getSize());
	}

	public Engine pop() throws InterruptedException
	{
		Engine toReturn = theQueue.pop();
		notifyCurrentCapacityListener(theQueue.getSize());
		return toReturn;
	}

	//SWING
	List<WarehouseCapacityListener> theListeners = new ArrayList<>();

	public void addCapacityListener(WarehouseCapacityListener newListener)
	{
		theListeners.add(newListener);
	}

	void notifyCurrentCapacityListener(int count)
	{
		for (WarehouseCapacityListener lstnr : theListeners)
		{
			lstnr.getCurrentCapacity(count);
		}
	}

	void notifyMaximumCapacityListener(int count)
	{
		for (WarehouseCapacityListener lstnr : theListeners)
		{
			lstnr.getMaximumCapacity(count);
		}
	}
}
