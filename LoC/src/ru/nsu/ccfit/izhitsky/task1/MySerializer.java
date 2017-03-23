package ru.nsu.ccfit.izhitsky.task1;

public class MySerializer
{
	public static MyFilter toParseFilter(String s)
	{
		switch (s)
		{
			case "AndFilter":
				return new AndFilter();
				break;
			case "OrFilter":
				return new OrFilter();
				break;
			case "NotFilter":
				return new NotFilter();
				break;
			case "ExtensionFilter":
				return new ExtensionFilter();
				break;
			case "TimeLessFilter":
				return new TimeLessFilter();
				break;
			case "TimeGreaterFilter":
				return new TimeGreaterFilter();
				break;
		}
	}
}
