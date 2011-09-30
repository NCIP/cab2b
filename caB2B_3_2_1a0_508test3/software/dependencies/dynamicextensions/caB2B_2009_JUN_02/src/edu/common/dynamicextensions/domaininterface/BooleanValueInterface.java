
package edu.common.dynamicextensions.domaininterface;

/**
 * This object stores the permissible value of boolean type.This is a user defined value.
 * @author geetika_bangard
 */
public interface BooleanValueInterface extends PermissibleValueInterface
{

	/**
	 * This method returns the value of the BooleanValue.
	 * @return the value of the BooleanValue.
	 */
	Boolean getValue();

	/**
	 * This method sets the value of the BooleanValue.
	 * @param value the value to be set.
	 */
	void setValue(Boolean value);

}
