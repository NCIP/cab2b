/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.joined-subclass table="DYEXTN_BOOLEAN_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 *
 */
public class BooleanValue extends PermissibleValue implements BooleanValueInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 6775534423957386289L;
	
	/**
	 * The value to be stored.
	 */
	protected Boolean value;

	/**
	 * This method returns the value of the BooleanValue.
	 * @hibernate.property name="value" type="boolean" column="VALUE" 
	 * @return the value of the BooleanValue.
	 */
	public Boolean getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of the BooleanValue.
	 * @param value the value to be set.
	 */
	public void setValue(Boolean value)
	{
		this.value = value;
	}

	/**
	 * This method returns the value of BooleanValue downcasted to the Object.
	 * @return the value of BooleanValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return value;
	}
	
}
