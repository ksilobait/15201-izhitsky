public class CoachworkWarehouse
{
	MyBlockingQueue<Object> theQueue;

	public CoachworkWarehouse(int size_)
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
