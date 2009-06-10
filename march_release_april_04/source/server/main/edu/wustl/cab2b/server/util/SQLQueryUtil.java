package edu.wustl.cab2b.server.util;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.util.logger.Logger;
/**
 * This provides utility methods to execute UPDATE and SELECT type of queries using Datasource.
 * The SELECT queries can be parameterized.
 * @author Chandrakant Talele
 */
public class SQLQueryUtil {
    /**
     * This menthod executes given update SQL and returns either the row count for <br>
     * INSERT, UPDATE or DELETE statements, or 0 for SQL statements that return nothing. 
     * @param sql SQL to execute
     * @return Returns the result of execution
     * @throws RemoteException EJB specific exception.
     * @throws SQLException If anything goes wrong while executing the SQL
     */
    public static int executeUpdate(String sql, Connection connection) {
        Statement statement = null;
        int result = -1;

        try {
            statement = connection.createStatement();
            Logger.out.debug("Executing the SQL :  " + sql);
            result = statement.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception while firing an Update Query",e,ErrorCodeConstants.DB_0004);
        } finally {
            close(statement);
        }
        return result;
    }
    /**
     * This method executes the given SELECT SQL query using passes parameters.
     * It uses prepare statement and maintains a static map of those to reuse those if same query is fired again.
     * @param sql The SQL statement.
     * @param params All the parameter objects.
     * @return Object[][] as result of the query with each row represents one record of result set and 
     * each column in array is a column present in SELECT clause. 
     * The order of columns is same as that present in the passes SQL.   
     * @throws RemoteException EJB specific exception.
     * @throws SQLException SQLException if some error occured while executing the SQL statement.
     */
    public static Object[][] executeQuery(String sql, Connection connection, Object... params) {
        
        PreparedStatement prepareStatement = null;
        ResultSet rs = null;
        Logger.out.debug("Executing the SQL :  " + sql);
        try {
            prepareStatement = connection.prepareStatement(sql);    
            
            int index = 1;
            for (Object param : params) {
                Logger.out.debug("Param " + index + " : " + param);
                if (param instanceof BigDecimal) {
                    prepareStatement.setBigDecimal(index++, (BigDecimal) param);
                } else if (param instanceof String) {
                    prepareStatement.setString(index++, (String) param);
                } else if (param instanceof Long) {
                    prepareStatement.setLong(index++, (Long) param);
                } else if (param instanceof Integer) {
                    prepareStatement.setInt(index++, (Integer) param);
                } else if (param instanceof Float) {
                    prepareStatement.setFloat(index++, (Float) param);
                } else {
                    Logger.out.error("Object Type not identfied for param " + param.toString());
                    prepareStatement.setObject(index++, param);
                }
            }
            rs = prepareStatement.executeQuery();
            return getResult(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Exception while firing Parameterized query.", e,ErrorCodeConstants.DB_0003);
        }
    }

    /**
     * Closes the statement and the connection.
     * @param statement Statement to be closed.
     * @param connection Connection to be closed. 
     */
    protected static void close(Statement statement) {

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                //DO Nothing
                Logger.out.debug(e.toString());
            }
        }
    }
    
    /**
     * This method executes the given SELECT SQL query using passes parameters.
     * It uses prepare statement and maintains a static map of those to reuse those if same query is fired again.
     * @param sql The SQL statement.
     * @param params All the parameter objects.
     * @return Object[][] as result of the query with each row represents one record of result set and 
     * each column in array is a column present in SELECT clause. 
     * The order of columns is same as that present in the passes SQL.   
     * @throws RemoteException EJB specific exception.
     * @throws SQLException SQLException if some error occured while executing the SQL statement.
     */
    public static Object[][] executeQuery(PreparedStatement prepareStatement) {
        try {
            ResultSet rs = prepareStatement.executeQuery();
            return getResult(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Exception while firing Parameterized query.", e,ErrorCodeConstants.DB_0003);
        }
    }
    
    public static Object[][] getResult(ResultSet rs) throws SQLException {
        List<Object[]> results = new ArrayList<Object[]>();
        int noOfColumns = rs.getMetaData().getColumnCount();

        while (rs.next()) {
            Object[] oneRow = new Object[noOfColumns];
            for (int i = 1; i <= noOfColumns; i++) {
                oneRow[i - 1] = rs.getObject(i);
            }
            results.add(oneRow);
        }

        return results.toArray(new Object[0][0]);
    }
}