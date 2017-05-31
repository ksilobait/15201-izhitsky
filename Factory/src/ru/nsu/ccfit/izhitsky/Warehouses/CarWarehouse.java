package ru.nsu.ccfit.izhitsky.Warehouses;

import ru.nsu.ccfit.izhitsky.MyBlockingQueue;
import ru.nsu.ccfit.izhitsky.Parts.Car;

public class CarWarehouse
{
	MyBlockingQueue<Car> theQueue;

	public CarWarehouse(int size_)
	{
		theQueue = new MyBlockingQueue<>(size_);
	}

	public void push(Car o) throws InterruptedException //to here from CarAssembler
	{
		theQueue.push(o);
	}

	public Car pop() throws InterruptedException //from here to dealers
	{
		return theQueue.pop();
	}

}
