
package edu.common.dynamicextensions.domaininterface.userinterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;

/**
 * ViewInterface stores necessary information for generating view on
 * dynamically generated user interface.  
 * @author geetika_bangard
 */
public interface ViewInterface extends DynamicExtensionBaseDomainObjectInterface
{

	/**
	 * @return Returns the id.
	 */
	Long getId();

	/**
	 * @return Returns the name.
	 */
	String getName();

	/**
	 * @param name The name to set.
	 */
	void setName(String name);

	/**
	 * @return Returns the containerCollection.
	 */
	Collection getContainerCollection();

	/**
	 * @param containerInterface The containerInterface to be added.
	 */
	void addContainer(ContainerInterface containerInterface);

}
