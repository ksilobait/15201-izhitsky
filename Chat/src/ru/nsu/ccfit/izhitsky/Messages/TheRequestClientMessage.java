package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Client.MyClient;
import ru.nsu.ccfit.izhitsky.Client.TheXMLMessageHandler;
import ru.nsu.ccfit.izhitsky.Server.MyServer;
import ru.nsu.ccfit.izhitsky.Server.ServerUsers.ServerUser;
import ru.nsu.ccfit.izhitsky.User;

import java.io.Serializable;

public class TheRequestClientMessage implements ClientMessage, Serializable
{
	public enum REQUEST_TYPE { LOGIN, LOGOUT, LIST }

	private REQUEST_TYPE requestType;
	private User theUser;
	private int sessionID;


	public TheRequestClientMessage(REQUEST_TYPE type)
	{
		this.requestType = type;
		theUser = new User();
	}

	public void setName(String theName)
	{
		theUser.setName(theName);
	}

	public void setType(MyClient.TYPE theType)
	{
		theUser.setType(theType);
	}

	public REQUEST_TYPE getRequestType()
	{
		return requestType;
	}

	public User getUser()
	{
		return theUser;
	}

	public void setSessionID(int sessionID)
	{
		this.sessionID = sessionID;
	}

	public int getSessionID()
	{
		return sessionID;
	}


	@Override
	public void process(MyServer server, ServerUser handler)
	{
		server.process(this, handler);
	}

	@Override
	public void documentize(TheXMLMessageHandler handler) { handler.documentize(this); }

}
