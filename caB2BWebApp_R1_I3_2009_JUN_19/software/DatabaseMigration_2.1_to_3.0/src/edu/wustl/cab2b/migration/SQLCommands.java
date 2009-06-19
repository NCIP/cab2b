package edu.wustl.cab2b.migration;

import edu.wustl.cab2b.util.ServerProperties;

public class SQLCommands {
    
    static ServerProperties serverProperties = new ServerProperties();

    public static final String USE_TEMPPORARY_DATABASE = "use cab2b_temp;";
    
    public static final String DROP_DATABASE_CAB2B = "drop database if exists ".concat(serverProperties.getDatabaseName()).concat(";");
    
    public static final String CREATE_DATABASE_CAB2B = "create database ".concat(serverProperties.getDatabaseName()).concat(";");
    
    public static final String USE_CAB2B_DATABASE = "use ".concat(serverProperties.getDatabaseName()).concat(";");
    
    public static final String DROP_DATABASE_CAB2B_TEMP = "drop database cab2b_temp;";
}
