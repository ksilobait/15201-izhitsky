package ru.nsu.ccfit.izhitsky.factory;

import ru.nsu.ccfit.izhitsky.factory.Suppliers.*;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.AccessoryWarehouse;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.CarWarehouse;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.CoachworkWarehouse;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.EngineWarehouse;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TheFactory
{
	private CarAssembler carAssembler;
	private Thread theCoachworkSupplierThread;
	private Thread theEngineSupplierThread;
	private Thread[] theAccessorySupplierThreads;
	private Thread theCarWarehouseControllerThread;
	private Thread[] theDealerThreads;
	private JPanel panel1;
	private JSlider coachworkSlider;
	private JProgressBar coachworkProgressBar;
	private JSlider engineSlider;
	private JSlider accessorySlider;
	private JSlider dealerSlider;
	private JProgressBar engineProgressBar;
	private JProgressBar accessoryProgressBar;
	private JProgressBar dealerProgressBar;
	private JLabel coachworkTotalLabel;
	private JLabel engineTotalLabel;
	private JLabel accessoryTotalLabel;
	private JLabel dealerTotalLabel;
	private JPanel suppliersPanel;
	private JPanel warehousesPanel;
	private JProgressBar coachworkWarehouseProgressBar;
	private JProgressBar engineWarehouseProgressBar;
	private JProgressBar accessoryWarehouseProgressBar;
	private JProgressBar dealerWarehouseProgressBar;
	private JButton STARTButton;
	private JButton STOPButton;
	private JPanel mainPanel;

	TheFactory(MyConfigReader theConfigReader)
	{
		//3 start warehouses (склады двигателей, кузовов и аксессуаров)
		EngineWarehouse theEngineWarehouse = new EngineWarehouse(theConfigReader.getEngineWarehouseSize());
		CoachworkWarehouse theCoachworkWarehouse = new CoachworkWarehouse(theConfigReader.getCoachworkWarehouseSize());
		AccessoryWarehouse theAccessoryWarehouse = new AccessoryWarehouse(theConfigReader.getAccessoryWarehouseSize());

		//final warehouse (склад готовых изделий)
		CarWarehouse theCarWarehouse = new CarWarehouse(theConfigReader.getCarWarehouseSize());

		//the car assembler (сборка машин)
		carAssembler = new CarAssembler(theConfigReader.getThreadPoolSize(), theConfigReader.getTaskQueueSize());
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
		theCoachworkSupplierThread = theCoachworkSupplier.getThread(); //t1
		theEngineSupplierThread = theEngineSupplier.getThread(); //t2
		int numberOfAccessorySuppliers = theConfigReader.getAccessorySuppliersNumber(); //[t3...
		theAccessorySupplierThreads = new Thread[numberOfAccessorySuppliers];
		for (int i = 0; i < numberOfAccessorySuppliers; i++)
		{
			theAccessorySupplierThreads[i] = theAccessorySupplier.getThread(); //...t3]
		}
		theCarWarehouseControllerThread = theCarWarehouseController.getThread(); //t4
		int numberOfDealers = theConfigReader.getDealersNumber(); //[t5...
		theDealerThreads = new Thread[numberOfDealers];
		for (int i = 0; i < numberOfDealers; i++)
		{
			theDealerThreads[i] = theDealerClass.getThread(); //...t5]
		}

		//-----------------------------------------------------------------------------------

		//SWING
		coachworkSlider.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent mouseEvent)
			{
				super.mouseReleased(mouseEvent);
				int theValue = coachworkSlider.getValue();
				theCoachworkSupplier.setTimeout(theValue);
				coachworkProgressBar.setValue(theValue);
				coachworkProgressBar.setString(theValue + "/" + coachworkProgressBar.getMaximum());
			}
		});

		theCoachworkSupplier.addTransactionCounterListener(new TransactionListener()
		                                                   {
			                                                   @Override
			                                                   public void totalTransactions(int counter)
			                                                   {
				                                                   coachworkTotalLabel.setText(String.valueOf(counter));
			                                                   }
		                                                   }
		);

		engineSlider.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent mouseEvent)
			{
				super.mouseReleased(mouseEvent);
				int theValue = engineSlider.getValue();
				theEngineSupplier.setTimeout(theValue);
				engineProgressBar.setValue(theValue);
				engineProgressBar.setString(theValue + "/" + engineProgressBar.getMaximum());
			}
		});

		theEngineSupplier.addTransactionCounterListener(new TransactionListener()
		                                                {
			                                                @Override
			                                                public void totalTransactions(int counter)
			                                                {
				                                                engineTotalLabel.setText(String.valueOf(counter));
			                                                }
		                                                }
		);

		accessorySlider.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent mouseEvent)
			{
				super.mouseReleased(mouseEvent);
				int theValue = accessorySlider.getValue();
				theAccessorySupplier.setTimeout(theValue);
				accessoryProgressBar.setValue(theValue);
				accessoryProgressBar.setString(theValue + "/" + accessoryProgressBar.getMaximum());
			}
		});

		theAccessorySupplier.addTransactionCounterListener(new TransactionListener()
		                                                   {
			                                                   @Override
			                                                   public void totalTransactions(int counter)
			                                                   {
				                                                   accessoryTotalLabel.setText(String.valueOf(counter));
			                                                   }
		                                                   }
		);

		dealerSlider.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent mouseEvent)
			{
				super.mouseReleased(mouseEvent);
				int theValue = dealerSlider.getValue();
				theDealerClass.setTimeout(theValue);
				dealerProgressBar.setValue(theValue);
				dealerProgressBar.setString(theValue + "/" + dealerProgressBar.getMaximum());
			}
		});

		theDealerClass.addTransactionCounterListener(new TransactionListener()
		                                             {
			                                             @Override
			                                             public void totalTransactions(int counter)
			                                             {
				                                             dealerTotalLabel.setText(String.valueOf(counter));
			                                             }
		                                             }
		);


	}

	void launch()
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

		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			//ignore
		}
	}

	void finish()
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

	private void createUIComponents()
	{
		// TODO: place custom component creation code here
	}

}
