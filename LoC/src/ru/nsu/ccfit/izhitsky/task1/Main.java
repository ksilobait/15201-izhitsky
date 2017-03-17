package ru.nsu.ccfit.izhitsky.task1;

import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("Usage: configFile directoryToAnalyze");
            return;
        }

	    MyController theController = new MyController(args[0]); //parse configFile
	    theController.toGatherStatistic(new File(args[1]));
	    MyStatisticSerializer.toPrintStatistic(theController.getStatistic());
    }
}
