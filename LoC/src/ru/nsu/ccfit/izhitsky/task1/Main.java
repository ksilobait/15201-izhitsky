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

	    //TEST1 (MyConfigFileIterator.hasNext)
	    /*
	    String fileName = "test/tests1_2.txt";
	    MyConfigFileIterator it = new MyConfigFileIterator(fileName);
	    if (it.hasNext())
	    {
		    System.out.println("TEST 1.1 COMPLETED");
	    }

	    if (it.hasNext())
	    {
		    System.out.println("TEST 1.2 COMPLETED");
	    }

	    if (it.hasNext())
	    {
		    System.out.println("TEST 1.3 COMPLETED");
	    }

	    if (!it.hasNext())
	    {
		    System.out.println("TEST 1.4 COMPLETED");
	    }*/

	    //TEST2 (MyConfigFileIterator.next)
	    /*
	    String fileName = "test/tests1_2.txt";
	    MyConfigFileIterator it = new MyConfigFileIterator(fileName);

	    it.hasNext();

	    if (it.next().equals(".hpp"))
	    {
		    System.out.println("TEST 2.1 COMPLETED");
	    }

	    it.hasNext();

	    if (it.next().equals(".cpp"))
	    {
		    System.out.println("TEST 2.2 COMPLETED");
	    }

	    it.hasNext();

	    if (it.next().equals("<(20.01.1997 00:00:00)"))
	    {
		    System.out.println("TEST 2.3 COMPLETED");
	    }*/

	    //TEST 3 (MyFilterFactory)
	    MyFilter f1 = MyFilterFactory.toCreate(".java");
	    if(f1.toString().equals(".java"))
	    	System.out.println("TEST 3.1 COMPLETED");

	    MyFilter f2 = MyFilterFactory.toCreate("<102345");
	    if(f2.toString().equals("<102345"))
		    System.out.println("TEST 3.2 COMPLETED");

	    MyFilter f3 = MyFilterFactory.toCreate("!.java");
	    if(f3.toString().equals("!(.java)"))
		    System.out.println("TEST 3.3 COMPLETED");

	    MyFilter f4 = MyFilterFactory.toCreate("|(<102345 !.java)");
	    if(f4.toString().equals("|(<102345 !(.java))"))
		    System.out.println("TEST 3.4 COMPLETED");

	    MyFilter f5 = MyFilterFactory.toCreate("!(&(|(.java .txt) .jpg) .mp3)");
	    if(f5.toString().equals("!(&(|(.java .txt) .jpg) .mp3)"))
		    System.out.println("TEST 3.5 COMPLETED");
    }
}
