package ru.nsu.ccfit.izhitsky.task1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class MyConfigFileIterator implements Iterator<String>
{
	//FIELDS
	private BufferedReader configFileReader;
	private String lastReadLine;

	//METHODS
	public MyConfigFileIterator(String configFileName)
	{
		FileReader configFile = null;
		try
		{
			configFile = new FileReader(configFileName);
		}
		catch (FileNotFoundException e)
		{
			//TODO: throw the exception further or handle it;
		}
		configFileReader = new BufferedReader(configFile);
	}

	@Override
	public boolean hasNext()
	{
		try
		{
			lastReadLine = configFileReader.readLine();
			if (lastReadLine != null)
			{
				lastReadLine = lastReadLine.replaceAll("^\\s+", "").replaceAll("\\s+$", "").replaceAll("\\s+", " ");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return lastReadLine != null;
	}

	@Override
	public String next()
	{
		return lastReadLine;
	}
}
