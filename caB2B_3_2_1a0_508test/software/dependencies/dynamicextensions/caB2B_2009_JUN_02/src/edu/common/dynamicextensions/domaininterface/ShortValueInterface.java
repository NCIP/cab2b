
package edu.common.dynamicextensions.domaininterface;

/**
 * The permissible value of type short. 
 * @author geetika_bangard
 */
public interface ShortValueInterface extends PermissibleValueInterface
{
	/**
	 * This method returns the predefined value of ShortValue.
	 * @return the predefined value of ShortValue.
	 */
	Short getValue();

	/**
	 * This method sets the value of ShortValue to the given value.
	 * @param value the value to be set.
	 */
	void setValue(Short value);
	
}
