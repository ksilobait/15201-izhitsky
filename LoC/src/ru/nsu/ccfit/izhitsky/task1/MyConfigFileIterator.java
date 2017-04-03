package ru.nsu.ccfit.izhitsky.task1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

//TODO: think of this function
public class MyConfigFileIterator implements Iterator<String>
{
	//FIELDS
	private BufferedReader configFileReader;
	private String lastReadLine;

	//METHODS
	public MyConfigFileIterator(String configFileName) //transfers FileReader to BufferedReader
	{
		FileReader theFileReader = null;
		try
		{
			theFileReader = new FileReader(configFileName);
		}
		catch (FileNotFoundException e)
		{
			//TODO: throw the exception further or handle it;
		}
		this.configFileReader = new BufferedReader(theFileReader);
	}

	@Override
	public boolean hasNext()
	{
		try
		{
			this.lastReadLine = this.configFileReader.readLine();
			if (this.lastReadLine != null)
			{
				this.lastReadLine = this.lastReadLine.replaceAll("^\\s+", ""); //spaces in the beginning
				this.lastReadLine = this.lastReadLine.replaceAll("\\s+", " "); //spaces in the middle
				this.lastReadLine = this.lastReadLine.replaceAll("\\s+$", ""); //spaces in the end
			}
		}
		catch (IOException e)
		{
			System.out.println("ERROR in hasNext");
		}

		boolean isIterationHasMoreElements = (this.lastReadLine != null);
		return isIterationHasMoreElements;
	}

	@Override
	public String next()
	{
		return this.lastReadLine;
	}
}
