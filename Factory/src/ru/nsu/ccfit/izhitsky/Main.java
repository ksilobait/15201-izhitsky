package ru.nsu.ccfit.izhitsky;

import ru.nsu.ccfit.izhitsky.Suppliers.*;
import ru.nsu.ccfit.izhitsky.Warehouses.*;

public class Main
{
	public static void main(String[] args)
	{
		MyConfigReader theConfigReader = new MyConfigReader("config.txt");
		EngineWarehouse theEngineWarehouse = new EngineWarehouse(theConfigReader.getEngineWarehouseSize());
		CoachworkWarehouse theCoachworkWarehouse = new CoachworkWarehouse(theConfigReader.getCoachworkWarehouseSize());
		AccessoryWarehouse theAccessoryWarehouse = new AccessoryWarehouse(theConfigReader.getAccessoryWarehouseSize());
		CarWarehouse theCarWarehouse = new CarWarehouse(theConfigReader.getCarWarehouseSize());

		MyAssembler theAssembler = new MyAssembler(theConfigReader.getThreadPoolSize(), theConfigReader.getTaskQueueSize());
		theAssembler.setEngineWarehouse(theEngineWarehouse);
		theAssembler.setCoachworkWarehouse(theCoachworkWarehouse);
		theAssembler.setAccessoryWarehouse(theAccessoryWarehouse);
		theAssembler.setCarWarehouse(theCarWarehouse);

		CarWarehouseController theCarWarehouseController = new CarWarehouseController(theAssembler, 1);

		EngineSupplier theEngineSupplier = new EngineSupplier(theEngineWarehouse, 1);

		CoachworkSupplier theCoachworkSupplier = new CoachworkSupplier(theCoachworkWarehouse, 1);

		AccessorySupplier theAccessorySupplier = new AccessorySupplier(theAccessoryWarehouse, 1);

		CarWarehouseController theCarWarehouseController = new CarWarehouseController(theCarWarehouse);

		theEngineSupplier.run();
		theCoachworkSupplier.run();
		theAccessorySupplier.run();

		MyThreadPool theThreadPool = new MyThreadPool(3);
	}
}
