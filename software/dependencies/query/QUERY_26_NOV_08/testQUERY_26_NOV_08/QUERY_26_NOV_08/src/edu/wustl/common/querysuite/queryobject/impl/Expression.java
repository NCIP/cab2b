package edu.wustl.common.querysuite.queryobject.impl;

/**
 * @author Mandar Shidhore
 * @version 1.0
 * @created 12-Oct-2006 11.12.04 AM Class implementation for IExpression.
 */

import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.IBaseExpression;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;

/**
 * 
 * @author chetan_patil
 * 
 * @created Aug 8, 2007, 3:55:45 PM
 * 
 * @hibernate.class table="QUERY_EXPRESSION"
 * @hibernate.cache usage="read-write"
 */
public class Expression extends BaseExpression<LogicalOperator, IExpressionOperand> implements IExpression {
    private static final long serialVersionUID = 1426555905287966634L;

    private IQueryEntity queryEntity;

    private boolean isInView = false;

    private boolean isVisible = true;

    private int expressionId;

    /**
     * Default Constructor
     */
    public Expression() {

    }

    /**
     * Parameterized Constructor
     * 
     * @param constraintEntity The reference to the Constraint Entity associated
     *            with this class.
     * @param expressionId The Expression Id for this Expression.
     */
    public Expression(IQueryEntity constraintEntity, int expressionId) {
        this.queryEntity = constraintEntity;
        this.expressionId = expressionId;
    }

    /**
     * Returns the identifier assigned to BaseQueryObject.
     * 
     * @return a unique id assigned to the Condition.
     * 
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="EXPRESSION_SEQ"
     */
    public Long getId() {
        return id;
    }

    /**
     * Name: Abhishek Mehta Reviewer Name : Deepti Bug ID: 5661 Patch ID: 5661_6
     * See also: 1-7 Description : Making cascade all-delete-orphan from cascade
     * save-update
     */

    /**
     * This method returns the ExpressionId of this Expression
     * 
     * @return the Expression Id of this Expression.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#getExpressionId()
     * 
     * @hibernate.many-to-one column="QUERY_EXPRESSIONID_ID"
     *                        class="edu.wustl.common.querysuite.queryobject.impl.ExpressionId"
     *                        unique="true" cascade="all-delete-orphan"
     *                        lazy="false"
     */
    public int getExpressionId() {
        return expressionId;
    }

    /**
     * This method sets the ExpressionId of this Expression
     * 
     * @param expressionId the expressionId to set
     */
    public void setExpressionId(int expressionId) {
        this.expressionId = expressionId;
    }

    /**
     * Name: Abhishek Mehta Reviewer Name : Deepti Bug ID: 5661 Patch ID: 5661_7
     * See also: 1-7 Description : Making cascade all-delete-orphan from cascade
     * save-update
     */

    /**
     * This method returns the constraint entity associcated with this
     * Expression
     * 
     * @return The Constraint Entity reference associated with this Expression.
     * @see edu.wustl.common.querysuite.queryobject.IExpression#getQueryEntity()
     * 
     * @hibernate.many-to-one column="QUERY_QUERY_ENTITY_ID"
     *                        class="edu.wustl.common.querysuite.queryobject.impl.QueryEntity"
     *                        cascade="all-delete-orphan" lazy="false"
     *                        not-null="true"
     */
    public IQueryEntity getQueryEntity() {
        return queryEntity;
    }

    /**
     * This method sets the constraint entity associcated with this Expression
     * 
     * @param constraintEntity the constraintEntity to set
     */
    public void setQueryEntity(IQueryEntity queryEntity) {
        this.queryEntity = queryEntity;
    }

    @Override
    public IExpressionOperand getOperand(int index) {
        return unwrapSubExpr(super.getOperand(index));
    }

    @Override
    public void setOperand(int index, IExpressionOperand operand) {
        setContainingExpressionForRule(operand);
        super.setOperand(index, wrapInSubExpr(operand));
    }

    @Override
    public IExpressionOperand removeOperand(int index) {
        return unwrapSubExpr(super.removeOperand(index));
    }

    @Override
    public int indexOfOperand(IExpressionOperand operand) {
        return super.indexOfOperand(wrapInSubExpr(operand));
    }

    @Override
    public int addOperand(IExpressionOperand operand) {
        setContainingExpressionForRule(operand);
        return super.addOperand(wrapInSubExpr(operand));
    }

    @Override
    public int addOperand(IConnector<LogicalOperator> logicalConnector, IExpressionOperand operand) {
        setContainingExpressionForRule(operand);
        return super.addOperand(logicalConnector, wrapInSubExpr(operand));
    }

    @Override
    public void addOperand(int index, IConnector<LogicalOperator> logicalConnector, IExpressionOperand operand) {
        setContainingExpressionForRule(operand);
        super.addOperand(index, logicalConnector, wrapInSubExpr(operand));
    }

