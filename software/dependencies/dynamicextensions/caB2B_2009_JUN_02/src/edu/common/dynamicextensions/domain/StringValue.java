/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_STRING_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class StringValue extends PermissibleValue implements StringValueInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 6552718005216538542L;

	/**
	 * The predefined String value.
	 */
	protected String value;

	/**
	 * This method returns the predefined value of StringValue.
	 * @hibernate.property name="value" type="string" column="VALUE"  
	 * @return the predefined value of StringValue.
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of DateValue to the given value.
	 * @param value the value to be set.
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * This method returns the value of DateValue downcasted to the Object.
	 * @return the value of the DateValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return value;
	}
	
}
