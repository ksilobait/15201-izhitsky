package ru.nsu.ccfit.izhitsky;

import ru.nsu.ccfit.izhitsky.Suppliers.AccessorySupplier;
import ru.nsu.ccfit.izhitsky.Suppliers.CoachworkSupplier;
import ru.nsu.ccfit.izhitsky.Suppliers.EngineSupplier;
import ru.nsu.ccfit.izhitsky.Warehouses.AccessoryWarehouse;
import ru.nsu.ccfit.izhitsky.Warehouses.CoachworkWarehouse;
import ru.nsu.ccfit.izhitsky.Warehouses.EngineWarehouse;

public class Main
{
	public static void main(String[] args)
	{
		MyConfigReader theConfigReader = new MyConfigReader("config.txt");

		EngineWarehouse theEngineWarehouse = new EngineWarehouse(theConfigReader.getEngineWarehouseSize());
		EngineSupplier theEngineSupplier = new EngineSupplier(theEngineWarehouse, 1);

		CoachworkWarehouse theCoachworkWarehouse = new CoachworkWarehouse(theConfigReader.getCoachworkWarehouseSize());
		CoachworkSupplier theCoachworkSupplier = new CoachworkSupplier(theCoachworkWarehouse, 1);

		AccessoryWarehouse theAccessoryWarehouse = new AccessoryWarehouse(theConfigReader.getAccessoryWarehouseSize());
		AccessorySupplier theAccessorySupplier = new AccessorySupplier(theAccessoryWarehouse, 1);

		theEngineSupplier.run();
		theCoachworkSupplier.run();
		theAccessorySupplier.run();

		MyThreadPool theThreadPool = new MyThreadPool(3);
	}
}
