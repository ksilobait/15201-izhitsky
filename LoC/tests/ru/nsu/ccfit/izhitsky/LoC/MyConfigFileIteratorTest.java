package ru.nsu.ccfit.izhitsky.LoC;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyConfigFileIteratorTest
{
	@Test
	void hasNext() throws Exception
	{
		MyConfigFileIterator it = new MyConfigFileIterator("tests/ru/nsu/ccfit/izhitsky/LoC/MyConfigFileIteratorTest.txt");
		assertTrue(it.hasNext());
		assertTrue(it.hasNext());
		assertTrue(it.hasNext());
		assertFalse(it.hasNext());
		System.out.println("TEST for MyConfigFileIterator 1/2 PASSED");
	}

	@Test
	void next() throws Exception
	{
		MyConfigFileIterator it = new MyConfigFileIterator("tests/ru/nsu/ccfit/izhitsky/LoC/MyConfigFileIteratorTest.txt");
		it.hasNext();
		assertEquals(".firstline", it.next());
		it.hasNext();
		assertEquals("&(.secondLine .second)", it.next());
		it.hasNext();
		assertEquals("<(3)", it.next());
		System.out.println("TEST for MyConfigFileIterator 2/2 PASSED");
	}
}