package edu.wustl.cab2b.client.ui.query;

import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * This class contains the utility methods required for building the UI.
 * @author Chandrakant Talele
 * @author Gautam Shetty
 */
public class Utility {

    /**
     * Returns the RelationalOperator, given the relational operator string. 
     * @param operator The relational operator string.
     * @return the RelationalOperator, given the relational operator string.
     */
    public static RelationalOperator getRelationalOperator(String operator) {
        return RelationalOperator.getOperatorForStringRepresentation(operator);
    }

    /**
     * @param relationalOperator Input Relational Operator
     * @return Display string for given operator 
     */
    public static String displayStringForRelationalOperator(RelationalOperator relationalOperator) {
        return relationalOperator.getStringRepresentation();
    }

    /**
     * Returns the LogicalOperator, given the logical operator string. 
     * @param operator The logical operator string.
     * @return the LogicalOperator, given the logical operator string.
     */
    public static LogicalOperator getLogicalOperator(String operator) {
        LogicalOperator logicalOperator = LogicalOperator.And;
        if (operator.equals("OR")) {
            logicalOperator = LogicalOperator.Or;
        }

        return logicalOperator;
    }

    /**
     * @param path Input path
     * @return string representation of given path
     */
    public static String getPathDisplayString(IPath path) {
        StringBuffer sb = new StringBuffer();
        sb.append("<HTML><B>Path</B>:");
        sb.append(path.getSourceEntity().getName());
        for (IAssociation association : path.getIntermediateAssociations()) {
            sb.append("<B>----></B>");
            sb.append(association.getTargetEntity().getName());
        }
        sb.append("<HTML>");
        return sb.toString();
    }

    /**
     * @param association Input association object
     * @return The roleName for given association
     */
    public static String getRoleName(IAssociation association) {
        String roleName = "";
        if (association instanceof IIntraModelAssociation) {
            IIntraModelAssociation intraModel = (IIntraModelAssociation) association;
            roleName = intraModel.getDynamicExtensionsAssociation().getTargetRole().getName();
        } else {
            IInterModelAssociation interModel = (IInterModelAssociation) association;
            roleName = interModel.getSourceAttribute().getName() + " = "
                    + interModel.getTargetAttribute().getName();
        }
        return roleName;
    }
}