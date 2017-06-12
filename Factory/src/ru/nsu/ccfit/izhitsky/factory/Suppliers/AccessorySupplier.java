package ru.nsu.ccfit.izhitsky.factory.Suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.factory.Parts.Accessory;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.AccessoryWarehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessorySupplier implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(AccessorySupplier.class);

	private AccessoryWarehouse theWarehouse;
	private static AtomicInteger availableID = new AtomicInteger();
	private static AtomicInteger transactionCounter = new AtomicInteger();
	private int timeout;
	private final Object timeoutLock = new Object();

	public AccessorySupplier(AccessoryWarehouse warehouseAccessory_, int timeout_)
	{
		theWarehouse = warehouseAccessory_;
		timeout = timeout_;
	}

	public Thread getThread()
	{
		Thread theThread = new Thread(this);
		theThread.setName("ASThread");
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
				theWarehouse.push(new Accessory(availableID.get()));
				theLogger.info("pushed Accessory #" + availableID.getAndIncrement() + " into Accessory WH");
				notifyTransactionCounterListener(transactionCounter.incrementAndGet()); //SWING
				mySleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("AccessorySupplier was interrupted");
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
