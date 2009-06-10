package edu.wustl.cab2b.common.user;

import java.util.Collection;

public interface UserInterface {

	Collection<ServiceURLInterface> getServiceURLCollection();

	/**
	 * @return Returns userId
	 */
	Long getUserId();

	/**
	 * @return Returns the userName.
	 */
	String getUserName();
}