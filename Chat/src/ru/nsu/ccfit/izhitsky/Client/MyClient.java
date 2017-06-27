package ru.nsu.ccfit.izhitsky.Client;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.izhitsky.GUI.GUIIntro;
import ru.nsu.ccfit.izhitsky.Messages.*;
import ru.nsu.ccfit.izhitsky.User;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

public class MyClient
{
	static
	{
		Properties props = System.getProperties();
		props.setProperty("log4j.configurationFile", "file:src/log4j2.xml");
	}

	public enum TYPE {OS, XML}
	private static final Logger theLogger = LogManager.getLogger(MyClient.class);

	private User currentUser;
	private boolean loggedIn;
	private Thread readerThread;
	private Thread writerThread;
	private Socket theSocket;
	private int port;
	private String IP;
	private TYPE type;
	private int sessionID;

	private ArrayBlockingQueue<TheClientMessage> textMessages;
	private ArrayBlockingQueue<ClientMessage> messagesQueue;
	private ArrayBlockingQueue<Document> docMessagesQueue;
	private List<User> theUsers;
	private TheMessageHandler theMessageHandler;
	private TheXMLMessageHandler theXMLMessageHandler;

	public static void main(String[] args)
	{
		new GUIIntro();
	}

	public MyClient(String IP_, int port_, TYPE type_)
	{
		this.IP = IP_;
		this.port = port_;
		this.type = type_;
		loggedIn = false;

		currentUser = new User();
		currentUser.setType(type);
		theMessageHandler = new TheMessageHandler(this);
		textMessages = new ArrayBlockingQueue<>(10000);
		messagesQueue = new ArrayBlockingQueue<>(10000);
		docMessagesQueue = new ArrayBlockingQueue<>(10000);
		theXMLMessageHandler = new TheXMLMessageHandler(docMessagesQueue);
		theUsers = new ArrayList<>();
		connections = new ArrayList<>();
	}

	public void connectToServer()
	{
		try
		{
			theLogger.info("IP = " + IP +"; PORT = " + port);
			theSocket = new Socket();
			theSocket.setKeepAlive(true);
			theSocket.connect(new InetSocketAddress(IP, port), 5000); //5 sec timeout
		}
		catch (SocketTimeoutException e)
		{
			theLogger.error("SocketTimeoutException");
			connectionListener.handle(false);
			return;
		}
		catch (IOException e)
		{
			theLogger.error("error in connection to server");
			connectionListener.handle(false);
			return;
		}

		if (connectionListener != null)
		{
			connectionListener.handle(true);
		}

		switch (type)
		{
			case OS:
			{
				readerThread = new Thread(new ObjectReader(), "ObjectReader");
				writerThread = new Thread(new ObjectWriter(), "ObjectWriter");
				readerThread.start();
				writerThread.start();
				theLogger.info("connected to OS Server");
				break;
			}
			case XML:
			{
				readerThread = new Thread(new XMLReader(), "XMLReader");
				writerThread = new Thread(new XMLWriter(), "XMLWriter");
				readerThread.start();
				writerThread.start();
				theLogger.info("connected to XML Server");
				break;
			}
		}
	}

	public TheClientMessage takeTextMessage()
	{
		try
		{
			TheClientMessage toReturn = textMessages.take();
			return toReturn;
		}
		catch (InterruptedException e)
		{
			theLogger.error("takeTextMessage() was interrupted");
		}
		return null;
	}

	public void sendMessage(ClientMessage theMessage)
	{
		messagesQueue.add(theMessage);
	}

	public void interrupt()
	{
		readerThread.interrupt();
		writerThread.interrupt();
		try
		{
			theSocket.close();
		}
		catch (IOException e)
		{
			theLogger.error("socket wasn't closed");
		}
	}

	public void disconnectFromServer()
	{
		try
		{
			theSocket.close();
		}
		catch (IOException e)
		{
			theLogger.error("error in closing socket");
		}
	}


	//GETTERS AND SETTERS------------------------------------------------------------------

	public int getSessionID()
	{
		return sessionID;
	}

	public void setSessionID(int sessionID)
	{
		this.sessionID = sessionID;
	}

