package ru.nsu.ccfit.izhitsky.factory.Suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.factory.CarAssembler;

import java.util.concurrent.atomic.AtomicInteger;

public class CarWarehouseController implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(CarWarehouseController.class);

	private CarAssembler theAssembler;
	private static AtomicInteger availableID = new AtomicInteger();
	private int timeout;

	public CarWarehouseController(CarAssembler theAssembler_, int timeout_)
	{
		this.theAssembler = theAssembler_;
		timeout = timeout_;
	}

	public Thread getThread()
	{
		Thread theThread = new Thread(this);
		theThread.setName("CWCThread");
		return theThread;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				theAssembler.createOneCar(availableID.get());
				theLogger.info("created the car #" + availableID.getAndIncrement());
				Thread.sleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("CarWarehouseController was interrupted");
		}
	}
}