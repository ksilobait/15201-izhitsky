package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Client.MessageHandler;
import ru.nsu.ccfit.izhitsky.Client.MyClient;
import ru.nsu.ccfit.izhitsky.Client.TheXMLMessageHandler;
import ru.nsu.ccfit.izhitsky.User;

import java.io.Serializable;

public class TheUserServerMessage implements ServerMessage, Serializable
{
	public enum STATUS {DEFAULT, LOGIN, LOGOUT}

	private String data;
	private STATUS status;
	private User user;

	public TheUserServerMessage(STATUS status_)
	{
		this.status = status_;
		user = new User();
	}

	@Override
	public void process(MessageHandler theHandler)
	{
		theHandler.process(this);
	}

	@Override
	public void documentize(TheXMLMessageHandler handler) {
		System.out.println("TheUserServerMessage.documentize");
		handler.documentize(this); }

	public void setUserName(String userName)
	{
		user.setName(userName);
	}

	public String getUserName()
	{
		return user.getName();
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getData()
	{
		return data;
	}

	public User getUser()
	{
		return user;
	}

	public void setClientType(MyClient.TYPE type)
	{
		user.setType(type);
	}

	public STATUS getType()
	{
		return status;
	}
}
