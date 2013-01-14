/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.util;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class handles fetching properties from cab2b.properties file
 * 
 * @author Chandrakant_Talele
 * @author lalit_chand
 */
public class PropertyLoader {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(PropertyLoader.class);

    private static final String propertyfile = "cab2b.properties";

    private static Properties props = Utility.getPropertiesFromFile(propertyfile);

    /**
     * Returns the Path of domain model XML file
     * 
     * @param applicationName
     *            Name of the application
     * @return Returns the File Path
     */
    public static String getModelPath(String applicationName) {
        String path = props.getProperty(applicationName + ".ModelPath");
        if (path == null || path.length() == 0) {
            logger.error("Model path for application : " + applicationName + " is not configured in "
                    + propertyfile);
        }
        return path;
    }

    /**
     * Returns names of all application for which caB2B is configured
     * 
     * @return Returns the Application Names
     */
    public static String[] getAllApplications() {
        String[] allApplications = props.getProperty("all.applications").split(",");
        if (allApplications == null || allApplications.length == 0) {
            logger.error("No value for key 'all.applications' is found in " + propertyfile);
        }

        return allApplications;
    }

    /**
     * @return The URL of JNDI service running on caB2B server
     */
    public static String getJndiUrl() {
        String serverIP = props.getProperty("caB2B.server.ip");
        String jndiPort = props.getProperty("caB2B.server.port");
        return "jnp://" + serverIP + ":" + jndiPort;
    }

    /**
     * @param idP
     * @return Dorian url for given idP
     */
    public static String getDorianUrl(String idP) {
        return props.getProperty(idP + ".dorian.url");
    }

    /**
     * @return all the names of identity providers to show in drop down of login frame
     */
    public static String[] getIdPNames() {
        String allNames = props.getProperty("IdPs");
        String[] idPNames = allNames.split(",");
        return idPNames;
    }

    /**
     * @return all the index urls used to get the service information
     */
    public static String[] getIndexServiceUrls() {
        String allUrls = props.getProperty("indexurls");
        String[] urls = allUrls.split(",");
        return urls;
    }

    /**
     * 
     * @return the cdsdelegated url
     */
    public static String getCDSDelegatedUrl() {
        String cdsUrl = props.getProperty("cdsdelegatedurl");
        return cdsUrl;
    }

    /**
     * 
     * @return the cdstraining url
     */
    public static String getCDSTrainingUrl() {
        String cdsTrainingUrl = props.getProperty("cdstrainingurl");
        return cdsTrainingUrl;
    }

    /**
     * 
     * @return the training grid cert location
     */
    public static String getTrainingGridCert() {
        String trainingCert = props.getProperty("training_grid_cert_location");
        return trainingCert;
    }

    
    
    /**
     * 
     * @return the training grid key location
     */
    public static String getTrainingGridKey() {
        String trainingKey = props.getProperty("training_grid_key_location");
        return trainingKey;
    }
    
    /**
     * 
     * @return the production grid cert location
     */
    public static String getProductionGridCert() {
        String getProdCert = props.getProperty("production_grid_cert_location");
        return getProdCert;
    }

    
    
    /**
     * 
     * @return the production grid key location
     */
    public static String getProductionGridKey() {
        String getProdKey = props.getProperty("production_grid_key_location");
        return getProdKey;
    }
    
    public static String getDelegetee() {
        return props.getProperty("delegetee_identifier");
    }
    
    
}
