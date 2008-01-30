package edu.wustl.cab2b.common.user;

import java.util.Collection;

public interface UserInterface {
	
	Collection<ServiceURLInterface> getServiceURLCollection();
	
	void setServiceURLCollection(Collection<ServiceURLInterface> serviceURLCollection);
	
	Long getUserId();
	/**
	 * @param userId The userId to set.
	 */
	void setUserId(Long userId);

	/**
	 * @return Returns the userName.
	 */
	String getUserName();
	/**
	 * @param userName The userName to set.
	 */
	void setUserName(String userName);

}
