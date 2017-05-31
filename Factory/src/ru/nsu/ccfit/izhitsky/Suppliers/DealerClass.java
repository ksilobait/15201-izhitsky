package ru.nsu.ccfit.izhitsky.Suppliers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.Parts.Car;
import ru.nsu.ccfit.izhitsky.Warehouses.CarWarehouse;

public class DealerClass implements Runnable
{
	private static final Logger theLogger = LogManager.getLogger(EngineSupplier.class);

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
		theThread.setName("The Dealer Thread (one of many)");
		return theThread;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				Car theCar = carWarehouse.pop();
				theLogger.info("Dealer got the car with IDs:" +
						theCar.getIdNumber() + " " +
						theCar.getCoachwork().getIdNumber() + "C " +
						theCar.getEngine().getIdNumber() + "E " +
						theCar.getAccessory().getIdNumber() + "A"
				);
				Thread.sleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			theLogger.info("DealerClass was interrupted");
		}
	}
}
