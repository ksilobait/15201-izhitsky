package ru.nsu.ccfit.izhitsky.task1;

import ru.nsu.ccfit.izhitsky.task1.MyFilters.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MyComplexFilterFactory
{
	private static final Map<Character, String> factoryMap;

	static
	{
		factoryMap = new HashMap<>();
		factoryMap.put('&', "AndFilter");
		factoryMap.put('|', "OrFilter");
		factoryMap.put('!', "NotFilter");
		factoryMap.put('.', "ExtensionFilter");
		factoryMap.put('<', "TimeLessFilter");
		factoryMap.put('>', "TimeGreaterFilter");
	}

	MyFilter toCreate(String configLine) throws Exception //e.g. "&(.java <100)"
	{
		char filterTypeChar = configLine.charAt(0); //e.g. "&"
		String filterParameter = configLine.substring(1); //e.g. "(.java <100)"

		if (!factoryMap.containsKey(filterTypeChar))
		{
			throw new Exception("an invalid symbol was used in the configuration file " + configLine);
		}

		String filterType = factoryMap.get(filterTypeChar);

		switch (filterType)
		{
			case "AndFilter":
				return new AndFilter(toParseStringIntoFilters(filterParameter));
			case "OrFilter":
				return new OrFilter(toParseStringIntoFilters(filterParameter));
			case "NotFilter":
				MyFilter returnedFilter;
				if (filterParameter.charAt(0) == '(')
				{
					MyComplexFilterFactory tempFactory = new MyComplexFilterFactory();
					returnedFilter = tempFactory.toCreate(filterParameter.substring(1, filterParameter.length() - 1));
				}
				else
				{
					MyComplexFilterFactory tempFactory = new MyComplexFilterFactory();
					returnedFilter = tempFactory.toCreate(filterParameter);
				}
				return new NotFilter(returnedFilter);
			case "ExtensionFilter":
				return new ExtensionFilter(filterParameter);
			case "TimeLessFilter":
				return new TimeLessFilter(filterParameter);
			case "TimeGreaterFilter":
				return new TimeGreaterFilter(filterParameter);
			default:
				throw new Exception("no such filter found in the map");
		}
	}


	private static MyFilter[] toParseStringIntoFilters(String theString_) throws Exception
	{
		String theString = theString_.substring(1, theString_.length() - 1) + ' ';
		List<String> filterList = new ArrayList<>();
		StringBuilder temp = new StringBuilder("");
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
				filterList.add(temp.toString());
				temp.delete(0, temp.length()); //temp=""
				continue;
			}
			temp.append(c);
		}

		if (bracketsCounter != 0)
		{
			throw new Exception("invalid brackets in aggregated filter");
		}

		List<MyFilter> filtersList = new ArrayList<>();

		for (String theFilter : filterList)
		{
			MyComplexFilterFactory localFactory = new MyComplexFilterFactory();
			filtersList.add(localFactory.toCreate(theFilter));
		}

		return filtersList.toArray(new MyFilter[filtersList.size()]);
	}

}
