package ru.nsu.ccfit.izhitsky.Suppliers;

import ru.nsu.ccfit.izhitsky.Parts.Accessory;
import ru.nsu.ccfit.izhitsky.Warehouses.AccessoryWarehouse;

public class AccessorySupplier implements Runnable
{
	private AccessoryWarehouse theWarehouse;
	private int availableID;
	private int timeout;

	public AccessorySupplier(AccessoryWarehouse warehouseAccessory_, int timeout_)
	{
		theWarehouse = warehouseAccessory_;
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
				theWarehouse.push(new Accessory(availableID));
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
