/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import java.util.Date;

import edu.common.dynamicextensions.domaininterface.DateTypeInformationInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_DATE_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DateAttributeTypeInformation extends AttributeTypeInformation
        implements
            DateTypeInformationInterface
{

    /**
     * Serial Version Unique Identifier
     */
    private static final long serialVersionUID = 5655678242696814276L;

    /**
     *  Default value of this date attribute.
     */
    protected Date defaultValue;

    /**
     * format of the attribute value (Data entry/display)
     */
    protected String format;

    /**
     * Empty Constructor
     */
    public DateAttributeTypeInformation()
    {

    }

    /**This method returns the format of the DateAttributeTypeInformation.
     * @hibernate.property name="format" type="string" column="FORMAT" 
     * @return Returns the format.
     */
    public String getFormat()
    {
        return format;
    }

    /**
     * @param format The format to set.
     */
    public void setFormat(String format)
    {
        this.format = format;
    }

	/** 
	 * @see edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface#getDataType()
	 */
	public String getDataType()
	{
		
		return EntityManagerConstantsInterface.DATE_ATTRIBUTE_TYPE;
	}

}