package ru.nsu.ccfit.izhitsky.Messages;

import ru.nsu.ccfit.izhitsky.Server.MyServer;
import ru.nsu.ccfit.izhitsky.Server.ServerHandlers.ServerHandler;

public interface ClientMessage extends Message
{
	void process(MyServer theServer, ServerHandler theHandler); //al
}
