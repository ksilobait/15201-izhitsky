package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Client.TheMessageHandler;
import ru.nsu.ccfit.izhitsky.Client.TheXMLMessageHandler;

import java.io.Serializable;

public class TheTextServerMessage implements ServerMessage, Serializable
{
	public enum STATUS {SUCCESS, ERROR}

	private STATUS status;
	private String errorMessage;
	private String data;

	public TheTextServerMessage(STATUS status_)
	{
		this.status = status_;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public STATUS getType()
	{
		return status;
	}

	@Override
	public void process(TheMessageHandler handler)
	{
		handler.process(this);
	}

	@Override
	public void documentize(TheXMLMessageHandler handler) {
		System.out.println("TheTextServerMessage::documentize");
		handler.documentize(this); }

}
