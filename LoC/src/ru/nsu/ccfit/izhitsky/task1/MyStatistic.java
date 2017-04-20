package ru.nsu.ccfit.izhitsky.task1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MyStatistic
{
	//INNER
	class MyRecord
	{
		//FIELDS
		private int totalFiles;
		private int totalLines;

		//METHODS
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
	////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////
	//FIELDS
	private HashMap<MyFilter, MyRecord> statisticMap;
	private List<File> checkedFiles;
	private int totalFiles;
	private int totalLines;


	//METHODS
	int getTotalFiles()
	{
		return totalFiles;
	}

	int getTotalLines()
	{
		return totalLines;
	}

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


	//METHODS
	MyStatistic()
	{
		statisticMap = new HashMap<>();
		checkedFiles = new ArrayList<>();
		totalFiles = 0;
		totalLines = 0;
	}

	void toAdd(MyFilter theFilter, File theFile) throws Exception
	{
		if (!checkedFiles.contains(theFile))
		{
			checkedFiles.add(theFile);
			totalFiles++;
			totalLines = totalLines + toCountLines(theFile);
		}

		if (!statisticMap.containsKey(theFilter))
		{
			statisticMap.put(theFilter, new MyRecord());
		}

		statisticMap.get(theFilter).toIncrementFileCountByOne();
		statisticMap.get(theFilter).toIncrementLineCountBy(toCountLines(theFile));
	}

	void toAdd(File theFile) throws Exception //when the file was never used
	{
		checkedFiles.add(theFile);
		totalFiles++;
		totalLines = totalLines + toCountLines(theFile);
	}

	private int toCountLines(File theFile) throws Exception
	{
		int linesCount = 0;

		try
		{
			BufferedReader theReader = new BufferedReader(new FileReader(theFile));
			while (theReader.readLine() != null)
			{
				linesCount++;
			}
			theReader.close();
		}
		catch (IOException e)
		{
			throw new Exception("IOException");
			//e.printStackTrace();
		}

		return linesCount;
	}

	public TreeMap<MyFilter, MyRecord> toSortByValue()
	{
		Comparator<MyFilter> theComparator = new MyValueComparator(statisticMap);
		TreeMap<MyFilter, MyRecord> theResult = new TreeMap<>(theComparator);

		theResult.putAll(statisticMap);
		return theResult;
	}
}
