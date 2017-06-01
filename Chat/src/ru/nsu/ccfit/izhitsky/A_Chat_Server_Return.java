package ru.nsu.ccfit.izhitsky;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class A_Chat_Server_Return
{
	//Globals
	Socket SOCK;
	private Scanner INPUT;
	private PrintWriter OUT;
	String MESSAGE = "";

	public A_Chat_Server_Return(Socket X)
	{
		this.SOCK = X;
	}

	public void CheckConnection() throws IOException
	{
		if (!SOCK.isConnected())
		{
			for (int i = 1; i <= A_Chat_Server.ConnectionArray.size(); i++)
			{
				if (A_Chat_Server.ConnectionArray.get(i) == SOCK)
				{
					A_Chat_Server.ConnectionArray.remove(i);
				}
			}

			for (int i = 1; i <= A_Chat_Server.ConnectionArray.size(); i++)
			{
				Socket TEMP_SOCK = (Socket) A_Chat_Server.ConnectionArray.get(i - 1);
				PrintWriter TEMP_OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
				TEMP_OUT.println(TEMP_SOCK.getLocalAddress().getHostName() + " disconnected!");
				TEMP_OUT.flush();
				//Show disconnection at SERVER
				System.out.println(TEMP_SOCK.getLocalAddress().getHostName() + " disconnected!");
			}
		}
	}
	//----------------------------------------------------------------------------------

	public void run()
	{
		try
		{
			try
			{
				INPUT = new Scanner(SOCK.getInputStream());
				OUT = new PrintWriter(SOCK.getOutputStream());

				while (true)
				{
					CheckConnection();

					if (!INPUT.hasNext())
					{
						return;
					}

					MESSAGE = INPUT.nextLine();

					System.out.println("Client said: " + MESSAGE);

					for (int i = 1; i <= A_Chat_Server.ConnectionArray.size(); i++)
					{
						//Note: If necessary take CAST below out. I added it to make it compile.
						Socket TEMP_SOCK = (Socket) A_Chat_Server.ConnectionArray.get(i - 1);
						PrintWriter TEMP_OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
						TEMP_OUT.println(MESSAGE);
						TEMP_OUT.flush();
						System.out.println("Sent to: " + TEMP_SOCK.getLocalAddress().getHostName());
					} //close for loop

				} //close while loop
			} //close inner try
			finally
			{
				SOCK.close();
			}
		} //close outer try
		catch (Exception X)
		{
			System.out.print(X);
		}
	}

}
