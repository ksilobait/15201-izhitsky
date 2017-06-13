package ru.nsu.ccfit.izhitsky.factory;

import ru.nsu.ccfit.izhitsky.factory.Suppliers.*;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	//SWING
	public JPanel mainPanel;
	private JSlider coachworkSlider;
	private JSlider engineSlider;
	private JSlider accessorySlider;
	private JSlider dealerSlider;
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
	private JButton STOPAndCLOSEButton;
	private JPanel buttonsPanel;
	private JLabel coachwork1Label;
	private JLabel engine1Label;
	private JLabel engine2Label;
	private JLabel coachwork2Label;
	private JLabel accessory1Label;
	private JLabel accessory2Label;
	private JLabel dealer1Label;
	private JLabel dealer2Label;

	TheFactory(MyConfigReader theConfigReader)
	{
		JFrame theFrame = new JFrame("The Factory");
		theFrame.setContentPane(mainPanel);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.pack();
		theFrame.setVisible(true);

		//the car assembler (сборка машин)
		carAssembler = new CarAssembler(theConfigReader.getThreadPoolSize(), theConfigReader.getTaskQueueSize());
		carAssembler.setEngineWarehouse(new EngineWarehouse(theConfigReader.getEngineWarehouseSize())); //src1
		carAssembler.setCoachworkWarehouse(new CoachworkWarehouse(theConfigReader.getCoachworkWarehouseSize())); //src2
		carAssembler.setAccessoryWarehouse(new AccessoryWarehouse(theConfigReader.getAccessoryWarehouseSize())); //src3
		carAssembler.setCarWarehouse(new CarWarehouse(theConfigReader.getCarWarehouseSize())); //dest
		engineWarehouseProgressBar.setMaximum(theConfigReader.getEngineWarehouseSize());
		coachworkWarehouseProgressBar.setMaximum(theConfigReader.getCoachworkWarehouseSize());
		accessoryWarehouseProgressBar.setMaximum(theConfigReader.getAccessoryWarehouseSize());
		dealerWarehouseProgressBar.setMaximum(theConfigReader.getCarWarehouseSize());

		//final controller (контроллер склада готовых изделий)
		CarWarehouseController theCarWarehouseController = new CarWarehouseController(carAssembler.getTheCarWarehouse());
		theCarWarehouseController.setTheAssembler(carAssembler);
		carAssembler.getTheCarWarehouse().setTheController(theCarWarehouseController);

		//suppliers (поставщики кузовов, деталей и аксессуаров)
		EngineSupplier theEngineSupplier = new EngineSupplier(carAssembler.getTheEngineWarehouse(), theConfigReader.getEngineSupplierTimeout());
		CoachworkSupplier theCoachworkSupplier = new CoachworkSupplier(carAssembler.getTheCoachworkWarehouse(), theConfigReader.getCoachworkSupplierTimeout());
		AccessorySupplier theAccessorySupplier = new AccessorySupplier(carAssembler.getTheAccessoryWarehouse(), theConfigReader.getAccessorySupplierTimeout());
		coachwork1Label.setText(String.valueOf(theConfigReader.getCoachworkSupplierTimeout()));
		coachworkSlider.setValue(theConfigReader.getCoachworkSupplierTimeout());
		engine1Label.setText(String.valueOf(theConfigReader.getEngineSupplierTimeout()));
		engineSlider.setValue(theConfigReader.getEngineSupplierTimeout());
		accessory1Label.setText(String.valueOf(theConfigReader.getAccessorySupplierTimeout()));
		accessorySlider.setValue(theConfigReader.getAccessorySupplierTimeout());

		//dealers residence (продажа машин)
		DealerClass theDealerClass = new DealerClass(carAssembler.getTheCarWarehouse(), theConfigReader.getDealerTimeout());
		dealer1Label.setText(String.valueOf(theConfigReader.getDealerTimeout()));

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
				coachwork1Label.setText(String.valueOf(theValue));
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
				engine1Label.setText(String.valueOf(theValue));
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
				accessory1Label.setText(String.valueOf(theValue));
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
				dealer1Label.setText(String.valueOf(theValue));
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

		carAssembler.getTheCoachworkWarehouse().addCapacityListener(new WarehouseCapacityListener()
		{
			@Override
			public void getCurrentCapacity(int size)
			{
				coachworkWarehouseProgressBar.setValue(size);
				coachworkWarehouseProgressBar.setString(size + "/" + coachworkWarehouseProgressBar.getMaximum());
			}
		});

		carAssembler.getTheEngineWarehouse().addCapacityListener(new WarehouseCapacityListener()
		{
			@Override
			public void getCurrentCapacity(int size)
			{
				engineWarehouseProgressBar.setValue(size);
				engineWarehouseProgressBar.setString(size + "/" + engineWarehouseProgressBar.getMaximum());
			}
		});

		carAssembler.getTheAccessoryWarehouse().addCapacityListener(new WarehouseCapacityListener()
		{
			@Override
			public void getCurrentCapacity(int size)
			{
				accessoryWarehouseProgressBar.setValue(size);
				accessoryWarehouseProgressBar.setString(size + "/" + accessoryWarehouseProgressBar.getMaximum());
			}
		});

		carAssembler.getTheCarWarehouse().addCapacityListener(new WarehouseCapacityListener()
		{
			@Override
			public void getCurrentCapacity(int size)
			{
				dealerWarehouseProgressBar.setValue(size);
				dealerWarehouseProgressBar.setString(size + "/" + dealerWarehouseProgressBar.getMaximum());
			}
		});


		//BUTTONS----------------------------------------------------------------------------

		STARTButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				STARTButton.setEnabled(false);
				STOPAndCLOSEButton.setEnabled(true);
				launch();
			}
		});

		STOPAndCLOSEButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				finish();
				System.exit(0);
			}
		});
	}

	//----------------------------------------------------------------------------------

	private void launch()
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

	private void finish()
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

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$()
	{
		mainPanel = new JPanel();
		mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
		suppliersPanel = new JPanel();
		suppliersPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 5, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(suppliersPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		suppliersPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "SUPPLIERS TIMEOUTS"));
		final JLabel label1 = new JLabel();
		label1.setText("Coachwork");
		suppliersPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label2 = new JLabel();
		label2.setText("Engine");
		suppliersPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label3 = new JLabel();
		label3.setText("Accessory");
		suppliersPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label4 = new JLabel();
		label4.setText("Dealer");
		suppliersPanel.add(label4, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		coachworkSlider = new JSlider();
		coachworkSlider.setMaximum(10000);
		coachworkSlider.setMinimum(0);
		coachworkSlider.setToolTipText("timeout (in ms) for coachwork supplier");
		suppliersPanel.add(coachworkSlider, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		engineSlider = new JSlider();
		engineSlider.setMaximum(10000);
		engineSlider.setMinimum(0);
		engineSlider.setToolTipText("timeout (in ms) for engine supplier");
		suppliersPanel.add(engineSlider, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		accessorySlider = new JSlider();
		accessorySlider.setMaximum(10000);
		accessorySlider.setMinimum(0);
		accessorySlider.setToolTipText("timeout (in ms) for accessory supplier");
		suppliersPanel.add(accessorySlider, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		dealerSlider = new JSlider();
		dealerSlider.setMaximum(10000);
		dealerSlider.setMinimum(0);
		dealerSlider.setToolTipText("timeout (in ms) for coachwork dealer");
		suppliersPanel.add(dealerSlider, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		coachworkTotalLabel = new JLabel();
		coachworkTotalLabel.setText("-9999");
		coachworkTotalLabel.setToolTipText("coachworks made from the start");
		suppliersPanel.add(coachworkTotalLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		engineTotalLabel = new JLabel();
		engineTotalLabel.setText("-9999");
		engineTotalLabel.setToolTipText("engines made from the start");
		suppliersPanel.add(engineTotalLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		accessoryTotalLabel = new JLabel();
		accessoryTotalLabel.setText("-9999");
		accessoryTotalLabel.setToolTipText("accessories made from the start");
		suppliersPanel.add(accessoryTotalLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		dealerTotalLabel = new JLabel();
		dealerTotalLabel.setText("-9999");
		dealerTotalLabel.setToolTipText("cars bought from the start");
		suppliersPanel.add(dealerTotalLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		coachwork2Label = new JLabel();
		coachwork2Label.setText("/10000");
		coachwork2Label.setToolTipText("max timeout (in ms)");
		suppliersPanel.add(coachwork2Label, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		engine2Label = new JLabel();
		engine2Label.setText("/10000");
		engine2Label.setToolTipText("max timeout (in ms)");
		suppliersPanel.add(engine2Label, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		accessory2Label = new JLabel();
		accessory2Label.setText("/10000");
		accessory2Label.setToolTipText("max timeout (in ms)");
		suppliersPanel.add(accessory2Label, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		dealer2Label = new JLabel();
		dealer2Label.setText("/10000");
		dealer2Label.setToolTipText("max timeout (in ms)");
		suppliersPanel.add(dealer2Label, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		coachwork1Label = new JLabel();
		coachwork1Label.setText("-9999");
		coachwork1Label.setToolTipText("timeout (in ms) for coachwork supplier");
		suppliersPanel.add(coachwork1Label, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		engine1Label = new JLabel();
		engine1Label.setText("-9999");
		engine1Label.setToolTipText("timeout (in ms) for engine supplier");
		suppliersPanel.add(engine1Label, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		accessory1Label = new JLabel();
		accessory1Label.setText("-9999");
		accessory1Label.setToolTipText("timeout (in ms) for accessory supplier");
		suppliersPanel.add(accessory1Label, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		dealer1Label = new JLabel();
		dealer1Label.setText("-9999");
		dealer1Label.setToolTipText("timeout (in ms) for coachwork dealer");
		suppliersPanel.add(dealer1Label, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		warehousesPanel = new JPanel();
		warehousesPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(warehousesPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		warehousesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-4473925)), "WAREHOUSES CAPACITY"));
		final JLabel label5 = new JLabel();
		label5.setText("Coachwork");
		warehousesPanel.add(label5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label6 = new JLabel();
		label6.setText("Engine");
		warehousesPanel.add(label6, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label7 = new JLabel();
		label7.setText("Accessory");
		warehousesPanel.add(label7, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label8 = new JLabel();
		label8.setText("Car Storage");
		warehousesPanel.add(label8, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		coachworkWarehouseProgressBar = new JProgressBar();
		coachworkWarehouseProgressBar.setString("0/0");
		coachworkWarehouseProgressBar.setStringPainted(true);
		coachworkWarehouseProgressBar.setToolTipText("number of coachworks in its warehouse");
		warehousesPanel.add(coachworkWarehouseProgressBar, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		engineWarehouseProgressBar = new JProgressBar();
		engineWarehouseProgressBar.setString("0/0");
		engineWarehouseProgressBar.setStringPainted(true);
		engineWarehouseProgressBar.setToolTipText("number of engines in its warehouse");
		warehousesPanel.add(engineWarehouseProgressBar, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		accessoryWarehouseProgressBar = new JProgressBar();
		accessoryWarehouseProgressBar.setString("0/0");
		accessoryWarehouseProgressBar.setStringPainted(true);
		accessoryWarehouseProgressBar.setToolTipText("number of accessories in its warehouse");
		warehousesPanel.add(accessoryWarehouseProgressBar, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		dealerWarehouseProgressBar = new JProgressBar();
		dealerWarehouseProgressBar.setString("0/0");
		dealerWarehouseProgressBar.setStringPainted(true);
		dealerWarehouseProgressBar.setToolTipText("number of cars in its warehouse");
		warehousesPanel.add(dealerWarehouseProgressBar, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(buttonsPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		STARTButton = new JButton();
		STARTButton.setBackground(new Color(-16760576));
		STARTButton.setForeground(new Color(-1));
		STARTButton.setHideActionText(false);
		STARTButton.setText("START");
		buttonsPanel.add(STARTButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		STOPAndCLOSEButton = new JButton();
		STOPAndCLOSEButton.setBackground(new Color(-12517376));
		STOPAndCLOSEButton.setEnabled(false);
		STOPAndCLOSEButton.setForeground(new Color(-1));
		STOPAndCLOSEButton.setHideActionText(false);
		STOPAndCLOSEButton.setText("STOP and CLOSE");
		buttonsPanel.add(STOPAndCLOSEButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return mainPanel;
	}
}
