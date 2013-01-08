/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.StringTypeInformationInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_STRING_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class StringAttributeTypeInformation extends AttributeTypeInformation implements StringTypeInformationInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 8457915421460880246L;

	/**
	 * The size of the field.
	 */
	protected Integer size;

	/**
	 * Empty Constructor.
	 */
	public StringAttributeTypeInformation()
	{

	}

	/**
	 * @see java.lang.Object#finalize()
	 */
	public void finalize() throws Throwable
	{
		super.finalize();
	}

	/**
	 * This method returns the length of the string.
	 * @hibernate.property name="size" type="integer" column="MAX_SIZE" 
	 * @return Returns the length of the string.
	 */
	public Integer getSize()
	{
		return size;
	}

	/**
	 * @param size The size to set.
	 */
	public void setSize(Integer size)
	{
		this.size = size;
	}

	/** 
	 * @see edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface#getDataType()
	 */
	public String getDataType()
	{
		
		return EntityManagerConstantsInterface.STRING_ATTRIBUTE_TYPE;
	}
}