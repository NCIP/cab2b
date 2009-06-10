package edu.wustl.cab2b.server;

/**
 * This class holds all the common constants needed by server side classes.
 * @author Chandrakant Talele
 */
public interface ServerConstants {
    /**
     *  JNDI name for looking up the datasource for local caB2B Database.
     *  Value of this must be always in sync with <jndi-name> in caB2B-ds.xml
     *  if <use-java-context> is TRUE  then value should be "java:/cab2bDS"
     *  if <use-java-context> is FALSE then value should be "cab2bDS" 
     */
    public static String CAB2B_DS_NAME = "java:/cab2bDS";
    
    /**
     * File name which hold server side properties.
     */
    public static String SERVER_PROPERTY_FILE = "server.properties";
}
