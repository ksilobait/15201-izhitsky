package ru.nsu.ccfit.izhitsky.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.factory.Parts.*;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.*;
import ru.nsu.ccfit.izhitsky.threadpool.MyThreadPool;

public class CarAssembler
{
	private static final Logger theLogger = LogManager.getLogger(CarAssembler.class);

	private MyThreadPool theThreadPool;
	private EngineWarehouse theEngineWarehouse;
	private CoachworkWarehouse theCoachworkWarehouse;
	private AccessoryWarehouse theAccessoryWarehouse;
	private CarWarehouse theCarWarehouse;
	private int currentID;

	CarAssembler(int threadPoolSize_, int taskQueueSize_)
	{
		this.theThreadPool = new MyThreadPool(threadPoolSize_, taskQueueSize_);
	}

	public void createOneCar(int id_) //TODO: id
	{
		this.currentID = id_;

		try
		{
			Runnable theTask = new CreateOneCarTask();
			theThreadPool.addTask(theTask);
		}
		catch (InterruptedException e)
		{
			theLogger.info("the creating of a car was interrupted");
		}
	}

	class CreateOneCarTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				Engine theEngine = theEngineWarehouse.pop();
				Coachwork theCoachwork = theCoachworkWarehouse.pop();
				Accessory theAccessory = theAccessoryWarehouse.pop();
				Car theCar = new Car(currentID, theCoachwork, theEngine, theAccessory);
				theLogger.info("new car was created with IDs:" +
						theCar.getCoachwork().getIdNumber() + "C " +
						theCar.getEngine().getIdNumber() + "E " +
						theCar.getAccessory().getIdNumber() + "A"
				);
				theCarWarehouse.push(theCar);
			}
			catch (InterruptedException e)
			{
				theLogger.info("the creating of a car was interrupted");
			}
		}
	}

	public MyThreadPool getTheThreadPool()
	{
		return theThreadPool;
	}

	public CarWarehouse getTheCarWarehouse()
	{
		return theCarWarehouse;
	}

	public EngineWarehouse getTheEngineWarehouse()
	{
		return theEngineWarehouse;
	}

	public CoachworkWarehouse getTheCoachworkWarehouse()
	{
		return theCoachworkWarehouse;
	}

	public AccessoryWarehouse getTheAccessoryWarehouse()
	{
		return theAccessoryWarehouse;
	}

	void setEngineWarehouse(EngineWarehouse theEngineWarehouse_)
	{
		this.theEngineWarehouse = theEngineWarehouse_;
	}

	void setCoachworkWarehouse(CoachworkWarehouse theCoachworkWarehouse_)
	{
		this.theCoachworkWarehouse = theCoachworkWarehouse_;
	}

	void setAccessoryWarehouse(AccessoryWarehouse theAccessoryWarehouse_)
	{
		this.theAccessoryWarehouse = theAccessoryWarehouse_;
	}

	void setCarWarehouse(CarWarehouse theCarWarehouse_)
	{
		this.theCarWarehouse = theCarWarehouse_;
	}
}
