/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.util;

import java.util.Properties;

import edu.wustl.cab2b.common.util.Utility;

/**
 * This class is wrapper around property file server.properties.
 * @author Chandrakant Talele
 * @author chetan_patil
 */
public class ServerProperties {
    protected static Properties properties = Utility.getPropertiesFromFile("server.properties");
    
    /**
     * @return The database loader to be used
     */
    public static String getDatabaseLoader() {
        return properties.getProperty("database.loader");
    }

    /**
     * @return The data-source name
     */
    public static String getDatasourceName() {
        return properties.getProperty("datasource.name");
    }
    
    /**
     * Gives the default user mentioned in server.properties
     * @return String user name
     */
    public static String getDefaultUser() {
        return properties.getProperty("default.user");
    }
}