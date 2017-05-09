package ru.nsu.ccfit.izhitsky.LoC;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MyControllerTest
{
	@Test
	void toGatherStatistic()
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
			assertEquals(i1, i2);
		}

		assertEquals(21, stat.getTotalLines());
		assertEquals(6, stat.getTotalFiles());

		System.out.println("TEST for MyController PASSED");
	}
}