package ru.nsu.ccfit.izhitsky.Suppliers;

import ru.nsu.ccfit.izhitsky.MyAssembler;
import ru.nsu.ccfit.izhitsky.Parts.Car;
import ru.nsu.ccfit.izhitsky.Warehouses.CarWarehouse;

public class CarWarehouseController implements Runnable
{
	private MyAssembler theAssembler;
	private int availableID;
	private int timeout;

	CarWarehouseController(MyAssembler theAssembler_, int timeout_)
	{
		this.theAssembler = theAssembler_;
		timeout = timeout_;
		availableID = 0;

	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				theWarehouse.push(new Car(availableID));
				availableID++;
				Thread.sleep(timeout);

			}
		}
		catch (InterruptedException e)
		{
			System.out.println("interruption in EngineSupplier");
			System.exit(-1);
		}
		finally
		{
			//something?
		}
	}
}