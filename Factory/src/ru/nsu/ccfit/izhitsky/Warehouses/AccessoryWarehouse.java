package ru.nsu.ccfit.izhitsky.Warehouses;
import ru.nsu.ccfit.izhitsky.MyBlockingQueue;
import ru.nsu.ccfit.izhitsky.Parts.Accessory;

public class AccessoryWarehouse
{
	MyBlockingQueue<Accessory> theQueue;

	public AccessoryWarehouse(int size_)
	{
		theQueue = new MyBlockingQueue<>(size_);
	}

	public void push(Accessory o) throws InterruptedException
	{
		theQueue.push(o);
	}

	public Accessory pop() throws InterruptedException
	{
		return theQueue.pop();
	}
}
