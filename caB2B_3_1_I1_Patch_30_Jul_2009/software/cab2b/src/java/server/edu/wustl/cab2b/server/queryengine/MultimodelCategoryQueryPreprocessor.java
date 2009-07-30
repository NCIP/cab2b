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

/**
 * @author chetan_patil
 *
 */
public class MultimodelCategoryQueryPreprocessor {

    public void preprocessQuery(MultiModelCategoryQuery mmcQuery) {
        Map<String, List<String>> attributeNameValuesMap = getAttributeNameValuesMap(mmcQuery);

        Collection<ICab2bQuery> subQueries = mmcQuery.getSubQueries();
        for (ICab2bQuery query : subQueries) {
            IRule rule = getOperand(query);
            Iterator<ICondition> conditionIterator = rule.iterator();
            while (conditionIterator.hasNext()) {
                ICondition condition = conditionIterator.next();
                AttributeInterface attribute = condition.getAttribute();
                String[] nameToken = attribute.getName().split("_");
                List<String> values = attributeNameValuesMap.get(nameToken[0]);
                if (values != null) {
                    condition.setValues(values);
                }else{
                    conditionIterator.remove();
                }
            }
        }
    }

    private Map<String, List<String>> getAttributeNameValuesMap(MultiModelCategoryQuery mmcQuery) {
        Map<String, List<String>> attributeNameValuesMap = new HashMap<String, List<String>>();
        IRule rule = getOperand(mmcQuery);
        for (ICondition condition : rule) {
            attributeNameValuesMap.put(condition.getAttribute().getName(), condition.getValues());
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

}
