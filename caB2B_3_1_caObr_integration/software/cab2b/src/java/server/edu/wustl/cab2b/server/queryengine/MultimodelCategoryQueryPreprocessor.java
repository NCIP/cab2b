/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * @author chetan_patil
 *
 */
public class MultimodelCategoryQueryPreprocessor {

    /**
     * This method adds the user defined conditions in the sub-queries of the compound queries.
     * @param mmcQuery
     */
    public void preprocessQuery(MultiModelCategoryQuery mmcQuery) {
        Map<String, UserDefinedCondition> attributeNameValuesMap = getAttributeNameValuesMap(mmcQuery);

        Collection<ICab2bQuery> subQueries = mmcQuery.getSubQueries();
        for (ICab2bQuery query : subQueries) {
            IRule rule = getOperand(query);
            Iterator<ICondition> conditionIterator = rule.iterator();
            while (conditionIterator.hasNext()) {
                ICondition condition = conditionIterator.next();
                AttributeInterface attribute = condition.getAttribute();
                String[] nameToken = attribute.getName().split("_");
                UserDefinedCondition userDefinedCondition = attributeNameValuesMap.get(nameToken[0]);

                if (userDefinedCondition != null) {
                    RelationalOperator relationaloperator = userDefinedCondition.getRelationalOperator();
                    List<String> values = userDefinedCondition.getValues();
                    if (values != null && !values.isEmpty()) {
                        condition.setRelationalOperator(relationaloperator);
                        condition.setValues(values);
                    }
                } else {
                    conditionIterator.remove();
                }
            }
        }
    }

    private Map<String, UserDefinedCondition> getAttributeNameValuesMap(MultiModelCategoryQuery mmcQuery) {
        Map<String, UserDefinedCondition> attributeNameValuesMap = new HashMap<String, UserDefinedCondition>();
        IRule rule = getOperand(mmcQuery);
        for (ICondition condition : rule) {
            UserDefinedCondition userDefinedCondition =
                    new UserDefinedCondition(condition.getRelationalOperator(), condition.getValues());
            attributeNameValuesMap.put(condition.getAttribute().getName(), userDefinedCondition);
        }
        return attributeNameValuesMap;
    }

    private IRule getOperand(ICab2bQuery query) {
        IExpression expression = query.getConstraints().getExpression(1);
        IExpressionOperand operand = expression.getOperand(0);

        IRule rule = null;
        if (operand instanceof IRule) {
            rule = (IRule) operand;
        }

        return rule;
    }
    
    private class UserDefinedCondition {
        private RelationalOperator relationalOperator;

        private List<String> values;

        private UserDefinedCondition(RelationalOperator relationalOperator, List<String> values) {
            this.relationalOperator = relationalOperator;
            this.values = values;
        }

        /**
         * @return the relationalOperator
         */
        private RelationalOperator getRelationalOperator() {
            return relationalOperator;
        }

        /**
         * @return the values
         */
        private List<String> getValues() {
            return values;
        }
    }


}
