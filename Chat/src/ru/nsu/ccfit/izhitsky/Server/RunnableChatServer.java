package ru.nsu.ccfit.izhitsky.Server;

import ru.nsu.ccfit.izhitsky.Server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RunnableChatServer implements Runnable
{
	private Socket SOCK;

	public RunnableChatServer(Socket X)
	{
		this.SOCK = X;
	}

	public void checkConnection() throws IOException
	{
		if (!SOCK.isConnected())
		{
			for (int i = 1; i <= Server.connectionArray.size(); i++) //TODO ???
			{
				if (Server.connectionArray.get(i) == SOCK)
				{
					Server.connectionArray.remove(i);
				}
			}

			for (int i = 0; i < Server.connectionArray.size(); i++)
			{
				Socket tempSocket = Server.connectionArray.get(i);
				PrintWriter out = new PrintWriter(tempSocket.getOutputStream());
				out.println(tempSocket.getLocalAddress().getHostName() + " disconnected!");
				out.flush();
				//Show disconnection at SERVER
				System.out.println(tempSocket.getLocalAddress().getHostName() + " disconnected!");
			}
		}
	}

	@Override
	public void run()
	{
		try
		{
			try
			{
				Scanner in = new Scanner(SOCK.getInputStream());
				//out = ???

				while (true)
				{
					checkConnection();

					if (!in.hasNext())
					{
						return;
					}

					String message = in.nextLine();

					System.out.println("Client said: " + message);

					for (int i = 0; i < Server.connectionArray.size(); i++)
					{
						Socket TEMP_SOCK = Server.connectionArray.get(i);
						PrintWriter TEMP_OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
						TEMP_OUT.println(message);
						TEMP_OUT.flush();
						System.out.println("Sent to: " + TEMP_SOCK.getLocalAddress().getHostName());
					}
				}
			}
			finally
			{
				SOCK.close();
			}
		}
		catch (Exception e)
		{
			System.out.print(e.getMessage());
		}
	}

}
