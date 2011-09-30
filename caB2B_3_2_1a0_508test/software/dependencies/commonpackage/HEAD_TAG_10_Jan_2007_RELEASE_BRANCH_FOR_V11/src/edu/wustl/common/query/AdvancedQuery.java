package edu.wustl.common.query;




/**
 *<p>Title: AdvancedQuery</p>
 *<p>Description:  This class provides implementation for advanced query interface</p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class AdvancedQuery extends Query {
    
	/**
	 * Name: Prafull
	 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
	 * object of class QuerySessionData will be saved session, which will contain the required information for query execution while navigating through query result pages.
	 * 
	 * For Advance Query the Order by clause will be added in following order given by array.  
	 */
	public static String[] QUERY_ORDERBY_SEQUENCE = {PARTICIPANT,COLLECTION_PROTOCOL, COLLECTION_PROTOCOL_REGISTRATION, SPECIMEN_COLLECTION_GROUP, SPECIMEN};
	
    /**
     * Constructs a AdvancedQuery object
     * @param queryStartObject - Start object for the query
     */
	public AdvancedQuery(final String queryStartObject){
	    this.whereConditions = new CatissueAdvancedConditionsImpl();
	    this.queryStartObject = queryStartObject;
	}

	/**
	 * Add child conditions node to parent conditions node
	 * @param parent parent node
	 * @param child child node
	 * @return true if child is added to parent else false
	 */
	public boolean addCondition(AdvancedConditionsNode parent , AdvancedConditionsNode child)
	{
		return ((AdvancedConditionsImpl)this.whereConditions).addCondition(parent,child);
	}
	
	/**
	 * Add condition to root
	 * @param condition - object to be added
	 * @return
	 */
	public boolean addCondition(AdvancedConditionsNode condition)
	{
		return ((AdvancedConditionsImpl)this.whereConditions).addCondition(condition);
	}

}