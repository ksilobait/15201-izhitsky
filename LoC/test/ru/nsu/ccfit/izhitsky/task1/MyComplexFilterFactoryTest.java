package ru.nsu.ccfit.izhitsky.task1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyComplexFilterFactoryTest
{
	@Test
	void toCreate() throws Exception
	{
		MyComplexFilterFactory myFactory = new MyComplexFilterFactory();
		MyFilter f1 = myFactory.toCreate(".a");
		MyFilter f2 = myFactory.toCreate(">1");
		MyFilter f3 = myFactory.toCreate("!.c");
		MyFilter f4 = myFactory.toCreate("|(&(|(.d .e) !<50) .f)");

		assertEquals(".a", f1.toString());
		assertEquals(">1", f2.toString());
		assertEquals("!(.c)", f3.toString());
		assertEquals("|(&(|(.d .e) !(<50)) .f)", f4.toString());
		System.out.println("TEST for MyComplexFilterFactory PASSED");
	}
}