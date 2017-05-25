package ru.nsu.ccfit.izhitsky.Suppliers;

import ru.nsu.ccfit.izhitsky.Parts.Engine;
import ru.nsu.ccfit.izhitsky.Warehouses.EngineWarehouse;

public class EngineSupplier implements Runnable
{
	private EngineWarehouse theWarehouse;
	private int availableID;
	private int timeout;

	public EngineSupplier(EngineWarehouse warehouseEngine_, int timeout_)
	{
		theWarehouse = warehouseEngine_;
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
				theWarehouse.push(new Engine(availableID));
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
