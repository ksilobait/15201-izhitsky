package ru.nsu.ccfit.izhitsky.factory;

import ru.nsu.ccfit.izhitsky.factory.Suppliers.*;
import ru.nsu.ccfit.izhitsky.factory.Warehouses.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUIFactory extends JFrame
{
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

	GUIFactory(TheFactory theFactory)
	{
		MyConfigReader theConfigReader = theFactory.getTheConfigReader();

		JFrame theFrame = new JFrame("The Factory");
		theFrame.setContentPane(mainPanel);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.pack();
		theFrame.setVisible(true);

		//the car assembler (сборка машин)
		engineWarehouseProgressBar.setMaximum(theConfigReader.getEngineWarehouseSize());
		coachworkWarehouseProgressBar.setMaximum(theConfigReader.getCoachworkWarehouseSize());
		accessoryWarehouseProgressBar.setMaximum(theConfigReader.getAccessoryWarehouseSize());
		dealerWarehouseProgressBar.setMaximum(theConfigReader.getCarWarehouseSize());

		//suppliers (поставщики кузовов, деталей и аксессуаров)
		coachwork1Label.setText(String.valueOf(theConfigReader.getCoachworkSupplierTimeout()));
		coachworkSlider.setValue(theConfigReader.getCoachworkSupplierTimeout());
		engine1Label.setText(String.valueOf(theConfigReader.getEngineSupplierTimeout()));
		engineSlider.setValue(theConfigReader.getEngineSupplierTimeout());
		accessory1Label.setText(String.valueOf(theConfigReader.getAccessorySupplierTimeout()));
		accessorySlider.setValue(theConfigReader.getAccessorySupplierTimeout());

		//dealers residence (продажа машин)
		dealer1Label.setText(String.valueOf(theConfigReader.getDealerTimeout()));

		coachworkSlider.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent mouseEvent)
			{
				super.mouseReleased(mouseEvent);
				int theValue = coachworkSlider.getValue();
				theFactory.theCoachworkSupplier.setTimeout(theValue);
				coachwork1Label.setText(String.valueOf(theValue));
			}
		});

		theFactory.theCoachworkSupplier.addTransactionCounterListener(new TransactionListener()
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
				theFactory.theEngineSupplier.setTimeout(theValue);
				engine1Label.setText(String.valueOf(theValue));
			}
		});

		theFactory.theEngineSupplier.addTransactionCounterListener(new TransactionListener()
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
				theFactory.theAccessorySupplier.setTimeout(theValue);
				accessory1Label.setText(String.valueOf(theValue));
			}
		});

		theFactory.theAccessorySupplier.addTransactionCounterListener(new TransactionListener()
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
				theFactory.theDealerClass.setTimeout(theValue);
				dealer1Label.setText(String.valueOf(theValue));
			}
		});

		theFactory.theDealerClass.addTransactionCounterListener(new TransactionListener()
		                                             {
			                                             @Override
			                                             public void totalTransactions(int counter)
			                                             {
				                                             dealerTotalLabel.setText(String.valueOf(counter));
			                                             }
		                                             }
		);

		theFactory.carAssembler.getTheCoachworkWarehouse().addCapacityListener(new WarehouseCapacityListener()
		{
			@Override
			public void getCurrentCapacity(int size)
			{
				coachworkWarehouseProgressBar.setValue(size);
				coachworkWarehouseProgressBar.setString(size + "/" + coachworkWarehouseProgressBar.getMaximum());
			}
		});

		theFactory.carAssembler.getTheEngineWarehouse().addCapacityListener(new WarehouseCapacityListener()
		{
			@Override
			public void getCurrentCapacity(int size)
			{
				engineWarehouseProgressBar.setValue(size);
				engineWarehouseProgressBar.setString(size + "/" + engineWarehouseProgressBar.getMaximum());
			}
		});

		theFactory.carAssembler.getTheAccessoryWarehouse().addCapacityListener(new WarehouseCapacityListener()
		{
			@Override
			public void getCurrentCapacity(int size)
			{
				accessoryWarehouseProgressBar.setValue(size);
				accessoryWarehouseProgressBar.setString(size + "/" + accessoryWarehouseProgressBar.getMaximum());
			}
		});

		theFactory.carAssembler.getTheCarWarehouse().addCapacityListener(new WarehouseCapacityListener()
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
				theFactory.launch();
			}
		});

		STOPAndCLOSEButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				theFactory.finish();
				System.exit(0);
			}
		});
	}

	private void createUIComponents()
	{
		// TODO: place custom component creation code here
	}

}
