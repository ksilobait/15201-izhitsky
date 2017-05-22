package ru.nsu.ccfit.izhitsky.LoC;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MyControllerTest
{
	@Test
	public void toGatherStatistic()
	{
		MyController controller;
		try
		{
			controller = new MyController("tests/ru/nsu/ccfit/izhitsky/LoC/MyControllerTest.txt");
			controller.toGatherStatistic(new File("tests/ru/nsu/ccfit/izhitsky/LoC/directoryToAnalyze"));
		}
		catch (Exception e)
		{
			System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
			return;
		}

		//expected results:
		Map<String, Integer> testMap = new HashMap<>();
		testMap.put(".txt", 3);
		testMap.put(".c", 1);
		testMap.put("&(|(.txt .c) !(.txt))", 1);
		testMap.put("&(|(.txt .c) !(.ROAR))", 3);
		testMap.put("&(|(.txt .c) >0)", 3);
		testMap.put("&(|(.txt .c) <0)", 0);

		MyStatistic stat = controller.getStatistic();
		MyFilter[] filters = controller.getFilters();

		for (MyFilter filter : filters)
		{
			Integer i1 = testMap.get(filter.toString());
			Integer i2 = stat.getTotalFiles(filter);
			Assert.assertEquals(i1, i2);
		}

		Assert.assertEquals(21, stat.getTotalLines());
		Assert.assertEquals(6, stat.getTotalFiles());

		System.out.println("TEST for MyController PASSED");
	}
}