package ru.nsu.ccfit.izhitsky;

import java.util.LinkedList;

public class MyBlockingQueue<T>
{
	private int maxSize;
	private LinkedList<T> theQueue;

	public MyBlockingQueue(int size_)
	{
		this.maxSize = size_;
		theQueue = new LinkedList<>();
	}

	public synchronized void push(T item) throws InterruptedException
	{
		while (theQueue.size() == maxSize) //overload
		{
			wait();
		}

		if (theQueue.size() == 0) //TODO empty
		{
			notifyAll();
		}

		theQueue.add(item);
	}

	public synchronized T pop() throws InterruptedException
	{
		while (theQueue.size() == 0) //empty
		{
			wait();
		}

		if (theQueue.size() == maxSize) //TODO overload
		{
			notifyAll();
		}

		return theQueue.remove(0);
	}
}