package edu.wustl.common.query;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Vector;

import edu.wustl.common.util.global.Constants;
 



/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class SimpleConditionsImpl extends ConditionsImpl {

	/**
	 * Vector of SimpleConditionsNode objects 
	 * This forms the storage of conditions in Simple Query Interface
	 */
    private Vector whereConditions = new Vector();
	

	public SimpleConditionsImpl(){

	}

	/**
	 * Adds condition to whereConditions Vector 
	 * @param condition
	 * @return true (as per the general contract of Collection.add).
	 */
	public boolean addCondition(SimpleConditionsNode condition){
		return whereConditions.add(condition);
	}
	
	/**
	 * 
	 * @return
	 */
	public SimpleConditionsNode getCondition(){
		return null;
	}

	public boolean editCondition(){
		return false;
	}

    /* (non-Javadoc)
     * @see edu.wustl.caTISSUECore.query.ConditionsImpl#getString()
     */
    public String getString(int tableSufix) throws SQLException
    {
        StringBuffer whereConditionsString = new StringBuffer();
        SimpleConditionsNode simpleConditionsNode;
       
        Vector activityStatusConditions = new Vector();
        boolean isActivityStatusField = false;
        for(int i=0; i < whereConditions.size(); i++)
        {
        	simpleConditionsNode = (SimpleConditionsNode)whereConditions.get(i);
        	if(i==0 && hasConditionsExceptActivityStatus())
        		whereConditionsString.append(" ( ");
        	
        	// Separating the activity status fields from rest of the fields so that 
        	//they are not included in brackets. 
        	//Assumption is that activity status conditions are always at the end
        	String field = simpleConditionsNode.getCondition().getDataElement().getField();
        	if(field != null && field.equalsIgnoreCase(Constants.ACTIVITY_STATUS_COLUMN))
        	{
        		for(int j=i; j<whereConditions.size(); j++)
        		{
        			activityStatusConditions.add(whereConditions.get(j));
        		}
        		break;
        	}
            if(i != whereConditions.size()-1 && !((SimpleConditionsNode)whereConditions.get(i+1)).getCondition().getDataElement().getField().equals(Constants.ACTIVITY_STATUS_COLUMN))
                whereConditionsString.append((simpleConditionsNode).toSQLString(tableSufix));
            else if(hasConditionsExceptActivityStatus())
                whereConditionsString.append((simpleConditionsNode).getCondition().toSQLString(tableSufix)+" ) ");
            whereConditionsString.append(" ");
        }
        
        //Adding activity status fields in the end
        if(activityStatusConditions.size() > 0 && hasConditionsExceptActivityStatus())
        {
        	whereConditionsString.append(" "+Operator.AND+" ");
        }
        else
        {
        	whereConditionsString.append(" ");
        }
        for(int i=0; i < activityStatusConditions.size(); i++)
        {
        	 if(i != activityStatusConditions.size()-1 )
        	 	whereConditionsString.append(((SimpleConditionsNode)activityStatusConditions.get(i)).toSQLString(tableSufix));
        	 else
        	 	whereConditionsString.append(((SimpleConditionsNode)activityStatusConditions.get(i)).getCondition().toSQLString(tableSufix));
        }

        return whereConditionsString.toString();
    }

//    public HashSet getConditionObjects()
//    {
//        HashSet set = new HashSet();
//        
//        /**
//         * For all elements in whereConditions add  
//         * objects to the set
//         */ 
//        for(int i=0; i<whereConditions.size();i++)
//        {
//            SimpleConditionsNode conditionsNode = (SimpleConditionsNode)whereConditions.get(i);
//            set.add(conditionsNode.getCondition().getDataElement().getTable());
//        }
//        return set;
//    }
    
    /**
     * Inserts the specified condition at the specified position in whereConditions Vector. Shifts the element currently at 
     * that position (if any) and any subsequent elements to the right (adds one to their indices).
     * @param position index at which the specified element is to be inserted.
     * @param condition condition to be inserted
     */
    public boolean addCondition(int position, SimpleConditionsNode condition)
    {
        try
        {
         whereConditions.insertElementAt(condition,position);
         return true;
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            return false;
        }
        
    }

/* (non-Javadoc)
 * @see edu.wustl.catissuecore.query.ConditionsImpl#getQueryObjects(java.lang.String)
 */
public HashSet getQueryObjects()
{
    HashSet queryObjects = new HashSet();
    SimpleConditionsNode condition;
    for(int i=0; i<whereConditions.size(); i++)
    {
        condition = (SimpleConditionsNode) whereConditions.get(i);
        if(condition !=null)
        {
            queryObjects.add(condition.getConditionTable());
        }
    }
    return queryObjects;
}

/* (non-Javadoc)
 * @see edu.wustl.catissuecore.query.ConditionsImpl#hasConditions()
 */
public boolean hasConditions() {
	boolean hasConditions = false;
	if(whereConditions.size()>0)
	{
	    SimpleConditionsNode simpleConditionsNode;
		for(int i=0; i< whereConditions.size(); i++)
		{
			simpleConditionsNode = (SimpleConditionsNode) whereConditions.get(i);
			if(simpleConditionsNode!= null )
					{
			    		String field = simpleConditionsNode.getCondition().getDataElement().getField();
			    		if(field != null )
			    		{
			    		    hasConditions = true;
							break;
			    		}
					}
		}
	}
	return hasConditions;
}

/**
 * Returns true if there are conditions other than activity status condition in query
 * @return
 */
public boolean hasConditionsExceptActivityStatus() {
	boolean hasConditions = false;
	SimpleConditionsNode simpleConditionsNode;
	for(int i=0; i< whereConditions.size(); i++)
	{
		simpleConditionsNode = (SimpleConditionsNode) whereConditions.get(i);
		if(simpleConditionsNode!= null )
				{
		    
		    		String field = simpleConditionsNode.getCondition().getDataElement().getField();
		    		if(field != null && !field.equals(Constants.ACTIVITY_STATUS_COLUMN))
		    		{
		    		    hasConditions = true;
						break;
		    		}
				}
	}
	return hasConditions;
}

/* (non-Javadoc)
 * @see edu.wustl.catissuecore.query.ConditionsImpl#hasConditionOnIdentifiedField()
 */
public boolean hasConditionOnIdentifiedField() {
	boolean hasConditionOnIdentifiedField = false;
	SimpleConditionsNode simpleConditionsNode;
	Condition condition;
	for(int i=0; i< whereConditions.size(); i++)
	{
		simpleConditionsNode = (SimpleConditionsNode) whereConditions.get(i);
		condition = (Condition) simpleConditionsNode.getCondition();
		if(condition.isConditionOnIdentifiedField())
		{
			hasConditionOnIdentifiedField = true;
			return hasConditionOnIdentifiedField;
		}
	}
	return hasConditionOnIdentifiedField;
}

    
}