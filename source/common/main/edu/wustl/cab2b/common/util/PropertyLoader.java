package edu.wustl.cab2b.common.util;

import java.util.Properties;

import edu.wustl.common.util.logger.Logger;

/**
 * This class handles fetching properties from cab2b.properties file
 * @author Chandrakant Talele
 */
public class PropertyLoader {

    private static final String propertyfile = "cab2b.properties";

    private static Properties props = Utility.getPropertiesFromFile(propertyfile);

    /**
     * Returns the Path of domain model XML file
     * @param applicationName Name of the application
     * @return Returns the File Path  
     */
    public static String getModelPath(String applicationName) {
        String path = props.getProperty(applicationName + ".ModelPath");
        if (path == null || path.length() == 0) {
            Logger.out.error("Model path for application : " + applicationName + " is not configured in " + propertyfile);
        }
        return path;
    }

    /**
     * Returns names of all application for which caB2B is configured
     * @return Returns the Application Names
     */
    public static String[] getAllApplications() {
        String[] allApplications = props.getProperty("all.applications").split(",");
        if (allApplications == null || allApplications.length == 0) {
            Logger.out.error("No value for key 'all.applications' is found in " + propertyfile);
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
}