	public void setLoggedIn(boolean b) { loggedIn = b; }

	public boolean isLoggedIn()
	{
		return this.loggedIn;
	}

	public boolean isSocketAlive()
	{
		return !theSocket.isClosed();
	}

	public void setName(String name)
	{
		currentUser.setName(name);
	}

	public String getName()
	{
		return currentUser.getName();
	}

	public MyClient.TYPE getType()
	{
		return currentUser.getType();
	}

	public List<User> getUsers()
	{
		return theUsers;
	}

	public void addUser(User user)
	{
		if (!theUsers.contains(user))
		{
			theUsers.add(user);
		}
	}

	public void deleteUser(User user)
	{
		theUsers.remove(user);
	}

	public void clearUsers() { theUsers.clear(); }

	//CLASSES------------------------------------------------------------------------------

	private class ObjectWriter implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				ObjectOutputStream writerStream = new ObjectOutputStream(theSocket.getOutputStream());
				while (!Thread.interrupted())
				{
					Message mssg = messagesQueue.take();
					writerStream.writeObject(mssg);
					writerStream.flush();
					if (mssg instanceof TheClientMessage)
					{
						textMessages.add((TheClientMessage) mssg);
					}
					theLogger.info("client message written to server");
				}
			}
			catch (InterruptedException e)
			{
				theLogger.error("ObjectWriter was interrupted");
			}
			catch (IOException e)
			{
				theLogger.error("socket was closed");
			}
			finally
			{
				theLogger.info("thread finished");
			}
		}
	}

	private class ObjectReader implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				ObjectInputStream readerStream = new ObjectInputStream(theSocket.getInputStream());
				while(!Thread.interrupted())
				{
					ServerMessage message = (ServerMessage) readerStream.readObject();
					theLogger.info("ClientObjectReader is processing the message");
					message.process(theMessageHandler);
				}
			}
			catch (IOException | ClassNotFoundException e)
			{
				theLogger.error("socket has been closed");
				connectionListener.handle(false);
				writerThread.interrupt();
			}
			finally
			{
				theLogger.info("thread finished");
			}
		}
	}

	private class XMLWriter implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				DataOutputStream outputStream = new DataOutputStream(theSocket.getOutputStream());
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				while (!Thread.interrupted())
				{
					ClientMessage message = messagesQueue.take();
					message.documentize(theXMLMessageHandler);

					Document document = docMessagesQueue.take();
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					transformer.transform(new DOMSource(document), new StreamResult(byteArrayOutputStream));
					outputStream.writeInt(byteArrayOutputStream.size());
					outputStream.write(byteArrayOutputStream.toByteArray());
					outputStream.flush();

					if (message instanceof TheClientMessage)
					{
						textMessages.add((TheClientMessage) message);
					}
					theLogger.info("Data written to socket from ClientXMLWriter");
				}
			}
			catch (InterruptedException e)
			{
				theLogger.error("interrupted");
			}
			catch (IOException e)
			{
				theLogger.error("could not write message");
			}
			catch (TransformerException e)
			{
				theLogger.error(e.getMessage());
			}
		}
	}

	private class XMLReader implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				DataInputStream inputStream = new DataInputStream(theSocket.getInputStream());
				while (!Thread.interrupted())
				{
					int length = inputStream.readInt();
					if (length < 0)
					{
						theLogger.error("message length is negative");
						break;
					}

					//else
					byte[] data = new byte[length];
					int readBytes = 0;

					while (readBytes != length)
					{
						int temp = inputStream.read(data, readBytes, length - readBytes);
						readBytes = readBytes + temp;
					}

					DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					Document document = documentBuilder.parse(new InputSource(new InputStreamReader(new ByteArrayInputStream(data, 0, length), "UTF-8")));
					switch (document.getDocumentElement().getTagName())
					{
						case "error":
						{
							theLogger.info("client XML reader faced the error");
							TheTextServerMessage mssg = new TheTextServerMessage(TheTextServerMessage.STATUS.ERROR);
							mssg.setErrorMessage(document.getElementsByTagName("message").item(0).getTextContent());
							mssg.process(theMessageHandler);
							break;
						}
						case "event":
						{
							TheUserServerMessage mssg;
							switch (document.getDocumentElement().getAttribute("name"))
							{
								case "message":
								{
									theLogger.info("client XML reader is going to display a message");
									mssg = new TheUserServerMessage(TheUserServerMessage.STATUS.DEFAULT);
									mssg.setData(document.getElementsByTagName("message").item(0).getTextContent());
									mssg.setUserName(document.getElementsByTagName("name").item(0).getTextContent());
									mssg.process(theMessageHandler);
									break;
								}
								case "userlogin":
								{
									theLogger.info("client XML reader is going to login");
									mssg = new TheUserServerMessage(TheUserServerMessage.STATUS.LOGIN);
									mssg.setUserName(document.getElementsByTagName("name").item(0).getTextContent());
									mssg.process(theMessageHandler);
									break;
								}
								case "userlogout":
								{
									theLogger.info("client XML reader is going to logout");
									mssg = new TheUserServerMessage(TheUserServerMessage.STATUS.LOGOUT);
									mssg.setUserName(document.getElementsByTagName("name").item(0).getTextContent());
									mssg.process(theMessageHandler);
									break;
								}
							}
							break;
						}
						case "success":
						{
							NodeList list = document.getElementsByTagName("user");
							if (list.getLength() == 0)
							{
								Node session = document.getElementsByTagName("session").item(0);
								if (session == null)
								{
									theLogger.info("client XML reader has TheTextServerMessage::SUCCESS");
									TheTextServerMessage mssg = new TheTextServerMessage(TheTextServerMessage.STATUS.SUCCESS);
									mssg.process(theMessageHandler);
								}
								else
								{
									theLogger.info("client XML reader has TheActionServerMessage::LOGINSUCCESS");
									TheActionServerMessage mssg = new TheActionServerMessage(TheTextServerMessage.STATUS.SUCCESS, TheRequestClientMessage.REQUEST_TYPE.LOGIN);
									mssg.setSessionID(Integer.valueOf(session.getTextContent()));
									mssg.process(theMessageHandler);
								}
							}
							else
							{
								ArrayList<User> listusers = new ArrayList<>();
								for (int i = 0; i < list.getLength(); i++)
								{
									Element elmnt = (Element) list.item(i);
									User user = new User();
									user.setName(elmnt.getElementsByTagName("name").item(0).getTextContent());
									String type = elmnt.getElementsByTagName("type").item(0).getTextContent();
									switch (type)
									{
										case "xml":
										case "XML":
										{
											user.setType(MyClient.TYPE.XML);
											break;
										}
										default:
										{
											user.setType(MyClient.TYPE.OS);
											break;
										}
									}
									listusers.add(user);
								}
								theLogger.info("client XML reader has TheActionServerMessage::LISTSUCCESS");
								TheActionServerMessage mssg = new TheActionServerMessage(TheTextServerMessage.STATUS.SUCCESS, TheRequestClientMessage.REQUEST_TYPE.LIST);
								mssg.setUsers(listusers);
								mssg.process(theMessageHandler);
							}
							break;
						}
					}
				}
			}
			catch (IOException e)
			{
				theLogger.error(e.getMessage());
				disconnectFromServer();
				writerThread.interrupt();
				connectionListener.handle(false);
			}
			catch (SAXException e)
			{
				theLogger.error("could not deserialize server data");
			}
			catch (ParserConfigurationException e)
			{
				theLogger.error(e.getMessage());
			}
		}
	}

	//--------------------------------------------------------------------------------------

	public interface MyConnection
	{
		void process(ServerMessage mssg);
	}

	private List<MyConnection> connections;

	public void addConnection(MyConnection cnnctn)
	{
		connections.add(cnnctn);
	}

	public void notifyListeners(ServerMessage message)
	{
		for (MyConnection cnnctn : connections)
		{
			cnnctn.process(message);
		}
	}

	//--------------------------------------------------------------------------------------

	public interface ConnectionListener
	{
		void handle(boolean connected);
	}

	private ConnectionListener connectionListener;

	public void setConnectionListener(ConnectionListener connectionListener_)
	{
		this.connectionListener = connectionListener_;
	}


}
