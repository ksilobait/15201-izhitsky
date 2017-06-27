package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Client.TheMessageHandler;
import ru.nsu.ccfit.izhitsky.Client.TheXMLMessageHandler;

public interface ServerMessage extends Message
{
	void process(TheMessageHandler theHandler);
	void documentize(TheXMLMessageHandler theHandler);
}
