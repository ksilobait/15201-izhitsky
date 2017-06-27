package ru.nsu.ccfit.izhitsky.Server.ServerUsers;

import org.w3c.dom.Document;
import ru.nsu.ccfit.izhitsky.Messages.Message;
import ru.nsu.ccfit.izhitsky.Messages.ServerMessage;
import ru.nsu.ccfit.izhitsky.Messages.TheTextServerMessage;
import ru.nsu.ccfit.izhitsky.Messages.TheUserServerMessage;
import ru.nsu.ccfit.izhitsky.Server.MyServer;
import ru.nsu.ccfit.izhitsky.User;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ServerUser
{
	private User theUser = new User();

	MyServer theServer;
	Socket theSocket;
	Thread readerThread;
	Thread writerThread;
	BlockingQueue<Message> messagesQueue;
	ArrayBlockingQueue<Document> docMessagesQueue;
	AtomicInteger sessionIDGenerator = new AtomicInteger();
	int sessionID;

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

	public void setUser(User theUser)
	{
		this.theUser = theUser;
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

	void blockUser() throws IOException
	{
		if ((theUser.getName() != null) && theServer.getUserArray().contains(theUser))
		{
			theServer.getUserArray().remove(theUser);
		}

		TheUserServerMessage mssg = new TheUserServerMessage(TheUserServerMessage.STATUS.LOGOUT);
		mssg.setUserName(getUserName());
		theServer.getServerMessages().add(mssg);
		new Thread(() -> {theServer.disableConnection(this);}).start();
		writerThread.interrupt();
		closeSocket();

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
