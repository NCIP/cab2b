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
       return RelationalOperator.getOperatorForStringRepresentation(operator);
    }
    
    public static String displayStringForRelationalOperator(RelationalOperator relationalOperator)
    {
    	return relationalOperator.getStringRepresentation();
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