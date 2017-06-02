package ru.nsu.ccfit.izhitsky.factory.Suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.factory.Parts.Engine;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.EngineWarehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class EngineSupplier implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(EngineSupplier.class);

	private EngineWarehouse theWarehouse;
	private static AtomicInteger availableID = new AtomicInteger();
	private int timeout;

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

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				theWarehouse.push(new Engine(availableID.get()));
				theLogger.info("pushed Engine #" + availableID.getAndIncrement() + "into Engine WH");
				Thread.sleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("EngineSupplier was interrupted");
		}
	}
}
