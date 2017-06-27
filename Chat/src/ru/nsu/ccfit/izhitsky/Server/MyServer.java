package ru.nsu.ccfit.izhitsky.Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.Messages.*;
import ru.nsu.ccfit.izhitsky.Server.ServerUsers.*;
import ru.nsu.ccfit.izhitsky.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MyServer
{
	private static final Logger theLogger = LogManager.getLogger(MyServer.class);

	private Thread osListenerThread;
	private Thread xmlListenerThread;
	private Thread speaker;
	private ServerSocket osSocket;
	private ServerSocket xmlSocket;
	private final Object arrayLock = new Object();

	private List<User> userArray;
	private List<ServerUser> connectionArray;
	private BlockingQueue<ServerMessage> serverMessages;
	private BlockingQueue<ServerMessage> messageQueue;

	public static void main(String[] args)
	{
		MyServer theServer = new MyServer();
		theServer.launch();
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("server launched; type 'exit' here to exit");
			while (!in.readLine().equals("exit"))
			{
				//continue
			}
		}
		catch (IOException e)
		{
			theLogger.error(e.getMessage());
		}

		theServer.terminate();
	}

	public MyServer()
	{
		int osPort = 10671;
		int xmlPort = 10672;
		System.out.println("objects are on port #" + osPort);
		System.out.println("XML is on port #" + xmlPort);

		userArray = new ArrayList<>();
		connectionArray = new ArrayList<>();
		serverMessages = new ArrayBlockingQueue<>(10000);
		messageQueue = new ArrayBlockingQueue<>(10);

		try
		{
			osSocket = new ServerSocket(osPort);
			xmlSocket = new ServerSocket(xmlPort);
		}
		catch (IOException e)
		{
			theLogger.error(e.getMessage());
			System.exit(-1);
		}
	}

	private void launch()
	{
		osListenerThread = new Thread(new ObjectReader(), "OS thread");
		osListenerThread.start();

		xmlListenerThread = new Thread(new XMLReader(), "XML thread");
		xmlListenerThread.start();

		speaker = new Thread(new Writer(), "Speaker thread");
		speaker.start();
	}

	private void terminate()
	{
		try
		{
			osSocket.close();
			xmlSocket.close();
			speaker.interrupt();
			synchronized (arrayLock)
			{
				for (ServerUser hdnlr : connectionArray)
				{
					hdnlr.interruptWriter();
					hdnlr.closeSocket();
				}
			}
			osListenerThread.join();
			xmlListenerThread.join();
			speaker.join();
		}
		catch (IOException e)
		{
			theLogger.error(e.getMessage());
		}
		catch (InterruptedException e)
		{
			theLogger.error(e.getMessage());
		}
	}

	public void disconnectTheClient(ServerUser theHandler)
	{
		theHandler.interruptWriter();
		userArray.remove(theHandler.getUser());
		synchronized (arrayLock)
		{
			disableConnection(theHandler);
		}
		TheUserServerMessage mssg = new TheUserServerMessage(TheUserServerMessage.STATUS.LOGOUT);
		mssg.setUserName(theHandler.getUserName());
		serverMessages.add(mssg);
	}

	public void disableConnection(ServerUser cnnctn)
	{
		synchronized (arrayLock)
		{
			connectionArray.remove(cnnctn);
		}
	}


	public void process(TheClientMessage theMessage, ServerUser theHandler)
	{
		if (theMessage.getSessionID() == theHandler.getSessionID())
		{
			TheTextServerMessage mssgtsnd = new TheTextServerMessage(TheTextServerMessage.STATUS.SUCCESS);
			mssgtsnd.setData(theMessage.getData());
			theHandler.sendMessage(mssgtsnd);
			theLogger.info("text response was sent to " + theHandler.getUserName());

			TheUserServerMessage messageToSend = new TheUserServerMessage(TheUserServerMessage.STATUS.DEFAULT);
			messageToSend.setUserName(theHandler.getUserName());
			messageToSend.setData(theMessage.getData());
			serverMessages.add(messageToSend);

			theLogger.info("text message was sent to everybody");
		}
		else
		{
			TheTextServerMessage mssg = new TheTextServerMessage(TheTextServerMessage.STATUS.ERROR);
			mssg.setErrorMessage("session IDs are not equal");
			theHandler.sendMessage(mssg);
			theLogger.info("session IDs are not equal");
		}
	}

	public void process(TheRequestClientMessage theMessage, ServerUser theHandler)
	{
		switch (theMessage.getRequestType())
		{
			case LOGIN:
			{
				User user = theMessage.getUser();
				if (userArray.contains(user))
				{
					TheActionServerMessage response = new TheActionServerMessage(TheTextServerMessage.STATUS.ERROR, TheRequestClientMessage.REQUEST_TYPE.LOGIN);
					response.setErrorMessage("user name is already in use");
					theHandler.sendMessage(response);
					theLogger.info("user name is already in use");
				}
				else
				{
					theHandler.setUser(user);
					userArray.add(user);
					TheActionServerMessage response = new TheActionServerMessage(TheTextServerMessage.STATUS.SUCCESS, TheRequestClientMessage.REQUEST_TYPE.LOGIN);
					response.setSessionID(theHandler.getSessionID());
					theHandler.sendMessage(response);

					for (Object mssg : messageQueue.toArray())
					{
						theHandler.sendMessage((ServerMessage) mssg);
					}
					theLogger.info("added new user: " + user.getName() + " with sessionID#" + theHandler.getSessionID());

					TheUserServerMessage mssg = new TheUserServerMessage(TheUserServerMessage.STATUS.LOGIN);
					mssg.setUserName(user.getName());
					mssg.setClientType(user.getType());
					serverMessages.add(mssg);
				}
				break;
			}
			case LOGOUT:
			{
				if (theMessage.getSessionID() != theHandler.getSessionID())
				{
					TheActionServerMessage mssg = new TheActionServerMessage(TheTextServerMessage.STATUS.ERROR, TheRequestClientMessage.REQUEST_TYPE.LOGOUT);
					mssg.setErrorMessage("session IDs are not equal");
					theHandler.sendMessage(mssg);
					theLogger.info("in logout request session IDs are not equal");
				}
				else
				{
					theHandler.sendMessage(new TheActionServerMessage(TheTextServerMessage.STATUS.SUCCESS, TheRequestClientMessage.REQUEST_TYPE.LOGOUT));
					theLogger.info("successful logout response sent to user " + theHandler.getUserName());
					userArray.remove(theHandler.getUser());

					TheUserServerMessage mssg = new TheUserServerMessage(TheUserServerMessage.STATUS.LOGOUT);
					mssg.setUserName(theHandler.getUserName());
					serverMessages.add(mssg);

					theHandler.interruptWriter();
					synchronized (arrayLock)
					{
						disableConnection(theHandler);
					}
					theLogger.info("sent user logout message to everyone");
				}
				break;
			}
			case LIST:
			{
				if (theMessage.getSessionID() != theHandler.getSessionID())
				{
					TheActionServerMessage mssg = new TheActionServerMessage(TheTextServerMessage.STATUS.ERROR, TheRequestClientMessage.REQUEST_TYPE.LIST);
					mssg.setErrorMessage("session IDs are not equal");
					theHandler.sendMessage(mssg);
					theLogger.info("in list users request session IDs are not equal");
				}
				else
				{
					TheActionServerMessage mssg = new TheActionServerMessage(TheTextServerMessage.STATUS.SUCCESS, TheRequestClientMessage.REQUEST_TYPE.LIST);
					mssg.setUsers(userArray);
					theHandler.sendMessage(mssg);
					theLogger.info("sent list users " + userArray + " to " + theHandler.getUserName());
				}
				break;
			}
		}
	}

	//GETTERS AND SETTERS------------------------------------------------------------------

	public List<User> getUserArray()
	{
		return userArray;
	}

	public BlockingQueue<ServerMessage> getServerMessages()
	{
		return serverMessages;
	}

	//CLASSES------------------------------------------------------------------------------

	private class ObjectReader implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					Socket theSocket = osSocket.accept();
					OSUser theUser = new OSUser(MyServer.this ,theSocket);

					synchronized (arrayLock)
					{
						connectionArray.add(theUser);
					}

					theUser.launch();
				}
			}
			catch (IOException e)
			{
				theLogger.error(e.getMessage());
			}
		}
	}

	private class XMLReader implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					Socket theSocket = xmlSocket.accept();
					XMLUser theHandler = new XMLUser(MyServer.this ,theSocket);

					synchronized (arrayLock)
					{
						connectionArray.add(theHandler);
					}

					theHandler.launch();
				}
			}
			catch (IOException e)
			{
				theLogger.error(e.getMessage());
			}
		}
	}

	private class Writer implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				while (!Thread.interrupted())
				{
					ServerMessage mssg = serverMessages.take();
					if (mssg instanceof TheUserServerMessage)
					{
						while (!messageQueue.offer(mssg))
						{
							messageQueue.take();
						}
						synchronized (arrayLock)
						{
							for (ServerUser hndlr : connectionArray)
							{
								String s1 = ((TheUserServerMessage) mssg).getUserName();
								String s2 = hndlr.getUserName();
								if (s1 != null && s2 != null && !s1.equals(s2)) //send to everyone but me
								{
									hndlr.sendMessage(mssg);
								}
							}
						}
					}
					else
					{
						synchronized (arrayLock)
						{
							for (ServerUser hdnlr : connectionArray)
							{
								hdnlr.sendMessage(mssg);
							}
						}
					}
				}
			}
			catch (InterruptedException e)
			{
				theLogger.error(e.getMessage());
			}
		}
	}
}
