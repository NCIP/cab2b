/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;

/**
 * The permissible value of type double. 
 * @author geetika_bangard
 */
public interface DoubleValueInterface extends PermissibleValueInterface
{

	/**
	 * This method returns the predefined value of DoubleValue.
	 * @return the predefined value of DoubleValue.
	 */
	Double getValue();

	/**
	 * This method sets the value of DoubleValue to the given value.
	 * @param value the value to be set.
	 */
	void setValue(Double value);
	
}
