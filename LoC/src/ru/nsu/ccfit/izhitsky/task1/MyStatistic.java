package ru.nsu.ccfit.izhitsky.task1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MyStatistic
{
	//INNER
	public class MyRecord
	{
		//FIELDS
		private int fileCount;
		private int lineCount;

		//METHODS
		public MyRecord()
		{
			fileCount = 0;
			lineCount = 0;
		}

		public void toIncrementFileCountByOne()
		{
			fileCount++;
		}

		public void toIncrementLineCountBy(int k)
		{
			lineCount = lineCount + k;
		}

		public int getLineCount()
		{
			return lineCount;
		}

		public int getFileCount()
		{
			return fileCount;
		}
	}
	////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////
	//FIELDS
	private HashMap<MyFilter, MyRecord> statisticMap;
	private List<File> checkedFiles;
	private int totalFiles;
	private int totalLines;

	public int getTotalFiles()
	{
		return totalFiles;
	}

	public int getTotalLines()
	{
		return totalLines;
	}

	//METHODS
	public MyStatistic()
	{
		statisticMap = new HashMap<>();
		checkedFiles = new ArrayList<>();
		totalFiles = 0;
		totalLines = 0;
	}

	public void toAdd(MyFilter theFilter, File theFile)
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

	public int toCountLines(File theFile)
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
			//TODO
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
