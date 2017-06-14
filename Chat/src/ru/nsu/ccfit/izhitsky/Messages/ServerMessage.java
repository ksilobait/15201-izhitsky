package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Client.MessageHandler;

public interface ServerMessage extends Message
{
	void process(MessageHandler theHandler);
}
