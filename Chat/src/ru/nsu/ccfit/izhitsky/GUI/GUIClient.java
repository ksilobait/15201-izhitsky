package ru.nsu.ccfit.izhitsky.GUI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.Client.MessageHandler;
import ru.nsu.ccfit.izhitsky.Client.MyClient;
import ru.nsu.ccfit.izhitsky.Messages.*;
import ru.nsu.ccfit.izhitsky.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUIClient implements MessageHandler
{
	private static final Logger theLogger = LogManager.getLogger(GUIClient.class);

	private String UserName = "Anonymous";
	private MyClient theClient;

	//private static TEMP_RunnableChatClient ChatClient;

	private JFrame theFrame;

	private JPanel mainPanel;
	private JPanel messagePanel;
	private JPanel nicknamePanel;
	private JPanel buttonsPanel;
	private JPanel conversationPanel;
	private JPanel onlinePanel;
	private JButton B_DISCONNECT;
	private JButton B_CONNECT;
	private JButton B_SEND;
	private JTextField TF_Message;
	private JTextArea TA_CONVERSATION;
	private JTextArea JL_ONLINE;
	private JLabel L_Message;
	private JLabel L_Conversation;
	private JLabel L_ONLINE;
	private JLabel L_LoggedInAs;
	private JTextField nameField;
	private JLabel L_LoggedInAsBox;

	public GUIClient(MyClient theClient_)
	{
		this.theClient = theClient_;

		theFrame = new JFrame("Chat Client Main Window");
		theFrame.setContentPane(mainPanel);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.pack();
		theFrame.setVisible(true);

		theClient.addConnection(new MyClient.MyConnection()
		{
			@Override
			public void process(ServerMessage mssg)
			{
				mssg.process(GUIClient.this);
			}
		});


		theClient.setConnectionListener(new MyClient.ConnectionListener()
		{
			@Override
			public void handle(boolean connected)
			{
				if (!connected)
				{
					JOptionPane.showMessageDialog(theFrame, "server connection error, you've been disconnected");
					nameField.setEditable(true);
					B_CONNECT.setEnabled(true);

					B_DISCONNECT.setEnabled(false);
					B_SEND.setEnabled(false);
				}
			}
		});

		B_CONNECT.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				if (!theClient.isSocketAlive())
				{
					theClient.connectToServer();
				}

				String name = nameField.getText();
				TheRequestClientMessage theMessage = new TheRequestClientMessage(TheRequestClientMessage.REQUEST_TYPE.LOGIN);
				theClient.setName(name);
				theMessage.setName(name);
				theMessage.setType(theClient.getType());
				theClient.sendMessage(theMessage);
			}
		});

		//try to disconnect
		B_DISCONNECT.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				TheRequestClientMessage mssg = new TheRequestClientMessage(TheRequestClientMessage.REQUEST_TYPE.LOGOUT);
				mssg.setSessionID(theClient.getSessionID());
				theClient.sendMessage(mssg);

				B_SEND.setEnabled(false);
				B_DISCONNECT.setEnabled(false);
				TF_Message.setEnabled(false);
				theFrame.dispose();
				System.exit(0);
			}
		});


		B_SEND.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				TheClientMessage mssg = new TheClientMessage();
				mssg.setData(TF_Message.getText());
				mssg.setSessionID(theClient.getSessionID());
				theClient.sendMessage(mssg);

				TF_Message.setText("");
			}
		});
	}

	private void refreshUserList()
	{
		List<User> users = theClient.getUsers();
		JL_ONLINE.setText("");
		for (User user : users)
		{
			JL_ONLINE.append(user.getName() + "\n");
		}
	}

	public void process(TheUserServerMessage mssg)
	{
		switch (mssg.getType())
		{
			case DEFAULT: //display the message
			{
				TA_CONVERSATION.append(mssg.getUserName() + " > " + mssg.getData() + "\n");
				theLogger.info("GUIClient::process::TheUserServerMessage.Default printed");
				break;
			}
			case LOGIN:
			{
				refreshUserList();
				TA_CONVERSATION.append("#" + mssg.getUserName() + " connected\n");
				theLogger.info("user " + mssg.getUserName() + " was added to userList");
				break;
			}
			case LOGOUT:
			{
				refreshUserList();
				TA_CONVERSATION.append("#" + mssg.getUserName() + " disconnected" + "\n");
				theLogger.info("user " + mssg.getUserName() + " was removed from userList");
				break;
			}
		}
	}

	public void process(TheTextServerMessage mssg)
	{
		switch (mssg.getType())
		{
			case SUCCESS:
			{
				TA_CONVERSATION.append(theClient.getName() + "> " + theClient.takeTextMessage().getData() + "\n");
				theLogger.info("TheTextServerMessage.SUCCESS: message was delivered");
				break;
			}
			case ERROR:
			{
				TA_CONVERSATION.append("! " + mssg.getErrorMessage() + "\n");
				theLogger.info("message wasn't delivered");
				break;
			}
		}
	}

	public void process(TheActionServerMessage mssg)
	{
		switch (mssg.getRequestType())
		{
			case LOGIN:
			{
				switch (mssg.getStatus())
				{
					case SUCCESS:
					{
						TA_CONVERSATION.setText("");
						TF_Message.setEnabled(true);
						nameField.setText(theClient.getName());
						nameField.setEditable(false);
						B_DISCONNECT.setEnabled(true);
						B_CONNECT.setEnabled(false);
						B_SEND.setEnabled(true);
						theLogger.info("the form is ready to exchange messages");
						break;
					}
					case ERROR:
					{
						JOptionPane.showMessageDialog(theFrame, mssg.getErrorMessage());
						theLogger.info("user was informed about loginError");
						break;
					}
				}
				break;
			}
			case LOGOUT:
			{
				switch (mssg.getStatus())
				{
					case SUCCESS:
					{
						refreshUserList();
						nameField.setEditable(true);
						B_CONNECT.setEnabled(true);
						theClient.disconnectFromServer();
						theLogger.info("you were disconnected");
						break;
					}
					case ERROR:
					{
						B_DISCONNECT.setEnabled(true);
						B_SEND.setEnabled(true);
						TF_Message.setEnabled(true);
						JOptionPane.showMessageDialog(theFrame, mssg.getErrorMessage());
						theLogger.info("disconnection was unsuccessful");
						break;
					}
				}
				break;
			}
			case LIST:
			{
				switch (mssg.getStatus())
				{
					case SUCCESS:
					{
						refreshUserList();
						theLogger.info("refreshed online users");
						break;
					}
					case ERROR:
					{
						theLogger.info("online users cannot be displayed");
						break;
					}
				}
				break;
			}
		}
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
		mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
		messagePanel = new JPanel();
		messagePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(messagePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		L_Message = new JLabel();
		L_Message.setText("Message");
		messagePanel.add(L_Message, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		TF_Message = new JTextField();
		TF_Message.setEnabled(false);
		messagePanel.add(TF_Message, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(buttonsPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		B_DISCONNECT = new JButton();
		B_DISCONNECT.setBackground(new Color(-12517376));
		B_DISCONNECT.setEnabled(false);
		B_DISCONNECT.setForeground(new Color(-1));
		B_DISCONNECT.setText("DISCONNECT");
		buttonsPanel.add(B_DISCONNECT, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		B_SEND = new JButton();
		B_SEND.setBackground(new Color(-16777151));
		B_SEND.setEnabled(false);
		B_SEND.setForeground(new Color(-1));
		B_SEND.setText("SEND");
		buttonsPanel.add(B_SEND, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		B_CONNECT = new JButton();
		B_CONNECT.setBackground(new Color(-16760576));
		B_CONNECT.setForeground(new Color(-1));
		B_CONNECT.setHideActionText(true);
		B_CONNECT.setText("CONNECT");
		buttonsPanel.add(B_CONNECT, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		nicknamePanel = new JPanel();
		nicknamePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(nicknamePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		L_LoggedInAs = new JLabel();
		L_LoggedInAs.setText("Logged in as");
		nicknamePanel.add(L_LoggedInAs, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		nameField = new JTextField();
		nicknamePanel.add(nameField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		conversationPanel = new JPanel();
		conversationPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(conversationPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		L_Conversation = new JLabel();
		L_Conversation.setText("Conversation");
		conversationPanel.add(L_Conversation, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JScrollPane scrollPane1 = new JScrollPane();
		conversationPanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		TA_CONVERSATION = new JTextArea();
		TA_CONVERSATION.setEditable(false);
		TA_CONVERSATION.setLineWrap(true);
		scrollPane1.setViewportView(TA_CONVERSATION);
		onlinePanel = new JPanel();
		onlinePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel.add(onlinePanel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		L_ONLINE = new JLabel();
		L_ONLINE.setText("Currently Online");
		onlinePanel.add(L_ONLINE, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JScrollPane scrollPane2 = new JScrollPane();
		onlinePanel.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		JL_ONLINE = new JTextArea();
		JL_ONLINE.setEditable(false);
		scrollPane2.setViewportView(JL_ONLINE);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$()
	{
		return mainPanel;
	}
}
