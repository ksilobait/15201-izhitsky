package ru.nsu.ccfit.izhitsky.Server.ServerHandlers;

import ru.nsu.ccfit.izhitsky.Messages.ClientMessage;
import ru.nsu.ccfit.izhitsky.Server.MyServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class OSHandler extends ServerHandler
{
	private Socket theSocket;
	private MyServer theServer;

	public OSHandler(MyServer theServer_, Socket theSocket_)
	{
		this.theSocket = theSocket_;
		this.theServer = theServer_;
	}

	private class ReaderThread
	{
		ReaderThread()
		{
			try
			{
				ObjectInputStream inputStream = new ObjectInputStream(theSocket.getInputStream());
				while (!Thread.interrupted())
				{
					try
					{
						ClientMessage mssg = (ClientMessage) inputStream.readObject();
						mssg.process(theServer, this);
					}
					catch (ClassNotFoundException e)
					{
						System.out.println("message has an unknown type; user blocked");
						blockUser();
					}
					catch (OutOfMemoryError e)
					{
						System.out.println("message is too big");
						sendOutOfMemoryError();
					}
				}
			}
			catch (IOException e)
			{
				System.out.println("socket was closed");
				theServer.disconnectTheClient(this);
			}
		}
	}
}
