
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.IntegerTypeInformationInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_INTEGER_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 *
 */
public class IntegerAttributeTypeInformation extends NumericAttributeTypeInformation
		implements
			IntegerTypeInformationInterface
{

	/**
	 * Empty Constructor.
	 */
	public IntegerAttributeTypeInformation()
	{

	}
	
	/** 
	 * @see edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface#getDataType()
	 */
	public String getDataType()
	{
		
		return EntityManagerConstantsInterface.INTEGER_ATTRIBUTE_TYPE;
	}
}
