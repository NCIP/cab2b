/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * @author chandrakant_talele
 */
public class TestConnectionUtil {
    private final static String driver = "com.mysql.jdbc.Driver";

    private static Properties serverProperties;
    static {
        String propertyfile = "server.properties";
        InputStream is = TestUtil.class.getClassLoader().getResourceAsStream(propertyfile);
        if (is == null) {
            System.out.println("Unable fo find property file : " + propertyfile
                    + "\n please put this file in classpath");
        }
        serverProperties = new Properties();
        try {
            serverProperties.load(is);
        } catch (IOException e) {
            System.out.println("Unable to load properties from : " + propertyfile);
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
            e.printStackTrace();
        }
        if (con == null)
            System.out.println("Got null connection");
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
                e.printStackTrace();
            }
        }
    }
}
