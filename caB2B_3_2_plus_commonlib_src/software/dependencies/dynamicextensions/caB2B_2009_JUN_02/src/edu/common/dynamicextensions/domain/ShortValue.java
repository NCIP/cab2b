
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ShortValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_SHORT_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 */
public class ShortValue extends PermissibleValue implements ShortValueInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 5377962679920380224L;

	/**
	 * The predefined Date value.
	 */
	protected Short value;

	/**
	 * This method returns the predefined value of ShortValue.
	 * @hibernate.property name="value" type="short" column="VALUE"  
	 * @return the predefined value of ShortValue.
	 */
	public Short getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of ShortValue to the given value.
	 * @param value the value to be set.
	 */
	public void setValue(Short value)
	{
		this.value = value;
	}

	/**
	 * This method returns the value of ShortValue downcasted to the Object.
	 * @return the value of the DateValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return value;
	}
	
}
