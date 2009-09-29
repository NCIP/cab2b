/**
 * <p>Title:XMLPropertyHandler Class>
 * <p>Description:This class parses from caTissue_Properties.xml(includes properties name & value pairs) file using DOM parser.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Tapan Sahoo
 * @version 1.00
 * Created on May 15, 2006
 */

package edu.wustl.common.util;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * This class gives the properties value by giving properties name.
 * 
 * @author tapan_sahoo
 */
public class XMLPropertyHandler
{

	private static Document document = null;

	public static void main(String[] args) throws Exception
	{
		Variables.applicationHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure("Logger.properties");

		XMLPropertyHandler.init("caTissueCore_Properties.xml");

		String propertyValue1 = XMLPropertyHandler.getValue("server.port");
		Logger.out.debug(""+propertyValue1);
	}

	public static void init(String path) throws Exception
	{
		Logger.out.debug("path.........................."+path);
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();// throws
			// ParserConfigurationException
			if (path != null)
			{
				document = dbuilder.parse(path);
				// throws SAXException,IOException,IllegalArgumentException(if path is null
			}
		}
		catch (SAXException e)
		{
			Logger.out.error(e.getMessage(),e);
			throw e;
		}
		catch (IOException e)
		{
			Logger.out.error(e.getMessage(),e);
			throw e;
		}
		catch (ParserConfigurationException e)
		{
			Logger.out.error("Could not locate a JAXP parser: "+e.getMessage(),e);
			throw e;
		}
	}

	/**
	 * <p>
	 * Description:This method takes the property name as String argument and
	 * returns the properties value as String. Put the xml file in the path as
	 * you will provide the path
	 * </p>
	 */

	public static String getValue(String propertyName)
	{
		// it gives the rootNode of the xml file
		Element root = document.getDocumentElement();

		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node child = children.item(i);

			if (child instanceof Element)
			{
				// it gives the subchild nodes in the xml file(name & value)
				NodeList subChildNodes = child.getChildNodes();

				boolean isNameFound = false;
				//Logger.out.debug("subchildNodes : "+subChildNodes.getLength()); 
				for (int j = 0; j < subChildNodes.getLength(); j++)
				{
					Node subchildNode = subChildNodes.item(j);
					String subNodeName = subchildNode.getNodeName();
					//Logger.out.debug("subnodeName : "+subNodeName);
					if (subNodeName.equals("name"))
					{
						String pName = (String) subchildNode.getFirstChild().getNodeValue();
						//Logger.out.debug("pName : "+pName);
						if (propertyName.equals(pName))
						{
							//Logger.out.debug("pName : "+pName);
							isNameFound = true;
						}
					}
					
					if(isNameFound && subNodeName.equals("value"))
					{
						//Check for null
						String pValue="";
						if(subchildNode!=null&&subchildNode.getFirstChild()!=null)
						{
							pValue = (String) subchildNode.getFirstChild().getNodeValue();
						}
						return pValue;
					}
				}
			}
		}
		return null;
	}
}