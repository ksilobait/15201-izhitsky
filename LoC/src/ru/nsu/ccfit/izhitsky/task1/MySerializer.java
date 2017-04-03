package ru.nsu.ccfit.izhitsky.task1;

import java.util.ArrayList;
import java.util.List;

public class MySerializer
{
	private static MyFilter[] toParseString(String theString_)
	{
		String theString = theString_.substring(1, theString_.length() - 1) + ' ';
		List<String> filterList = new ArrayList<>();
		String temp = "";
		int bracketsCounter = 0;

		for (int i = 0; i < theString.length(); i++)
		{
			char c = theString.charAt(i);
			if (c == '(')
			{
				bracketsCounter++;
			}
			else if (c == ')')
			{
				bracketsCounter--;
			}
			else if (c == ' ' && bracketsCounter == 0)
			{
				filterList.add(temp);
				temp = "";
				continue;
			}
			temp = temp + c;
		}

		if (bracketsCounter != 0)
		{
			//TODO: throw something
		}

		List<MyFilter> filtersList = new ArrayList<>();

		for (String theFilter : filterList)
		{
			filtersList.add(MyComplexFilterFactory.toCreate(theFilter));
		}

		return filtersList.toArray(new MyFilter[filtersList.size()]);
	}

	public static MyFilter toParseFilter(String s)
	{
		MyFilter[] filters = toParseString(s);

		switch (s)
		{
			case "AndFilter":
				return new AndFilter(filters);
				break;
			case "OrFilter":
				return new OrFilter(filters);
				break;
			case "NotFilter":
				return new NotFilter(filters);
				break;
			case "ExtensionFilter":
				return new ExtensionFilter(filters);
				break;
			case "TimeLessFilter":
				return new TimeLessFilter(filters);
				break;
			case "TimeGreaterFilter":
				return new TimeGreaterFilter(filters);
				break;
		}
	}
}
