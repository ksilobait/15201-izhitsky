package ru.nsu.ccfit.izhitsky.factory.Warehouses;
import ru.nsu.ccfit.izhitsky.threadpool.MyBlockingQueue;
import ru.nsu.ccfit.izhitsky.factory.Parts.Coachwork;

public class CoachworkWarehouse
{
	MyBlockingQueue<Coachwork> theQueue;

	public CoachworkWarehouse(int size_)
	{
		theQueue = new MyBlockingQueue<>(size_);
	}

	public void push(Coachwork o) throws InterruptedException
	{
		theQueue.push(o);
	}

	public Coachwork pop() throws InterruptedException
	{
		return theQueue.pop();
	}
}
