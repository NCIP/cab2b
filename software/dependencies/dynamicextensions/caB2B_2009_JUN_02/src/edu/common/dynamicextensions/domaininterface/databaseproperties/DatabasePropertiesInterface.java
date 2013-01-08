/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface.databaseproperties;

import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;

/**
 * The database properties are the properties of the dynamically created tables or
 * columns from those tables.
 * @author geetika_bangard
 */
public interface DatabasePropertiesInterface extends DynamicExtensionBaseDomainObjectInterface
{
	
	/**
	 * This method returns the Unique Identifier of the Object.
	 * @return the Unique Identifier of the Object.
	 */
	Long getId();

	/**
	 * This method returns the name of the table or name of the column.
	 * @return the name of the table or name of the column.
	 */
	String getName();

	/**
	 * This method sets the name of the table or name of the column.
	 * @param name the name of the table or name of the column to be set.
	 */
	void setName(String name);

}
