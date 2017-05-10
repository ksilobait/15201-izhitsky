public class Main
{
	public static void main(String[] args)
	{
		MyConfigReader theConfigReader = new MyConfigReader();

		EngineWarehouse warehouseEngine = new EngineWarehouse(theConfigReader.getEngineWarehouseSize());
		CoachworkWarehouse warehouseCoachwork = new CoachworkWarehouse(theConfigReader.getCoachworkWarehouseSize());
		AccessoryWarehouse warehouseAccessory = new AccessoryWarehouse(theConfigReader.getAccessoryWarehouseSize());

		MyThreadPool threadpool = new MyThreadPool(warehouseEngine, warehouseCoachwork, warehouseAccessory);
	}
}
