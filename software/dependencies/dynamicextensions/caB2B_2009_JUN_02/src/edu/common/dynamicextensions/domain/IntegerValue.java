/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_INTEGER_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 */
public class IntegerValue extends PermissibleValue implements IntegerValueInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = -1598647162647981710L;
	
	/**
	 * The predefined Integer value.
	 */
	protected Integer value;

	/**
	 * This method returns the predefined value of IntegerValue.
	 * @hibernate.property name="value" type="integer" column="VALUE" 
	 * @return the predefined value of IntegerValue.
	 */
	public Integer getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of IntegerValue to the given value.
	 * @param value the value to be set.
	 */
	public void setValue(Integer value)
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
