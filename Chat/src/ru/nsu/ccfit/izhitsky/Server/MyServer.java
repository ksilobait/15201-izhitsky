package ru.nsu.ccfit.izhitsky.Server;

import ru.nsu.ccfit.izhitsky.Messages.ServerMessage;
import ru.nsu.ccfit.izhitsky.Messages.TheClientMessage;
import ru.nsu.ccfit.izhitsky.Messages.TheTextServerMessage;
import ru.nsu.ccfit.izhitsky.Messages.TheUserServerMessage;
import ru.nsu.ccfit.izhitsky.Server.ServerHandlers.*;
import ru.nsu.ccfit.izhitsky.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class MyServer
{
	private Thread osListenerThread;
	private Thread xmlListenerThread;
	private Thread speaker;
	private ServerSocket osSocket;
	private ServerSocket xmlSocket;
	private final Object arrayLock = new Object();

	private List<User> userArray;
	private List<ServerHandler> connectionArray;
	private BlockingQueue<ServerMessage> serverMessages;
	private BlockingQueue<ServerMessage> messageQueue;

	public static void main(String[] args)
	{
		MyServer theServer = new MyServer();
		theServer.launch();

		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (!in.readLine().equals("exit"))
			{
				//continue
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}

		theServer.terminate();
	}

	public MyServer()
	{
		int osPort = 10671;
		int xmlPort = 10672;
		try
		{
			osSocket = new ServerSocket(osPort);
			xmlSocket = new ServerSocket(xmlPort);
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}

	private void launch()
	{
		osListenerThread = new Thread(new OSRunnable(), "OS thread");
		osListenerThread.start();

		xmlListenerThread = new Thread(new XMLRunnable(), "XML thread");
		xmlListenerThread.start();

		speaker = new Thread(new SpeakerRunnable(), "Speaker thread");
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
				for (ServerHandler hdnlr : connectionArray)
				{
					hdnlr.interruptWriter();
					hdnlr.closeSocket();
				}
			}

		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	public void disconnectTheClient(ServerHandler theHandler)
	{
		theHandler.interruptWriter();
		userArray.remove(theHandler.getUser());
		synchronized (arrayLock)
		{
			connectionArray.remove(theHandler);
		}
		TheUserServerMessage mssg = new TheUserServerMessage(TheUserServerMessage.STATUS.LOGOUT);
		mssg.setName(theHandler.getName());
		serverMessages.add(mssg);
	}


	public void process(TheClientMessage theMessage, ServerHandler theHandler)
	{
		if (theMessage.getSessionID() == theHandler.getSessionID())
		{
			theHandler.sendMessage(new TheTextServerMessage(TheTextServerMessage.STATUS.SUCCESS));
			System.out.println("successful text response sent to {}" + theHandler.getUserName());

			TheUserServerMessage messageToSend = new TheUserServerMessage();
			messageToSend.setName(theHandler.getUserName());
			messageToSend.setData(theMessage.getData());
			serverMessages.add(messageToSend);

			System.out.println("text message was sent to everybody");
		}
		else
		{
			TheTextServerMessage mssg = new TheTextServerMessage(TheTextServerMessage.STATUS.ERROR);
			mssg.setErrorMessage("session IDs are not equal");
			theHandler.sendMessage(mssg);
			System.out.println("session IDs are not equal");
		}
	}

	//GETTERS AND SETTERS------------------------------------------------------------------

	public List<User> getUserArray()
	{
		return userArray;
	}

	//CLASSES------------------------------------------------------------------------------

	private class OSRunnable implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					Socket theSocket = osSocket.accept();
					OSHandler theHandler = new OSHandler(theSocket);

					synchronized (arrayLock)
					{
						connectionArray.add(theHandler);
					}

					theHandler.launch();
				}
			}
			catch (IOException e)
			{
				System.out.println(e.getMessage());
			}
		}
	}

	private class XMLRunnable implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					Socket theSocket = xmlSocket.accept();
					XMLHandler theHandler = new XMLHandler(theSocket);

					synchronized (arrayLock)
					{
						connectionArray.add(theHandler);
					}

					theHandler.launch();
				}
			}
			catch (IOException e)
			{
				System.out.println(e.getMessage());
			}
		}
	}

	private class SpeakerRunnable implements Runnable
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
							String s1 = ((TheUserServerMessage) mssg).getName();
							for (ServerHandler hdnlr : connectionArray)
							{
								String s2 = hdnlr.getUserName();
								if (!s1.equals(s2)) //send to everyone but me
								{
									hdnlr.sendMessage(mssg);
								}
							}
						}
					}
					else
					{
						synchronized (arrayLock)
						{
							for (ServerHandler hdnlr : connectionArray)
							{
								hdnlr.sendMessage(mssg);
							}
						}
					}
				}
			}
			catch (InterruptedException e)
			{
				System.out.println(e.getMessage());
			}
		}
	}

	/*
	{
		ServerSocket server = new ServerSocket(OSport);
		System.out.println("Server started waiting for clients...");

		while (true)
		{
			Socket SOCK = server.accept();
			connectionArray.add(SOCK);

			System.out.println("Client connection from: " + SOCK.getLocalAddress().getHostAddress());

			AddUserName(SOCK);

			RunnableChatServer CHAT = new RunnableChatServer(SOCK);
			Thread X = new Thread(CHAT);
			X.start();
		}

	}

	public static void AddUserName(Socket X) throws IOException
	{
		Scanner in = new Scanner(X.getInputStream());
		String userName = in.nextLine();
		currentUsers.add(userName);

		for (int i = 0; i < Server.connectionArray.size(); i++)
		{
			Socket tempSocket = Server.connectionArray.get(i);
			PrintWriter out = new PrintWriter(tempSocket.getOutputStream());
			out.println("#?!" + currentUsers); //command "add users"
			out.flush();
		}
	}*/
}
