/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.util;

/**
 * This class is wrapper around property file server.properties. It is extension
 * of {@link edu.wustl.cab2b.server.util.ServerProperties} for newly added
 * properties in admin module
 * 
 * @author chandrakant_talele
 */
public class ServerProperties extends edu.wustl.cab2b.server.util.ServerProperties {
    /**
     * @return The URLs of all the index services
     */
    public static String[] getIndexServiceUrls() {
        return properties.getProperty("indexurls").split(",");
    }

    /**
     * @return cadsr url
     */
    public static String getCadsrUrl() {
        return properties.getProperty("cadsrurl");
    }
    
    /**
     * @return cadsr url
     */
    public static String getMMSUrl() {
        return properties.getProperty("mmsurl");
    }

    /**
     * returns CadsrRefreshTime
     * 
     * @return
     */
    public static String getCadsrRefreshTime() {
        return properties.getProperty("cadsr.refresh.time");
    }

}
