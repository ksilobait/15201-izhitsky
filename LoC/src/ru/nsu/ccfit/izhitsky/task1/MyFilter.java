package ru.nsu.ccfit.izhitsky.task1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface MyFilter
{
	public boolean toCheck(File theFile);

	public static MyFilter[] toParseIntoFilters(String fileName)
	{
		List<MyFilter> theFiltersList = new ArrayList<>();

		for (MyConfigFileIterator it = new MyConfigFileIterator(fileName); it.hasNext(); )
		{
			MyFilter theFilter = MyFilterFactory.toCreate(it.next());
			if (!theFiltersList.contains(theFilter))
			{
				theFiltersList.add(theFilter);
			}
		}

		return theFiltersList.toArray(new MyFilter[theFiltersList.size()]);
	}

}
