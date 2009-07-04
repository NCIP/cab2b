package edu.wustl.cab2b.common.queryengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

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
    
    private static int uiResultSize = 1000;

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
                uiResultSize = Integer.parseInt(pro.getProperty("ui.result.limit"));
                
                logger.info("GlobalThreadLimit " + globalThreadLimit);
                logger.info("Per Query Max Thread Limit : " + perQueryMaxThreadLimit);
                logger.info("Per Query Min Thread Limit : " + perQueryMinThreadLimit);
                logger.info("Global Allowed Records : " + globalAllowedRecords);
                logger.info("PerQuery Allowed Records : " + perQueryAllowedRecords);
                logger.info("Result Size : " + perQueryAllowedRecords);
            } catch (IOException e) {
                logger.info("File " + path + " not found. Using default properties");
            }
        } else {
            logger.info("File " + path + " not found. Using default properties");
        }
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
        return uiResultSize;
    }
}