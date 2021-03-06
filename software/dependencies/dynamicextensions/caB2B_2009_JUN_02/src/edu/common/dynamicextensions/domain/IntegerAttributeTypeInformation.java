/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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
