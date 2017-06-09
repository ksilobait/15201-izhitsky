package ru.nsu.ccfit.izhitsky.LoC;

import java.util.Map;

class MyStatisticSerializer
{
	static void toPrintStatistic(MyStatistic theStatistic)
	{
		System.out.println("Total - " + theStatistic.getTotalLines() + " lines in " + theStatistic.getTotalFiles() + " files");

		Map<MyFilter, MyStatistic.MyRecord> sortedStatisticMap = theStatistic.toSortByValue();

		int offset1 = 0; //filter name
		int number2 = 0; //lines
		int number3 = 0; //files
		for (Map.Entry<MyFilter, MyStatistic.MyRecord> theEntry : sortedStatisticMap.entrySet())
		{
			if (theEntry.getKey().toString().length() > offset1)
				offset1 = theEntry.getKey().toString().length();
			if (theEntry.getValue().getTotalLines() > number2)
				number2 = theEntry.getValue().getTotalLines();
			if (theEntry.getValue().getTotalFiles() > number3)
				number3 = theEntry.getValue().getTotalFiles();
		}

		int offset2 = calcDigits(number2); //lines
		int offset3 = calcDigits(number3); //files

		StringBuilder hyphens = new StringBuilder("");
		for (int i = 0; i < offset1 + 1; i++)
			hyphens.append('-');
		System.out.println(hyphens); // -------

		for (Map.Entry<MyFilter, MyStatistic.MyRecord> theEntry : sortedStatisticMap.entrySet())
		{
			StringBuilder temp = new StringBuilder(theEntry.getKey().toString());
			for (int i = 0; i < offset1 - theEntry.getKey().toString().length(); i++)
				temp.append(' ');

			temp.append(" - ");
			temp.append(theEntry.getValue().getTotalLines());
			for (int i = 0; i < offset2 - calcDigits(theEntry.getValue().getTotalLines()); i++)
				temp.append(' ');

			temp.append(" lines in ");
			temp.append(theEntry.getValue().getTotalFiles());
			for (int i = 0; i < offset3 - calcDigits(theEntry.getValue().getTotalFiles()); i++)
				temp.append(' ');

			temp.append(" files");

			System.out.println(temp);
		}
	}

	static private int calcDigits(int x)
	{
		int digits = 0;
		while (x > 0)
		{
			digits++;
			x = x / 10;
		}
		return digits;
	}
}
