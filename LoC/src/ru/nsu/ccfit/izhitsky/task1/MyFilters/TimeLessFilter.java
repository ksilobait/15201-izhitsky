package ru.nsu.ccfit.izhitsky.task1.MyFilters;

import ru.nsu.ccfit.izhitsky.task1.MyFilter;

import java.io.File;

public class TimeLessFilter implements MyFilter
{
	private long timeOfLastModify;

	public TimeLessFilter(String timeOfLastModify_) throws Exception
	{
		long num;
		try
		{
			num = Long.parseLong(timeOfLastModify_);
		}
		catch (NumberFormatException e)
		{
			throw new Exception("TimeLessFilter faced not a number parameter");
		}

		this.timeOfLastModify = num;
	}

	@Override
	public boolean toCheck(File theFile)
	{
		return (theFile.lastModified() < timeOfLastModify);
	}

	@Override
	public String toString()
	{
		return "<" + timeOfLastModify;
	}

	@Override
	public int hashCode()
	{
		return Long.hashCode(timeOfLastModify);
	}

	@Override
	public boolean equals(Object anotherObject)
	{
		boolean b1 = this.getClass().isInstance(anotherObject);
		boolean b2 = this.hashCode() == anotherObject.hashCode();
		return (b1 && b2);
	}
}
