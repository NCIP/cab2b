package edu.wustl.cab2b.server.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.ServerConstants;
import edu.wustl.common.util.logger.Logger;

/**
 * Utility which generates and closes connection.
 * @author Chandrakant Talele
 */
public class ConnectionUtil {
    /**
     * Creates a connection from datasource and returns it.
     * @return Returns the Connection object
     * @throws SQLException 
     */
    public static Connection getConnection() {
        DataSource dataSource = null;

        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup(ServerConstants.CAB2B_DS_NAME);
        } catch (NamingException e) {
            throw new RuntimeException("Unable to look up Datasource from JNDI", e, ErrorCodeConstants.JN_0001);
        }

        Connection connection = null;
        if (dataSource != null) {
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to create a connection from datasource.", e,
                        ErrorCodeConstants.DB_0003);
            }
        } else {
            throw new RuntimeException("Datasource lookup failed, got null datasource", new RuntimeException(),
                    ErrorCodeConstants.JN_0001);
        }
        return connection;
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
//    public static Connection getConnection() {
//        Logger.configure();
//        String url = "jdbc:mysql://localhost:3306/cab2b";
//        String driver = "com.mysql.jdbc.Driver";
//        String userName = "root";
//        String password = "";
//        Connection con = null;
//        try {
//            Class.forName(driver).newInstance();
//            con = DriverManager.getConnection(url, userName, password);
//        } catch (Exception e) {
//            System.out.println("Exception in getting connection");
//            e.printStackTrace();
//        }
//        if (con == null)
//            System.out.println("Got null connection");
//        return con;
//    }
}
