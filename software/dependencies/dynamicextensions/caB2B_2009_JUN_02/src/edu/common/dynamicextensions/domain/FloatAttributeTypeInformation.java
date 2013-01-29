/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.FloatTypeInformationInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;

/**
 * This Class represent the Floating value Attribute of the Entity.
 * @hibernate.joined-subclass table="DYEXTN_FLOAT_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 */
public class FloatAttributeTypeInformation extends NumericAttributeTypeInformation
		implements
			FloatTypeInformationInterface
{

	/**
	 * Empty Constructor.
	 */
	public FloatAttributeTypeInformation()
	{

	}
	
	/** 
	 * @see edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface#getDataType()
	 */
	public String getDataType()
	{
		
		return EntityManagerConstantsInterface.FLOAT_ATTRIBUTE_TYPE;
	}
}
