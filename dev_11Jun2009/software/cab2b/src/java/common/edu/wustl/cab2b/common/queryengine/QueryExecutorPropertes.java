package edu.wustl.cab2b.common.queryengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Class to set performance properties 
 * @author chandrakant_talele
 */
/*
global.thread.limit=4000
per.query.max.thread.limit=700
per.query.min.thread.limit=100
global.record.limit=100000
per.query.record.limit=5000
ui.result.limit=1000
ui.page.size=30

#For LINUX
#global.thread.limit=8700
 
 */
public class QueryExecutorPropertes {

    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(QueryExecutorPropertes.class);

    private static final String fileName = "performance.properties";

    private static Properties pro = null;

    private static int globalThreadLimit = 4000;

    private static int perQueryMaxThreadLimit = 500;

    private static int perQueryMinThreadLimit = 50;

    private static int globalAllowedRecords = 60000;

    private static int perQueryAllowedRecords = 10000;
    
    private static int uiResultLimit = 1000;
    
    private static int uiPageLimit = 30;

    static {
        String path = System.getProperty("user.home") + File.separator + fileName;
        File f = new File(path);
        if (f.exists()) {
            pro = new Properties();
            try {
                pro.load(new FileInputStream(f));

                globalThreadLimit = Integer.parseInt(pro.getProperty("global.thread.limit"));
                perQueryMaxThreadLimit = Integer.parseInt(pro.getProperty("per.query.max.thread.limit"));
                perQueryMinThreadLimit = Integer.parseInt(pro.getProperty("per.query.min.thread.limit"));
                globalAllowedRecords = Integer.parseInt(pro.getProperty("global.record.limit"));
                perQueryAllowedRecords = Integer.parseInt(pro.getProperty("per.query.record.limit"));
                uiResultLimit = Integer.parseInt(pro.getProperty("ui.result.limit"));
                uiPageLimit = Integer.parseInt(pro.getProperty("ui.page.size"));
            } catch (IOException e) {
                logger.info("File " + path + " not found. Using default properties");
            }
        } else {
            logger.info("File " + path + " not found. Using default properties");
        }
        logger.info("GlobalThreadLimit " + globalThreadLimit);
        logger.info("Per Query Max Thread Limit : " + perQueryMaxThreadLimit);
        logger.info("Per Query Min Thread Limit : " + perQueryMinThreadLimit);
        logger.info("Global Allowed Records : " + globalAllowedRecords);
        logger.info("PerQuery Allowed Records : " + perQueryAllowedRecords);
        logger.info("UI Result Limit : " + uiResultLimit);
        logger.info("UI Page Limit : " + uiPageLimit);
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

    private QueryExecutorPropertes() {
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
    public static int getUiResultLimit(){
        return uiResultLimit;
    }
    /**
     * @return result size for UI
     */
    public static int getUiPageLimit(){
        return uiPageLimit;
    }
}