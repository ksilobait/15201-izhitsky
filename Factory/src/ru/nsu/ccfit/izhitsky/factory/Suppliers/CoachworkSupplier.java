package ru.nsu.ccfit.izhitsky.factory.Suppliers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.factory.Parts.Coachwork;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.CoachworkWarehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CoachworkSupplier implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(CoachworkSupplier.class);

	private CoachworkWarehouse theWarehouse;
	private static AtomicInteger availableID = new AtomicInteger();
	private static AtomicInteger transactionCounter = new AtomicInteger();
	private int timeout;

	public CoachworkSupplier(CoachworkWarehouse warehouseCoachwork_, int timeout_)
	{
		theWarehouse = warehouseCoachwork_;
		timeout = timeout_;
	}

	public Thread getThread()
	{
		Thread theThread = new Thread(this);
		theThread.setName("CSThread");
		return theThread;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				theWarehouse.push(new Coachwork(availableID.get()));
				theLogger.info("pushed Coachwork #" + availableID.getAndIncrement() + " into Coachwork WH");
				notifyTransactionCounterListener(transactionCounter.incrementAndGet()); //SWING
				Thread.sleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("CoachworkSupplier was interrupted");
		}
	}

	//SWING
	List<TransactionListener> theListeners = new ArrayList<>();

	public void addTransactionCounterListener(TransactionListener newListener)
	{
		theListeners.add(newListener);
	}

	void notifyTransactionCounterListener(int count)
	{
		for (TransactionListener lstnr : theListeners)
		{
			lstnr.totalTransactions(count);
		}
	}

}
