package ru.nsu.ccfit.izhitsky;

import ru.nsu.ccfit.izhitsky.Client.MyClient;

import java.io.Serializable;

public class User implements Serializable
{
	private String name;
	private MyClient.TYPE type;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setType(MyClient.TYPE type)
	{
		this.type = type;
	}

	public MyClient.TYPE getType()
	{
		return type;
	}
}
