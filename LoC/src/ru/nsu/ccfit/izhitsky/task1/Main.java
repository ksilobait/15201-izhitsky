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
