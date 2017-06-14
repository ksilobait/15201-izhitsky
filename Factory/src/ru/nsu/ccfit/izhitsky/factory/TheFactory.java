package ru.nsu.ccfit.izhitsky.factory;

import ru.nsu.ccfit.izhitsky.factory.Suppliers.*;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.*;

public class TheFactory
{
	private Thread theCoachworkSupplierThread;
	private Thread theEngineSupplierThread;
	private Thread[] theAccessorySupplierThreads;
	private Thread theCarWarehouseControllerThread;
	private Thread[] theDealerThreads;

	public CarAssembler carAssembler;
	private MyConfigReader theConfigReader;
	public EngineSupplier theEngineSupplier;
	public CoachworkSupplier theCoachworkSupplier;
	public AccessorySupplier theAccessorySupplier;
	public DealerClass theDealerClass;

	TheFactory(MyConfigReader theConfigReader_)
	{
		this.theConfigReader = theConfigReader_;

		//the car assembler (сборка машин)
		carAssembler = new CarAssembler(theConfigReader.getThreadPoolSize(),theConfigReader.getTaskQueueSize());
		carAssembler.setEngineWarehouse(new EngineWarehouse(theConfigReader.getEngineWarehouseSize())); //src1
		carAssembler.setCoachworkWarehouse(new CoachworkWarehouse(theConfigReader.getCoachworkWarehouseSize())); //src2
		carAssembler.setAccessoryWarehouse(new AccessoryWarehouse(theConfigReader.getAccessoryWarehouseSize())); //src3
		carAssembler.setCarWarehouse(new CarWarehouse(theConfigReader.getCarWarehouseSize())); //dest

		//final controller (контроллер склада готовых изделий)
		CarWarehouseController theCarWarehouseController = new CarWarehouseController(carAssembler.getTheCarWarehouse());
		theCarWarehouseController.setTheAssembler(carAssembler);
		carAssembler.getTheCarWarehouse().setTheController(theCarWarehouseController);

		//suppliers (поставщики кузовов, деталей и аксессуаров)
		theEngineSupplier = new EngineSupplier(carAssembler.getTheEngineWarehouse(), theConfigReader.getEngineSupplierTimeout());
		theCoachworkSupplier = new CoachworkSupplier(carAssembler.getTheCoachworkWarehouse(), theConfigReader.getCoachworkSupplierTimeout());
		theAccessorySupplier = new AccessorySupplier(carAssembler.getTheAccessoryWarehouse(), theConfigReader.getAccessorySupplierTimeout());

		//dealers residence (продажа машин)
		theDealerClass = new DealerClass(carAssembler.getTheCarWarehouse(), theConfigReader.getDealerTimeout());

		//prepare to launch (get threads)
		theCoachworkSupplierThread = new Thread(theCoachworkSupplier, "CSThread"); //t1
		theEngineSupplierThread = new Thread(theEngineSupplier, "ESThread"); //t2
		int numberOfAccessorySuppliers = theConfigReader.getAccessorySuppliersNumber(); //[t3...
		theAccessorySupplierThreads = new Thread[numberOfAccessorySuppliers];
		for (int i = 0; i < numberOfAccessorySuppliers; i++)
		{
			theAccessorySupplierThreads[i] = new Thread(theAccessorySupplier, "ASThread#" + i); //...t3]
		}
		theCarWarehouseControllerThread = new Thread(theCarWarehouseController, "CWCThread"); //t4
		int numberOfDealers = theConfigReader.getDealersNumber(); //[t5...
		theDealerThreads = new Thread[numberOfDealers];
		for (int i = 0; i < numberOfDealers; i++)
		{
			theDealerThreads[i] = new Thread(theDealerClass, "DThread#" + i); //...t5]
		}
	}

	public void launch()
	{
		theCoachworkSupplierThread.start(); //r1
		theEngineSupplierThread.start(); //r2
		for (Thread t : theAccessorySupplierThreads)
		{
			t.start(); //r3
		}
		for (Thread t : carAssembler.getTheThreadPool().getPoolOfThreads())
		{
			t.start(); //r4
		}
		theCarWarehouseControllerThread.start(); //r5
		for (Thread thread : theDealerThreads)
		{
			thread.start(); //r6
		}
	}

	public void finish()
	{
		theCoachworkSupplierThread.interrupt(); //r1
		theEngineSupplierThread.interrupt(); //r2
		for (Thread t : theAccessorySupplierThreads)
		{
			t.interrupt(); //r3
		}
		for (Thread t : carAssembler.getTheThreadPool().getPoolOfThreads())
		{
			t.interrupt(); //r4
		}
		theCarWarehouseControllerThread.interrupt(); //r5
		for (Thread thread : theDealerThreads)
		{
			thread.interrupt(); //r6
		}
	}

	public MyConfigReader getTheConfigReader()
	{
		return theConfigReader;
	}
}
