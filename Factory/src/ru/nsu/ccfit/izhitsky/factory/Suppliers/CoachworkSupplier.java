package ru.nsu.ccfit.izhitsky.factory.Suppliers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.factory.Parts.Coachwork;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.CoachworkWarehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class CoachworkSupplier implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(CoachworkSupplier.class);

	private CoachworkWarehouse theWarehouse;
	private static AtomicInteger availableID = new AtomicInteger();
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

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				theWarehouse.push(new Coachwork(availableID.get()));
				theLogger.info("pushed Coachwork #" + availableID.getAndIncrement() + " into Coachwork WH");
				Thread.sleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("CoachworkSupplier was interrupted");
		}
	}
}
