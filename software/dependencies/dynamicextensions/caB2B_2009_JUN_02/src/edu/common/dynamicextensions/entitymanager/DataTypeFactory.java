/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.entitymanager;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.util.global.Variables;

/**
 * This is a Singleton class which parses the XML file that consists of mapping between PrimitiveAttribute 
 * and Database DataTypes and generates the Map of the same. 
 * @author chetan_patil
 */
public class DataTypeFactory
{

	/**
	 * 
	 */
	private static DataTypeFactory dataTypeFactory = null;

	/**
	 * 
	 */
	private Map<String, String> dataTypeMap;

	/**
	 * Empty constructor
	 */
	protected DataTypeFactory()
	{
	}

	/**
	 * This method returns the instance of DataTypeFactory
	 * @return DataTypeFactory instance
	 * @throws DataTypeFactoryInitializationException on Exception
	 */
	public static synchronized DataTypeFactory getInstance()
			throws DataTypeFactoryInitializationException
	{
		if (dataTypeFactory == null)
		{
			dataTypeFactory = new DataTypeFactory();
			String dataTypeMappingFileName = "PrimitiveAttributeDataTypes" + "_"
					+ Variables.databaseName + ".xml";
			dataTypeFactory.populateDataTypeMap(dataTypeMappingFileName);
		}
		return dataTypeFactory;
	}

	/**
	 * This method updates module map by parsing xml file
	 * @param xmlFileName file to be parsed
	 * @return dataType Map
	 * @throws DataTypeFactoryInitializationException on Exception
	 */
	public final Map populateDataTypeMap(String xmlFileName)
			throws DataTypeFactoryInitializationException
	{
		dataTypeMap = new HashMap<String, String>();

		SAXReader saxReader = new SAXReader();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(xmlFileName);

		Document document = null;

		try
		{
			document = saxReader.read(inputStream);
			Element name = null;
			Element databaseDataType = null;

			Element primitiveAttributesElement = document.getRootElement();
			Iterator primitiveAttributeElementIterator = primitiveAttributesElement
					.elementIterator("Primitive-Attribute");

			Element primitiveAttributeElement = null;

			while (primitiveAttributeElementIterator.hasNext())
			{
				primitiveAttributeElement = (Element) primitiveAttributeElementIterator.next();

				name = primitiveAttributeElement.element("name");
				databaseDataType = primitiveAttributeElement.element("database-datatype");

				dataTypeMap.put(name.getStringValue(), databaseDataType.getStringValue());
			}
		}
		catch (DocumentException documentException)
		{
			throw new DataTypeFactoryInitializationException(documentException);
		}

		return dataTypeMap;
	}

	/**
	 * This method returns the name of the Database Datatype given the name
	 * of the corresponding Primitive attribute.
	 * @param primitiveAttribute The name of the primitive attribute
	 * @return String The name of Database datatype
	 * @throws DataTypeFactoryInitializationException If dataTypeMap is unpopulated
	 */
	public String getDatabaseDataType(String primitiveAttribute)
			throws DataTypeFactoryInitializationException
	{
		String databaseDataType = null;
		if (dataTypeMap != null)
		{
			databaseDataType = (String) dataTypeMap.get(primitiveAttribute);
		}
		else
		{
			throw new DataTypeFactoryInitializationException("Cannot find populated dataType Map.");
		}
		return databaseDataType;
	}

}
