/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;

import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.exception.QueryConverterException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * This Class converts the query having ANDed conditions to the query having ORed conditions
 *
 * @author chetan_patil
 */
public class QueryConverter {

    /**
     * This method converts the query having ANDed conditions to Query having ORed conditions.
     *
     * @param oredQuery
     * @return
     */
    public ICab2bQuery convertToKeywordQuery(ICab2bQuery oredQuery) {
        if (!isFeasibleToConvert(oredQuery)) {
            throw new QueryConverterException("Query must include at least one Condition having String Attribute.");
        }
        oredQuery.setIsKeywordSearch(Boolean.TRUE);

        for (IExpression expression : oredQuery.getConstraints()) {
            int noOfOperands = expression.numberOfOperands();
            for (int index = 0; index < noOfOperands; index++) {
                IExpressionOperand operand = expression.getOperand(index);
                if (operand instanceof IRule) {
                    convertRule((IRule) operand, index);
                } else if (operand instanceof IExpression) {
                    convertSubExpression(expression, operand, index);
                }
            }
        }

        return oredQuery;
    }

    /**
     * This method converts the ANDed conditions in the given Rule to multiple Rules each having single condition,
     * connected by OR operator.
     *
     * @param rule
     * @return the index of the last Rule added.
     */
    private void convertRule(IRule rule, int index) {
        IExpression expression = rule.getContainingExpression();
        Collection<ICondition> conditions = getAllStringConditions(rule);
        expression.removeOperand(rule);

        IRule newRule = QueryObjectFactory.createRule();
        for (ICondition condition : conditions) {
            condition.setRelationalOperator(RelationalOperator.Contains);
            newRule.addCondition(condition);
        }
        IConnector<LogicalOperator> connector = QueryObjectFactory.createLogicalConnector(LogicalOperator.Or, 0);
        expression.addOperand(index, newRule, connector);
    }

    private void convertSubExpression(IExpression expression, IExpressionOperand operand, int index) {
        IConnector<LogicalOperator> oldConnector = null;
        try {
            oldConnector = expression.getConnector(index, index + 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            oldConnector = QueryObjectFactory.createLogicalConnector(LogicalOperator.Unknown, 0);
        }

        IConnector<LogicalOperator> orConnector = QueryObjectFactory.createLogicalConnector(LogicalOperator.Or, 0);
        if (!LogicalOperator.Unknown.equals(oldConnector.getOperator())) {
            expression.removeOperand(operand);
            expression.addOperand(index, operand, orConnector);
        }
    }

    /**
     * This method returns all the conditions having string type attribute from the given Rule.
     *
     * @param rule
     * @return
     */
    private Collection<ICondition> getAllStringConditions(IRule rule) {
        Collection<ICondition> stringConditions = new ArrayList<ICondition>(rule.size());
        for (ICondition condition : rule) {
            AttributeInterface attribute = condition.getAttribute();
            if (attribute.getAttributeTypeInformation() instanceof StringAttributeTypeInformation) {
                stringConditions.add(condition);
            }
        }

        return stringConditions;
    }

    /**
     * This method check whether the given query is ORed or not.
     *
     * @param query
     * @return true if ORed; false otherwise
     */
    public Boolean isKeywordQuery(ICab2bQuery query) {
        IConstraints constraints = query.getConstraints();
        try {
            constraints.getRootExpression();
        } catch (MultipleRootsException e) {
            throw new QueryConverterException(e.getMessage(), e);
        }

        Boolean isORedQuery = query.isKeywordSearch();
        final IConnector<LogicalOperator> connectorOR =
                QueryObjectFactory.createLogicalConnector(LogicalOperator.Or);

        LOOP: for (IExpression expression : constraints) {
            int noOfOperands = expression.numberOfOperands();
            for (int i = 0; i < noOfOperands; i++) {
                if ((i + 1) < noOfOperands) {
                    IConnector<LogicalOperator> connector = expression.getConnector(i, i + 1);
                    if (!connectorOR.equals(connector)) {
                        isORedQuery = Boolean.FALSE;
                        break LOOP;
                    }
                }
            }
        }

        return isORedQuery;
    }

    /**
     * This method check if the regular ANDed query can be converted to ORed keyword search query
     * @param query
     * @return true if can be converted; false otherwise
     */
    public Boolean isFeasibleToConvert(ICab2bQuery query) {
        Boolean isFeasible = Boolean.FALSE;

        LOOP: for (IExpression expression : query.getConstraints()) {
            for (IExpressionOperand operand : expression) {
                if (operand instanceof IRule) {
                    final IRule rule = (IRule) operand;
                    isFeasible = !getAllStringConditions(rule).isEmpty();
                    if (isFeasible) {
                        break LOOP;
                    }
                }
            }
        }

        return isFeasible;
    }

}
