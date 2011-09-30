
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

/**
 * When the permissible values for an attribute are user defined the data element is of type
 * UserDefinedDE.This type of data element contains collection of user defined permissible values. 
 * @author sujay_narkar
 * @version 1.0
 */
public interface UserDefinedDEInterface extends DataElementInterface
{

	/**
	 * This method returns the Collection of PermissibleValues.
	 * @return the Collection of PermissibleValues.
	 */
	Collection<PermissibleValueInterface> getPermissibleValueCollection();

	/**
	 * This method adds a PermissibleValue to the Collection of PermissibleValues.
	 * @param permissibleValue the PermissibleValue to be added.
	 */
	void addPermissibleValue(PermissibleValueInterface permissibleValue);

}
