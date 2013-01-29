/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

/**
 * @author Mandar Shidhore
 * @version 1.0
 * @created 12-Oct-2006 15.16.04 AM
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;

/**
 * 
 * @author chetan_patil
 * @created Aug 8, 2007, 2:30:11 PM
 * 
 * @hibernate.joined-subclass table="QUERY_RULE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class Rule extends BaseQueryObject implements IRule {

    private static final long serialVersionUID = 7408369497435719981L;

    private List<ICondition> conditions = new ArrayList<ICondition>();

    private IExpression containingExpression;

    /**
     * Default Constructor
     */
    public Rule() {

    }

    /**
     * Constructor to instanciate object of Rule class.
     * 
     * @param conditions The list of Conditions to set.
     */
    public Rule(List<ICondition> conditions) {
        if (conditions != null) {
            this.conditions = conditions;
        }
    }

    /**
     * This method returns the list of conditions of the rule.
     * 
     * @return List of ICondition objects.
     * @see edu.wustl.common.querysuite.queryobject.ICondition#getValues()
     * 
     * @hibernate.list name="conditions" cascade="all-delete-orphan"
     *                 inverse="false" lazy="false"
     * @hibernate.collection-key column="QUERY_RULE_ID"
     * @hibernate.collection-index column="POSITION" type="integer"
     * @hibernate.cache usage="read-write"
     * @hibernate.collection-one-to-many class="edu.wustl.common.querysuite.queryobject.impl.Condition"
     */
    @SuppressWarnings("unused")
    // for hibernate
    private List<ICondition> getConditions() {
        return conditions;
    }

    @SuppressWarnings("unused")
    // for hibernate
    private void setConditions(List<ICondition> conditions) {
        this.conditions = conditions;
    }

    /**
     * This method returns the containing Expression
     * 
     * @return the expression to which this rule belongs.
     * @see edu.wustl.common.querysuite.queryobject.IRule#getContainingExpression()
     * 
     * @hibernate.many-to-one column="QUERY_EXPRESSION_ID"
     *                        class="edu.wustl.common.querysuite.queryobject.impl.Expression"
     *                        not-null="true" inverse="true"
     * @hibernate.cache usage="read-write"
     */
    public IExpression getContainingExpression() {
        return containingExpression;
    }

    /**
     * @param containingExpression the containingExpression to set
     */
    void setContainingExpression(IExpression parentExpression) {
        this.containingExpression = parentExpression;
    }

    /**
     * @return The reference to the newly added condition.
     * @see edu.wustl.common.querysuite.queryobject.IRule#addCondition()
     */
    public ICondition addCondition() {
        ICondition iCondition = QueryObjectFactory.createCondition();
        conditions.add(iCondition);
        return iCondition;
    }

    /**
     * @param condition The condition to be added.
     * @return the reference to the newly added condition.
     * @see edu.wustl.common.querysuite.queryobject.IRule#addCondition(edu.wustl.common.querysuite.queryobject.ICondition)
     */
    public ICondition addCondition(ICondition condition) {
        conditions.add(condition);
        return condition;
    }

    /**
     * @param index The index of the Condition.
     * @return the reference to the Condition indexed by given index in the
     *         condition list of Rule.
     * @see edu.wustl.common.querysuite.queryobject.IRule#getCondition(int)
     */
    public ICondition getCondition(int index) {
        return conditions.get(index);
    }

    /**
     * @return The no. of conditions present in the Rule.
     * @see edu.wustl.common.querysuite.queryobject.IRule#size()
     */
    public int size() {
        return conditions.size();
    }

    /**
     * To clear condition List of the Rule.
     * 
     * @see edu.wustl.common.querysuite.queryobject.IRule#removeAllConditions()
     */
    public void removeAllConditions() {
        conditions.clear();
    }

    public boolean removeCondition(ICondition condition) {
        return conditions.remove(condition);
    }
    
    /**
     * To get the copy of the Rule. Note that, this is not deep copy.
     * 
     * @return The copy og the Rule.
     */
    public IRule getCopy() {
        List<ICondition> theConditions = new ArrayList<ICondition>();
        theConditions.addAll(this.conditions);
        IRule rule = new Rule(theConditions);
        return rule;
    }

    public Iterator<ICondition> iterator() {
        return conditions.iterator();
    }

    /**
     * @return String representation of Rule object in the form: [[conditions]]
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < conditions.size(); i++) {
            string = string + conditions.get(i).toString();
            if (i != conditions.size() - 1) {
                string = string + " " + LogicalOperator.And + " ";
            }
        }
        return "[" + string + "]";
    }

    /**
     * To get the HashCode for the object. It will be calculated based on
     * containingExpression, conditions.
     * 
     * @return The hash code value for the object.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(containingExpression).append(conditions).toHashCode();
    }

    /**
     * To check whether two objects are equal.
     * 
     * @param obj reference to the object to be checked for equality.
     * @return true if containingExpression, conditions of both Rules are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && this.getClass() == obj.getClass()) {
            Rule rule = (Rule) obj;
            if (this.containingExpression != null && this.containingExpression.equals(rule.containingExpression)
                    && new HashSet<ICondition>(this.conditions).equals(new HashSet<ICondition>(rule.conditions))) {
                return true;
            }
        }
        return false;
    }


}
