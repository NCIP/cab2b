/**
 * <p>Title: Query Executor Class for MySQL database</p>
 * <p>Description:  MysqlQueryExecutor class is a class which contains code to execute the sql query to get the results from MYSQL database. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author prafull_kadam
 * @version 1.00
 * Created on June 29, 2007
 */

package edu.wustl.common.dao.queryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author prafull_kadam
 * Query Executor class implementation for MySQL database.
 * The Logic implemented for executing query is:
 * - Execute Query
 * - Set Resultset absolute to startIndex
 * - Iterate on Result for given no. of records
 * - Call resultset.last() & get the cursor position for totalNoOfRecords
 */
public class MysqlQueryExecutor extends AbstractQueryExecutor
{

	/**
	 * Default constructor.
	 */
	protected MysqlQueryExecutor()
	{
	}

	/**
	 * This method will 
	 * - create statement object by executing SQL query, create scrollable resultset
	 * - moves the cursor position to the startIndex, depending upon its value. if value of startIndex is -1, it will return all the query results.
	 * - after getting required number of records, move cursor to last record & get the cursor position index.
	 * - return the Results.
	 * @return The reference to PagenatedResultData object, which will have pagenated data list & total no. of records that query can return.
	 * @see edu.wustl.common.dao.queryExecutor.AbstractQueryExecutor#createStatemtentAndExecuteQuery()
	 */
	protected PagenatedResultData createStatemtentAndExecuteQuery() throws SQLException
	{
		stmt = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		resultSet = stmt.executeQuery();

		if (getSublistOfResult)
		{
			if (startIndex > 0) // move cursor to the start index.
			{
				resultSet.absolute(startIndex);
			}
		}
		List list = getListFromResultSet(); // get the resulset.

		// find the total number of records.
		int totalRecords;
		if (getSublistOfResult)
		{
			resultSet.last();
			totalRecords = resultSet.getRow();
		}
		else
		{
			totalRecords = list.size(); // these are all records returned from query.
		}
		return new PagenatedResultData(list, totalRecords);
	}
}
