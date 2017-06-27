package ru.nsu.ccfit.izhitsky.Server.ServerUsers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ru.nsu.ccfit.izhitsky.Client.MyClient;
import ru.nsu.ccfit.izhitsky.Client.TheXMLMessageHandler;
import ru.nsu.ccfit.izhitsky.Messages.*;
import ru.nsu.ccfit.izhitsky.Server.MyServer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class XMLUser extends ServerUser
{
	private static final Logger theLogger = LogManager.getLogger(XMLUser.class);

	public XMLUser(MyServer theServer_, Socket theSocket_)
	{
		this.theServer = theServer_;
		this.theSocket = theSocket_;
		sessionID = sessionIDGenerator.getAndIncrement();
		messagesQueue = new ArrayBlockingQueue<>(10000);
		docMessagesQueue = new ArrayBlockingQueue<>(10000);
		TheXMLMessageHandler theServerHandler = new TheXMLMessageHandler(docMessagesQueue);

		readerThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					DataInputStream inputStream = new DataInputStream(theSocket.getInputStream());
					while (!Thread.interrupted())
					{
						byte[] data;
						int length = inputStream.readInt();
						theLogger.info("data fetched from ServerXMLReader");
						if (length < 0)
						{
							theLogger.error("message length is negative; user is blocked");
							blockUser();
							break;
						}

						//else
						int readBytes = 0;
						data = new byte[length];

						while (readBytes != length)
						{
							int temp = inputStream.read(data, readBytes, length - readBytes);
							readBytes = readBytes + temp;
						}

						DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
						Document document = documentBuilder.parse(new InputSource(new InputStreamReader(new ByteArrayInputStream(data, 0, length), "UTF-8")));
						switch (document.getDocumentElement().getAttribute("name"))
						{
							case "login":
							{
								TheRequestClientMessage mssg = new TheRequestClientMessage(TheRequestClientMessage.REQUEST_TYPE.LOGIN);
								mssg.setName(document.getElementsByTagName("name").item(0).getTextContent());
								String type = document.getElementsByTagName("type").item(0).getTextContent();
								switch (type)
								{
									case "xml":
									case "XML":
									{
										mssg.setType(MyClient.TYPE.XML);
										break;
									}
									case "obj":
									case "OBJ":
									case "os":
									case "OS":
									{
										mssg.setType(MyClient.TYPE.OS);
										break;
									}
								}
								mssg.process(theServer, XMLUser.this);
								break;
							}
							case "logout":
							{
								TheRequestClientMessage mssg = new TheRequestClientMessage(TheRequestClientMessage.REQUEST_TYPE.LOGOUT);
								mssg.setSessionID(Integer.parseInt(document.getElementsByTagName("session").item(0).getTextContent()));
								mssg.process(theServer, XMLUser.this);
								break;
							}
							case "list":
							{
								TheRequestClientMessage mssg = new TheRequestClientMessage(TheRequestClientMessage.REQUEST_TYPE.LIST);
								mssg.setSessionID(Integer.parseInt(document.getElementsByTagName("session").item(0).getTextContent()));
								mssg.process(theServer, XMLUser.this);
								break;
							}
							case "message":
							{
								TheClientMessage mssg = new TheClientMessage();
								mssg.setData(document.getElementsByTagName("message").item(0).getTextContent());
								mssg.setSessionID(Integer.parseInt(document.getElementsByTagName("session").item(0).getTextContent()));
								mssg.process(theServer, XMLUser.this);
								break;
							}
							default:
							{
								throw new Exception("message has an unknown type");
							}
						}
					}
				}
				catch (IOException e)
				{
					theLogger.error("stopped");
					theServer.disconnectTheClient(XMLUser.this);
				}
				catch (Exception e)
				{
					theLogger.error(e.getMessage());
				}
			}
		});

		writerThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					DataOutputStream dataOutputStream = new DataOutputStream(theSocket.getOutputStream());

					while (!Thread.interrupted())
					{
						ServerMessage message = (ServerMessage) messagesQueue.take();
						message.documentize(theServerHandler);

						Document document = docMessagesQueue.take();
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						Transformer transformer = TransformerFactory.newInstance().newTransformer();
						transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
						transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
						transformer.transform(new DOMSource(document), new StreamResult(byteArrayOutputStream));
						dataOutputStream.writeInt(byteArrayOutputStream.size());
						dataOutputStream.write(byteArrayOutputStream.toByteArray());
						dataOutputStream.flush();
						theLogger.info("sent a message to client: " + message.getClass().getName());
					}
				}
				catch (IOException e)
				{
					theLogger.error("IO Exception");
				}
				catch (InterruptedException e)
				{
					theLogger.error("interrupted");
				}
				catch (TransformerException e)
				{
					theLogger.error(e.getMessage());
				}
			}
		});
	}
}
