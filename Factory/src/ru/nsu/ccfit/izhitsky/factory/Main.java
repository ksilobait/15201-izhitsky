package ru.nsu.ccfit.izhitsky.factory;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Properties;

public class Main
{
	static
	{
		Properties props = System.getProperties();
		props.setProperty("log4j.configurationFile", "file:src/log4j2.xml");
	}

	private static final Logger theLogger = LogManager.getLogger(Main.class);

	public static void main(String[] args)
	{
		//read config
		MyConfigReader theConfigReader = new MyConfigReader("config.txt");

		//disable logging
		if (!theConfigReader.isLogSale())
		{
			Logger anotherLogger = LogManager.getRootLogger();
			Configurator.setLevel(anotherLogger.getName(), Level.OFF);
		}

		TheFactory theFactory = new TheFactory(theConfigReader);

		theLogger.info("everything's ready");
	}
}
