package edu.wustl.common.dao.queryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class Db2QueryExecuter extends AbstractQueryExecutor
{

	protected Db2QueryExecuter()
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
