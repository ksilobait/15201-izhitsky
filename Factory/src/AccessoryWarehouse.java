public class AccessoryWarehouse
{
	MyBlockingQueue<Object> theQueue;

	public AccessoryWarehouse(int size_)
	{
		theQueue = new MyBlockingQueue<>(size_);
	}

	void push(Object o) throws InterruptedException
	{
		theQueue.push(o);
	}

	Object pop() throws InterruptedException
	{
		return theQueue.pop();
	}
}
