/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.user;


public interface ServiceURLInterface {

	public String getEntityGroupName();

	
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
