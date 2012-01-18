package edu.wustl.common.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.queryExecutor.PagenatedResultData;
import edu.wustl.common.util.dbManager.DAOException;

/** This interface defines methods which are specific to JDBC operations.*/
public interface JDBCDAO extends AbstractDAO
{
	/**
     * Creates a table with the query specified.
     * @param query Query create table.
     * @throws DAOException
     */
      public void createTable(String query) throws DAOException;

      /**
      * Returns the ResultSet containing all the rows according to the columns specified
      * from the table represented in sourceObjectName.
      * @param sourceObjectName The table name.
      * @param selectColumnName The column names in select clause.
      * @param onlyDistictRows true if only distict rows should be selected
      * @return The ResultSet containing all the rows according to the columns specified
      * from the table represented in sourceObjectName.
      * @throws ClassNotFoundException
      * @throws SQLException
      */
	  public List retrieve(String sourceObjectName, String[] selectColumnName, boolean onlyDistinctRows)
	            throws DAOException;

	  /**
	     * Retrieves the records for class name in sourceObjectName according to field values passed in the passed session.
	     * @param selectColumnName An array of field names in select clause.
	     * @param whereColumnName An array of field names in where clause.
	     * @param whereColumnCondition The comparision condition for the field values.
	     * @param whereColumnValue An array of field values.
	     * @param joinCondition The join condition.
	     * @param onlyDistictRows true if only distict rows should be selected
	     */
	  public List retrieve(String sourceObjectName, String[] selectColumnName,
	            String[] whereColumnName, String[] whereColumnCondition,
	            Object[] whereColumnValue, String joinCondition, boolean onlyDistinctRows)
	            throws DAOException;
	  /**
	   *
	     * Executes the query.
	     * @param query
	     * @param sessionDataBean TODO
	     * @param isSecureExecute TODO
	     * @param columnIdsMap
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	  public List executeQuery(String query, SessionDataBean sessionDataBean, boolean isSecureExecute, Map queryResultObjectDataMap) throws ClassNotFoundException, DAOException;


	  /**
	     * Executes the query.
	     * @param query
	     * @param sessionDataBean TODO
	     * @param isSecureExecute TODO
	     * @param columnIdsMap
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	  public List executeQuery(String query, SessionDataBean sessionDataBean, boolean isSecureExecute, boolean hasConditionOnIdentifiedField, Map queryResultObjectDataMap) throws ClassNotFoundException, DAOException;


	/**
	 * Executes the query & returns the results specified by the offset values i.e. startIndex & noOfRecords.
	 * @param query The SQL String
	 * @param sessionDataBean Session databeab reference
	 * @param isSecureExecute
	 * @param hasConditionOnIdentifiedField
	 * @param queryResultObjectDataMap
	 * @param startIndex
	 * @param noOfRecords
	 * @return
	 * @throws ClassNotFoundException
	 * @throws DAOException
	 */
	public PagenatedResultData executeQuery(String query, SessionDataBean sessionDataBean, boolean isSecureExecute, boolean hasConditionOnIdentifiedField, Map queryResultObjectDataMap, int startIndex, int noOfRecords) throws ClassNotFoundException, DAOException;

	  public void insert(String tableName, List columnValues) throws DAOException,SQLException;

	 /**
	 * @param tableName Name of the table in which record to be inserted
	 * @param columnValues column data
	 * @param columnNames (optional)column names- if not not provided, all coumn names of the table are added to the list of column names
	 * @throws DAOException
	 * @throws SQLException
	 */
	public void insert(String tableName, List columnValues, List<String>... columnNames) throws DAOException, SQLException;

	  /**
	     * Creates a table with the name and columns specified.
	     * @param tableName Name of the table to create.
	     * @param columnNames Columns in the table.
	     * @throws DAOException
	     */
	 public void create(String tableName, String[] columnNames) throws DAOException;

	  /**
	     * Deletes the specified table
	     * @param tableName
	     * @throws DAOException
	     */
	  public void delete(String tableName) throws DAOException;


	  public String getActivityStatus(String sourceObjectName, Long indetifier) throws DAOException;

	  public void executeUpdate(String sql) throws DAOException;
		/**
		 *
		 * @param autoCommitFlag
		 * @throws DAOException
		 */
	  public void setAutoCommit(boolean autoCommitFlag) throws DAOException;
		/**
		 * getConnection.
		 * @param autoCommitFlag
		 * @throws DAOException
		 */
		public Connection getConnection();


}
