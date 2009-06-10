package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.Cab2bQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.utils.QueryUtility;
import edu.wustl.common.util.ObjectCloner;

/**
 * Datamodel class for Save query operations 
 * @author deepak_shingan
 * 
 */
public class ParameterizedQueryDataModel {

    /**
     * Cab2b Parameterized Query 
     */
    private ICab2bQuery query;

    /**
     * Constructor
     * Creates a new parameterized caB2B query
     */
    public ParameterizedQueryDataModel() {
        query = new Cab2bQuery();
    }

    /**
     * @param iQuery
     */
    public ParameterizedQueryDataModel(ICab2bQuery iQuery) {
        if (iQuery == null) {
            this.query = new Cab2bQuery();
        } else {
            query = ObjectCloner.clone(iQuery);
        }
    }

    /**
     * @param query
     */
    public void setQuery(Cab2bQuery query) {
        this.query = query;
    }

    /**
     * @return ICab2bQuery  query
     */
    public ICab2bQuery getQuery() {
        return query;
    }

    /**
     * Gets query name
     * @return query name
     */
    public String getQueryName() {
        if (query == null)
            return null;
        return query.getName();
    }

    /**
     * Sets query name
     * @param queryName
     */
    public void setQueryName(String queryName) {
        if (query == null)
            return;
        query.setName(queryName);
    }

    /**
     * @return String QueryDescription
     */
    public String getQueryDescription() {
        if (query == null)
            return null;
        return query.getDescription();
    }

    /**
     * Sets query desription
     * @param String description
     */
    public void setQueryDescription(String description) {
        if (query == null)
            return;
        query.setDescription(description);
    }

    /**
     * @return Collection<IQueryEntity> of query entities
     */
    public Collection<IQueryEntity> getQueryEntities() {
        if (query == null)
            return null;
        return query.getConstraints().getQueryEntities();
    }

    /**
     * @return Map<Integer, Collection<ICondition>> for All Selected Conditions
     */
    public Map<Integer, Collection<ICondition>> getConditions() {
        if (query != null)
            return convert(QueryUtility.getAllSelectedConditions(query));

        return null;
    }

    /**
     * Returns Map of expressionID and collection of AttributeInterface for the query      
     * @return
     */
    public Map<Integer, Collection<AttributeInterface>> getAllAttributes() {
        if (query != null)
            return convert(QueryUtility.getAllAttributes(query));
        return null;
    }

    /**
     * @param <T>
     * @param map
     * @return
     */
    private <T> Map<Integer, T> convert(Map<IExpression, T> map) {
        Map<Integer, T> m = new HashMap<Integer, T>(map.size());
        Set<Entry<IExpression, T>> entrySet = map.entrySet();
        for (Entry<IExpression, T> entry : entrySet) {
            m.put(entry.getKey().getExpressionId(), entry.getValue());
        }
        return m;
    }

    /**
     * Method to remove conditions from query
     * 
     * @param expressionID
     * @param newCondition
     */
    public void removeCondition(int expressionID, ICondition newCondition) {
        if (query == null || expressionID < 0 || newCondition == null)
            return;

        IExpression expression = query.getConstraints().getExpression(expressionID);
        int noOfOperand = expression.numberOfOperands();

        ALL: for (int i = 0; i < noOfOperand; i++) {
            IExpressionOperand expressionOperand = expression.getOperand(i);
            if (expressionOperand instanceof IRule) {
                IRule rule = (IRule) expressionOperand;
                Iterator<ICondition> iterator = rule.iterator();
                while (iterator.hasNext()) {
                    ICondition condition = iterator.next();
                    if (condition.getAttribute() == newCondition.getAttribute()) {
                        iterator.remove();
                        break ALL;
                    }
                }
            }
        }
    }

    /**
     * Method to add/replace/change conditions from query
     * 
     * @param expressionID
     * @param newCondition
     */
    public void addCondition(Integer expressionID, ICondition newCondition) {
        if (query == null || expressionID == null || newCondition == null)
            return;

        IExpression expression = query.getConstraints().getExpression(expressionID);
        int noOfOperand = expression.numberOfOperands();

        ALL: for (int i = 0; i < noOfOperand; i++) {
            IExpressionOperand expressionOperand = expression.getOperand(i);
            if (expressionOperand instanceof IRule) {
                boolean isConditionAdded = false;

                IRule rule = (IRule) expressionOperand;
                Iterator<ICondition> iterator = rule.iterator();
                while (iterator.hasNext()) {
                    ICondition condition = iterator.next();
                    if (condition.getAttribute() == newCondition.getAttribute()) {
                        iterator.remove();
                        rule.addCondition(newCondition);
                        isConditionAdded = true;
                        break ALL;
                    }
                }
                if (!isConditionAdded) {
                    rule.addCondition(newCondition);
                    break;
                }
            }
        }
    }

    /**
     * This method sets the user id for query.
     * @param userId
     */
    public void setQueryUserId(Long userId) {
        query.setUserId(userId);
    }
}