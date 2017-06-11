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
		this.timeout = timeout;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				Car theCar = carWarehouse.pop();
				theLogger.info("Dealer " + id.get() + ": Auto " +
						theCar.getIdNumber() + " (Body: " +
						theCar.getCoachwork().getIdNumber() + ", Motor: " +
						theCar.getEngine().getIdNumber() + ", Accessory: " +
						theCar.getAccessory().getIdNumber() + ")"
				);
				notifyTransactionCounterListener(transactionCounter.incrementAndGet()); //SWING
				Thread.sleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("DealerClass was interrupted");
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
