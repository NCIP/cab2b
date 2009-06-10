package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;

import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.cab2b.common.exception.QueryConverterException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.common.hibernate.HibernateCleanser;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;

/**
 * This Class converts the query having ANDed conditions to the query having ORed conditions
 *
 * @author chetan_patil
 */
public class QueryConverter {

    /**
     * This method converts the query having ANDed conditions to Query having ORed conditions.
     *
     * @param query
     * @return
     */
    public ICab2bQuery changeConditionOperator(ICab2bQuery query) {
        if (!isFeasibleToConvert(query)) {
            throw new QueryConverterException("Query must include at least one Condition having String Attribute.");
        }

        ICab2bQuery oredQuery = (ICab2bQuery) DynamicExtensionsUtility.cloneObject(query);
        if (oredQuery.getId() != null) {
            new HibernateCleanser(oredQuery).clean();
        }
        oredQuery.setName(oredQuery.getName() + "_ORed");

        for (IExpression expression : oredQuery.getConstraints()) {
            Collection<IRule> rules = new ArrayList<IRule>();
            for (IExpressionOperand operand : expression) {
                if (operand instanceof IRule) {
                    rules.add((IRule) operand);
                }
            }

            for (IRule rule : rules) {
                convertRule(rule);
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
    private void convertRule(IRule rule) {
        IExpression expression = rule.getContainingExpression();
        Collection<ICondition> conditions = getAllConditions(rule);
        expression.removeOperand(rule);

        final int totalOperands = expression.numberOfOperands() + conditions.size() - 1;
        int index = 0;
        for (ICondition condition : conditions) {
            AttributeInterface attribute = condition.getAttribute();
            if (attribute.getAttributeTypeInformation() instanceof StringAttributeTypeInformation) {
                IRule newRule = QueryObjectFactory.createRule();
                newRule.addCondition(condition);

                if (index < totalOperands) {
                    IConnector<LogicalOperator> connector = QueryObjectFactory.createLogicalConnector(
                                                                                                      LogicalOperator.Or,
                                                                                                      0);
                    expression.addOperand(index++, newRule, connector);
                } else {
                    expression.addOperand(newRule);
                }
            }
        }
    }

    /**
     * This method returns all the conditions from the given Rule.
     *
     * @param rule
     * @return
     */
    private Collection<ICondition> getAllConditions(IRule rule) {
        Collection<ICondition> conditions = new ArrayList<ICondition>(rule.size());
        for (ICondition condition : rule) {
            conditions.add(condition);
        }

        return conditions;
    }

    /**
     * This method check whether the given query is ORed or not.
     *
     * @param query
     * @return true if ORed; false otherwise
     */
    public Boolean isORedQuery(ICab2bQuery query) {
        boolean isORedQuery = Boolean.TRUE;
        final IConnector<LogicalOperator> connectorOR = QueryObjectFactory.createLogicalConnector(LogicalOperator.Or);

        IConstraints constraints = query.getConstraints();

        IExpression rootExpression = null;
        try {
            rootExpression = constraints.getRootExpression();
        } catch (MultipleRootsException e) {
            throw new QueryConverterException(e.getMessage(), e);
        }

        if (rootExpression != null) {
            LOOP: for (IExpression expression : constraints) {
                int noOfOperands = expression.numberOfOperands();
                for (int i = 0; i < noOfOperands - 1; i++) {
                    IConnector<LogicalOperator> connector = expression.getConnector(i, i + 1);
                    if (!connector.equals(connectorOR)) {
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
                    isFeasible = isAnyStringCondition(getAllConditions(rule));
                    if (isFeasible) {
                        break LOOP;
                    }
                }
            }
        }

        return isFeasible;
    }

    /**
     * This method checks if there is any condition with String type attribute in given list
     * @param conditions
     * @return
     */
    private Boolean isAnyStringCondition(final Collection<ICondition> conditions) {
        Boolean hasStringCondition = Boolean.FALSE;
        for (ICondition condition : conditions) {
            final AttributeInterface attribute = condition.getAttribute();
            if (attribute.getAttributeTypeInformation() instanceof StringAttributeTypeInformation) {
                hasStringCondition = Boolean.TRUE;
                break;
            }
        }
        return hasStringCondition;
    }

}
