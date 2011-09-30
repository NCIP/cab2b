/**
 * <p>Title: DatabaseConnectionParams Class>
 * <p>Description:	DatabaseConnectionParams handles opening closing ,initialization of all database specific
 * parameters  .</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana_thakur
 */
package edu.wustl.common.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author kalpana_thakur
 * This class will handles all database specific parameters.
 */

public class DatabaseConnectionParams
{
	protected AuditManager auditManager;
	/**
	 * Connection statement.
	 */
	private Statement statement;
	/**
	 * Query resultSet.
	 */
	private ResultSet resultSet;
	/**
	 * connection object.
	 */
	private Connection connection;
	/**
	 * Query preparedStatement.
	 */
	private PreparedStatement preparedStatement;

	/**
	 * class logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(DatabaseConnectionParams.class);

	/**
	 * This method will be called to create new connection statement.
	 * @throws DAOException :Generic Exception
	 * @return Statement
	 */
	public Statement getDatabaseStatement()throws DAOException
	{
		try
		{
			statement = connection.createStatement();
			// resultSet.set
		}
		catch (SQLException sqlExp)
		{
			logger.fatal(sqlExp);
			throw new DAOException("Connection statement failed....");
		}
		return statement;

	}

	/**
	 * This method will return the query ResultSet.
	 * @param query Query String
	 * @throws DAOException :Generic Exception
	 * @return ResultSet
	 */
	public ResultSet getResultSet(String query) throws DAOException
	{
		try
		{
			statement = getDatabaseStatement();
			resultSet = statement.executeQuery(query);
		}
		catch (SQLException sqlExp)
		{
			logger.fatal(sqlExp);
			throw new DAOException("Connection RESULT SET failed....");
		}
		return resultSet;
	}


	/**
	 * This method will be called to return query meta data.
	 * @param query : Query String
	 * @return ResultSetMetaData
	 * @throws DAOException :Generic Exception
	 */
	public ResultSetMetaData getMetaData(String query)throws DAOException
	{
		ResultSetMetaData metaData = null;
		try
		{

			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			metaData = resultSet.getMetaData();
		}
		catch (SQLException sqlExp)
		{
			logger.fatal(sqlExp);
			throw new DAOException("Connection GET METADATA failed....");
		}

		return metaData;
	}

	/**
	 * This method will be called to close all the Database connections.
	 * @throws DAOException :Generic Exception
	 */
	public void closeConnectionParams()throws DAOException
	{
		try
		{
			if(resultSet != null )
			{
				resultSet.close();
				resultSet = null;
			}
			if (statement != null)
			{
				statement.close();
				statement = null;
			}
			if (preparedStatement != null)
			{
				preparedStatement.close();
				statement = null;
			}

		}
		catch(SQLException sqlExp)
		{
			logger.fatal(sqlExp);
			throw new DAOException("CLOSE Connection failed....");
		}
	}

	/**
	 * This method will return the Query prepared statement.
	 * @param query :Query String
	 * @return PreparedStatement.
	 * @throws DAOException :Generic Exception
	 */
	public PreparedStatement getPreparedStatement(String query) throws DAOException
	{
		try
		{
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
		}
		catch (SQLException sqlExp)
		{
			logger.fatal(sqlExp);
			throw new DAOException("PREPAReD STATEMENT failed....");
		}
		return preparedStatement;
	}

