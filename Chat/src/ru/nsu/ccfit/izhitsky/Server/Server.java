package ru.nsu.ccfit.izhitsky.Server;

import jdk.nashorn.internal.ir.annotations.Ignore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server
{
	//public static ArrayList<Socket> connectionArray = new ArrayList<>(); //all connections
	//public static ArrayList<String> currentUsers = new ArrayList<>();

	private Thread osListenerThread;
	private Thread xmlListenerThread;
	private Thread speaker;

	private ServerSocket osSocket;
	private ServerSocket xmlSocket;

	public static void main(String[] args) throws IOException
	{
		Server theServer = new Server();
		theServer.launch();

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (!in.readLine().equals("exit"))
		{
			//continue
		}
		theServer.terminate();
	}

	private void launch()
	{
		osListenerThread = new Thread(new OSRunnable(), "OS thread");
		osListenerThread.start();

		xmlListenerThread = new Thread(new XMLRunnable(), "XML thread");
		xmlListenerThread.start();

		speaker = new Thread(new SpeakerRunnable(), "Speaker thread");
		speaker.start();
	}

	private void terminate()
	{
		try
		{
			osSocket.close();
			xmlSocket.close();
			speaker.interrupt();
			synchronized (lock)
			{
				for (ClientHandler h : clientHandlers)
				{
					h.interruptWriter();
					h.closeSocket();
				}
			}

		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}

	}

		//final int OSport = 4445;
		//final int XMLport = 4446;

		ServerSocket server = new ServerSocket(OSport);
		System.out.println("Server started waiting for clients...");

		while (true)
		{
			Socket SOCK = server.accept();
			connectionArray.add(SOCK);

			System.out.println("Client connection from: " + SOCK.getLocalAddress().getHostAddress());

			AddUserName(SOCK);

			RunnableChatServer CHAT = new RunnableChatServer(SOCK);
			Thread X = new Thread(CHAT);
			X.start();
		}

	}

	public static void AddUserName(Socket X) throws IOException
	{
		Scanner in = new Scanner(X.getInputStream());
		String userName = in.nextLine();
		currentUsers.add(userName);

		for (int i = 0; i < Server.connectionArray.size(); i++)
		{
			Socket tempSocket = Server.connectionArray.get(i);
			PrintWriter out = new PrintWriter(tempSocket.getOutputStream());
			out.println("#?!" + currentUsers); //command "add users"
			out.flush();
		}
	}
}
