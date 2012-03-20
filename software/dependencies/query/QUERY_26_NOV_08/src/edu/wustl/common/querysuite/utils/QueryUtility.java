/**
 *
 */

package edu.wustl.common.querysuite.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IParameter;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.util.Collections;

/**
 * @author chetan_patil
 * @created Sep 20, 2007, 12:07:59 PM
 */
public class QueryUtility {

    /**
     * This method returns all the selected Condition for a given query.
     *
     * @param query
     * @return Map of ExpressionId -> Collection of Condition
     */
    public static Map<IExpression, Collection<ICondition>> getAllSelectedConditions(IQuery query) {
        Map<IExpression, Collection<ICondition>> expressionIdConditionCollectionMap = new HashMap<IExpression, Collection<ICondition>>();
        if (query != null) {
            for (IExpression expression : query.getConstraints()) {
                if (expression.isVisible()) {
                    Collection<ICondition> conditions = new ArrayList<ICondition>();
                    for (IExpressionOperand expressionOperand : expression) {
                        if (expressionOperand instanceof IRule) {
                            IRule rule = (IRule) expressionOperand;
                            conditions.addAll(Collections.list(rule));
                        }
                    }
                    expressionIdConditionCollectionMap.put(expression, conditions);
                }
            }
        }

        return expressionIdConditionCollectionMap;
    }

    /**
     * This method returns the collection of all non-parameterized conditions form a given query.
     *
     * @param paramQuery parameterized query
     * @return collection of non-parameterized conditions
     */
    public static Collection<ICondition> getAllNonParameteriedConditions(IParameterizedQuery paramQuery) {
        Map<IExpression, Collection<ICondition>> conditionsMap = getAllSelectedConditions(paramQuery);
        Collection<ICondition> nonParamConditions = new ArrayList<ICondition>();
        for (Collection<ICondition> conditions : conditionsMap.values()) {
            nonParamConditions.addAll(conditions);
        }

        nonParamConditions.removeAll(getAllParameterizedConditions(paramQuery));

        return nonParamConditions;
    }

    public static Collection<ICondition> getAllParameterizedConditions(IParameterizedQuery paramQuery) {
        Collection<ICondition> paramConditions = new ArrayList<ICondition>();

        List<IParameter<?>> parameters = paramQuery.getParameters();
        for (IParameter<?> parameter : parameters) {
            if (parameter.getParameterizedObject() instanceof ICondition) {
                paramConditions.add((ICondition) parameter.getParameterizedObject());
            } 
        }

        return paramConditions;
    }

    /**
     * This method returns all the attributes of the expressions involved in a given query.
     *
     * @param query
     * @return Map of ExpressionId -> Collection of Attribute
     */
    public static Map<IExpression, Collection<AttributeInterface>> getAllAttributes(IQuery query) {
        Map<IExpression, Collection<AttributeInterface>> expressionIdAttributeCollectionMap = new HashMap<IExpression, Collection<AttributeInterface>>();
        if (query != null) {
            for (IExpression expression : query.getConstraints()) {
                if (expression.isVisible()) {
                    EntityInterface deEntity = expression.getQueryEntity().getDynamicExtensionsEntity();
                    Collection<AttributeInterface> attributeCollection = deEntity.getEntityAttributesForQuery();
                    expressionIdAttributeCollectionMap.put(expression, attributeCollection);
                }
            }
        }
        return expressionIdAttributeCollectionMap;
    }

    public static Set<ICustomFormula> getCustomFormulas(IQuery query) {
        return getCustomFormulas(query.getConstraints());
    }

    public static Set<ICustomFormula> getCustomFormulas(IConstraints constraints) {
        Set<ICustomFormula> res = new HashSet<ICustomFormula>();
        for (IExpression expression : constraints) {
            res.addAll(getCustomFormulas(expression));
        }
        return res;
    }

    public static Set<ICustomFormula> getCustomFormulas(IExpression expression) {
        Set<ICustomFormula> res = new HashSet<ICustomFormula>();
        for (IExpressionOperand operand : expression) {
            if (operand instanceof ICustomFormula) {
                res.add((ICustomFormula) operand);
            }
        }
        return res;
    }

    public static Set<IExpression> getExpressionsInFormula(ICustomFormula formula) {
        Set<IExpression> res = new HashSet<IExpression>();
        res.addAll(getExpressionsInTerm(formula.getLhs()));
        for (ITerm rhs : formula.getAllRhs()) {
            res.addAll(getExpressionsInTerm(rhs));
        }
        return res;
    }

    public static Set<IExpressionAttribute> getAttributesInFormula(ICustomFormula formula) {
        Set<IExpressionAttribute> res = new HashSet<IExpressionAttribute>();
        res.addAll(getAttributesInTerm(formula.getLhs()));
        for (ITerm rhs : formula.getAllRhs()) {
            res.addAll(getAttributesInTerm(rhs));
        }
        return res;
    }

    public static Set<IExpression> getExpressionsInTerm(ITerm term) {
        Set<IExpression> res = new HashSet<IExpression>();
        for (IExpressionAttribute attr : getAttributesInTerm(term)) {
            res.add(attr.getExpression());
        }
        return res;
    }

    public static Set<IExpressionAttribute> getAttributesInTerm(ITerm term) {
        Set<IExpressionAttribute> res = new HashSet<IExpressionAttribute>();
        for (IArithmeticOperand operand : term) {
            if (operand instanceof IExpressionAttribute) {
                res.add((IExpressionAttribute) operand);
            }
        }
        return res;
    }

    public static Set<IExpression> getContainingExpressions(IConstraints constraints, ICustomFormula formula) {
        Set<IExpression> res = new HashSet<IExpression>();
        for (IExpression expression : constraints) {
            if (expression.containsOperand(formula)) {
                res.add(expression);
            }
        }
        return res;
    }

    public static Collection<ICustomFormula> getAllParameterizedCustomFormulas(IParameterizedQuery paramQuery) {
        Collection<ICustomFormula> paramCustomFormulas = new HashSet<ICustomFormula>();

        List<IParameter<?>> parameters = paramQuery.getParameters();
        for (IParameter<?> parameter : parameters) {
            if (parameter.getParameterizedObject() instanceof ICustomFormula) {
                paramCustomFormulas.add((ICustomFormula) parameter.getParameterizedObject());
                parameter.getName();
            }
        }
        return paramCustomFormulas;
    }

    public static IExpression getExpression(IParameter<ICondition> parameter, IQuery query) {
        ICondition condition = parameter.getParameterizedObject();
        IExpression returnExpression = null;
        if (query != null) {
            LOOP: for (IExpression expression : query.getConstraints()) {
                if (expression.isVisible()) {
                    for (IExpressionOperand expressionOperand : expression) {
                        if (expressionOperand instanceof IRule) {
                            IRule rule = (IRule) expressionOperand;
                            for (ICondition ruleCondition : rule) {
                                if (ruleCondition.equals(condition)) {
                                    returnExpression = expression;
                                    break LOOP;
                                }
                            }
                        }
                    }
                }
            }
        }
        return returnExpression;
    }

}
