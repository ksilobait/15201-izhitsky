package ru.nsu.ccfit.izhitsky.task1;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MyFilterFactory
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

	public static MyFilter toCreate (String configLine) //e.g. "&(.java <100)"
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
			Class theFilter = Class.forName(factoryMap.get(filterType)); //e.g. "&" -> "AndFilter"
			Method theMethod = theFilter.getMethod("toParseFilter", String.class);
			return (MyFilter) theMethod.invoke(null, filterParameter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
