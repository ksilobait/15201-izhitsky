package ru.nsu.ccfit.izhitsky.Suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.Parts.Accessory;
import ru.nsu.ccfit.izhitsky.Warehouses.AccessoryWarehouse;

public class AccessorySupplier implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(AccessorySupplier.class);

	private AccessoryWarehouse theWarehouse;
	private int availableID;
	private int timeout;

	public AccessorySupplier(AccessoryWarehouse warehouseAccessory_, int timeout_)
	{
		theWarehouse = warehouseAccessory_;
		timeout = timeout_;
		availableID = 0;
	}

	public Thread getThread()
	{
		Thread theThread = new Thread(this);
		theThread.setName("Accessory Supplier Thread");
		return theThread;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				theWarehouse.push(new Accessory(availableID));
				theLogger.info("pushed Accessory #" + availableID + " into Accessory WH");
				availableID++;
				Thread.sleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("AccessorySupplier was interrupted");
		}
	}
}
