package ru.nsu.ccfit.izhitsky.LoC.MyFilters;

import ru.nsu.ccfit.izhitsky.LoC.MyFilter;
import java.io.File;

public class TimeGreaterFilter implements MyFilter
{
	private long timeOfLastModify;

	//--------------------------------------------------------------------------------------

	public TimeGreaterFilter(String timeOfLastModify_) throws Exception
	{
		long num;
		try
		{
			num = Long.parseLong(timeOfLastModify_);
		}
		catch (NumberFormatException e)
		{
			throw new Exception("TimeGreaterFilter faced not a number parameter");
		}

		this.timeOfLastModify = num;
	}

	@Override
	public boolean toCheck(File theFile)
	{
		return (theFile.lastModified() > timeOfLastModify);
	}

	@Override
	public String toString()
	{
		return ">" + timeOfLastModify;
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