    @Override
    public void addOperand(int index, IExpressionOperand operand, IConnector<LogicalOperator> logicalConnector) {
        setContainingExpressionForRule(operand);
        super.addOperand(index, wrapInSubExpr(operand), logicalConnector);
    }

    /**
     * To set the containing Expression in case when the operand is Rule.
     * 
     * @param operand The reference to IExpressionOperand.
     */
    private void setContainingExpressionForRule(IExpressionOperand operand) {
        if (operand instanceof Rule) {
            Rule rule = (Rule) operand;
            rule.setContainingExpression(this);
        }
    }

    private IExpressionOperand wrapInSubExpr(IExpressionOperand operand) {
        if (operand instanceof IExpression) {
            return new SubExpression((IExpression) operand);
        } else {
            return operand;
        }
    }

    private IExpressionOperand unwrapSubExpr(IExpressionOperand operand) {
        if (operand instanceof SubExpression) {
            SubExpression subExpr = (SubExpression) operand;
            return subExpr.getWrappedExpr();
        } else {
            return operand;
        }
    }

    /**
     * This method returns whether this Expression is in view or not
     * 
     * @return true if Expression is in view; false otherwise
     * 
     * @hibernate.property name="isInView" column="IS_IN_VIEW" type="boolean"
     */
    @SuppressWarnings("unused")
    private boolean getIsInView() {
        return isInView;
    }

    /**
     * This method sets whether this Expression is in view or not
     * 
     * @param isInView
     */
    @SuppressWarnings("unused")
    private void setIsInView(boolean isInView) {
        this.isInView = isInView;
    }

    /**
     * This method returns whether this Expression is in visiblew or not
     * 
     * @return true if Expression is visible; false otherwise
     * 
     * @hibernate.property name="isVisible" column="IS_VISIBLE" type="boolean"
     */
    @SuppressWarnings("unused")
    private boolean getIsVisible() {
        return isVisible;
    }

    /**
     * This method sets whether the expression in visible or not.
     * 
     * @param isVisible true if this Expression should be visible; false
     *            otherwise
     */
    @SuppressWarnings("unused")
    private void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * This method returns whether this Expression is in view or not
     * 
     * @return true if Expression is in view; false otherwise
     */
    public boolean isInView() {
        return isInView;
    }

    public void setInView(boolean isInView) {
        this.isInView = isInView;
    }

    /**
     * This method sets whether the expression in visible or not.
     * 
     * @param isVisible true if this Expression should be visible; false
     *            otherwise
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * This method returns whether this Expression is in visiblew or not
     * 
     * @return true if Expression is visible; false otherwise
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * @see edu.wustl.common.query.queryobject.IExpression#removeOperandAndFollowingConnector(edu.wustl.common.query.queryobject.IExpressionOperand)
     */
    /*
     * public boolean removeOperandAndFollowingConnector(IExpressionOperand
     * operand) throws Exception { if (iExpressionOperandList.contains(operand)) {
     * int i = iExpressionOperandList.indexOf(operand);
     * iExpressionOperandList.remove(i); iLogicalConnector.remove(i + 1); } else {
     * throw new Exception("The operand does not exist"); } return false; }
     */

    /**
     * @see edu.wustl.common.query.queryobject.IExpression#removeOperandAndPrecedingConnector(edu.wustl.common.query.queryobject.IExpressionOperand)
     */
    /*
     * public boolean removeOperandAndPrecedingConnector(IExpressionOperand
     * operand) throws Exception { if (iExpressionOperandList.contains(operand)) {
     * int i = iExpressionOperandList.indexOf(operand);
     * iExpressionOperandList.remove(i); iLogicalConnector.remove(i - 1); } else {
     * throw new Exception("The operand does not exist"); } return false; }
     */

