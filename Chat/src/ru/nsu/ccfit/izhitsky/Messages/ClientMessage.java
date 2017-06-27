package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Client.TheXMLMessageHandler;
import ru.nsu.ccfit.izhitsky.Server.MyServer;
import ru.nsu.ccfit.izhitsky.Server.ServerUsers.ServerUser;

public interface ClientMessage extends Message
{
	void process(MyServer theServer, ServerUser theHandler);
	void documentize(TheXMLMessageHandler theXMLMessageHandler);
}
