package edu.wustl.cab2b.server.path;

import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.util.logger.Logger;
/**
 * This class handles fetching properties from demo.properties file
 * @author Chandrakant Talele
 */
public class PropertyLoader {
    /**
     * Returns the Path of domain model XML file
     * @param applicationName Name of the application
     * @return Returns the File Path  
     */
    static public String getModelPath(String applicationName) {
        String path = Utility.getProperties().getProperty(applicationName + ".ModelPath"); 
        if(path == null || path.length() == 0) {
            Logger.out.error("Model path for application : "+ applicationName  + " is not configured in demo.properties"); 
        }
        return path;
    }
    /**
     * Returns names of all application for which caB2B is configured
     * @return Returns the Application Names
     */
    static public String[] getAllApplications() {
        String[] allApplications = Utility.getProperties().getProperty("all.applications").split(",");
        if(allApplications == null || allApplications.length == 0) {
            Logger.out.error("No value for key 'all.applications' is found in demo.properties"); 
        }
        
        return allApplications;
    }
}