package edu.wustl.common.query;

import java.sql.SQLException;




/**
 *<p>Title: SimpleConditionsNode</p>
 *<p>Description:  This represents a condition node in case of Simple query</p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class SimpleConditionsNode {
    
    /**
     * A Condition on a data element in the query
     */
	private Condition condition = new Condition();
	
	/**
	 * Operation with next consition i.e. AND/OR
	 */
	private Operator operator = new Operator(Operator.AND);

	public SimpleConditionsNode()
	{
	    
	}
	
	/**
	 * Constructor
	 * @param condition
	 * @param operator
	 */
	public SimpleConditionsNode(Condition condition, Operator operationWithNextcondition)
	{
	    this.operator = operationWithNextcondition;
	    this.condition = condition;
	}
	
	/**
	 * 
	 * @param tableSufix
	 * @return String representation for this node condition
	 * @throws SQLException
	 */
	public String toSQLString(int tableSufix) throws SQLException
	{
        return condition.toSQLString(tableSufix) + " " + operator.toSQLString();
	}

    public Condition getCondition()
    {
        return condition;
    }
    public void setCondition(Condition condition)
    {
        this.condition = condition;
    }
    public Operator getOperator()
    {
        return operator;
    }
    public void setOperator(
            Operator operator)
    {
        this.operator = operator;
    }
    
//    public String getConditionObject()
//    {
//        return condition.getDataElement().getTableAliasName();
//    }
    
    public Table getConditionTable()
    {
        return condition.getDataElement().getTable();
    }
}