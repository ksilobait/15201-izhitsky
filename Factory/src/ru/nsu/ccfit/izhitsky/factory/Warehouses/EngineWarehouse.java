package ru.nsu.ccfit.izhitsky.factory.Warehouses;
import ru.nsu.ccfit.izhitsky.threadpool.MyBlockingQueue;
import ru.nsu.ccfit.izhitsky.factory.Parts.Engine;

import java.util.ArrayList;
import java.util.List;

public class EngineWarehouse
{
	private MyBlockingQueue<Engine> theQueue;

	public EngineWarehouse(int size_)
	{
		theQueue = new MyBlockingQueue<>(size_);
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
	private List<WarehouseCapacityListener> theListeners = new ArrayList<>();

	public void addCapacityListener(WarehouseCapacityListener newListener)
	{
		theListeners.add(newListener);
	}

	private void notifyCurrentCapacityListener(int count)
	{
		for (WarehouseCapacityListener lstnr : theListeners)
		{
			lstnr.getCurrentCapacity(count);
		}
	}
}
