package ru.nsu.ccfit.izhitsky.factory.Suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.factory.Parts.Car;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.CarWarehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DealerClass implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(EngineSupplier.class);
	private static AtomicInteger id = new AtomicInteger();
	private static AtomicInteger transactionCounter = new AtomicInteger();
	private final Object timeoutLock = new Object();

	private CarWarehouse carWarehouse;
	private int timeout;

	public DealerClass(CarWarehouse carWarehouse_, int timeout_)
	{
		this.carWarehouse = carWarehouse_;
		this.timeout = timeout_;
	}

	public Thread getThread()
	{
		Thread theThread = new Thread(this);
		theThread.setName("DealerThread#" + id.getAndIncrement());
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
				Car theCar = carWarehouse.pop();
				theLogger.info("Dealer " + id.get() + ": Auto " +
						theCar.getIdNumber() + " (Body: " +
						theCar.getCoachwork().getIdNumber() + ", Motor: " +
						theCar.getEngine().getIdNumber() + ", Accessory: " +
						theCar.getAccessory().getIdNumber() + ")"
				);
				notifyTransactionCounterListener(transactionCounter.incrementAndGet()); //SWING
				mySleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("DealerClass was interrupted");
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
