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

	public static MyFilter toCreate (String config)
	{
		char filterType = config.charAt(0);
		String filterParameter = config.substring(1);
		if (!factoryMap.containsKey(filterType))
		{
			System.err.println("Invalid symbol used in configuration file : " + config);
			System.exit(1);
		}
		try
		{
			String filterName = factoryMap.get(filterType);
			return MySerializer.toParseFilter(filterName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
