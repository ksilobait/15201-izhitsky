package ru.nsu.ccfit.izhitsky.factory.Suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.factory.CarAssembler;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.CarWarehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class CarWarehouseController implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(CarWarehouseController.class);

	private CarAssembler theAssembler;
	private CarWarehouse theWarehouse;
	private static AtomicInteger availableID = new AtomicInteger();
	private final Object theLock = new Object();

	public CarWarehouseController(CarWarehouse theWarehouse)
	{
		this.theWarehouse = theWarehouse;
	}

	public void setTheAssembler(CarAssembler theAssembler)
	{
		this.theAssembler = theAssembler;
	}

	@Override
	public void run()
	{
		try
		{
			while (!Thread.interrupted())
			{
				synchronized (theLock)
				{
					int carsToProduce = theWarehouse.getFreeSize() - theAssembler.getTheThreadPool().getThreadPoolSize();
					for (int i = 0; i < carsToProduce; i++)
					{
						theAssembler.createOneCar(availableID.get());
						theLogger.info("created the car #" + availableID.getAndIncrement());
					}
					theLock.wait();
				}
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("CarWarehouseController was interrupted");
		}
	}

	public void notifyController()
	{
		synchronized (theLock)
		{
			theLock.notify();
		}
	}

}