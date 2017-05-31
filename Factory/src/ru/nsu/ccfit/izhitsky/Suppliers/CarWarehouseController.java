package ru.nsu.ccfit.izhitsky.Suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.CarAssembler;

public class CarWarehouseController implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(CarWarehouseController.class);

	private CarAssembler theAssembler;
	private int availableID;
	private int timeout;

	public CarWarehouseController(CarAssembler theAssembler_, int timeout_)
	{
		this.theAssembler = theAssembler_;
		timeout = timeout_;
		availableID = 0;
	}

	public Thread getThread()
	{
		Thread theThread = new Thread(this);
		theThread.setName("CarWarehouse Controller Thread");
		return theThread;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				theAssembler.createOneCar(availableID);
				theLogger.info("created the car #" + availableID);
				availableID++;
				Thread.sleep(timeout);

			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("CarWarehouseController was interrupted");
		}
	}
}