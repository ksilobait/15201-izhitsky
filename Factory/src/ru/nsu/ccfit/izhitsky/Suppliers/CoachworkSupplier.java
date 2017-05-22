package ru.nsu.ccfit.izhitsky.Suppliers;


import ru.nsu.ccfit.izhitsky.Parts.Coachwork;
import ru.nsu.ccfit.izhitsky.Warehouses.CoachworkWarehouse;

public class CoachworkSupplier implements Runnable
{
	private CoachworkWarehouse theWarehouse;
	private int availableID;
	private int timeout;

	public CoachworkSupplier(CoachworkWarehouse warehouseCoachwork_, int timeout_)
	{
		theWarehouse = warehouseCoachwork_;
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
				theWarehouse.push(new Coachwork(availableID));
				availableID++;
				Thread.sleep(timeout);
			}
		}
		catch (InterruptedException e)
		{
			System.out.println("interruption in EngineSupplier");
			System.exit(-1);
		}
	}
}
