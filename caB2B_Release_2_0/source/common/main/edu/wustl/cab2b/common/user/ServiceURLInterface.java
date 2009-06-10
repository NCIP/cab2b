package edu.wustl.cab2b.common.user;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;

public interface ServiceURLInterface {

	Collection<UserInterface> getUsers();

	/**
	 * @return Returns the entityGroupInterface.
	 */
	EntityGroupInterface getEntityGroups();

	/**
	 * @param entityGroupInterface
	 *            The entityGroupInterface to set.
	 */
	void setEntityGroupInterface(EntityGroupInterface entityGroupInterface);

	/**
	 * @return Returns the urlId.
	 */
	Long getUrlId();

	/**
	 * @return Returns the urlLocation.
	 */
	String getUrlLocation();

	/**
	 * @return Returns the isAdminDefined.
	 */
	boolean isAdminDefined();
}
