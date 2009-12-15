
package edu.common.dynamicextensions.domaininterface;

/**
 * The permissible value of type integer. 
 * @author geetika_bangard
 */
public interface IntegerValueInterface extends PermissibleValueInterface
{

	/**
	 * This method returns the predefined value of IntegerValue.
	 * @return the predefined value of IntegerValue.
	 */
	Integer getValue();

	/**
	 * This method sets the value of IntegerValue to the given value.
	 * @param value the value to be set.
	 */
	void setValue(Integer value);
	
}
