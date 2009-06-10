package edu.wustl.cab2b.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import edu.wustl.common.util.logger.Logger;

/**
 * This class handles fetching properties from cab2b.properties file
 * @author Chandrakant Talele
 */
public class PropertyLoader {

    private static final String propertyfile = "cab2b.properties";

    private static Properties props = getPropertiesFromFile(propertyfile);

    /**
     * Loads properties from a file present in classpath to java objects.
     * If any exception occurs, it is callers responsibility to handle it. 
     * @param propertyfile Name of property file. It MUST be present in classpath
     * @return Properties loaded from given file.
     */
    public static Properties getPropertiesFromFile(String propertyfile) {
        Properties properties = null;
        try {
            URL url = Utility.getResource(propertyfile);
            InputStream is = url.openStream();
            if (is == null) {
                Logger.out.error("Unable fo find property file : " + propertyfile
                        + "\n please put this file in classpath");
            }

            properties = new Properties();
            properties.load(is);

        } catch (IOException e) {
            Logger.out.error("Unable to load properties from : " + propertyfile);
            e.printStackTrace();
        }

        return properties;
    }

    /**
     * Returns the Path of domain model XML file
     * @param applicationName Name of the application
     * @return Returns the File Path  
     */
    public static String getModelPath(String applicationName) {
        String path = props.getProperty(applicationName + ".ModelPath");
        if (path == null || path.length() == 0) {
            Logger.out.error("Model path for application : " + applicationName + " is not configured in "
                    + propertyfile);
        }
        return path;
    }

    public static String[] getServiceUrls(String applicationName) {
        String[] urls = props.getProperty(applicationName + ".ServiceURL").split(",");
        if (urls == null || urls.length == 0) {
            Logger.out.error("No URLs are configured for application : " + applicationName + " in  : "
                    + propertyfile);
        }
        return urls;
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

    public static String getJndiUrl() {
        String serverIP = props.getProperty("caB2B.server.ip");
        String jndiPort = props.getProperty("caB2B.server.port");
        return "jnp://"+serverIP+":"+jndiPort;
    }
}
