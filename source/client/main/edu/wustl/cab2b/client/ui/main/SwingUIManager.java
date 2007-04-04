/**
 *<p>Copyright: (c) Washington University, School of Medicine 2006.</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *<p>ClassName: edu.wustl.cab2b.client.ui.main.SwingUIManager </p> 
 */

package edu.wustl.cab2b.client.ui.main;

import java.lang.reflect.Constructor;

import edu.common.dynamicextensions.domain.BooleanAttributeTypeInformation;
import edu.common.dynamicextensions.domain.ByteArrayAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.FloatAttributeTypeInformation;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domain.NumericAttributeTypeInformation;
import edu.common.dynamicextensions.domain.ShortAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.CheckedException;

/**
 * This class is used for creating UI panel that shows attribute name, data-type 
 * conditions and components for entering/showing the values.
 * 
 * @author Kaushal Kumar
 * @version 1.0
 */
public class SwingUIManager
{

	/**
	 * This method returns a panel object that shows attribute name, data-type 
	 * conditions and components for entering/showing the values.
	 * 
	 * @param parseFile
	 * @param entity
	 * @return panel object
	 */
	public static Object generateUIPanel(ParseXMLFile parseFile, AttributeInterface attributeEntity)
			throws CheckedException
	{
		Object[] object = null;
		Object uiObject = null;
		Constructor[] cls = null;
		AttributeTypeInformationInterface attributeTypeInformation = attributeEntity
				.getAttributeTypeInformation();
		// Check if attribute has value domain (i.e it is enum)
		if (true == edu.wustl.cab2b.common.util.Utility.isEnumerated(attributeEntity))
		{
			if ((attributeTypeInformation instanceof StringAttributeTypeInformation)
					|| (attributeTypeInformation instanceof ByteArrayAttributeTypeInformation))
			{
				final String dataTypeString = "string";
				try
				{
					cls = Class.forName(parseFile.getEnumClassName(dataTypeString))
							.getConstructors();
					object = new Object[2];
					object[0] = parseFile.getEnumConditionList(dataTypeString);
					object[1] = attributeEntity;
					uiObject = cls[0].newInstance(object);
				}
				catch (Exception e)
				{
					throw new CheckedException(e.getMessage(), e, ErrorCodeConstants.RF_0001);
				}
			}
			else if ((attributeTypeInformation instanceof IntegerAttributeTypeInformation)
					|| (attributeTypeInformation instanceof LongAttributeTypeInformation)
					|| (attributeTypeInformation instanceof FloatAttributeTypeInformation)
					|| (attributeTypeInformation instanceof DoubleAttributeTypeInformation)
					|| (attributeTypeInformation instanceof NumericAttributeTypeInformation)
					|| (attributeTypeInformation instanceof ShortAttributeTypeInformation))

			{
				final String dataTypeString = "number";
				try
				{
					cls = Class.forName(parseFile.getEnumClassName(dataTypeString))
							.getConstructors();
					object = new Object[2];
					object[0] = parseFile.getEnumConditionList(dataTypeString);
					object[1] = attributeEntity;
					uiObject = cls[0].newInstance(object);

				}
				catch (Exception e)
				{
					throw new CheckedException(e.getMessage(), e, ErrorCodeConstants.RF_0001);
				}
			}
			else if (attributeTypeInformation instanceof BooleanAttributeTypeInformation)
			{
				final String dataTypeString = "boolean";
				try
				{
					cls = Class.forName(parseFile.getEnumClassName(dataTypeString))
							.getConstructors();
					object = new Object[2];
					object[0] = parseFile.getEnumConditionList(dataTypeString);
					object[1] = attributeEntity;
					uiObject = cls[0].newInstance(object);
				}
				catch (Exception e)
				{
					throw new CheckedException(e.getMessage(), e, ErrorCodeConstants.RF_0001);
				}
			}
			else if (attributeTypeInformation instanceof DateAttributeTypeInformation)
			{
				final String dataTypeString = "date";
				try
				{
					cls = Class.forName(parseFile.getEnumClassName(dataTypeString))
							.getConstructors();
					object = new Object[2];
					object[0] = parseFile.getEnumConditionList(dataTypeString);
					object[1] = attributeEntity;
					uiObject = cls[0].newInstance(object);
				}
				catch (Exception e)
				{
					throw new CheckedException(e.getMessage(), e, ErrorCodeConstants.RF_0001);
				}
			}

		}
		else
		/* If an entity is of non-enumerated type. */
		{
			if ((attributeTypeInformation instanceof StringAttributeTypeInformation)
					|| (attributeTypeInformation instanceof ByteArrayAttributeTypeInformation))
			{
				final String dataTypeString = "string";
				try
				{
					cls = Class.forName(parseFile.getNonEnumClassName(dataTypeString))
							.getConstructors();
					object = new Object[2];
					object[0] = parseFile.getNonEnumConditionList(dataTypeString);
					object[1] = attributeEntity;
					uiObject = cls[0].newInstance(object);
					//System.out.println(uiObject.toString());
				}
				catch (Exception e)
				{
					throw new CheckedException(e.getMessage(), e, ErrorCodeConstants.RF_0001);
				}

			}
			else if ((attributeTypeInformation instanceof IntegerAttributeTypeInformation)
					|| (attributeTypeInformation instanceof LongAttributeTypeInformation)
					|| (attributeTypeInformation instanceof FloatAttributeTypeInformation)
					|| (attributeTypeInformation instanceof DoubleAttributeTypeInformation)
					|| (attributeTypeInformation instanceof NumericAttributeTypeInformation)
					|| (attributeTypeInformation instanceof ShortAttributeTypeInformation))
			{
				final String dataTypeString = "number";
				try
				{
					cls = Class.forName(parseFile.getNonEnumClassName(dataTypeString))
							.getConstructors();
					object = new Object[2];
					object[0] = parseFile.getNonEnumConditionList(dataTypeString);
					object[1] = attributeEntity;
					uiObject = cls[0].newInstance(object);
				}
				catch (Exception e)
				{
					throw new CheckedException(e.getMessage(), e, ErrorCodeConstants.RF_0001);
				}
			}
			else if (attributeTypeInformation instanceof DateAttributeTypeInformation)
			{
				final String dataTypeString = "date";
				try
				{
					cls = Class.forName(parseFile.getNonEnumClassName(dataTypeString))
							.getConstructors();
					//System.out.println(cls.length);
					object = new Object[2];
					object[0] = parseFile.getNonEnumConditionList(dataTypeString);
					object[1] = attributeEntity;
					uiObject = cls[0].newInstance(object);
				}
				catch (Exception e)
				{
					throw new CheckedException(e.getMessage(), e, ErrorCodeConstants.RF_0001);
				}
			}
			else if (attributeTypeInformation instanceof BooleanAttributeTypeInformation)
			{
				try
				{
					final String dataTypeString = "boolean";
					cls = Class.forName(parseFile.getNonEnumClassName(dataTypeString))
							.getConstructors();
					object = new Object[2];
					object[0] = parseFile.getNonEnumConditionList(dataTypeString);
					object[1] = attributeEntity;
					uiObject = cls[0].newInstance(object);
				}
				catch (Exception e)
				{
					throw new CheckedException(e.getMessage(), e, ErrorCodeConstants.RF_0001);
				}
			}
		}
		return uiObject;
	}
}