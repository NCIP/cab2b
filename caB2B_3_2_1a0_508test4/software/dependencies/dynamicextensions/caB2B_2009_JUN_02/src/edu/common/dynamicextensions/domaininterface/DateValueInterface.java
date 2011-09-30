
package edu.common.dynamicextensions.domaininterface;

import java.util.Date;

/**
 * This object stores the permissible value of date type.This is a user defined value.
 * @author geetika_bangard
 */
public interface DateValueInterface extends PermissibleValueInterface
{

	/**
	 * This method returns the predefined value of DateValue.
	 * @return the predefined value of DateValue.
	 */
	Date getValue();

	/**
	 * This method sets the value of DateValue to the given value.
	 * @param value the value to be set.
	 */
	void setValue(Date value);
	
}
