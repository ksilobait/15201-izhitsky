package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Client.TheXMLMessageHandler;
import ru.nsu.ccfit.izhitsky.Server.MyServer;
import ru.nsu.ccfit.izhitsky.Server.ServerUsers.ServerUser;

import java.io.Serializable;

public class TheClientMessage implements ClientMessage, Serializable
{
	private int sessionID;
	private String data;

	@Override
	public void process(MyServer theServer, ServerUser theConnection)
	{
		theServer.process(this, theConnection);
	}

	@Override
	public void documentize(TheXMLMessageHandler handler) { handler.documentize(this); }

	public int getSessionID()
	{
		return sessionID;
	}

	public void setSessionID(int sessionID)
	{
		this.sessionID = sessionID;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}
}
