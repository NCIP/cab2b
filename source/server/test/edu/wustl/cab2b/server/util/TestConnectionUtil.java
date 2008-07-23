package edu.wustl.cab2b.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import edu.wustl.common.util.logger.Logger;

/**
 * @author chandrakant_talele
 */
public class TestConnectionUtil {
    private final static String driver = "com.mysql.jdbc.Driver";

    private static Properties serverProperties;
    static {
        Logger.configure();
        String propertyfile = "server.properties";
        InputStream is = TestUtil.class.getClassLoader().getResourceAsStream(propertyfile);
        if (is == null) {
            Logger.out.error("Unable fo find property file : " + propertyfile
                    + "\n please put this file in classpath");
        }
        serverProperties = new Properties();
        try {
            serverProperties.load(is);
        } catch (IOException e) {
            Logger.out.error("Unable to load properties from : " + propertyfile);
            e.printStackTrace();
        }
    }

    
    public static Connection getConnection() {
        String ip = serverProperties.getProperty("database.server.ip");
        String port = serverProperties.getProperty("database.server.port");
        String name = serverProperties.getProperty("database.name");
        String userName = serverProperties.getProperty("database.username");
        String password = serverProperties.getProperty("database.password");
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + name + "";

        Connection con = null;
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            Logger.out.error("Exception in getting connection");
            e.printStackTrace();
        }
        if (con == null)
            Logger.out.error("Got null connection");
        return con;
    }

    /**
     * Closes the connection.
     * @param connection to be closed.
     */
    public static void close(Connection connection) {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                //DO NOTHING
                Logger.out.debug(e.toString());
            }
        }
    }
}
