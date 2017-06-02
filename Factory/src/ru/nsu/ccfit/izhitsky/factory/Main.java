package ru.nsu.ccfit.izhitsky.factory;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import ru.nsu.ccfit.izhitsky.factory.Suppliers.*;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.*;

import java.util.Properties;

public class Main
{
	static
	{
		Properties props = System.getProperties();
		props.setProperty("log4j.configurationFile", "file:src/log4j2.xml");
	}

	private static final Logger theLogger = LogManager.getLogger(Main.class);

	public static void main(String[] args)
	{
		//read config
		MyConfigReader theConfigReader = new MyConfigReader("config.txt");

		//disable logging
		if (!theConfigReader.isLogSale())
		{
			Logger anotherLogger = LogManager.getRootLogger();
			Configurator.setLevel(anotherLogger.getName(), Level.OFF);
		}

		//3 start warehouses (склады двигателей, кузовов и аксессуаров)
		EngineWarehouse theEngineWarehouse = new EngineWarehouse(theConfigReader.getEngineWarehouseSize());
		CoachworkWarehouse theCoachworkWarehouse = new CoachworkWarehouse(theConfigReader.getCoachworkWarehouseSize());
		AccessoryWarehouse theAccessoryWarehouse = new AccessoryWarehouse(theConfigReader.getAccessoryWarehouseSize());

		//final warehouse (склад готовых изделий)
		CarWarehouse theCarWarehouse = new CarWarehouse(theConfigReader.getCarWarehouseSize());

		//the car assembler (сборка машин)
		CarAssembler carAssembler = new CarAssembler(theConfigReader.getThreadPoolSize(), theConfigReader.getTaskQueueSize());
		carAssembler.setEngineWarehouse(theEngineWarehouse); //src1
		carAssembler.setCoachworkWarehouse(theCoachworkWarehouse); //src2
		carAssembler.setAccessoryWarehouse(theAccessoryWarehouse); //src3
		carAssembler.setCarWarehouse(theCarWarehouse); //dest

		//final controller (контроллер склада готовых изделий)
		CarWarehouseController theCarWarehouseController = new CarWarehouseController(carAssembler, 100);

		//suppliers (поставщики кузовов, деталей и аксессуаров)
		EngineSupplier theEngineSupplier = new EngineSupplier(theEngineWarehouse, theConfigReader.getEngineSupplierTimeout());
		CoachworkSupplier theCoachworkSupplier = new CoachworkSupplier(theCoachworkWarehouse, theConfigReader.getCoachworkSupplierTimeout());
		AccessorySupplier theAccessorySupplier = new AccessorySupplier(theAccessoryWarehouse, theConfigReader.getAccessorySupplierTimeout());

		//dealers residence (продажа машин)
		DealerClass theDealerClass = new DealerClass(theCarWarehouse, theConfigReader.getDealerTimeout());

		//prepare to launch (get threads)
		Thread theCoachworkSupplierThread = theCoachworkSupplier.getThread(); //t1
		Thread theEngineSupplierThread = theEngineSupplier.getThread(); //t2
		int numberOfAccessorySuppliers = theConfigReader.getAccessorySuppliersNumber(); //[t3...
		Thread[] theAccessorySupplierThreads = new Thread[numberOfAccessorySuppliers];
		for (int i = 0; i < numberOfAccessorySuppliers; i++)
		{
			theAccessorySupplierThreads[i] = theAccessorySupplier.getThread(); //...t3]
		}
		Thread theCarWarehouseControllerThread = theCarWarehouseController.getThread(); //t4
		int numberOfDealers = theConfigReader.getDealersNumber(); //[t5...
		Thread[] theDealerThreads = new Thread[numberOfDealers];
		for (int i = 0; i < numberOfDealers; i++)
		{
			theDealerThreads[i] = theDealerClass.getThread(); //...t5]
		}
		theLogger.info("everything's ready");

		//launch
		theCoachworkSupplierThread.start(); //r1
		theEngineSupplierThread.start(); //r2
		for (Thread t: theAccessorySupplierThreads)
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

		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			//ignore
		}

		//finish
		theLogger.info("");
		theLogger.info("the termination process started");
		theCoachworkSupplierThread.interrupt(); //r1
		theEngineSupplierThread.interrupt(); //r2
		for (Thread t: theAccessorySupplierThreads)
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
}