	/**
	 * This method will be called to execute query.
	 * @param query :query string.
	 * @throws DAOException :Generic Exception
	 */
	public void executeUpdate(String query) throws DAOException
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = getPreparedStatement(query);
			stmt.executeUpdate();
		//	connection.commit();
		}
		catch (SQLException sqlExp)
		{
			logger.fatal(sqlExp);
			throw new DAOException("EXECUTE UPDATE failed....");
		}
		finally
		{
			closeConnectionParams();
		}
	}

	/**
	 * @return This method will return the connection object.
	 */
	public Connection getConnection()
	{
		return connection;
	}

	
	/**
	 * This method will be called to set connection Object.
	 * @param connection :connection object.
	 */
	public void setConnection(Connection connection)
	{
			this.connection = connection;

	}

	/**
	 * Checks result set.
	 * @return :true if result set exists.
	 * @param query : query String
	 * @throws DAOException : DAOException
	 */
	public boolean isResultSetExists(String query)throws DAOException
	{
		boolean isResultSetExists = false;
		try
		{

			resultSet = getResultSet(query);
			if(resultSet.next())
			{
				isResultSetExists = true;
			}

		}
		catch(SQLException exp)
		{
			throw new DAOException("ResultSetExists failed....");
		}
		return isResultSetExists;
	}

	/**
	 *To get database meta data object for the connection.
	 * @return Database meta data.
	 * @throws DAOException  :
	 */
	public DatabaseMetaData getDatabaseMetaData() throws DAOException
	{
		try
		{
			return (DatabaseMetaData)connection.getMetaData();
		}
		catch(SQLException exp)
		{
			throw new DAOException("ResultSetExists failed....");
		}
	}

	/**
	 *For tempory use ..
	 *TODO will be removed when getCleanConnection get removed.
	 * @throws SQLException :
	 */
	public void commit() throws SQLException
	{
		if(connection != null)
		{
			connection.commit();
		}
	}


	/**
	 * This method will be used to establish the session with the database.
	 * Declared in AbstractDAO class.
	 *
	 * @throws DAOException
	 */
	public void openSession(String jndiName) throws DAOException
	{
		try
		{
			InitialContext ctx = new InitialContext();
		       DataSource ds = (DataSource)ctx.lookup(jndiName);
		       connection = ds.getConnection();
			
			//Creates a connection.
//			connection = DriverManager.getConnection(
//					"jdbc:db2://10.88.25.116:50000/cider4:currentSchema=ADMINISTRATOR;",
//					"administrator", "v-cider4!@#");//DBUtil.getConnection();// getConnection(database, loginName, password);
			connection.setAutoCommit(false);
		}
		catch (Exception sqlExp)
		{
			//throw new DAOException(sqlExp.getMessage(),sqlExp);
			Logger.out.error(sqlExp.getMessage(), sqlExp);
			throw new DAOException(Constants.GENERIC_DATABASE_ERROR, sqlExp);
		}
	}

	/**
	 * This method will be used to close the session with the database.
	 * Declared in AbstractDAO class.
	 * @throws DAOException
	 */
	public void closeSession() throws DAOException
	{
		try
		{
			closeConnectionParams();
			
			// DBUtil.closeConnection();
			if (connection != null && !connection.isClosed())
        	    connection.close();
		}
		catch (Exception sqlExp)
		{
			//            new DAOException(sqlExp.getMessage(),sqlExp);
			Logger.out.error(sqlExp.getMessage(), sqlExp);
			throw new DAOException(Constants.GENERIC_DATABASE_ERROR, sqlExp);

		}
	}


	/**
	 * Rollback all the changes after last commit.
	 * Declared in AbstractDAO class.
	 * @throws DAOException
	 */
	public void rollback()
	{
		try
		{
			if (connection != null)
				connection.rollback();
		}
		catch (SQLException dbex)
		{
			Logger.out.error(dbex.getMessage(), dbex);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.wustl.common.dao.JDBCDAO#insert(java.lang.String, java.util.List)
	 */
	public void insert(String tableName, List columnValues) throws DAOException, SQLException
	{
		insert(tableName, columnValues, null);
	}

	/**
	 * @param tableName
	 * @param columnValues
	 * @param columnNames
	 * @throws DAOException
	 * @throws SQLException
	 */
	public void insert(String tableName, List columnValues, List<String>... columnNames) throws DAOException, SQLException
	{
		//Get metadate for temp table to set default values in date fields
		StringBuffer sql = new StringBuffer("Select ");
		Statement statement = connection.createStatement();
		ResultSet resultSet = null;
		ResultSetMetaData metaData = null;
		//Make a list of Date columns
		List dateColumns = new ArrayList();
		List numberColumns = new ArrayList();

		//added by Kunal
		List<String> columnNames_t;
		if (columnNames != null && columnNames.length > 0)
		{
			columnNames_t = columnNames[0];
			for (int i = 0; i < columnNames_t.size(); i++)
			{
				sql.append(columnNames_t.get(i));
				if (i != columnNames_t.size() - 1)
				{
					sql.append(",");
				}
			}
			sql.append(" from " + tableName + " where 1!=1");
			resultSet = statement.executeQuery(sql.toString());
			metaData = resultSet.getMetaData();

		}
		else
		{
			sql.append("* from " + tableName + " where 1!=1");
			resultSet = statement.executeQuery(sql.toString());
			metaData = resultSet.getMetaData();

			columnNames_t = new ArrayList<String>();
			for (int i = 1; i <= metaData.getColumnCount(); i++)
			{
				columnNames_t.add(metaData.getColumnName(i));
			}
		}

		/** Name : Aarti Sharma
		* Reviewer: Prafull Kadam
		* Bug ID: 4126
		* Patch ID: 4126_1
		* See also: 4126_2
		* Desciption: Make a list of tinyint columns.
		* Tinyint datatype is used as a replacement for boolean in MySQL.
		*/
		List tinyIntColumns = new ArrayList();

		for (int i = 1; i <= metaData.getColumnCount(); i++)
		{
			String type = metaData.getColumnTypeName(i);
			if (type.equals("DATE"))
				dateColumns.add(new Integer(i));
			if (type.equals("NUMBER"))
				numberColumns.add(new Integer(i));
			if (type.equals("TINYINT"))
				tinyIntColumns.add(new Integer(i));

		}

		resultSet.close();
		statement.close();
		StringBuffer query = new StringBuffer("INSERT INTO " + tableName + "(");

		Iterator<String> columnIterator = columnNames_t.iterator();
		while (columnIterator.hasNext())
		{
			query.append(columnIterator.next());
			if (columnIterator.hasNext())
			{
				query.append(",");
			}
			else
			{
				query.append(") values(");
			}
		}

		//StringBuffer query = new StringBuffer("INSERT INTO " + tableName + " values(");
		//Changed implementation with column names

		int i;

		Iterator it = columnValues.iterator();
		while (it.hasNext())
		{
			it.next();
			query.append("?");

			if (it.hasNext())
				query.append(",");
			else
				query.append(")");
		}

		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement(query.toString());
			for (i = 0; i < columnValues.size(); i++)
			{
				Object obj = columnValues.get(i);
				/**
				 * For Number -1 is used as MarkUp data For Date 1-1-9999 is used as markUp data.
				 * Please refer bug 3576
				 */

				if (obj != null && dateColumns.contains(new Integer(i + 1)) && obj.toString().equals("##"))
				{
					java.util.Date date = null;
					try
					{
						date = Utility.parseDate("1-1-9999", "mm-dd-yyyy");
					}
					catch (ParseException e)
					{
						e.printStackTrace();
					}
					Date sqlDate = new Date(date.getTime());
					stmt.setDate(i + 1, sqlDate);
				}
				/** Name : Aarti Sharma
				* Reviewer:  Prafull Kadam
				* Bug ID: 4126
				* Patch ID: 4126_2
				* See also: 4126_1
				* Desciption: If the value of the column is true set 1 in the statement else set 0.
				* This is necessary for MySQL since all boolean values in MySQL are stored in tinyint.
				* If this is not done then all values will be set as 0
				* irrespective of whether the value is true or false.
				*/
				else if (tinyIntColumns.contains(new Integer(i + 1)))
				{
					if (obj != null && (obj.equals("true") || obj.equals("TRUE") || obj.equals("1")))
					{
						stmt.setObject(i + 1, 1);
					}
					else
					{
						stmt.setObject(i + 1, 0);
					}
				}
				else
				{
					Timestamp date = isColumnValueDate(obj);
					if (date != null)
					{
						stmt.setObject(i + 1, date);
						//Logger.out.debug("t.toString(): " + "---" + date);
					}
					else
					{
						if (obj != null && numberColumns.contains(new Integer(i + 1)) && obj.toString().equals("##"))
						{
							stmt.setObject(i + 1, new Integer(-1));
						}
						else
						{
							stmt.setObject(i + 1, obj);
						}
					}
				}
			}
			stmt.executeUpdate();
		}
		catch (SQLException sqlExp)
		{
			sqlExp.printStackTrace();
			throw new DAOException(sqlExp.getMessage(), sqlExp);
		}
		finally
		{
			try
			{
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException ex)
			{
				throw new DAOException(ex.getMessage(), ex);
			}
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private Timestamp isColumnValueDate(Object value)
	{
		//Logger.out.debug("Column value: " + value);
		try
		{
			DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			formatter.setLenient(false);
			java.util.Date date = formatter.parse((String) value);
			Timestamp t = new Timestamp(date.getTime());
			// Date sqlDate = new Date(date.getTime());

			//Logger.out.debug("Column date value: " + date);
			if (value != null && value.toString().equals("") == false)
			{
				//Logger.out.debug("Return true: " + value);
				return t;
			}
		}
		catch (Exception e)
		{

		}
		return null;
	}
	
}
