/**
 * 
 */
package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.wustl.cab2b.common.queryengine.Cab2bQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.util.QueryUtility;

/**
 * 
 * @author deepak_shingan
 * 
 */
public class ParameterizedQueryDataModel {

    private ICab2bParameterizedQuery query;

    public ParameterizedQueryDataModel() {
        query = new Cab2bQuery();
    }

    public ParameterizedQueryDataModel(ICab2bQuery iQuery) {
        //converting query to Parameterized query 
        query = new Cab2bQuery(iQuery);
    }

    public ParameterizedQueryDataModel(ICab2bParameterizedQuery query) {
        this.query = query;
    }

    public void setQuery(ICab2bParameterizedQuery query) {
        this.query = query;
    }

    public ICab2bParameterizedQuery getQuery() {
        return query;
    }

    public String getQueryName() {
        return query.getName();
    }

    public void setQueryName(String queryName) {
        query.setName(queryName);
    }

    public String getQueryDescription() {
        return query.getDescription();
    }

    public void setQueryDescription(String description) {
        query.setDescription(description);
    }

    public Collection<IQueryEntity> getQueryEntities() {
        return query.getConstraints().getQueryEntities();
    }

    public Map<IExpressionId, Collection<ICondition>> getConditions() {
        return QueryUtility.getSelectedConditions(query);
    }

    public void addCondition(IExpressionId expressionID, ICondition newCondition) {
        IExpression expression = query.getConstraints().getExpression(expressionID);
        int noOfOperand = expression.numberOfOperands();
        
        ALL: for (int i = 0; i < noOfOperand; i++) {
            IExpressionOperand expressionOperand = expression.getOperand(i);
            if (!expressionOperand.isSubExpressionOperand()) {
                IRule rule = (IRule) expressionOperand;
                List<ICondition> conditionList = rule.getConditions();

                boolean isConditionAdded = false;
                for (int index = 0; index < conditionList.size(); index++) {
                    ICondition condition = conditionList.get(index);
                    if (condition.getAttribute().getId() == newCondition.getAttribute().getId()) {
                        conditionList.remove(condition);
                        conditionList.add(index, newCondition);
                        isConditionAdded = true;
                        break ALL;
                    } 
                }                
                if(!isConditionAdded) {
                    conditionList.add(newCondition);
                    break;
                }
            }
        }
    }

}
