package ru.nsu.ccfit.izhitsky.Client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.izhitsky.Messages.*;
import ru.nsu.ccfit.izhitsky.User;

public class TheMessageHandler
{
	private static final Logger theLogger = LogManager.getLogger(TheMessageHandler.class);

	private MyClient theClient;

	public TheMessageHandler(MyClient theClient_)
	{
		this.theClient = theClient_;
	}

	public void process(TheTextServerMessage theMessage)
	{
		switch (theMessage.getType())
		{
			case SUCCESS:
			{
				theClient.notifyListeners(theMessage);
				theLogger.info("text success is processing " + theMessage.getClass());
				break;
			}
			case ERROR:
			{
				TheClientMessage undeliveredMessage = theClient.takeTextMessage();
				theMessage.setData(undeliveredMessage.getData());
				theClient.notifyListeners(theMessage);
				theLogger.info("text error is processing " + theMessage.getClass());
				break;
			}
		}
	}

	public void process(TheUserServerMessage theMessage)
	{
		if (theClient.isLoggedIn())
		{
			theClient.notifyListeners(theMessage);
			switch (theMessage.getType())
			{
				//case DEFAULT: do nothing
				case LOGIN:
				{
					theClient.addUser(theMessage.getUser());
					theLogger.info("user login is processing " + theMessage.getClass());
					break;
				}
				case LOGOUT:
				{
					theClient.deleteUser(theMessage.getUser());
					theLogger.info("user logout is processing " + theMessage.getClass());
					break;
				}
			}
		}
	}

	public void process(TheActionServerMessage theMessage)
	{
		switch (theMessage.getRequestType())
		{
			case LIST:
			{
				switch (theMessage.getStatus())
				{
					case SUCCESS:
					{
						for (User user : theMessage.getUsers())
						{
							theClient.addUser(user);
							theLogger.info("LIST::SUCCESS is processing " + theMessage.getClass());
						}
						theClient.notifyListeners(theMessage);
						break;
					}
					case ERROR:
					{
						theLogger.info("LIST::ERROR is processing " + theMessage.getClass());
						break;
					}
				}
				break;
			}
			case LOGIN:
			{
				switch (theMessage.getStatus())
				{
					case SUCCESS:
					{
						theClient.notifyListeners(theMessage);
						theClient.setSessionID(theMessage.getSessionID());
						theClient.setLoggedIn(true);
						theLogger.info("LOGIN:SUCCESS is processing " + theMessage.getClass());

						TheRequestClientMessage msg = new TheRequestClientMessage(TheRequestClientMessage.REQUEST_TYPE.LIST);
						msg.setSessionID(theMessage.getSessionID());
						theClient.sendMessage(msg);
						break;
					}
					case ERROR:
					{
						theClient.notifyListeners(theMessage);
						theLogger.info("LOGIN:ERROR is processing " + theMessage.getClass());
						break;
					}
				}
				break;
			}
			case LOGOUT:
			{
				switch (theMessage.getStatus())
				{
					case SUCCESS:
					{
						theClient.clearUsers();
						theClient.notifyListeners(theMessage);
						theClient.interrupt();
						theClient.setLoggedIn(false);
						theLogger.info("LOGOUT::SUCCESS is processing " + theMessage.getClass());
						break;
					}
					case ERROR:
					{
						theClient.notifyListeners(theMessage);
						theLogger.info("LOGOUT::ERROR is processing " + theMessage.getClass());
						break;
					}
				}
				break;
			}
		}
	}
}
