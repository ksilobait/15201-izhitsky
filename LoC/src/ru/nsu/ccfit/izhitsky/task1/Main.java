package ru.nsu.ccfit.izhitsky.task1;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main
{
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("Usage: configFile directoryToAnalyze");
	        System.out.println("the program was switched into TEST MODE");
	        System.out.println();

	        MyController controller;
	        try
	        {
		        controller = new MyController("test/configFile.txt");
		        controller.toGatherStatistic(new File("test/directoryToAnalyze"));
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

	        boolean isCorrect = true;
	        for (MyFilter filter : filters)
	        {
		        Integer i1 = testMap.get(filter.toString());
		        Integer i2 = stat.getTotalFiles(filter);
		        isCorrect = i1.equals(i2);

		        if (!isCorrect)
		        {
			        break;
		        }
	        }

	        boolean b2 = stat.getTotalLines() == 21;
	        boolean b3 = stat.getTotalFiles() == 6;
	        if (isCorrect && b2 && b3)
	        {
		        System.out.println("test passed");
	        }
	        else
	        {
		        System.out.println("TEST FAILED");
	        }
            return;
        }

	    try
	    {
		    MyController theController = new MyController(args[0]); //parse configFile
		    theController.toGatherStatistic(new File(args[1]));
		    MyStatisticSerializer.toPrintStatistic(theController.getStatistic());
	    }
	    catch (Exception e)
	    {
		    System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
	    }



    }
}
