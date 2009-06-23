package edu.wustl.cab2b.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author gaurav_mehta
 *
 */
public class DatabaseProperties {

    /**
     * ResourceBundle for ServerProperties
     */
    protected Properties properties = new Properties();

    public DatabaseProperties() {
        try {
            URL url = DatabaseProperties.class.getClassLoader().getResource("database.properties");
            properties.load(url.openStream());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gives the database server IP mentioned in server.properties
     * @return String user name
     */
    public String getDatabaseServer() {
        return properties.getProperty("database.server.ip");
    }

    /**
     * Gives the database server Port mentioned in server.properties
     * @return String user name
     */
    public String getDatabaseServerPort() {
        return properties.getProperty("database.server.port");
    }

    /**
     * Gives the database Name mentioned in server.properties
     * @return String user name
     */
    public String getDatabaseName() {
        return properties.getProperty("database.name");
    }

    /**
     * Gives the database User Name mentioned in server.properties
     * @return String user name
     */
    public String getDatabaseUserName() {
        return properties.getProperty("database.username");
    }

    /**
     * Gives the database User Password mentioned in server.properties
     * @return String user name
     */
    public String getDatabasePassword() {
        return properties.getProperty("database.password");
    }
}