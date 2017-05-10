import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MyConfigReader
{
	public int getEngineWarehouseSize()
	{
		return EngineWarehouseSize;
	}

	public int getCoachworkWarehouseSize()
	{
		return CoachworkWarehouseSize;
	}

	public int getAccessoryWarehouseSize()
	{
		return AccessoryWarehouseSize;
	}

	public int getAccessorySuppliers()
	{
		return AccessorySuppliers;
	}

	public int getWorkers()
	{
		return Workers;
	}

	public int getDealers()
	{
		return Dealers;
	}

	public boolean isLogSale()
	{
		return LogSale;
	}

	//default values
	private int EngineWarehouseSize = 100;
	private int CoachworkWarehouseSize = 100;
	private int AccessoryWarehouseSize = 100;
	//private int StorageAutoSize = 100;
	private int AccessorySuppliers = 5;
	private int Workers = 10;
	private int Dealers = 20;
	private boolean LogSale = true;

	MyConfigReader()
	{
		File configFile = new File("config.txt");
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
					case "StorageBodySize":
					{
						EngineWarehouseSize = Integer.parseInt(sValue);
						break;
					}
					case "StorageMotorSize":
					{
						CoachworkWarehouseSize = Integer.parseInt(sValue);
						break;
					}
					case "StorageAccessorySize":
					{
						AccessoryWarehouseSize = Integer.parseInt(sValue);
						break;
					}
					/*case "StorageAutoSize":
					{
						StorageAutoSize = Integer.parseInt(sValue);
						break;
					}*/
					case "AccessorySuppliers":
					{
						AccessorySuppliers = Integer.parseInt(sValue);
						break;
					}
					case "Workers":
					{
						Workers = Integer.parseInt(sValue);
						break;
					}
					case "Dealers":
					{
						Dealers = Integer.parseInt(sValue);
						break;
					}
					case "LogSale":
					{
						LogSale = Boolean.parseBoolean(sValue);
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
