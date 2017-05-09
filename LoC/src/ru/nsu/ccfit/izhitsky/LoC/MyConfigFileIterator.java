package ru.nsu.ccfit.izhitsky.LoC;

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
	MyConfigFileIterator(String configFileName) throws Exception //transfers FileReader to BufferedReader
	{
		FileReader theFileReader;
		try
		{
			theFileReader = new FileReader(configFileName);
		}
		catch (FileNotFoundException e)
		{
			throw new Exception("File not found");
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

		return (this.lastReadLine != null); //does iteration have more elements
	}

	@Override
	public String next()
	{
		return this.lastReadLine;
	}
}