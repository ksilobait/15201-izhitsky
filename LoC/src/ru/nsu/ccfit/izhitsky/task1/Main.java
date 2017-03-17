package ru.nsu.ccfit.izhitsky.task1;

import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
    	/*
        if (args.length != 2)
        {
            System.out.println("Usage: configFile directoryToAnalyze");
            return;
        }

	    MyController theController = new MyController(args[0]); //parse configFile
	    theController.toGatherStatistic(new File(args[1]));
	    MyStatisticSerializer.toPrintStatistic(theController.getStatistic());
		*/

	    //TEST
	    String fileName = "test/ConfigIteratorTest.txt";
	    MyConfigFileIterator it = new MyConfigFileIterator(fileName);
	    if (it.hasNext())
	    {
		    System.out.println("TEST 1 COMPLETED");
	    }
	    if (it.hasNext())
	    {
		    System.out.println("TEST 2 COMPLETED");
	    }
	    if (it.hasNext())
	    {
		    System.out.println("TEST 3 COMPLETED");
	    }
	    if (!it.hasNext())
	    {
		    System.out.println("TEST 4 COMPLETED");
	    }
    }
}
