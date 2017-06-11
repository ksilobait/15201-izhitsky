package ru.nsu.ccfit.izhitsky.factory.Warehouses;
import ru.nsu.ccfit.izhitsky.threadpool.MyBlockingQueue;
import ru.nsu.ccfit.izhitsky.factory.Parts.Coachwork;

import java.util.ArrayList;
import java.util.List;

public class CoachworkWarehouse
{
	MyBlockingQueue<Coachwork> theQueue;

	public CoachworkWarehouse(int size_)
	{
		theQueue = new MyBlockingQueue<>(size_);
		notifyMaximumCapacityListener(size_);
	}

	public void push(Coachwork o) throws InterruptedException
	{
		theQueue.push(o);
		notifyCurrentCapacityListener(theQueue.getSize());
	}

	public Coachwork pop() throws InterruptedException
	{
		Coachwork toReturn = theQueue.pop();
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
