package ru.nsu.ccfit.izhitsky.LoC;

import java.util.Comparator;
import java.util.HashMap;

public class MyValueComparator implements Comparator<MyFilter>
{
	private HashMap<MyFilter, MyStatistic.MyRecord> theMap;

	//--------------------------------------------------------------------------------------

	MyValueComparator(HashMap<MyFilter, MyStatistic.MyRecord> anotherMap)
	{
		theMap = new HashMap<>();
		theMap.putAll(anotherMap);
	}

	@Override
	public int compare(MyFilter filter01, MyFilter filter02)
	{
		if (theMap.get(filter01).getTotalLines() >= theMap.get(filter02).getTotalLines())
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
}
