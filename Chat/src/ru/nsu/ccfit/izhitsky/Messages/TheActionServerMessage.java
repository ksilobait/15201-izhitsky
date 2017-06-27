package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Client.MessageHandler;
import ru.nsu.ccfit.izhitsky.Client.TheXMLMessageHandler;
import ru.nsu.ccfit.izhitsky.User;

import java.io.Serializable;
import java.util.List;

public class TheActionServerMessage implements ServerMessage, Serializable
{
	private TheTextServerMessage.STATUS type2;
	private TheRequestClientMessage.REQUEST_TYPE type3;
	private String errorMessage;
	private int sessionID;
	private List<User> users;

	public TheActionServerMessage(TheTextServerMessage.STATUS type2_, TheRequestClientMessage.REQUEST_TYPE type3_)
	{
		this.type2 = type2_;
		this.type3 = type3_;
	}

	public TheTextServerMessage.STATUS getStatus()
	{
		return type2;
	}

	public TheRequestClientMessage.REQUEST_TYPE getRequestType()
	{
		return type3;
	}

	public void setErrorMessage(String mssg)
	{
		this.errorMessage = mssg;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setSessionID(Integer sessionID)
	{
		this.sessionID = sessionID;
	}

	public Integer getSessionID()
	{
		return sessionID;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}

	public List<User> getUsers()
	{
		return users;
	}

	@Override
	public void process(MessageHandler handler)
	{
		handler.process(this);
	}

	@Override
	public void documentize(TheXMLMessageHandler handler) { handler.documentize(this); }

}
