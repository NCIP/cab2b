/**
 * <p>Title: Query Executor Class for Oracle database</p>
 * <p>Description:  MysqlQueryExecutor class is a class which contains code to execute the sql query to get the results from Oracle database. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author prafull_kadam
 * @version 1.00
 * Created on July 11, 2007
 */

package edu.wustl.common.dao.queryExecutor;

import java.sql.SQLException;
import java.util.List;

/**
 * @author prafull_kadam
 * Query Executor class implementation for Oracel database.
 * The Logic implemented for executing query is:
 * - Modify Query to get only required no of record
 * - Iterate on all result set
 * - Fire another query of type "select count(*) from table" to get the totalNoOfRecords
 */
public class OracleQueryExecutor extends AbstractQueryExecutor
{

	/**
	 * Default constructor.
	 */
	protected OracleQueryExecutor()
	{

	}

	/**
	 * This method will 
	 * - modify the SQL, by adding the rownum conditions, to get the required number of records starting from startIndex.
	 * - create statement object by executing modifed SQL query & get the results. It will modify SQL if value of startIndex is -1, elseit will return all the query results.
	 * - Fire another "Select Count(*)..." type SQL to get the total number of records that given sql can return.
	 * - return the Results.
	 * @return The reference to PagenatedResultData object, which will have pagenated data list & total no. of records that query can return.
	 * @see edu.wustl.common.dao.queryExecutor.AbstractQueryExecutor#createStatemtentAndExecuteQuery()
	 */
	protected PagenatedResultData createStatemtentAndExecuteQuery() throws SQLException
	{
		String sqlToBeExecuted = query;
		// modify the SQL by adding rownum condition if required.
		if (getSublistOfResult)
		{
			sqlToBeExecuted = putPageNumInSQL(query, startIndex, noOfRecords);
		}

		// execute the modified query & get Results
		stmt = connection.prepareStatement(sqlToBeExecuted);
		resultSet = stmt.executeQuery();
		List list = getListFromResultSet();

		// Find the total number of records that query can return.
		int totalRecords;
		if (getSublistOfResult)
		{
			sqlToBeExecuted = getCountQuery(query);
			resultSet.close();
			resultSet = stmt.executeQuery(sqlToBeExecuted);
			resultSet.next();
			totalRecords = resultSet.getInt(1);
		}
		else
		{
			totalRecords = list.size(); // these are all records returned from query.
		}
		return new PagenatedResultData(list, totalRecords);
	}
}
