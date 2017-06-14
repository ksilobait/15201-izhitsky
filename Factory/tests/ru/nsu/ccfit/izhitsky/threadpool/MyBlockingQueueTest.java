package ru.nsu.ccfit.izhitsky.threadpool;

import org.junit.Assert;
import org.junit.Test;

public class MyBlockingQueueTest
{
	@Test
	public void push_and_pop()
	{
		final int SIZE = 7569;
		MyBlockingQueue<Integer> theQueue = new MyBlockingQueue<>(SIZE);

		Thread theConsumer = new Thread(() ->
		{
			for (int i = 0; i < SIZE; i++)
			{
				try
				{
					int data = theQueue.pop();
					Assert.assertEquals(i * 2, data);
				}
				catch (InterruptedException e)
				{
					System.out.println(e.toString());
				}
			}
		});

		Thread theProducer = new Thread(() ->
		{
			for (int i = 0; i < SIZE; i++)
			{
				try
				{
					theQueue.push(i * 2);
				}
				catch (InterruptedException e)
				{
					System.out.println(e.toString());
				}
			}
		});

		theConsumer.start();
		theProducer.start();
		System.out.println("TEST for MyBlockingQueue PASSED");
	}
}