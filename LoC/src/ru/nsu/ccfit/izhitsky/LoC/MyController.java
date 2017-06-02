package ru.nsu.ccfit.izhitsky.LoC;

import java.io.File;

class MyController
{
	private MyFilter[] filters;
	private MyStatistic statistic;

	//--------------------------------------------------------------------------------------

	MyController(String configFileName) throws Exception
	{
		MyFilterParser theParser = new MyFilterParser();
		filters = theParser.toParseConfigFile(configFileName); //parse
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
				toGatherStatistic(theFile); //recursive plunge
			}
			else //is file
			{
				boolean checkedAtLeastOnce = false;
				for (MyFilter theFilter : filters)
				{
					if (theFilter.toCheck(theFile))
					{
						statistic.toAdd(theFilter, theFile); //sub counters
						checkedAtLeastOnce = true;
					}
				}

				if (checkedAtLeastOnce)
				{
					statistic.toAdd(theFile); //main counter
				}
			}
		}
	}
}
