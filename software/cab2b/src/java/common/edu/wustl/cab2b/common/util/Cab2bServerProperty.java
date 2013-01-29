/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.common.util;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author gaurav_mehta
 *
 */
public class Cab2bServerProperty {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(Cab2bServerProperty.class);

    protected static Properties properties = JbossPropertyLoader.getPropertiesFromFile("cab2b_server.properties");;

    private static int globalThreadLimit = 2000;

    private static int perQueryMaxThreadLimit = 50;

    private static int perQueryMinThreadLimit = 50;

    private static int globalAllowedRecords = 600000;

    private static int perQueryAllowedRecords = 50000;

    private static int uiResultLimit = 100;

    private static Boolean clusterMaster = false;

    static {
        if (properties != null) {
            globalThreadLimit = Integer.parseInt(properties.getProperty("global.thread.limit"));
            perQueryMaxThreadLimit = Integer.parseInt(properties.getProperty("per.query.max.thread.limit"));
            perQueryMinThreadLimit = Integer.parseInt(properties.getProperty("per.query.min.thread.limit"));
            globalAllowedRecords = Integer.parseInt(properties.getProperty("global.record.limit"));
            perQueryAllowedRecords = Integer.parseInt(properties.getProperty("per.query.record.limit"));
            uiResultLimit = Integer.parseInt(properties.getProperty("ui.result.limit"));
            String isClusterMaster = properties.getProperty("cluster.master");
            if ("true".equalsIgnoreCase(isClusterMaster)) {
                clusterMaster = true;
            }

        } else {
            logger.info("File not found. Using default properties");
        }
        logger.info("GlobalThreadLimit " + globalThreadLimit);
        logger.info("Per Query Max Thread Limit : " + perQueryMaxThreadLimit);
        logger.info("Per Query Min Thread Limit : " + perQueryMinThreadLimit);
        logger.info("Global Allowed Records : " + globalAllowedRecords);
        logger.info("PerQuery Allowed Records : " + perQueryAllowedRecords);
        logger.info("UI Result Limit : " + uiResultLimit);
    }

    /**
     * @return True/False depending on whether the server is considered as main server or not
     */
    public static Boolean isMasterJBoss() {
        return clusterMaster;
    }

    /**
     * @return the globalThreadLimit
     */
    public static int getGlobalThreadLimit() {
        return globalThreadLimit;
    }

    /**
     * @return the globalAllowedRecords
     */
    public static int getGlobalAllowedRecords() {
        return globalAllowedRecords;
    }

    /**
     * @return the perQueryAllowedRecords
     */
    public static int getPerQueryAllowedRecords() {
        return perQueryAllowedRecords;
    }

    /**
     * @return the perQueryMaxThreadLimit
     */
    public static int getPerQueryMaxThreadLimit() {
        return perQueryMaxThreadLimit;
    }

    /**
     * @return the perQueryMinThreadLimit
     */
    public static int getPerQueryMinThreadLimit() {
        return perQueryMinThreadLimit;
    }

    /**
     * @return result size for UI
     */
    public static int getUiResultLimit() {
        return uiResultLimit;
    }
}
