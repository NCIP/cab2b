/**
 * <p>Title: PagenatedResultData class</p>
 * <p>Description:  PagenatedResultData class is a class which can hold the Query Results. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author prafull_kadam
 * @version 1.00
 * Created on July 11, 2007
 */

package edu.wustl.common.dao.queryExecutor;

import java.util.List;

/**
 * @author prafull_kadam
 * To store the Results for the pagenated Results. 
 * This will hold the Results of the query & total number of Results. Result of the Data can be subset of the query result, depending upon pagenation parameters.
 */
public class PagenatedResultData
{

	// Holds the Result or Subset of query result.
	List result;
	// Total number of records that will be returned by the query. result.siez() will be always less than or equal to totalRecords.
	int totalRecords;

	/**
	 * Constructor to instanciate object of this class.
	 * It is not expected that it will be called from class other than Query Executor clases, so not keeping it public.   
	 * @param result The complete or Subset of query result.
	 * @param totalRecords Total number of records that will be returned by the query
	 */
	PagenatedResultData(List result, int totalRecords)
	{
		this.result = result;
		this.totalRecords = totalRecords;
	}

	/**
	 * To get the Result List.
	 * @return the result
	 */
	public List getResult()
	{
		return result;
	}

	/**
	 * To get the Total number of records.
	 * @return Total number of records that will be returned by the query
	 */
	public int getTotalRecords()
	{
		return totalRecords;
	}

}
