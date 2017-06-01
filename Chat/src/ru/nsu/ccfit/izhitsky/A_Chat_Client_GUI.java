package ru.nsu.ccfit.izhitsky;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.Socket;

public class A_Chat_Client_GUI
{
	//Globals
	private static A_Chat_Client ChatClient;
	public static String UserName = "Anonymous";

	//GUI Globals - Main Window
	public static JFrame MainWindow = new JFrame();
	private static JButton B_ABOUT = new JButton();
	private static JButton B_CONNECT = new JButton();
	private static JButton B_DISCONNECT = new JButton();
	private static JButton B_HELP = new JButton();
	private static JButton B_SEND = new JButton();
	private static JLabel L_Message = new JLabel("Message: ");
	private static JTextField TF_Message = new JTextField(20);
	private static JLabel L_Conversation = new JLabel();
	public static JTextArea TA_CONVERSATION = new JTextArea();
	private static JScrollPane SP_CONVERSATION = new JScrollPane();
	private static JLabel L_ONLINE = new JLabel();
	public static JList JL_ONLINE = new JList();
	private static JScrollPane SP_ONLINE = new JScrollPane();
	private static JLabel L_LoggedInAs = new JLabel();
	private static JLabel L_LoggedInAsBox = new JLabel();

	//GUI Globals - LogIn Window
	public static JFrame LogInWindow = new JFrame();
	private static JTextField TF_UserNameBox = new JTextField(20);
	private static JButton B_ENTER = new JButton("ENTER");
	private static JLabel L_EnterUserName = new JLabel("Enter username: ");
	private static JPanel P_LogIn = new JPanel();

	//----------------------------------------------------------------------------------
	public static void main(String[] args)
	{
		BuildMainWindow();
		Initialize();
	}
	//----------------------------------------------------------------------------------

	public static void Connect()
	{
		try
		{
			final int PORT = 444;
			final String HOST = "Galactica";
			Socket SOCK = new Socket(HOST, PORT);
			System.out.println("You connected to: " + HOST);

			ChatClient = new A_Chat_Client(SOCK);

			//Send Name to add to "OnLine" list
			PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());
			OUT.println(UserName);
			OUT.flush();

			Thread X = new Thread(ChatClient);
			X.start();
		}
		catch (Exception X)
		{
			System.out.print(X);
			JOptionPane.showMessageDialog(null, "Server not responding.");
			System.exit(0);
		}
	}
	//----------------------------------------------------------------------------------
	public static void Initialize()
	{
		B_SEND.setEnabled(false);
		B_DISCONNECT.setEnabled(false);
		B_CONNECT.setEnabled(true);
	}
	//----------------------------------------------------------------------------------
	public static void BuildLogInWindow()
	{
		
	}

}
