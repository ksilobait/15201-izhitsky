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
		/*factoryMap.put('.', ExtensionFilterSerializer.class.getName());
		factoryMap.put('<', LessTimeFilterSerializer.class.getName());
		factoryMap.put('>', GreaterTimeFilterSerializer.class.getName());
		factoryMap.put('&', AndFilterSerializer.class.getName());
		factoryMap.put('|', OrFilterSerializer.class.getName());
		factoryMap.put('!', NotFilterSerializer.class.getName());*/
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
			Class c = Class.forName(factoryMap.get(filterType));
			Method m = c.getMethod("parseFilter", String.class);
			return (MyFilter)m.invoke(null, filterParameter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
