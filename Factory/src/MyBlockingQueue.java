import java.util.LinkedList;
import java.util.Queue;

public class MyBlockingQueue<T>
{
	int maxSize;
	Queue<T> theQueue;
	Object theLock = new Object();


	MyBlockingQueue(int size_)
	{
		this.maxSize = size_;
		theQueue = new LinkedList<>();
	}

	void push(T t) throws InterruptedException
	{
		synchronized (theLock)
		{
			while (theQueue.size() == maxSize) //overload
			{
				theLock.wait();
			}
			theQueue.add(t);
			theLock.notifyAll();
		}
	}

	T pop() throws InterruptedException
	{
		T toReturn = null;
		synchronized (theLock)
		{
			while (theQueue.size() == 0) //empty
			{
				theLock.wait();
			}

			toReturn = theQueue.remove();
			theLock.notifyAll();
		}
		return toReturn;
	}

}
