/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.migration;

import edu.wustl.cab2b.util.DatabaseProperties;

public class SQLCommands {
    
    static DatabaseProperties databaseProperties = new DatabaseProperties();

    public static final String USE_TEMPPORARY_DATABASE = "use cab2b_temp;";
    
    public static final String DROP_DATABASE_CAB2B = "drop database if exists ".concat(databaseProperties.getDatabaseName()).concat(";");
    
    public static final String CREATE_DATABASE_CAB2B = "create database ".concat(databaseProperties.getDatabaseName()).concat(";");
    
    public static final String USE_CAB2B_DATABASE = "use ".concat(databaseProperties.getDatabaseName()).concat(";");
    
    public static final String DROP_DATABASE_CAB2B_TEMP = "drop database cab2b_temp;";
}
