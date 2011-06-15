/**
 * 
 */

package edu.wustl.common.dao;

import java.util.Map;

/**
 * @author prafull_kadam
 * This class will hold the information that will be saved in the session for the Pagenation of the query results.
 */
public class QuerySessionData
{
	// The SQL for the query for which user is navigating results.  
	String sql;
	Map queryResultObjectDataMap;
	boolean isSecureExecute;
	boolean hasConditionOnIdentifiedField;
	int recordsPerPage;
	int totalNumberOfRecords;

	/**
	 * @return the hasConditionOnIdentifiedField
	 */
	public boolean isHasConditionOnIdentifiedField()
	{
		return hasConditionOnIdentifiedField;
	}

	/**
	 * @param hasConditionOnIdentifiedField the hasConditionOnIdentifiedField to set
	 */
	public void setHasConditionOnIdentifiedField(boolean hasConditionOnIdentifiedField)
	{
		this.hasConditionOnIdentifiedField = hasConditionOnIdentifiedField;
	}

	/**
	 * @return the isSecureExecute
	 */
	public boolean isSecureExecute()
	{
		return isSecureExecute;
	}

	/**
	 * @param isSecureExecute the isSecureExecute to set
	 */
	public void setSecureExecute(boolean isSecureExecute)
	{
		this.isSecureExecute = isSecureExecute;
	}

	/**
	 * @return the queryResultObjectDataMap
	 */
	public Map getQueryResultObjectDataMap()
	{
		return queryResultObjectDataMap;
	}

	/**
	 * @param queryResultObjectDataMap the queryResultObjectDataMap to set
	 */
	public void setQueryResultObjectDataMap(Map queryResultObjectDataMap)
	{
		this.queryResultObjectDataMap = queryResultObjectDataMap;
	}

	/**
	 * @return the recordsPerPage
	 */
	public int getRecordsPerPage()
	{
		return recordsPerPage;
	}

	/**
	 * @param recordsPerPage the recordsPerPage to set
	 */
	public void setRecordsPerPage(int recordsPerPage)
	{
		this.recordsPerPage = recordsPerPage;
	}

	/**
	 * @return the sql
	 */
	public String getSql()
	{
		return sql;
	}

	/**
	 * @param sql the sql to set
	 */
	public void setSql(String sql)
	{
		this.sql = sql;
	}

	/**
	 * @return the totalNumberOfRecords
	 */
	public int getTotalNumberOfRecords()
	{
		return totalNumberOfRecords;
	}

	/**
	 * @param totalNumberOfRecords the totalNumberOfRecords to set
	 */
	public void setTotalNumberOfRecords(int totalNumberOfRecords)
	{
		this.totalNumberOfRecords = totalNumberOfRecords;
	}
}
