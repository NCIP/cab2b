package edu.wustl.cab2b.server.queryengine.resulttransformers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.dcql.DCQLQuery;

/**
 * Logs CQL and DCQL under {user.home}/cab2bQueries.<br>
 * If the base log folder {user.home}/cab2bQueries doesn't exist, it is created
 * in a static initializer. Each logger notes the time when it was instantiated
 * (to the precision of a second), and creates a folder under the base log
 * folder that contains this timestamp in its name. An example is <br>
 * C:\Documents and Settings\srinath_k\cab2bQueries\4 Sep 2007 01_13_06 PM.
 * 
 * @author srinath_k
 * 
 */
public class QueryLogger {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(QueryLogger.class);

    private static final String LOG_BASE_DIR = System.getProperty("user.home") + "/cab2bQueries";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dMMMyyyy_hh_mm_ss_a");

    private static final String LOG_FILE_NAME_PREFIX = "query_";

    private final String logFolderName;

    private int i = 1;

    static {
        new File(LOG_BASE_DIR).mkdir();
    }

    public QueryLogger() {
        logFolderName = LOG_BASE_DIR + "/" + getCurrentTime();
        new File(logFolderName).mkdir();
    }

    private String getCurrentTime() {
        Date currDate = new Date(System.currentTimeMillis());
        return dateFormat.format(currDate);
    }

    private boolean isLogEnabled() {
        return logger.isDebugEnabled();
    }

    /**
     * Serializes the given query to a file under the log dir for this logger.
     * 
     * @param dcqlQuery the query to log
     * @see Utils#serializeDocument(String, Object, QName)
     */
    public void log(DCQLQuery dcqlQuery) {
        if (!isLogEnabled()) {
            return;
        }
        try {
            Utils.serializeDocument(getLogFilePath(), dcqlQuery, new QName(
                    "http://caGrid.caBIG/1.0/gov.nih.nci.cagrid.dcql", "DCQLQuery"));
        } catch (Exception e) {
            logger.info("Could not log dcql.");
        }

    }

    /**
     * Serializes the given query to a file under the log dir for this logger.
     * 
     * @param cqlQuery the query to log
     * @see Utils#serializeDocument(String, Object, QName)
     */
    public void log(CQLQuery cqlQuery) {
        if (!isLogEnabled()) {
            return;
        }
        try {
            Utils.serializeDocument(getLogFilePath(), cqlQuery, new QName(
                    "http://caGrid.caBIG/1.0/gov.nih.nci.cagrid.cqlquery", "CQLQuery"));
        } catch (Exception e) {
            logger.info("Could not log cql.");
        }

    }

    private synchronized String getLogFilePath() {

        return logFolderName + "/" + LOG_FILE_NAME_PREFIX + i++ + ".xml";
    }
}
