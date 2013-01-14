/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.ejb.sqlquery;

import java.rmi.RemoteException;
import java.sql.SQLException;

import edu.wustl.cab2b.common.BusinessInterface;

/**
 * This provides utility methods to execute UPDATE and SELECT type of queries using Datasource.
 * The SELECT queries can be parameterized.
 * @author Chandrakant Talele
 */
public interface SQLQueryBusinessInterface extends BusinessInterface {
    /**
     * This menthod executes given update SQL and returns either the row count for <br>
     * INSERT, UPDATE or DELETE statements, or 0 for SQL statements that return nothing. 
     * @param sql SQL to execute
     * @return Returns the result of execution
     * @throws RemoteException EJB specific exception.
     * @throws SQLException If anything goes wrong while executing the SQL
     */
    public int executeUpdate(String sql) throws RemoteException,SQLException;
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
    public String[][] executeQuery(String sql, Object... params)  throws RemoteException,SQLException;
}