package ru.nsu.ccfit.izhitsky;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MyConfigReader
{
	public int getEngineWarehouseSize()
	{
		return engineWarehouseSize;
	}

	public int getCoachworkWarehouseSize()
	{
		return coachworkWarehouseSize;
	}

	public int getAccessoryWarehouseSize()
	{
		return accessoryWarehouseSize;
	}

	public int getAccessorySuppliers()
	{
		return accessorySuppliers;
	}

	public boolean isLogSale()
	{
		return logSale;
	}

	public int getThreadPoolSize()
	{
		return threadPoolSize;
	}

	public int getTaskQueueSize()
	{
		return taskQueueSize;
	}

	public int getCarWarehouseSize()
	{
		return carWarehouseSize;
	}

	//default values
	private int engineWarehouseSize = 100;
	private int coachworkWarehouseSize = 100;
	private int accessoryWarehouseSize = 100;
	private int carWarehouseSize = 100;

	private int accessorySuppliers = 1;

	private boolean logSale = true;
	private int threadPoolSize = 100;
	private int taskQueueSize = 100;

	MyConfigReader(String fileName)
	{
		File configFile = new File(fileName);
		try (BufferedReader in = new BufferedReader(new FileReader(configFile)))
		{
			while (true)
			{
				String s = in.readLine();
				if (s.isEmpty())
					break;

				int index;
				for (index = 0; index < s.length(); index++)
				{
					if (s.charAt(index) == '=')
					{
						break;
					}
				}

				if (index == s.length())
				{
					throw new Exception("Exception:: wrong config file:: no = sign");
				}
				String sName = s.substring(0, index - 1);
				String sValue = s.substring(index + 3, s.length());

				switch (sName)
				{
					case "ThreadPoolSize":
					{
						threadPoolSize = Integer.parseInt(sValue);
						break;
					}
					case "TaskQueueSize":
					{
						taskQueueSize = Integer.parseInt(sValue);
						break;
					}
					case "StorageBodySize":
					{
						engineWarehouseSize = Integer.parseInt(sValue);
						break;
					}
					case "StorageMotorSize":
					{
						coachworkWarehouseSize = Integer.parseInt(sValue);
						break;
					}
					case "StorageAccessorySize":
					{
						accessoryWarehouseSize = Integer.parseInt(sValue);
						break;
					}
					case "CarWarehouseSize":
					{
						carWarehouseSize = Integer.parseInt(sValue);
						break;
					}
					case "AccessorySuppliers":
					{
						accessorySuppliers = Integer.parseInt(sValue);
						break;
					}
					case "LogSale":
					{
						logSale = Boolean.parseBoolean(sValue);
						break;
					}
					default:
					{
						throw new Exception("Exception:: wrong config file:: no such value in the system");
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}