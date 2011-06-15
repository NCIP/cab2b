
package edu.common.dynamicextensions.domaininterface;

/**
 * The permissible value of type long. 
 * @author geetika_bangard
 */
public interface LongValueInterface extends PermissibleValueInterface
{

	/**
	 * This method returns the predefined value of LongValue.
	 * @return the predefined value of LongValue.
	 */
	Long getValue();

	/**
	 * This method sets the value of LongValue to the given value.
	 * @param value the value to be set.
	 */
	void setValue(Long value);
	
}
