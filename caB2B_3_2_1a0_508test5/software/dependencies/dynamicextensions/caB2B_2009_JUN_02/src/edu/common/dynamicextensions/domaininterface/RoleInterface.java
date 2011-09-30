
package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;

/**
 * For every entity association there are two roles invoved.They are source role and target role.
 * @author sujay_narkar
 *
 */
public interface RoleInterface extends DynamicExtensionBaseDomainObjectInterface
{

	/**
	 * This method returns the Unique identifier of this Object.
	 * @return the Unique identifier of this Object.
	 */
	Long getId();

	/**
	 * This method returns the type of Association.
	 * @return the type of Association. 
	 */
	AssociationType getAssociationsType();

	/**
	 * This method sets the type of Association. 
	 * @param associationType the type of Association to be set.
	 */
	void setAssociationsType(AssociationType associationType);

	/**
	 * This method returns the maximum cardinality.
	 * @return the maximum cardinality.
	 */
	Cardinality getMaximumCardinality();

	/**
	 * This method sets the maximum cardinality.
	 * @param maxCardinality the value to be set as maximum cardinality.
	 */
	void setMaximumCardinality(Cardinality maxCardinality);

	/**
	 * This method returns the minimum cardinality.
	 * @return Returns the minimum cardinality.
	 */
	Cardinality getMinimumCardinality();

	/**
	 * This method sets the minimum cardinality.
	 * @param minCardinality the value to be set as minimum cardinality.
	 */
	void setMinimumCardinality(Cardinality minCardinality);

	/**
	 * This method returns the name of the role.
	 * @return the name of the role.
	 */
	String getName();

	/**
	 * This method sets the name of the role.
	 * @param name the name to be set.
	 */
	void setName(String name);

}
