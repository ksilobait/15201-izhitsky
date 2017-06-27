package ru.nsu.ccfit.izhitsky.Server.ServerUsers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.Messages.ClientMessage;
import ru.nsu.ccfit.izhitsky.Messages.ServerMessage;
import ru.nsu.ccfit.izhitsky.Server.MyServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class OSUser extends ServerUser
{
	private static final Logger theLogger = LogManager.getLogger(OSUser.class);

	public OSUser(MyServer theServer_, Socket theSocket_)
	{
		this.theSocket = theSocket_;
		this.theServer = theServer_;
		sessionID = sessionIDGenerator.getAndIncrement();
		messagesQueue = new ArrayBlockingQueue<>(10000);

		readerThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					ObjectInputStream inputStream = new ObjectInputStream(theSocket.getInputStream());
					while (!Thread.interrupted())
					{
						try
						{
							ClientMessage mssg = (ClientMessage) inputStream.readObject();
							mssg.process(theServer, OSUser.this);
						}
						catch (ClassNotFoundException e)
						{
							theLogger.error("message has an unknown type; user was blocked");
							blockUser();
						}
						catch (OutOfMemoryError e)
						{
							theLogger.error("message is too big; user was blocked");
							sendOutOfMemoryError();
							blockUser();
						}
					}
				}
				catch (IOException e)
				{
					theLogger.error("socket was closed");
					theServer.disconnectTheClient(OSUser.this);
				}
			}
		});

		writerThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					ObjectOutputStream outputStream = new ObjectOutputStream(theSocket.getOutputStream());
					while (!Thread.interrupted())
					{
						ServerMessage mssg = (ServerMessage) messagesQueue.take();
						outputStream.writeObject(mssg);
						outputStream.flush();
					}
				}
				catch (IOException e)
				{
					theLogger.error("socket was closed");
				}
				catch (InterruptedException e)
				{
					theLogger.error("socket was interrupted");
				}
			}
		});
	}
}
