
package edu.common.dynamicextensions.domaininterface;

/**
 * The permissible value of type float. 
 * @author geetika_bangard
 */
public interface FloatValueInterface extends PermissibleValueInterface
{

	/**
	 * This method returns the predefined value of FloatValue.
	 * @return Returns the predefined value of FloatValue.
	 */
	Float getValue();

	/**
	 * This method sets the value of FloatValue to the given value.
	 * @param value the value to be set.
	 */
	void setValue(Float value);
	
}
