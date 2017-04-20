package ru.nsu.ccfit.izhitsky.task1;

import java.io.File;

class MyController
{
	//FIELDS
	private MyFilter[] filters;
	private MyStatistic statistic;

	//METHODS
	MyController(String configFileName) throws Exception
	{
		MyFilterParser theParser = new MyFilterParser();
		filters = theParser.toParseConfigFile(configFileName);
		statistic = new MyStatistic();
	}

	MyStatistic getStatistic()
	{
		return statistic;
	}

	MyFilter[] getFilters()
	{
		return filters;
	}


	void toGatherStatistic(File theDirectory) throws Exception
	{
		File[] theFiles = theDirectory.listFiles();
		if (theFiles == null)
		{
			throw new Exception("no such directory in " + theDirectory.getAbsolutePath());
		}

		for (File theFile : theFiles)
		{
			if (theFile.isDirectory())
			{
				toGatherStatistic(theFile);
			}
			else
			{
				boolean checkedAtLeastOnce = false;
				for (MyFilter theFilter : filters)
				{
					if (theFilter.toCheck(theFile))
					{
						statistic.toAdd(theFilter, theFile);
						checkedAtLeastOnce = true;
					}
				}

				if (!checkedAtLeastOnce)
				{
					statistic.toAdd(theFile);
				}
			}
		}
	}
}
