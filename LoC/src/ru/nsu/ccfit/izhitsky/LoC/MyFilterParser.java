package ru.nsu.ccfit.izhitsky.LoC;

import java.util.ArrayList;
import java.util.List;

class MyFilterParser
{
	MyFilter[] toParseConfigFile(String fileName) throws Exception
	{
		List<MyFilter> theFiltersList = new ArrayList<>();
		MyComplexFilterFactory myFactory = new MyComplexFilterFactory();

		MyConfigFileIterator it = new MyConfigFileIterator(fileName);

		while (it.hasNext())
		{
			MyFilter complexFilter = myFactory.toCreate(it.next());
			if (!theFiltersList.contains(complexFilter))
			{
				theFiltersList.add(complexFilter);
			}
		}

		return theFiltersList.toArray(new MyFilter[theFiltersList.size()]);
	}
}