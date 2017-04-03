package ru.nsu.ccfit.izhitsky.task1;

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
}