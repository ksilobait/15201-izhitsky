package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Server.MyServer;
import ru.nsu.ccfit.izhitsky.Server.ServerHandlers.ServerHandler;

public class TheClientMessage implements ClientMessage
{
	private int sessionID;
	private String data;

	@Override
	public void process(MyServer theServer, ServerHandler theHandler)
	{
		theServer.process(this, theHandler);
	}

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
}
