package ru.nsu.ccfit.izhitsky.Suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.Parts.Engine;
import ru.nsu.ccfit.izhitsky.Warehouses.EngineWarehouse;

public class EngineSupplier implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(EngineSupplier.class);

	private EngineWarehouse theWarehouse;
	private int availableID;
	private int timeout;

	public EngineSupplier(EngineWarehouse warehouseEngine_, int timeout_)
	{
		theWarehouse = warehouseEngine_;
		timeout = timeout_;
		availableID = 0;
	}

	public Thread getThread()
	{
		Thread theThread = new Thread(this);
		theThread.setName("Engine Supplier Thread");
		return theThread;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				theWarehouse.push(new Engine(availableID));
				theLogger.info("pushed Engine #" + availableID + "into Engine WH");
				availableID++;
				Thread.sleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("EngineSupplier was interrupted");
		}
	}
}
