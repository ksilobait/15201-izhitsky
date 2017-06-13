package ru.nsu.ccfit.izhitsky.Client;

import ru.nsu.ccfit.izhitsky.Client.A_Chat_Client_GUI;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RunnableChatClient implements Runnable
{
	private Socket theSocket;
	//Scanner send = new Scanner(System.in);
	private Scanner in;
	private PrintWriter out;

	RunnableChatClient(Socket X)
	{
		this.theSocket = X;
	}

	@Override
	public void run()
	{
		try
		{
			try
			{
				in = new Scanner(theSocket.getInputStream());
				out = new PrintWriter(theSocket.getOutputStream());
				out.flush();
				checkStream();
			}
			finally
			{
				theSocket.close();
			}
		}
		catch (Exception e)
		{
			System.out.print(e.getMessage());
		}
	}

	private void checkStream()
	{
		while (true)
		{
			receive();
		}
	}

	private void receive()
	{
		if (in.hasNext())
		{
			String message = in.nextLine();

			if (message.contains("#?!"))
			{
				String TEMP1 = message.substring(3);
				TEMP1 = TEMP1.replace("[", "");
				TEMP1 = TEMP1.replace("]","");

				String[] CurrentUsers = TEMP1.split(", ");

				A_Chat_Client_GUI.JL_ONLINE.setListData(CurrentUsers);
			}
			else
			{
				A_Chat_Client_GUI.TA_CONVERSATION.append(message + "\n");
			}
		}
	}

	void send(String X)
	{
		out.println(A_Chat_Client_GUI.UserName + ": " + X);
		out.flush();
		A_Chat_Client_GUI.TF_Message.setText("");
	}

	void disconnect() throws IOException
	{
		out.println(A_Chat_Client_GUI.UserName + " has disconnected.");
		out.flush();
		theSocket.close();
		JOptionPane.showMessageDialog(null, "You disconnected!");
		System.exit(0);
	}
}
