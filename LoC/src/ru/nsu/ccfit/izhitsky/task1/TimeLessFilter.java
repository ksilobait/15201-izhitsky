package ru.nsu.ccfit.izhitsky.task1;

import java.io.File;

public class TimeLessFilter implements MyFilter
{
	private long timeOfLastModify;

	public TimeLessFilter(String timeOfLastModify_)
	{
		this.timeOfLastModify = Long.parseLong(timeOfLastModify_);
	}

	@Override
	public boolean toCheck(File theFile)
	{
		return (theFile.lastModified() < timeOfLastModify);
	}
}
