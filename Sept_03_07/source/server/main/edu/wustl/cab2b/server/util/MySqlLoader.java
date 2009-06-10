package edu.wustl.cab2b.server.util;

import java.sql.Connection;

import edu.wustl.cab2b.server.path.PathConstants;
import edu.wustl.common.util.logger.Logger;

/**
 * Bulk data loader for MySQL database.
 * @author Chandrakant Talele
 */
public class MySqlLoader implements DataFileLoaderInterface {

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.util.FileDataLoaderInterface#loadDataFromFile(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.Class<?>[])
     */
    public void loadDataFromFile(Connection connection, String fileName, String columns, String tableName,
                                 Class<?>[] dataTypes) {
        Logger.out.debug("Entering method loadDataFromFile()");

        StringBuffer buffer = new StringBuffer();
        buffer.append(" LOAD DATA INFILE '");
        buffer.append(fileName.replaceAll("\\\\", "/"));
        buffer.append("' INTO TABLE ");
        buffer.append(tableName);
        buffer.append(" FIELDS TERMINATED BY '");
        buffer.append(PathConstants.FIELD_SEPARATOR);
        buffer.append("' LINES TERMINATED BY '\n' ");
        buffer.append(columns);

        Logger.out.info("Loading data to database from file : " + fileName);
        SQLQueryUtil.executeUpdate(buffer.toString(), connection);
        Logger.out.info("Loading data to database from file : " + fileName + " DONE");
        Logger.out.debug("Leaving method loadDataFromFile()");
    }
}