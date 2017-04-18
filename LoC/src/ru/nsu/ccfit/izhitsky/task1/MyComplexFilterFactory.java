package ru.nsu.ccfit.izhitsky.task1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyComplexFilterFactory
{
	private static final Map<Character, String> factoryMap;

	static
	{
		factoryMap = new HashMap<>();
		//TODO
		/*factoryMap.put('&', MySerializer_AndFilter.class.getName());
		factoryMap.put('|', MySerializer_OrFilter.class.getName());
		factoryMap.put('!', MySerializer_NotFilter.class.getName());
		factoryMap.put('.', MySerializer_ExtensionFilter.class.getName());
		factoryMap.put('<', MySerializer_TimeLessFilter.class.getName());
		factoryMap.put('>', MySerializer_TimeGreaterFilter.class.getName());*/
		factoryMap.put('&', "AndFilter");
		factoryMap.put('|', "OrFilter");
		factoryMap.put('!', "NotFilter");
		factoryMap.put('.', "ExtensionFilter");
		factoryMap.put('<', "TimeLessFilter");
		factoryMap.put('>', "TimeGreaterFilter");
	}

	public MyFilter toCreate(String configLine) //e.g. "&(.java <100)"
	{
		char filterTypeChar = configLine.charAt(0); //e.g. "&"
		String filterParameter = configLine.substring(1); //e.g. "(.java <100)"

		if (!factoryMap.containsKey(filterTypeChar))
		{
			//TODO: throw something
			System.err.println("ERROR: an invalid symbol was used in the configuration file : " + configLine);
			System.exit(1);
		}

		String filterType = factoryMap.get(filterTypeChar);
		MyFilter[] filters = toParseString(factoryMap.get(filterType)); //e.g. "&" -> "AndFilter"

		try
		{
			switch (filterType)
			{
				case "AndFilter":
					return new AndFilter(filters);
					break;
				case "OrFilter":
					return new OrFilter(filters);
					break;
				case "NotFilter":
					MyFilter theFilter;
					if (configLine.charAt(0) == '(')
					{
						MyComplexFilterFactory tempFactory = new MyComplexFilterFactory();
						theFilter = tempFactory.toCreate(configLine.substring(1, configLine.length() - 1));
					}
					else
					{
						MyComplexFilterFactory tempFactory = new MyComplexFilterFactory();
						theFilter = tempFactory.toCreate(configLine);
					}
					return new NotFilter(theFilter);
					break;
				case "ExtensionFilter":
					return new ExtensionFilter(configLine);
					break;
				case "TimeLessFilter":
					return new TimeLessFilter(configLine);
					break;
				case "TimeGreaterFilter":
					return new TimeGreaterFilter(configLine);
					break;
				default:
					//TODO: throw
					break;
			}
		}


		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}


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
			filtersList.add(MyComplexFilterFactory.toCreate(theFilter)); //TODO: fix static context
		}

		return filtersList.toArray(new MyFilter[filtersList.size()]);
	}

}
