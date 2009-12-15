package edu.wustl.cab2b.server.util;

import java.sql.Connection;

/**
 * Defines common interface to be used to represent data loading capabilities.
 * For each database you need to implement this interface. 
 * Each implementing class <b>MUST </b> have an default public constructor. 
 * If you want to use specific loader put its fully qualified name in server.properties file against key database.loader 
 * @author Chandrakant Talele
 */
public interface DataFileLoaderInterface {
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
    void loadDataFromFile(Connection connection, String fileName, String columns, String tableName,
                          Class<?>[] dataTypes,String fieldSeperator);
}
