package edu.wustl.cab2b.common.authentication.util;

import java.util.Properties;

import edu.wustl.cab2b.common.util.Utility;

public class CagridPropertyLoader {
    private static Properties properties = Utility.getPropertiesFromFile("cagrid.properties");

    /**
     *
     * @param gridType
     * @return it returns the delegetee .. means identifier of cab2b server
     */
    public static String getDelegetee() {
        return properties.getProperty("delegetee.identifier");
    }

    /**
     *
     * @return the CDS name space URI
     */
    public static String getCDSNamespaceURI() {
        return properties.getProperty("cds.namespace.uri");
    }

    /**
     * @param gridType
     * @return the CDS url for production grid and training grid
     */
    public static String getCDS_URL() {
        return properties.getProperty("cds.url");
    }

    /**
     * @param gridType
     * @return Dorian url for given idP
     */
    public static String getIdP_URL() {
        return properties.getProperty("idP.url");
    }

    /**
     * @param idP
     * @return Dorian url for given idP
     */
    public static String getAuthenticationURL() {
        return properties.getProperty("authentication.url");
    }

    /**
     *
     * @param gridType
     * @return the grid cert location
     */
    public static String getGridCert() {
        return properties.getProperty("grid.cert");
    }

    /**
     *
     * @param gridType
     * @return the grid key location
     */
    public static String getGridKey() {
        return properties.getProperty("grid.key");
    }

    /**
     * @return all the index urls used to get the service information
     */
    public static String getIndexServiceUrl() {
        return properties.getProperty("index.url");
    }

    /**
     *
     * @param gridType
     * @return returns the sys-description file for GTS
     */

    public static String getSyncDesFile() {
        return properties.getProperty("sync.description.file");
    }

    /**
     * @param gridType
     * @return signing policy for given idP
     */
    public static String getSigningPolicy() {
        return properties.getProperty("signing.policy");
    }

    /**
     * @param gridType
     * @return certificate for given idP
     */
    public static String getCertificate() {
        return properties.getProperty("certificate");
    }
    
    /**
     * @return cadsr url
     */
    public static String getCaDSRUrl() {
        return properties.getProperty("cadsr.url");
    }
    
    /**
     * @return cadsr url
     */
    public static String getMMSUrl() {
        return properties.getProperty("mms.url");
    }

    /**
     * returns CadsrRefreshTime
     * 
     * @return
     */
    public static String getCaDSRRefreshTime() {
        return properties.getProperty("cadsr.refresh.time");
    }

}
