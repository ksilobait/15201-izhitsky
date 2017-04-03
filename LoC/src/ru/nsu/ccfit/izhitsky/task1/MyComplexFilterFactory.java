package ru.nsu.ccfit.izhitsky.task1;

import java.lang.reflect.Method;
import java.util.HashMap;
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
		char filterType = configLine.charAt(0); //e.g. "&"
		String filterParameter = configLine.substring(1); //e.g. "(.java <100)"
		if (!factoryMap.containsKey(filterType))
		{
			//TODO: throw something
			System.err.println("Invalid symbol used in configuration file : " + configLine);
			System.exit(1);
		}
		try
		{
			String selectedFilter = factoryMap.get(filterType); //e.g. "&" -> "AndFilter"
			switch (selectedFilter)
			{
				case "AndFilter":
					return new AndFilter(parse(configLine));
					break;
				case "OrFilter":
					return new OrFilter(parse(configLine));
					break;
				case "NotFilter":
					MyFilter theFilter = null;
					if (configLine.charAt(0) == '(')
					{
						theFilter = MyComplexFilterFactory.toCreate(configLine.substring(1, configLine.length() - 1));
					}
					else
					{
						theFilter = MyComplexFilterFactory.toCreate(configLine);
					}
					return new NotFilter(theFilter);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
