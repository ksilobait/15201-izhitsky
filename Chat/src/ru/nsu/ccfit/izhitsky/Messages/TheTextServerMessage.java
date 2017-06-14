package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Client.MessageHandler;

public class TheTextServerMessage implements ServerMessage
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

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	@Override
	public void process(MessageHandler handler)
	{
		handler.process(this, status);
	}

}
