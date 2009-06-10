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
        return props.getProperty("indexurls").split(",");
    }

    /**
     * @return cadsr url
     */
    public static String getCadsrUrl() {
        return props.getProperty("cadsrurl");
    }

    /**
     * returns CadsrRefreshTime
     * 
     * @return
     */
    public static String getCadsrRefreshTime() {
        return props.getProperty("cadsr.refresh.time");
    }

}
