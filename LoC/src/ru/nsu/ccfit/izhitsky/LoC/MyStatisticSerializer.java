package ru.nsu.ccfit.izhitsky.LoC;

import java.util.Map;

class MyStatisticSerializer
{
	static void toPrintStatistic(MyStatistic theStatistic)
	{
		System.out.println("Total : " + theStatistic.getTotalLines() + " lines in " + theStatistic.getTotalFiles() + " files");
		System.out.println("////////////////////////////////////////////////////////////////////////////////");
		Map<MyFilter, MyStatistic.MyRecord> sortedStatisticMap = theStatistic.toSortByValue();

		for (Map.Entry<MyFilter, MyStatistic.MyRecord> theEntry : sortedStatisticMap.entrySet())
		{
			System.out.println(theEntry.getKey().toString() + " : " + theEntry.getValue().getTotalLines() + " lines in " + theEntry.getValue().getTotalFiles() + " files");
		}
	}
}
