package ru.nsu.ccfit.izhitsky.task1;

import java.io.File;

public class ExtensionFilter implements MyFilter
{
	private String theExtension;

	public ExtensionFilter(String theExtension_)
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
}
