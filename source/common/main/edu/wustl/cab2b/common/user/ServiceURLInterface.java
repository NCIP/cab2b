package edu.wustl.cab2b.common.user;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;

public interface ServiceURLInterface {
	
	Collection<UserInterface> getUserCollection();
	
	void setUserCollection(Collection<UserInterface> userCollection);
	
	/**
	 * @return Returns the entityGroupInterface.
	 */
	EntityGroupInterface getEntityGroupInterface();
	/**
	 * @param entityGroupInterface The entityGroupInterface to set.
	 */
	void setEntityGroupInterface(EntityGroupInterface entityGroupInterface);

	/**
	 * @return Returns the urlId.
	 */
	Long getUrlId();

	/**
	 * @param urlId The urlId to set.
	 */
	void setUrlId(Long urlId);
	/**
	 * @return Returns the urlLocation.
	 */
	String getUrlLocation();

	/**
	 * @param urlLocation The urlLocation to set.
	 */
	void setUrlLocation(String urlLocation);
	

	/**
	 * @return Returns the isAdminDefined.
	 */
	boolean isAdminDefined();

	/**
	 * @param isAdminDefined The isAdminDefined to set.
	 */
	void setAdminDefined(boolean isAdminDefined);
}
