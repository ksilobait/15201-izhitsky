package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Client.MessageHandler;

public class TheUserServerMessage implements ServerMessage
{
	public enum STATUS {DEFAULT, LOGIN, LOGOUT}

	private String name;
	private String data;
	private STATUS status;

	public TheUserServerMessage(STATUS status_)
	{
		this.status = status_;
	}

	@Override
	public void process(MessageHandler theHandler)
	{
		theHandler.process(this);
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setData(String data)
	{
		this.data = data;
	}
}
