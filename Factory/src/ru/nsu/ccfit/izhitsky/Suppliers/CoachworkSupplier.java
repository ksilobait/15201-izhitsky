package ru.nsu.ccfit.izhitsky.Suppliers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.Parts.Coachwork;
import ru.nsu.ccfit.izhitsky.Warehouses.CoachworkWarehouse;

public class CoachworkSupplier implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(CoachworkSupplier.class);

	private CoachworkWarehouse theWarehouse;
	private int availableID;
	private int timeout;

	public CoachworkSupplier(CoachworkWarehouse warehouseCoachwork_, int timeout_)
	{
		theWarehouse = warehouseCoachwork_;
		timeout = timeout_;
		availableID = 0;
	}

	public Thread getThread()
	{
		Thread theThread = new Thread(this);
		theThread.setName("Coachwork Supplier Thread");
		return theThread;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				theWarehouse.push(new Coachwork(availableID));
				theLogger.info("pushed Coachwork #" + availableID + "into Coachwork WH");
				availableID++;
				Thread.sleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("CoachworkSupplier was interrupted");
		}
	}
}
