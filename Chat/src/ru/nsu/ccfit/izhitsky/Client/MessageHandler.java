package ru.nsu.ccfit.izhitsky.Client;

import ru.nsu.ccfit.izhitsky.Messages.TheActionServerMessage;
import ru.nsu.ccfit.izhitsky.Messages.TheTextServerMessage;
import ru.nsu.ccfit.izhitsky.Messages.TheUserServerMessage;

public interface MessageHandler
{
	void process(TheTextServerMessage theMessage);
	void process(TheUserServerMessage theMessage);
	void process(TheActionServerMessage theMessage);
}
