package ru.nsu.ccfit.izhitsky.GUI;

import ru.nsu.ccfit.izhitsky.Client.MyClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIIntro
{
	private JPanel mainPanel;
	private JRadioButton javaObjectsRadioButton;
	private JRadioButton XMLRadioButton;
	private JTextField serverIPTextField;
	private JTextField serverPortTextField;
	private JButton LAUNCHCLIENTButton;
	private JButton EXITButton;
	private int port;
	private String IP;
	private MyClient.TYPE type;
	private MyClient theClient;

	public GUIIntro()
	{
		JFrame theFrame = new JFrame("Chat Client Intro Window");
		theFrame.setContentPane(mainPanel);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.pack();
		theFrame.setVisible(true);
		type = MyClient.TYPE.OS;

		LAUNCHCLIENTButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				IP = serverIPTextField.getText();
				boolean b1_0 = IP.toLowerCase().equals("localhost");
				boolean b1_1 = IP.matches("((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
				boolean b1 = b1_0 || b1_1; //valid IP

				boolean b2 = true;
				try
				{
					port = Integer.parseInt(serverPortTextField.getText());
					if (port < 1024 || port > 49151)
					{
						b2 = false;
					}
				}
				catch (NumberFormatException e)
				{
					b2 = false;
				}

				if (b1 && b2)
				{
					theClient = new MyClient(IP, port, type);
					theClient.setConnectionListener(new MyClient.ConnectionListener()
					{
						@Override
						public void handle(boolean connected)
						{
							if (connected)
							{
								new GUIClient(theClient);
								theFrame.dispose();
							}
							else
							{
								JOptionPane.showMessageDialog(mainPanel, "Cannot connect to the server");
							}
						}
					});

					Thread connectionThread = new Thread(new LocalRunnable(), "Connection thread");
					connectionThread.start();
				}
				else
				{
					if (!b1 && !b2)
						JOptionPane.showMessageDialog(mainPanel, "Invalid IP and port");
					else if (!b1)
						JOptionPane.showMessageDialog(mainPanel, "Invalid IP");
					else //!b2
						JOptionPane.showMessageDialog(mainPanel, "Invalid port");
				}
			}
		});

		javaObjectsRadioButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				type = MyClient.TYPE.OS;
			}
		});

		XMLRadioButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				type = MyClient.TYPE.XML;
			}
		});

		EXITButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				theFrame.dispose();
				System.exit(0);
			}
		});
	}

	//------------------------------------------------------------------------------------

	private class LocalRunnable implements Runnable
	{
		@Override
		public void run()
		{
			theClient.connectToServer();
		}
	}
}
