/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.util;

import java.util.Properties;

import edu.wustl.cab2b.common.util.Utility;

/**
 * @author gautam_shetty
 */
public class ClientPropertyLoader {
	private static Properties props = Utility
			.getPropertiesFromFile("client.properties");

	/**
	 * 
	 * @param gridType
	 * @return it returns the delegetee .. means identifier of cab2b server
	 */
	public static String getDelegetee(String gridType) {
		return props.getProperty(gridType + "_delegetee_identifier");
	}

	/**
	 * 
	 * @return the cdsdelegated url
	 */
	public static String getCDSDelegatedUrl() {
		return props.getProperty("cdsdelegatedurl");
	}

	/**
	 * @param gridType
	 * @return the CDS url for production grid and training grid
	 */
	public static String getCDSUrl(String gridType) {
		return props.getProperty(gridType + "_cds_url");
	}

	/**
	 * @param gridType
	 * @return Dorian url for given idP
	 */
	public static String getIdP(String gridType) {
		return props.getProperty(gridType + ".idP.url");
	}
	
	/**
	 * @param idP
     * @return Dorian url for given idP
	 */
	public static String getAuthenticationURL(String gridType) {
	    return props.getProperty(gridType + ".authentication.url");
	}

	/**
	 * @return all the names of identity providers to show in drop down of login
	 *         frame
	 */
	public static String[] getGridTypes() {
		String allNames = props.getProperty("grid.type");
		String[] idPNames = allNames.split(",");
		return idPNames;
	}
}