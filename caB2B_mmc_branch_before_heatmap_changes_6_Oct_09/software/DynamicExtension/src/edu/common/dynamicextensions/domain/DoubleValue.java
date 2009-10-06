
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_DOUBLE_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DoubleValue extends PermissibleValue implements DoubleValueInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 2104021070651742508L;

	/**
	 * The predefined Double value.
	 */
	protected Double value;

	/**
	 * This method returns the predefined value of DoubleValue.
	 * @hibernate.property name="value" type="double" column="VALUE"   
	 * @return the predefined value of DoubleValue.
	 */
	public Double getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of DoubleValue to the given value.
	 * @param value the value to be set.
	 */
	public void setValue(Double value)
	{
		this.value = value;
	}

	/**
	 * This method returns the value of DoubleValue downcasted to the Object.
	 * @return the value of DateValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return value;
	}

}
