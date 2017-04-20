package ru.nsu.ccfit.izhitsky.task1;

import java.io.File;

public class ExtensionFilter implements MyFilter
{
	private String theExtension;

	ExtensionFilter(String theExtension_)
	{
		this.theExtension = theExtension_;
	}

	@Override
	public boolean toCheck(File theFile)
	{
		String fullFileName = theFile.getAbsolutePath();
		String anotherExtension = fullFileName.substring(fullFileName.lastIndexOf('.') + 1);
		return anotherExtension.equals(theExtension);
	}

	@Override
	public String toString()
	{
		return ("." + this.theExtension);
	}

	@Override
	public int hashCode()
	{
		return theExtension.hashCode();
	}

	@Override
	public boolean equals(Object anotherObject)
	{
		boolean b1 = this.getClass().isInstance(anotherObject);
		boolean b2 = this.hashCode() == anotherObject.hashCode();
		return (b1 && b2);
	}
}
