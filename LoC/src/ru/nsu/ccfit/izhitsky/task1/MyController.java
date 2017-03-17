package ru.nsu.ccfit.izhitsky.task1;

import java.io.File;

public class MyController
{
	//FIELDS
	private MyFilter[] filters;
	private MyStatistic statistic;

	//METHODS
	public MyController(String configFileName)
	{
		filters = MyFilter.toParseIntoFilters(configFileName);
		statistic = new MyStatistic();
	}

	public MyStatistic getStatistic()
	{
		return statistic;
	}

	public MyFilter[] getFilters()
	{
		return filters;
	}


	public void toGatherStatistic(File theDirectory)
	{
		File[] theFiles = theDirectory.listFiles();
		if (theFiles == null)
		{
			//TODO
			//throw new NoSuchDirectory(theDirectory.getAbsolutePath());
		}

		for (File theFile : theFiles)
		{
			if (theFile.isDirectory())
			{
				toGatherStatistic(theFile);
			}
			else
			{
				for (MyFilter theFilter : filters)
				{
					if (theFilter.toCheck(theFile))
					{
						statistic.toAdd(theFilter, theFile);
					}
				}
			}
		}
	}
}
