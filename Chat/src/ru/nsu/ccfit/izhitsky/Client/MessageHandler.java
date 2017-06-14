package ru.nsu.ccfit.izhitsky.Client;

import ru.nsu.ccfit.izhitsky.Messages.TheTextServerMessage;
import ru.nsu.ccfit.izhitsky.Messages.TheUserServerMessage;

public class MessageHandler
{
	private Client theClient;

	public MessageHandler(Client theClient_)
	{
		this.theClient = theClient_;
	}

	public void process(TheTextServerMessage theMessage, TheTextServerMessage.STATUS status)
	{
		switch (status)
		{
			case SUCCESS:
			{
				theClient.notifyListeners(theMessage);
				System.out.println("processing " + theMessage.getClass());
			}
			case ERROR:
			{
				TheTextServerMessage undeliveredMessage = theClient.takeTextMessage();
				theMessage.setData(undeliveredMessage.getData());
				theClient.notifyListeners(theMessage);
				System.out.println("processing " + theMessage.getClass());
			}
		}
	}

	public void process(TheUserServerMessage theMessage)
	{
		if (theClient.loggedIn())
		{
			theClient.notifyListeners(theMessage);
		}
		System.out.println("processing " + theMessage.getClass());
	}

}
