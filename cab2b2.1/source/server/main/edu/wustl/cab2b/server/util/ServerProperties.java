/*L
 * Copyright Georgetown University.
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
     * @return The database IP
     */
    public static String getDatabaseIp() {
        return properties.getProperty("database.server.ip");
    }

    /**
     * @return The database port
     */
    public static String getDatabasePort() {
        return properties.getProperty("database.server.port");
    }

    /**
     * @return the name of the database
     */
    public static String getDatabaseName() {
        return properties.getProperty("database.name");
    }

    /**
     * @return the user to be used for connecting to the database
     */
    public static String getDatabaseUser() {
        return properties.getProperty("database.username");
    }

    /**
     * @return the password of database user
     */
    public static String getDatabasePassword() {
        return properties.getProperty("database.password");
    }

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
}