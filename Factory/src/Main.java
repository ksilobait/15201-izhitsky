import java.io.IOException;

public class Main
{
	public static void main(String[] args)
	{
		MyConfigReader theConfigReader = new MyConfigReader();

		Storage storage = new Storage();
		ThreadPool threadpool = new ThreadPool(storage);
	}
}
