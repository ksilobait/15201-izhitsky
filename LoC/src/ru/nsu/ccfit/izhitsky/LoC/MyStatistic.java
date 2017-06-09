package ru.nsu.ccfit.izhitsky.LoC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class MyStatistic
{
	class MyRecord
	{
		private int totalFiles;
		private int totalLines;

		MyRecord()
		{
			totalFiles = 0;
			totalLines = 0;
		}

		void toIncrementFileCountByOne()
		{
			totalFiles++;
		}

		void toIncrementLineCountBy(int k)
		{
			totalLines = totalLines + k;
		}

		int getTotalLines()
		{
			return totalLines;
		}

		int getTotalFiles()
		{
			return totalFiles;
		}
	}

	//--------------------------------------------------------------------------------------

	private HashMap<MyFilter, MyRecord> statisticMap;
	private int totalFiles;
	private int totalLines;

	//--------------------------------------------------------------------------------------

	int getTotalFiles()
	{
		return totalFiles;
	}

	int getTotalLines()
	{
		return totalLines;
	}

	//never used
	int getTotalLines(MyFilter theFilter)
	{
		if (statisticMap.containsKey(theFilter))
		{
			return statisticMap.get(theFilter).getTotalLines();
		}
		else
		{
			return 0;
		}
	}

	int getTotalFiles(MyFilter theFilter)
	{
		if (statisticMap.containsKey(theFilter))
		{
			return statisticMap.get(theFilter).getTotalFiles();
		}
		else
		{
			return 0;
		}
	}

	MyStatistic()
	{
		statisticMap = new HashMap<>();
		totalFiles = 0;
		totalLines = 0;
	}

	void toAdd(MyFilter theFilter, File theFile) throws Exception
	{
		if (!statisticMap.containsKey(theFilter))
		{
			statisticMap.put(theFilter, new MyRecord());
		}

		statisticMap.get(theFilter).toIncrementFileCountByOne();
		statisticMap.get(theFilter).toIncrementLineCountBy(toCountLines(theFile));
	}

	void toAdd(File theFile) throws Exception //when the file was never used
	{
		totalFiles++;
		totalLines = totalLines + toCountLines(theFile);
	}

	private int toCountLines(File theFile) throws Exception
	{
		int linesCount = 0;

		try (BufferedReader theReader = new BufferedReader(new FileReader(theFile)))
		{
			while (theReader.readLine() != null)
			{
				linesCount++;
			}
		}

		return linesCount;
	}

	TreeMap<MyFilter, MyRecord> toSortByValue()
	{
		Comparator<MyFilter> theComparator = new MyValueComparator(statisticMap);
		TreeMap<MyFilter, MyRecord> theResult = new TreeMap<>(theComparator);

		theResult.putAll(statisticMap);
		return theResult;
	}
}
