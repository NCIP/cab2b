/**
 * <p>Title: Utility Class>
 * <p>Description:  This class contains the utility methods required for
 * building the Query object from the UI.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.client.ui.query;

import java.util.List;

import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * This class contains the utility methods required for
 * building the Query object from the UI.
 * @author gautam_shetty
 */
public class Utility
{
    
    /**
     * Returns the RelationalOperator, given the relational operator string. 
     * @param operator The relational operator string.
     * @return the RelationalOperator, given the relational operator string.
     */
    public static RelationalOperator getRelationalOperator(String operator)
    {
        RelationalOperator relationalOperator = null;
        if (operator.equals("Starts With"))
        {
        	relationalOperator = RelationalOperator.StartsWith;
        }
        else if (operator.equals("Ends With"))
        {
            relationalOperator = RelationalOperator.EndsWith;
        }else if (operator.equals("Contains"))
        {
            relationalOperator = RelationalOperator.Contains;
        }else if (operator.equals("Equals"))
        {
            relationalOperator = RelationalOperator.Equals;
        }else if (operator.equals("Not Equals"))
        {
            relationalOperator = RelationalOperator.NotEquals;
        }else if (operator.equals("Is Null"))
        {
            relationalOperator = RelationalOperator.IsNull;
        }else if (operator.equals("Is Not Null"))
        {
            relationalOperator = RelationalOperator.IsNotNull;
        }else if (operator.equals("Between"))
        {
            relationalOperator = RelationalOperator.Between;
        }else if (operator.equals("Less than"))
        {
            relationalOperator = RelationalOperator.LessThan;
        }else if (operator.equals("Less than or Equal to"))
        {
            relationalOperator = RelationalOperator.LessThanOrEquals;
        }else if (operator.equals("Greater than"))
        {
            relationalOperator = RelationalOperator.GreaterThan;
        }else if (operator.equals("Greater than or Equal to"))
        {
            relationalOperator = RelationalOperator.GreaterThanOrEquals;
        }else if (operator.equals("In"))
        {
            relationalOperator = RelationalOperator.In;
        }else if (operator.equals("Not In"))
        {
            relationalOperator = RelationalOperator.NotIn;
        }
        
        return relationalOperator;
    }
    
    public static String displayStringForRelationalOperator(RelationalOperator relationalOperator)
    {
    	String displayString = null;
    	if(relationalOperator == RelationalOperator.StartsWith)
    	{
    		displayString = "Starts With";
    	}
    	else if (relationalOperator == RelationalOperator.EndsWith)
        {
           displayString = "Ends With";
        }else if (relationalOperator == RelationalOperator.Contains)
        {
            
            displayString = "Contains";
        }else if (relationalOperator == RelationalOperator.Equals)
        {
            displayString = "Equals";
        }else if (relationalOperator == RelationalOperator.NotEquals)
        {
            
            displayString = "Not Equals";
        }else if (relationalOperator == RelationalOperator.IsNull)
        {
            displayString = "Is Null";
        }else if (relationalOperator == RelationalOperator.IsNotNull)
        {
            displayString = "Is Not Null";
        }else if (relationalOperator == RelationalOperator.Between)
        {
            
            displayString = "Between";
        }else if (relationalOperator == RelationalOperator.LessThan)
        {
            
            displayString = "Less than";
        }else if (relationalOperator == RelationalOperator.LessThanOrEquals)
        {
            displayString = "Less than or Equal to";
        }else if (relationalOperator == RelationalOperator.GreaterThan)
        {
            displayString = "Greater than";
        }else if (relationalOperator == RelationalOperator.GreaterThanOrEquals)
        {
            displayString = "Greater than or Equal to";
        }else if (relationalOperator == RelationalOperator.In)
        {
        	displayString = "In";
        }else if (relationalOperator == RelationalOperator.NotIn)
        {
            displayString = "Not In";
        }
        
    	
    	return displayString;
    }
    
    /**
     * Returns the LogicalOperator, given the logical operator string. 
     * @param operator The logical operator string.
     * @return the LogicalOperator, given the logical operator string.
     */
    public static LogicalOperator getLogicalOperator(String operator)
    {
        LogicalOperator logicalOperator = LogicalOperator.And;
        if (operator.equals("OR"))
        {
            logicalOperator = LogicalOperator.Or;
        }
        
        return logicalOperator;
    }
    
    public static String getPathDisplayString(IPath path)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<HTML><B>Path</B>:");
		List<IAssociation> list = path.getIntermediateAssociations();
		sb.append(path.getSourceEntity().getName());
		for(int i=0; i<list.size(); i++)
		{
			sb.append("<B>----></B>");
			sb.append(list.get(i).getTargetEntity().getName());
		}
		sb.append("<HTML>");
		return sb.toString();
	}
    /**
     * Method to get roleName for given association
     * @param association
     * @return
     */
    public static String getRoleName(IAssociation association)
	{
		String roleName;
		if(association instanceof IIntraModelAssociation)
	    {
			roleName = ((IIntraModelAssociation)association).getDynamicExtensionsAssociation().getTargetRole().getName();
	    }
		else
		{
			 roleName = ((IInterModelAssociation)association).getSourceAttribute().getName() + " = " + 
			 ((IInterModelAssociation)association).getTargetAttribute().getName();
		}
		return roleName;
	}
}