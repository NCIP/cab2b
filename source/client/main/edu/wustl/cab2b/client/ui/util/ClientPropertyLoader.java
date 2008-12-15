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
	 * @param idP
	 * @return it returns the delegetee .. means identifier of cab2b server
	 */
	public static String getDelegetee(String idP) {
		return props.getProperty(idP + "_delegetee_identifier");
	}

	/**
	 * 
	 * @return the cdsdelegated url
	 */
	public static String getCDSDelegatedUrl() {
		return props.getProperty("cdsdelegatedurl");
	}

	/**
	 * @return the CDS url for production grid and training grid
	 */
	public static String getCDSUrl(String idP) {
		return props.getProperty(idP + "_cds_url");
	}

	/**
	 * @param idP
	 * @return returns the target grid certificate ,needed for generating Globus
	 *         certificate for user
	 */
	public static String getGridCertForGlobus(String idP) {
		return props.getProperty(idP + "_grid_gloubus");
	}

	/**
	 * @param idP
	 * @return Dorian url for given idP
	 */
	public static String getDorianUrl(String idP) {
		return props.getProperty(idP + ".dorian.url");
	}

	/**
	 * @return all the names of identity providers to show in drop down of login
	 *         frame
	 */
	public static String[] getIdPNames() {
		String allNames = props.getProperty("IdPs");
		String[] idPNames = allNames.split(",");
		return idPNames;
	}
}