package ru.nsu.ccfit.izhitsky.Client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.nsu.ccfit.izhitsky.Messages.*;
import ru.nsu.ccfit.izhitsky.User;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.concurrent.ArrayBlockingQueue;

public class TheXMLMessageHandler
{
	private static final Logger theLogger = LogManager.getLogger(TheXMLMessageHandler.class);

	private ArrayBlockingQueue<Document> messagesQueue;

	public TheXMLMessageHandler(ArrayBlockingQueue<Document> messagesQueue_)
	{
		this.messagesQueue = messagesQueue_;
	}

	public void documentize(TheClientMessage theMessage)
	{
		try
		{
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = document.createElement("command");
			root.setAttribute("name","message");
			document.appendChild(root);
			Element name = document.createElement("message");
			name.setTextContent(theMessage.getData());
			root.appendChild(name);
			Element type = document.createElement("session");
			type.setTextContent(Integer.toString(theMessage.getSessionID()));
			root.appendChild(type);
			messagesQueue.add(document);
		}
		catch (ParserConfigurationException e)
		{
			theLogger.error(e.getMessage());
		}
		theLogger.info("simple message is processing " + theMessage.getClass());
	}

	public void documentize(TheRequestClientMessage theMessage)
	{
		switch (theMessage.getRequestType())
		{
			case LIST:
			{
				try
				{
					Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element event = document.createElement("command");
					Attr eventName = document.createAttribute("name");
					eventName.setValue("list");
					event.setAttributeNode(eventName);
					document.appendChild(event);
					Element sender = document.createElement("session");
					sender.setTextContent(String.valueOf(theMessage.getSessionID()));
					event.appendChild(sender);
					messagesQueue.add(document);

					/*Element root = document.createElement("event");
					root.setAttribute("name", "message");
					document.appendChild(root);
					Element m = document.createElement("message");
					m.setTextContent(theMessage.getData());
					root.appendChild(m);
					Element from = document.createElement("login");
					from.setTextContent(theMessage.getUserName());
					root.appendChild(from);*/
				}
				catch (ParserConfigurationException e)
				{
					theLogger.error(e.getMessage());
				}
				theLogger.info("user is processing " + theMessage.getClass());
				break;
			}
			case LOGIN:
			{
				try
				{
					Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element event = document.createElement("command");
					Attr eventName = document.createAttribute("name");
					eventName.setValue("login");
					event.setAttributeNode(eventName);
					document.appendChild(event);
					Element sender = document.createElement("name");
					sender.setTextContent(theMessage.getUser().getName());
					event.appendChild(sender);
					Element type = document.createElement("type");
					switch (theMessage.getUser().getType())
					{
						case XML:
						{
							type.setTextContent("xml");
							break;
						}
						case OS:
						{
							type.setTextContent("obj");
							break;
						}
					}
					event.appendChild(type);
					messagesQueue.add(document);

					/*Element root = document.createElement("command");
					root.setAttribute("name","userlogin");
					document.appendChild(root);
					Element name = document.createElement("name");
					name.setTextContent(theMessage.getUser().getName());
					root.appendChild(name);*/
				}
				catch (ParserConfigurationException e)
				{
					theLogger.error(e.getMessage());
				}
				theLogger.info("user login is processing " + theMessage.getClass());
				break;
			}
			case LOGOUT:
			{
				try
				{
					Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element event = document.createElement("command");
					Attr eventName = document.createAttribute("name");
					eventName.setValue("logout");
					event.setAttributeNode(eventName);
					document.appendChild(event);
					Element sender = document.createElement("session");
					sender.setTextContent(String.valueOf(theMessage.getSessionID()));
					event.appendChild(sender);
					messagesQueue.add(document);

					/*Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element root = document.createElement("event");
					root.setAttribute("name","userlogout");
					document.appendChild(root);
					Element name = document.createElement("name");
					name.setTextContent(theMessage.getUser().getName());
					root.appendChild(name);*/
				}
				catch (ParserConfigurationException e)
				{
					theLogger.error(e.getMessage());
				}
				theLogger.info("user logout is processing " + theMessage.getClass());
				break;
			}
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////

	public void documentize(TheTextServerMessage theMessage)
	{
		switch (theMessage.getType())
		{
			case SUCCESS:
			{
				try
				{
					Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element root = document.createElement("success");
					document.appendChild(root);
					messagesQueue.add(document);
				}
				catch (ParserConfigurationException e)
				{
					theLogger.error(e.getMessage());
				}
				theLogger.info("text success is processing " + theMessage.getClass());
				break;
			}
			case ERROR:
			{
				try
				{
					Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element root = document.createElement("error");
					document.appendChild(root);
					Element child = document.createElement("message");
					child.setTextContent(theMessage.getErrorMessage());
					root.appendChild(child);
					messagesQueue.add(document);
				}
				catch (ParserConfigurationException e)
				{
					theLogger.error(e.getMessage());
				}
				theLogger.info("text error is processing " + theMessage.getClass());
				break;
			}
		}
	}

	public void documentize(TheUserServerMessage theMessage)
	{
		switch (theMessage.getType())
		{
			case DEFAULT:
			{
				try
				{
					Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element event = document.createElement("event");
					Attr eventName = document.createAttribute("name");
					eventName.setValue("message");
					event.setAttributeNode(eventName);
					document.appendChild(event);

					Element messageText = document.createElement("message");
					messageText.setTextContent(theMessage.getData());
					event.appendChild(messageText);

					Element sender = document.createElement("name");
					sender.setTextContent(theMessage.getUserName());
					event.appendChild(sender);
					messagesQueue.add(document);


				}
				catch (ParserConfigurationException e)
				{
					theLogger.error(e.getMessage());
				}
				break;
			}
			case LOGIN:
			{
				try
				{
					Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element root = document.createElement("event");
					root.setAttribute("name","userlogin");
					document.appendChild(root);
					Element name = document.createElement("name");
					name.setTextContent(theMessage.getUserName());
					root.appendChild(name);
					messagesQueue.add(document);
				}
				catch (ParserConfigurationException e)
				{
					theLogger.error(e.getMessage());
				}
				theLogger.info("TheUserServerMessage.login is processing " + theMessage.getClass());
				break;
			}
			case LOGOUT:
			{
				try
				{
					Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element root = document.createElement("event");
					root.setAttribute("name","userlogout");
					document.appendChild(root);
					Element name = document.createElement("name");
					name.setTextContent(theMessage.getUserName());
					root.appendChild(name);
					messagesQueue.add(document);
				}
				catch (ParserConfigurationException e)
				{
					theLogger.error(e.getMessage());
				}
				theLogger.info("TheUserServerMessage.logout is processing " + theMessage.getClass());
				break;
			}
		}
	}

	public void documentize(TheActionServerMessage theMessage)
	{
		switch (theMessage.getRequestType())
		{
			case LIST:
			{
				switch (theMessage.getStatus())
				{
					case SUCCESS:
					{
						try
						{
							Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
							Element root = document.createElement("success");
							document.appendChild(root);
							Element listusers = document.createElement("listusers");
							root.appendChild(listusers);
							for (User u : theMessage.getUsers())
							{
								Element user = document.createElement("user");
								listusers.appendChild(user);
								Element name = document.createElement("name");
								name.setTextContent(u.getName());
								user.appendChild(name);
								Element type = document.createElement("type");
								type.setTextContent("xml");
								user.appendChild(type);
							}
							messagesQueue.add(document);
						}
						catch (ParserConfigurationException e)
						{
							theLogger.error(e.getMessage());
						}
						theLogger.info("LIST::SUCCESS is processing " + theMessage.getClass());
						break;
					}
					case ERROR:
					{
						try
						{
							Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
							Element root = document.createElement("error");
							document.appendChild(root);
							Element child = document.createElement("message");
							child.setTextContent(theMessage.getErrorMessage());
							root.appendChild(child);
							messagesQueue.add(document);
						}
						catch (ParserConfigurationException e)
						{
							theLogger.error(e.getMessage());
						}
						theLogger.info("LIST::ERROR is processing " + theMessage.getClass());
						break;
					}
				}
				break;
			}
			case LOGIN:
			{
				switch (theMessage.getStatus())
				{
					case SUCCESS:
					{
						try
						{
							Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
							Element root = document.createElement("success");
							document.appendChild(root);
							Element child = document.createElement("session");
							child.setTextContent(Integer.toString(theMessage.getSessionID()));
							root.appendChild(child);
							messagesQueue.add(document);
						}
						catch (ParserConfigurationException e)
						{
							theLogger.error(e.getMessage());
						}
						theLogger.info("LOGIN:SUCCESS is processing " + theMessage.getClass());
						break;
					}
					case ERROR:
					{
						try
						{
							Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
							Element root = document.createElement("error");
							document.appendChild(root);
							Element child = document.createElement("message");
							child.setTextContent(theMessage.getErrorMessage());
							root.appendChild(child);
							messagesQueue.add(document);
						}
						catch (ParserConfigurationException e)
						{
							theLogger.error(e.getMessage());
						}
						theLogger.info("LOGIN:ERROR is processing " + theMessage.getClass());
						break;
					}
				}
				break;
			}
			case LOGOUT:
			{
				switch (theMessage.getStatus())
				{
					case SUCCESS:
					{
						try
						{
							Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
							Element root = document.createElement("success");
							document.appendChild(root);
							messagesQueue.add(document);
						}
						catch (ParserConfigurationException e)
						{
							theLogger.error(e.getMessage());
						}
						theLogger.info("XML_LOGOUT::SUCCESS is processing " + theMessage.getClass());
						break;
					}
					case ERROR:
					{
						try
						{
							Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
							Element root = document.createElement("error");
							document.appendChild(root);
							Element child = document.createElement("message");
							child.setTextContent(theMessage.getErrorMessage());
							root.appendChild(child);
							messagesQueue.add(document);

						}
						catch (ParserConfigurationException e)
						{
							theLogger.error(e.getMessage());
						}
						theLogger.info("XML_LOGOUT::ERROR is processing " + theMessage.getClass());
						break;
					}
				}
				break;
			}
		}
	}
}
