package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Client.MessageHandler;
import ru.nsu.ccfit.izhitsky.Client.TheXMLMessageHandler;

public interface ServerMessage extends Message
{
	void process(MessageHandler theHandler);
	void documentize(TheXMLMessageHandler theHandler);
}
