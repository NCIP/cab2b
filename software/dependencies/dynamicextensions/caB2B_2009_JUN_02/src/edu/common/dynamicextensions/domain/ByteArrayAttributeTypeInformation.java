/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ByteArrayTypeInformationInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_BYTE_ARRAY_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ByteArrayAttributeTypeInformation extends AttributeTypeInformation implements ByteArrayTypeInformationInterface
{
	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 7925152800204580441L;

	/**
	 * Content type for this file.
	 */
	protected String contentType;

	/**
	 * Empty Constructor.
	 */
	public ByteArrayAttributeTypeInformation()
	{
	}

	/**
	 * This method returns the Content type of the binary data (or file), e.g. JPG, DOC etc..
	 * @hibernate.property name="contentType" type="string" column="CONTENT_TYPE" 
	 * @return the Content type of the binary data (or file).
	 */
	public String getContentType()
	{
		return contentType;
	}

	/**
	 * This method sets the Content type of the binary data (or file) to be stored.
	 * @param contentType The contentType to be set.
	 */
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getDataType()
	{
		
		return null;
	}

}