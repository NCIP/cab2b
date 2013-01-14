/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.util;

import java.sql.Connection;
import org.apache.log4j.Logger;

/**
 * Bulk data loader for MySQL database.
 * @author Chandrakant Talele
 */
public class MySqlLoader implements DataFileLoaderInterface {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(MySqlLoader.class);

    /**
     * Loads data from given file into given table.
     * <b> NOTE : </b> This method will not create table in database. It assumes that table is already present
     * @param connection Connection to use to fire SQL
     * @param fileName Full path of data file.
     * @param columns Data columns in table. They should be in format (column1,column2,...) braces included
     * @param tableName Name of the table in which data to load.
     * @param dataTypes Data type of each column. 
     * @param fieldSeperator String used to separate values of columns in one line of given file.
     * Order of this array must be same as columns names given in "columns" parameter above.
     */
    public void loadDataFromFile(Connection connection, String fileName, String columns, String tableName,
                                 Class<?>[] dataTypes, String fieldSeperator) {
        logger.debug("Entering method loadDataFromFile()");

        StringBuffer buffer = new StringBuffer(200);
        buffer.append(" LOAD DATA LOCAL INFILE '");
        buffer.append(fileName.replaceAll("\\\\", "/"));
        buffer.append("' INTO TABLE ");
        buffer.append(tableName);
        buffer.append(" FIELDS TERMINATED BY '");
        buffer.append(fieldSeperator);
        buffer.append("' LINES TERMINATED BY '\n' ");
        buffer.append(columns);

        logger.info("Loading data to database from file : " + fileName);
        SQLQueryUtil.executeUpdate(buffer.toString(), connection);
        logger.info("Loading data to database from file : " + fileName + " DONE");
        logger.debug("Leaving method loadDataFromFile()");
    }
}