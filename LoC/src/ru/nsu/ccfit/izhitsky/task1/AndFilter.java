package ru.nsu.ccfit.izhitsky.task1;

import java.io.File;

public class AndFilter implements MyFilter
{
	private MyFilter[] filters;

	AndFilter(MyFilter[] filters_)
	{
		this.filters = filters_;
	}

	@Override
	public boolean toCheck(File theFile)
	{
		for (MyFilter the_filter : this.filters)
		{
			if (!the_filter.toCheck(theFile))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder toReturn = new StringBuilder("&(");

		for (MyFilter f : filters)
		{
			toReturn.append(f.toString());
			toReturn.append(" ");
		}

		toReturn.deleteCharAt(toReturn.length() - 1); //delete last space
		toReturn.append(")");
		return toReturn.toString();
	}

	@Override
	public int hashCode()
	{
		int hash = 0;
		for (MyFilter f : filters)
		{
			hash = hash + f.hashCode();
			hash = (hash * this.getClass().toString().hashCode()) % 100003; //100003 is a prime number
		}
		return hash;
	}

	@Override
	public boolean equals(Object anotherObject)
	{
		boolean b1 = this.getClass().isInstance(anotherObject);
		boolean b2 = this.hashCode() == anotherObject.hashCode();
		return (b1 && b2);
	}
}
