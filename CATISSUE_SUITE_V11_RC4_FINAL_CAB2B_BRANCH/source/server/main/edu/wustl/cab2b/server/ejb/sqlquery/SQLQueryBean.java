/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.ejb.sqlquery;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import edu.wustl.cab2b.common.ejb.sqlquery.SQLQueryBusinessInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.cab2b.server.util.SQLQueryUtil;
import edu.wustl.common.util.logger.Logger;

/**
 * This provides utility methods to execute UPDATE and SELECT type of queries using Datasource.
 * The SELECT queries can be parameterized.
 * @author Chandrakant Talele
 */
public class SQLQueryBean extends AbstractStatelessSessionBean implements SQLQueryBusinessInterface {
    private static final long serialVersionUID = -6568289691792853684L;
    
    /**
     * This menthod executes given update SQL and returns either the row count for <br>
     * INSERT, UPDATE or DELETE statements, or 0 for SQL statements that return nothing. 
     * @param sql SQL to execute
     * @return Returns the result of execution
     * @throws RemoteException EJB specific exception.
     * @throws SQLException If anything goes wrong while executing the SQL
     */
    public int executeUpdate(String sql) throws RemoteException, SQLException {
        Connection connection = ConnectionUtil.getConnection();
        try {
            int res = SQLQueryUtil.executeUpdate(sql, connection);
            return res;
        } finally {
            ConnectionUtil.close(connection);
        }
    }

    /**
     * This method executes the given SELECT SQL query using passes parameters.
     * @param sql The SQL statement.
     * @param params All the parameter objects.
     * @return String[][] as result of the query with each row represents one record of result set and 
     * each column in array is a column present in SELECT clause. 
     * The order of columns is same as that present in the passes SQL.   
     * @throws RemoteException EJB specific exception.
     * @throws SQLException SQLException if some error occured while executing the SQL statement.
     */
    public String[][] executeQuery(String sql, Object... params) throws RemoteException, SQLException {
        Connection connection = ConnectionUtil.getConnection();
        try {
            return SQLQueryUtil.executeQuery(sql, connection, params);
        } finally {
            ConnectionUtil.close(connection);
        }
    }
}