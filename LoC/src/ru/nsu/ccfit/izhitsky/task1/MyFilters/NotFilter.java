package ru.nsu.ccfit.izhitsky.task1.MyFilters;

import ru.nsu.ccfit.izhitsky.task1.MyFilter;

import java.io.File;

public class NotFilter implements MyFilter
{
	private MyFilter theFilter;

	public NotFilter(MyFilter theFilter_)
	{
		this.theFilter = theFilter_;
	}

	@Override
	public boolean toCheck(File theFile)
	{
		return !(theFilter.toCheck(theFile));
	}

	@Override
	public String toString()
	{
		return "!(" + theFilter.toString() + ")";
	}

	@Override
	public int hashCode()
	{
		return (theFilter.hashCode() * this.getClass().toString().hashCode() % 100003); //100003 is a prime number
	}

	@Override
	public boolean equals(Object anotherObject)
	{
		boolean b1 = this.getClass().isInstance(anotherObject);
		boolean b2 = this.hashCode() == anotherObject.hashCode();
		return (b1 && b2);
	}
}