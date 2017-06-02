package ru.nsu.ccfit.izhitsky.factory.Suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.factory.Parts.Accessory;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.AccessoryWarehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class AccessorySupplier implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(AccessorySupplier.class);

	private AccessoryWarehouse theWarehouse;
	private static AtomicInteger availableID = new AtomicInteger();
	private int timeout;

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

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				theWarehouse.push(new Accessory(availableID.get()));
				theLogger.info("pushed Accessory #" + availableID.getAndIncrement() + " into Accessory WH");
				Thread.sleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("AccessorySupplier was interrupted");
		}
	}
}
