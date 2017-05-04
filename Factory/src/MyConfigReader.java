import java.io.*;

public class MyConfigReader
{
	//default values
	int StorageBodySize = 100;
	int StorageMotorSize = 100;
	int StorageAccessorySize = 100;
	int StorageAutoSize = 100;
	int AccessorySuppliers = 5;
	int Workers = 10;
	int Dealers = 20;
	boolean LogSale = true;

	MyConfigReader()
	{
		File configFile = new File("config.txt");
		try (BufferedReader in = new BufferedReader(new FileReader(configFile)))
		{
			while (true)
			{
				String s = in.readLine();
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
						StorageBodySize = Integer.parseInt(sValue);
						break;
					}
					case "StorageMotorSize":
					{
						StorageMotorSize = Integer.parseInt(sValue);
						break;
					}
					case "StorageAccessorySize":
					{
						StorageAccessorySize = Integer.parseInt(sValue);
						break;
					}
					case "StorageAutoSize":
					{
						StorageAutoSize = Integer.parseInt(sValue);
						break;
					}
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
