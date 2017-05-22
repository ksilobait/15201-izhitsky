package ru.nsu.ccfit.izhitsky.LoC;

import org.junit.Assert;
import org.junit.Test;

public class MyComplexFilterFactoryTest
{
	@Test
	public void toCreate() throws Exception
	{
		MyComplexFilterFactory myFactory = new MyComplexFilterFactory();
		MyFilter f1 = myFactory.toCreate(".a");
		MyFilter f2 = myFactory.toCreate(">1");
		MyFilter f3 = myFactory.toCreate("!.c");
		MyFilter f4 = myFactory.toCreate("|(&(|(.d .e) !<50) .f)");

		Assert.assertEquals(".a", f1.toString());
		Assert.assertEquals(">1", f2.toString());
		Assert.assertEquals("!(.c)", f3.toString());
		Assert.assertEquals("|(&(|(.d .e) !(<50)) .f)", f4.toString());
		System.out.println("TEST for MyComplexFilterFactory PASSED");
	}
}