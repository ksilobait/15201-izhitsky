package ru.nsu.ccfit.izhitsky.factory.Suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.factory.Parts.Engine;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.EngineWarehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EngineSupplier implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(EngineSupplier.class);

	private EngineWarehouse theWarehouse;
	private static AtomicInteger availableID = new AtomicInteger();
	private static AtomicInteger transactionCounter = new AtomicInteger();
	private int timeout;
	private final Object timeoutLock = new Object();

	public EngineSupplier(EngineWarehouse warehouseEngine_, int timeout_)
	{
		theWarehouse = warehouseEngine_;
		timeout = timeout_;
	}

	public Thread getThread()
	{
		Thread theThread = new Thread(this);
		theThread.setName("ESThread");
		return theThread;
	}

	public void setTimeout(int timeout)
	{
		synchronized (timeoutLock)
		{
			this.timeout = timeout;
			timeoutLock.notifyAll();
		}
	}

	@Override
	public void run()
	{
		try
		{
			while (!Thread.interrupted())
			{
				theWarehouse.push(new Engine(availableID.get()));
				theLogger.info("pushed Engine #" + availableID.getAndIncrement() + "into Engine WH");
				notifyTransactionCounterListener(transactionCounter.incrementAndGet()); //SWING
				mySleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("EngineSupplier was interrupted");
		}
	}

	private void mySleep(int ms) throws InterruptedException
	{
		synchronized (timeoutLock)
		{
			if (ms > 0)
			{
				timeoutLock.wait(ms);
			}
		}
	}

	//SWING
	private List<TransactionListener> theListeners = new ArrayList<>();

	public void addTransactionCounterListener(TransactionListener newListener)
	{
		theListeners.add(newListener);
	}

	private void notifyTransactionCounterListener(int count)
	{
		for (TransactionListener lstnr : theListeners)
		{
			lstnr.totalTransactions(count);
		}
	}
}