    /**
     * To get the HashCode for the object. It will be calculated based on
     * expression Id.
     * 
     * @return The hash code value for the object.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(expressionId).toHashCode();
    }

    /**
     * To check whether two objects are equal.
     * 
     * @param obj reference to the object to be checked for equality.
     * @return true if attribute, expression id of both Expressions are equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj != null && this.getClass() == obj.getClass()) {
            Expression expression = (Expression) obj;
            if (this.expressionId == expression.expressionId) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return String representation of Expression object in the form:
     *         [ConstraintEntity : expressionId]
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + queryEntity + ":" + expressionId + "]";
    }

    /**
     * To check whether the child Expression is pseudo anded with the other
     * expression. The given expression is pseudo Anded if and only if it has
     * atleast one Expression having Equal Constraint Entity as that of given
     * Expression's & logical connector between then should be AND.
     * 
     * @param expressionId The child Expression Id.
     * @param constraints The reference to Constraints, which is having
     *            reference to this Expression & the passed expressionId.
     * @return true if the given Expression is pseudoAnded with other
     *         Expression, else returns false.
     * @throws IllegalArgumentException if the given Expression Id is not child
     *             of the Expression.
     */
    public boolean isPseudoAnded(int expressionId, IConstraints constraints) {
        IExpression currentExpression = constraints.getExpression(expressionId);
        int index = expressionOperands.indexOf(currentExpression);

        if (index < -1) {
            throw new IllegalArgumentException("The given Expression Id not found!!!");
        }

        int immediateOperandIndex = indexOfConnectorForOperand(currentExpression);

        if (immediateOperandIndex == Expression.NO_LOGICAL_CONNECTOR) // there
        // is no
        // operand
        // around
        // this
        // Expression.
        {
            return false;
        } else if (immediateOperandIndex == Expression.BOTH_LOGICAL_CONNECTOR) // both
        // logical
        // connector
        // sorrounding
        // this
        // expression
        // have
        // same
        // nesting
        // no.
        // need
        // to
        // check
        // 'And'
        // operator
        // for
        // both.
        {
            int preIndex = index - 1;
            int postindex = index + 1;

            IExpressionOperand operand = expressionOperands.get(preIndex);
            if (operand instanceof IExpression
                    && LogicalOperator.And.equals(getConnector(preIndex, index).getOperator())) {
                IExpression expression = (IExpression) operand;
                return isHavingSameClass(currentExpression, expression);
            }

            operand = expressionOperands.get(postindex);
            if (operand instanceof IExpression
                    && LogicalOperator.And.equals(getConnector(index, postindex).getOperator())) {
                IExpression expression = (IExpression) operand;
                return isHavingSameClass(currentExpression, expression);
            }
        } else
        // check logical connector between immediateOperandIndex & index.
        {
            if (LogicalOperator.And.equals(connectors.get(immediateOperandIndex).getOperator())) {
                int otherOperandIndex = immediateOperandIndex == index ? index + 1 : index - 1; // otherOperandIndex
                // =
                // immediateOperandIndex
                // =>
                // other
                // operand
                // index
                // is
                // (index+1)
                IExpressionOperand operand = expressionOperands.get(otherOperandIndex);
                if (operand instanceof IExpression) {
                    IExpression expression = (IExpression) operand;
                    return isHavingSameClass(currentExpression, expression);
                }
            }
        }
        return false;
    }

    /**
     * To check wether the given 2 Expressions having equal Constraint Entity.
     * 
     * @param expression1 1st expression to check.
     * @param expression2 2nd expression to check.
     * @return true if both the expression have same ConstraintEntity.
     */
    private boolean isHavingSameClass(IExpression expression1, IExpression expression2) {
        return ((QueryEntity) expression1.getQueryEntity()).isPseudoAndedEntity(expression2.getQueryEntity());
    }

    /**
     * To check whether there are any rule present in the Expression.
     * 
     * @return true if there is atleast one rule present in the operand list of
     *         expression.
     */
    public boolean containsRule() {
        for (int i = 0; i < numberOfOperands(); i++) {
            if (getOperand(i) instanceof IRule) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IExpression#containsCustomFormula()
     */
    public boolean containsCustomFormula() {
        for (int i = 0; i < numberOfOperands(); i++) {
            if (getOperand(i) instanceof ICustomFormula) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected IConnector<LogicalOperator> getUnknownOperator(int nestingNumber) {
        return QueryObjectFactory.createLogicalConnector(LogicalOperator.Unknown, nestingNumber);
    }

    @Override
    protected void setExpressionOperands(List<IExpressionOperand> expressionOperands) {
        super.setExpressionOperands(expressionOperands);
        for (IExpressionOperand operand : expressionOperands) {
            setContainingExpressionForRule(operand);
        }
    }

    @SuppressWarnings("unused")
    private static class SubExpression extends BaseQueryObject implements IExpressionOperand {
        private static final long serialVersionUID = 8219009897032071466L;

        private IExpression wrappedExpr;

        private SubExpression() {

        }

        private SubExpression(IExpression wrappedExpr) {
            setWrappedExpr(wrappedExpr);
        }

        private IExpression getWrappedExpr() {
            return wrappedExpr;
        }

        private void setWrappedExpr(IExpression wrappedExpr) {
            if (wrappedExpr == null) {
                throw new NullPointerException();
            }
            this.wrappedExpr = wrappedExpr;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SubExpression)) {
                return false;
            }
            SubExpression o = (SubExpression) obj;
            return wrappedExpr.equals(o.wrappedExpr);
        }

        @Override
        public int hashCode() {
            return wrappedExpr.hashCode();
        }
    }

    @Override
    protected IBaseExpression<LogicalOperator, IExpressionOperand> createEmpty() {
        return new Expression();
    }

    @Override
    public boolean containsOperand(IExpressionOperand operand) {
        return super.containsOperand(wrapInSubExpr(operand));
    }
}
