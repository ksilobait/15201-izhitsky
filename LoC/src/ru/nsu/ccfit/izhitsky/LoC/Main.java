package ru.nsu.ccfit.izhitsky.LoC;

import java.io.File;

public class Main
{
	public static void main(String[] args)
	{
		if (args.length != 2)
		{
			System.out.println("Usage: configFile directoryToAnalyze");
			System.out.println("the application was terminated");
			return;
		}

		try
		{
			MyController theController = new MyController(args[0]); //parse configFile
			theController.toGatherStatistic(new File(args[1])); //directory
			MyStatisticSerializer.toPrintStatistic(theController.getStatistic()); //print result
		}
		catch (Exception e)
		{
			System.out.println("EXCEPTION CAUGHT: " + e.getMessage());
		}
	}
}
