package edu.wustl.common.querysuite.utils;

/**
 * Represents database specific settings that are used while building SQL.
 * 
 * @author srinath_k
 */
public class DatabaseSQLSettings {
    private DatabaseType databaseType;

    /**
     * @param databaseType the database type
     * @param dateFormat the format in which date literals are specified.
     */
    public DatabaseSQLSettings(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

}
