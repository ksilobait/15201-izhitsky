package ru.nsu.ccfit.izhitsky.LoC;

import org.junit.jupiter.api.Test;
import ru.nsu.ccfit.izhitsky.LoC.MyFilters.*;

import static org.junit.jupiter.api.Assertions.*;

class MyFilterParserTest
{
	@Test
	void toParseConfigFile() throws Exception
	{
		MyFilter[] thirdLine = {
				new NotFilter(new ExtensionFilter("first")),
				new TimeGreaterFilter("200"),
				new ExtensionFilter("third")
		};

		MyFilter[] fourthLine = {
				new ExtensionFilter("first"),
				new ExtensionFilter("second"),
				new NotFilter(new TimeLessFilter("300"))
		};

		MyFilter[] correctAnswer = {
				new NotFilter(new ExtensionFilter("abc")),
				new TimeLessFilter("100"),
				new AndFilter(thirdLine),
				new OrFilter(fourthLine)
		};

		MyFilterParser myParser = new MyFilterParser();
		MyFilter[] filters = myParser.toParseConfigFile("tests/ru/nsu/ccfit/izhitsky/LoC/MyFilterParserTest.txt");

		for (int i = 0; i < correctAnswer.length; ++i)
		{
			assertEquals(correctAnswer[i].toString(), filters[i].toString());
		}
		System.out.println("TEST for MyFilterParser PASSED");
	}
}