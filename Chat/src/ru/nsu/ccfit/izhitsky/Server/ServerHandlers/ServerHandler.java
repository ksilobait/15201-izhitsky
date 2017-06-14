package ru.nsu.ccfit.izhitsky.Server.ServerHandlers;

import ru.nsu.ccfit.izhitsky.Messages.ServerMessage;
import ru.nsu.ccfit.izhitsky.Messages.TheTextServerMessage;
import ru.nsu.ccfit.izhitsky.Server.MyServer;
import ru.nsu.ccfit.izhitsky.User;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public abstract class ServerHandler
{
	private MyServer theServer;
	private Socket theSocket;
	private Thread readerThread;
	private Thread writerThread;
	private BlockingQueue<ServerMessage> messagesQueue;
	private User theUser;
	private int sessionID;

	public String getUserName()
	{
		return theUser.getName();
	}

	public int getSessionID()
	{
		return sessionID;
	}

	public User getUser()
	{
		return theUser;
	}

	//----------------------------------------------------------------------------------

	public void launch()
	{
		readerThread.start();
		writerThread.start();
	}

	public void sendMessage(ServerMessage mssg)
	{
		messagesQueue.add(mssg);
	}

	public void sendOutOfMemoryError()
	{
		TheTextServerMessage errorMessage = new TheTextServerMessage(TheTextServerMessage.STATUS.ERROR);
		errorMessage.setErrorMessage("Message was not sent : too big");
		messagesQueue.add(errorMessage);
	}


	void blockUser()
	{
		theServer.getUserArray().remove(theUser);
	}

	public void interruptWriter()
	{
		writerThread.interrupt();
	}

	public void closeSocket() throws IOException
	{
		theSocket.close();
	}


}
