package ru.nsu.ccfit.izhitsky.factory.Warehouses;

import ru.nsu.ccfit.izhitsky.factory.Suppliers.CarWarehouseController;
import ru.nsu.ccfit.izhitsky.threadpool.MyBlockingQueue;
import ru.nsu.ccfit.izhitsky.factory.Parts.Car;

import java.util.ArrayList;
import java.util.List;

public class CarWarehouse
{
	private MyBlockingQueue<Car> theQueue;
	private CarWarehouseController theController;

	public CarWarehouse(int size_)
	{
		theQueue = new MyBlockingQueue<>(size_);
	}

	public void setTheController(CarWarehouseController theController)
	{
		this.theController = theController;
	}

	public int getFreeSize()
	{
		return theQueue.getMaxSize() - theQueue.getSize();
	}

	public void push(Car o) throws InterruptedException //to here from CarAssembler
	{
		theQueue.push(o);
		theController.notifyController();
		notifyCurrentCapacityListener(theQueue.getSize());
	}

	public Car pop() throws InterruptedException //from here to dealers
	{
		Car toReturn = theQueue.pop();
		notifyCurrentCapacityListener(theQueue.getSize());
		theController.notifyController();
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
