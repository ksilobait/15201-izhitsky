package ru.nsu.ccfit.izhitsky.task1;

import java.io.File;

public class AndFilter implements MyFilter
{
	private MyFilter[] filters;

	public AndFilter(MyFilter[] filters_)
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
}
