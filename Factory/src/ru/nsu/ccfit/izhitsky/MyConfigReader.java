package ru.nsu.ccfit.izhitsky;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MyConfigReader
{
	private static final Logger theLogger = LogManager.getLogger(Main.class);

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

	public int getAccessorySuppliersNumber()
	{
		return accessorySuppliersNumber;
	}

	public int getDealersNumber()
	{
		return dealersNumber;
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
	private int threadPoolSize = 250;
	private int taskQueueSize = 30;

	private int coachworkWarehouseSize = 100;
	private int engineWarehouseSize = 200;
	private int accessoryWarehouseSize = 500;
	private int carWarehouseSize = 90;

	private int accessorySuppliersNumber = 1;
	private int dealersNumber = 1;

	private int coachworkSupplierTimeout = 800;
	private int engineSupplierTimeout = 600;
	private int accessorySupplierTimeout = 300;
	private int dealerTimeout = 300;

	public int getCoachworkSupplierTimeout()
	{
		return coachworkSupplierTimeout;
	}

	public int getEngineSupplierTimeout()
	{
		return engineSupplierTimeout;
	}

	public int getAccessorySupplierTimeout()
	{
		return accessorySupplierTimeout;
	}

	public int getDealerTimeout()
	{
		return dealerTimeout;
	}

	private boolean logSale = true;

	MyConfigReader(String fileName)
	{
		File configFile = new File(fileName);
		try (BufferedReader in = new BufferedReader(new FileReader(configFile)))
		{
			String s;

			while ((s = in.readLine()) != null)
			{
				if (s.isEmpty() || s.charAt(0) == '#') //comment or empty
				{
					continue;
				}

				int indexOfEqualSign;
				for (indexOfEqualSign = 0; indexOfEqualSign < s.length(); indexOfEqualSign++)
				{
					if (s.charAt(indexOfEqualSign) == '=')
					{
						break;
					}
				}

				if (indexOfEqualSign == s.length())
				{
					theLogger.error("there is a line in the config file with no equal sign");
					continue;
				}

				String sName = s.substring(0, indexOfEqualSign);
				String sValue = s.substring(indexOfEqualSign + 1, s.length());

				switch (sName)
				{
					//THREAD POOL:
					case "ThreadPoolSize":
					case "THREAD_POOL_SIZE":
					{
						threadPoolSize = Integer.parseInt(sValue);
						break;
					}
					case "TaskQueueSize":
					case "TASK_QUEUE_SIZE":
					{
						taskQueueSize = Integer.parseInt(sValue);
						break;
					}
					//WAREHOUSES:
					case "EngineWarehouseSize":
					case "ENGINE_WAREHOUSE_SIZE":
					{
						engineWarehouseSize = Integer.parseInt(sValue);
						break;
					}
					case "CoachworkWarehouseSize":
					case "COACHWORK_WAREHOUSE_SIZE":
					{
						coachworkWarehouseSize = Integer.parseInt(sValue);
						break;
					}
					case "AccessoryWarehouseSize":
					case "ACCESSORY_WAREHOUSE_SIZE":
					{
						accessoryWarehouseSize = Integer.parseInt(sValue);
						break;
					}
					case "CarWarehouseSize":
					case "CAR_WAREHOUSE_SIZE":
					{
						carWarehouseSize = Integer.parseInt(sValue);
						break;
					}
					//NUMBER
					case "AccessorySuppliersNumber":
					case "ACCESSORY_SUPPLIER_NUMBER":
					{
						accessorySuppliersNumber = Integer.parseInt(sValue);
						break;
					}
					case "DealersNumber":
					case "DEALER_NUMBER":
					{
						dealersNumber = Integer.parseInt(sValue);
						break;
					}
					//TIMEOUTS
					case "COACHWORK_SUPPLIER_TIMEOUT":
					case "CoachworkSupplierTimeout":
					{
						coachworkSupplierTimeout = Integer.parseInt(sValue);
						break;
					}
					case "ENGINE_SUPPLIER_TIMEOUT":
					case "EngineSupplierTimeout":
					{
						engineSupplierTimeout = Integer.parseInt(sValue);
						break;
					}
					case "ACCESSORY_SUPPLIER_TIMEOUT":
					case "AccessorySupplierTimeout":
					{
						accessorySupplierTimeout = Integer.parseInt(sValue);
						break;
					}
					case "DEALER_TIMEOUT":
					case "DealerTimeout":
					{
						dealerTimeout = Integer.parseInt(sValue);
						break;
					}
					//LOG
					case "LOG_SALE":
					case "LogSale":
					{
						logSale = Boolean.parseBoolean(sValue);
						break;
					}
					default:
					{
						theLogger.error("strange name in the config file");
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