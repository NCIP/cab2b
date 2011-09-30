package edu.wustl.common.query;

import java.sql.SQLException;
import java.util.HashSet;





/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public abstract class ConditionsImpl {

	public ConditionsImpl(){

	}
	
	public void formatTree()
	{
		
	}

	/**
	 * Returns String representation of itself 
	 * @param tableSufix sufix for tables
	 * @return string representation
	 * @throws SQLException
	 */
	public abstract String getString(int tableSufix) throws SQLException;
	
//	//method that returns object names of conditions
//	//to form "FROM" part of query
//	public abstract HashSet getConditionObjects();

	public abstract HashSet getQueryObjects();
	
	/**
	 * This method returns true if there are conditions
	 * present for where part
	 * @return
	 */
	public abstract boolean hasConditions();

	/**
	 * This method returns trueif there is atleast a single 
	 * condition on any identified field else false
	 * @return
	 */
	public abstract boolean hasConditionOnIdentifiedField();
	
	
	
}