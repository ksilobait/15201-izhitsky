package ru.nsu.ccfit.izhitsky;

import ru.nsu.ccfit.izhitsky.Client.TEMP_RunnableChatClient;
import ru.nsu.ccfit.izhitsky.Server.MyServer;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.Socket;

public class TEMP_ClientGUI
{
	private String UserName = "Anonymous";
	private static TEMP_RunnableChatClient ChatClient;

	static private JPanel MainWindow;
	private JPanel messagePanel;
	private JPanel nicknamePanel;
	private JPanel buttonsPanel;
	private JPanel conversationPanel;
	private JPanel onlinePanel;
	private JButton B_DISCONNECT;
	private JButton B_CONNECT;
	private JButton B_SEND;
	private JButton B_ABOUT;
	private JTextField TF_Message;
	private JTextArea TA_CONVERSATION;
	private JTextArea JL_ONLINE;
	private JLabel L_Message;
	private JLabel L_Conversation;
	private JScrollPane SP_CONVERSATION;
	private JLabel L_ONLINE;
	private JScrollPane SP_ONLINE;
	private JLabel L_LoggedInAs;
	private JLabel L_LoggedInAsBox;

	public JFrame LogInWindow = new JFrame();
	public JTextField TF_UserNameBox = new JTextField(20);
	private JButton B_ENTER = new JButton("ENTER");
	private JLabel L_EnterUserName = new JLabel("Enter username: ");
	private JPanel P_LogIn = new JPanel();

	public static void main(String[] args)
	{
		JFrame theFrame = new JFrame("The Chat");
		theFrame.setContentPane(MainWindow);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.pack();

		buildMainWindow();
	}

	void buildMainWindow()
	{
		MainWindow.setName(UserName + "'s Chat Box");

		SP_CONVERSATION.setViewportView(TA_CONVERSATION);
		SP_ONLINE.setViewportView(JL_ONLINE);
		L_LoggedInAsBox.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0,0,0)));

		B_DISCONNECT.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						try
						{
							ChatClient.disconnect();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
		);

		B_CONNECT.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						LogInWindow.setTitle("What's your name?");
						LogInWindow.setSize(400, 100);
						LogInWindow.setLocation(250, 200);
						LogInWindow.setResizable(false);
						P_LogIn = new JPanel();
						P_LogIn.add(L_EnterUserName);
						P_LogIn.add(TF_UserNameBox);
						P_LogIn.add(B_ENTER);
						LogInWindow.add(P_LogIn);

						B_ENTER.addActionListener(
								new java.awt.event.ActionListener()
								{
									public void actionPerformed(java.awt.event.ActionEvent evt)
									{
										ACTION_B_ENTER();
									}
								});

						LogInWindow.setVisible(true);
					}
				}
		);

		B_SEND.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						if (!TF_Message.getText().equals(""))
						{
							ChatClient.send(TF_Message.getText());
							TF_Message.requestFocus();
						}
					}
				}
		);

		B_ABOUT.addActionListener(
				new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						JOptionPane.showMessageDialog(null, "Multi-Client CHAT Program");
					}
				}
		);

		MainWindow.setVisible(true);
	}

	private void ACTION_B_ENTER()
	{
		if (!TF_UserNameBox.getText().equals(""))
		{
			UserName = TF_UserNameBox.getText().trim();
			L_LoggedInAsBox.setText(UserName);
			MyServer.currentUsers.add(UserName);
			MainWindow.setName(UserName + "'s Chat Box");
			LogInWindow.setVisible(false);
			B_SEND.setEnabled(true);
			B_DISCONNECT.setEnabled(true);
			B_CONNECT.setEnabled(true);
			Connect();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please enter a name!");
		}
	}

	private void Connect()
	{
		try
		{
			final int PORT = 4444;
			final String HOST = "localhost";
			Socket SOCK = new Socket(HOST, PORT);
			System.out.println("You connected to: " + HOST);

			ChatClient = new TEMP_RunnableChatClient(SOCK);

			//Send Name to add to "OnLine" list
			PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());
			OUT.println(UserName);
			OUT.flush();

			Thread X = new Thread(ChatClient);
			X.start();
		}
		catch (Exception e)
		{
			System.out.print(e.getMessage());
			JOptionPane.showMessageDialog(null, "Server not responding.");
			System.exit(0);
		}
	}

}
