package edu.wustl.common.querysuite.queryobject.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.wustl.common.querysuite.queryobject.IBaseExpression;
import edu.wustl.common.querysuite.queryobject.IBinaryOperator;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IOperand;
import edu.wustl.common.util.Collections;

abstract class BaseExpression<P extends IBinaryOperator, V extends IOperand> extends BaseQueryObject
        implements
            IBaseExpression<P, V> {

    private static final long serialVersionUID = 8426174732093549937L;

    public static final int NO_LOGICAL_CONNECTOR = -1;

    public static final int BOTH_LOGICAL_CONNECTOR = -2;

    protected List<V> expressionOperands = new ArrayList<V>();

    protected List<IConnector<P>> connectors = new ArrayList<IConnector<P>>();

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#removeOperand(edu.wustl.common.querysuite.queryobject.IOperand)
     */
    public boolean removeOperand(V operand) {
        int index = indexOfOperand(operand);
        return removeOperand(index) != null;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#addOperand(edu.wustl.common.querysuite.queryobject.IOperand)
     */
    public int addOperand(V operand) {
        expressionOperands.add(operand);
        if (expressionOperands.size() != 1) {
            connectors.add(getUnknownOperator(0));
        }
        return expressionOperands.size() - 1;
    }

    protected abstract IConnector<P> getUnknownOperator(int nestingNumber);

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#addOperand(edu.wustl.common.querysuite.queryobject.IConnector,
     *      edu.wustl.common.querysuite.queryobject.IOperand)
     */
    public int addOperand(IConnector<P> logicalConnector, V operand) {
        expressionOperands.add(operand);
        connectors.add(expressionOperands.size() - 2, logicalConnector);
        return expressionOperands.size() - 1;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#addOperand(int,
     *      edu.wustl.common.querysuite.queryobject.IConnector,
     *      edu.wustl.common.querysuite.queryobject.IOperand)
     */
    public void addOperand(int index, IConnector<P> logicalConnector, V operand) {
        expressionOperands.add(index, operand);
        connectors.add(index - 1, logicalConnector);
        if (index > 0
                && (((Connector) connectors.get(index)).getNestingNumber() > ((Connector) connectors.get(index - 1))
                        .getNestingNumber())) {
            ((Connector) connectors.get(index - 1)).setNestingNumber(((Connector) connectors.get(index))
                    .getNestingNumber());
        }
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#addOperand(int,
     *      edu.wustl.common.querysuite.queryobject.IOperand,
     *      edu.wustl.common.querysuite.queryobject.IConnector)
     */
    public void addOperand(int index, V operand, IConnector<P> logicalConnector) {
        expressionOperands.add(index, operand);
        connectors.add(index, logicalConnector);
        if (index > 0
                && (((Connector) connectors.get(index)).getNestingNumber() < ((Connector) connectors.get(index - 1))
                        .getNestingNumber())) {
            ((Connector) connectors.get(index)).setNestingNumber(((Connector) connectors.get(index - 1))
                    .getNestingNumber());
        }
    }

    /**
     * calls addParantheses(0, size-1)
     * 
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#addParantheses()
     */
    public void addParantheses() {
        addParantheses(0, expressionOperands.size() - 1);
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#addParantheses(int,
     *      int)
     */
    public void addParantheses(int leftOperandIndex, int rightOperandIndex) {
        for (int i = leftOperandIndex; i < rightOperandIndex; i++) {
            ((Connector) connectors.get(i)).incrementNestingNumber();
        }
    }

    /**
     * calls removeParantheses(0, size-1)
     * 
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#removeParantheses()
     */
    public void removeParantheses() {
        removeParantheses(0, connectors.size() - 1);

    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#removeParantheses(int,
     *      int)
     */
    public void removeParantheses(int leftOperandIndex, int rightOperandIndex) {
        for (int i = leftOperandIndex; i < rightOperandIndex; i++) {
            ((Connector) connectors.get(i)).setNestingNumber(((Connector) connectors.get(i)).getNestingNumber() - 1);
        }
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#getConnector(int,
     *      int)
     */
    public IConnector<P> getConnector(int leftOperandIndex, int rightOperandIndex) {
        checkAdjacentOperands(leftOperandIndex, rightOperandIndex);
        if (leftOperandIndex == -1 || leftOperandIndex == connectors.size()) {
            return getUnknownOperator(-1);
        }
        return connectors.get(leftOperandIndex);
    }

    /**
     * @throws IllegalArgumentException if
     *             <tt>rightOperandIndex != leftOperandIndex + 1</tt>
     */
    protected void checkAdjacentOperands(int leftOperandIndex, int rightOperandIndex) {
        if (rightOperandIndex != leftOperandIndex + 1) {
            throw new IllegalArgumentException("Incorrect indexes selected; please select adjacent indexes");
        }
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#getOperand(int)
     */
    public V getOperand(int index) {
        return expressionOperands.get(index);
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#setConnector(int,
     *      int, edu.wustl.common.querysuite.queryobject.IConnector)
     */
    public void setConnector(int leftOperandIndex, int rightOperandIndex, IConnector<P> logicalConnector) {
        if (rightOperandIndex == leftOperandIndex + 1) {
            connectors.set(leftOperandIndex, logicalConnector);
        } else {
            throw new IllegalArgumentException("Incorrect indexes selected; please select adjacent indexes");
        }
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#setOperand(int,
     *      edu.wustl.common.querysuite.queryobject.IOperand)
     */
    public void setOperand(int index, V operand) {
        expressionOperands.set(index, operand);
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#numberOfOperands()
     */
    public int numberOfOperands() {
        return expressionOperands.size();
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#removeOperand(int)
     */
    public V removeOperand(int index) {

        if (index == -1) {
            return null;
        }
        // A and (B or C) remove C => A and B
        // A and (B or C) remove B => A and C
        // A and (B or C) remove A => B or C
        // A and B or C remove B => A and C
        int connectorIndex = indexOfConnectorForOperand(expressionOperands.get(index));
        V operand = expressionOperands.get(index);

        expressionOperands.remove(index);
        if (connectorIndex == Expression.BOTH_LOGICAL_CONNECTOR) // if both
        /*
         * adjacent connectors have same nesting no. then remove connector
         * following the operand.
         */
        {
            connectorIndex = index;
        }

        if (connectorIndex != Expression.NO_LOGICAL_CONNECTOR) {
            connectors.remove(connectorIndex);
        }

        return operand;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#indexOfOperand(edu.wustl.common.querysuite.queryobject.IOperand)
     */
    public int indexOfOperand(V operand) {
    	
    	System.out.println("***Index of Operand # operands="+expressionOperands.size()+" operand="+operand+" indexOf="+
    	    	expressionOperands.indexOf(operand)+
    			" lastindexOf="+expressionOperands.lastIndexOf(operand));

    	    	
        return expressionOperands.indexOf(operand);
    }

    public int indexOfConnectorForOperand(V operand) {
        int index = indexOfOperand(operand);

        if (index != -1) {
            if (expressionOperands.size() == 1) // if there is only one
            // Expression then there is no logical connector associated with it.
            {
                index = Expression.NO_LOGICAL_CONNECTOR;
            } else if (index == expressionOperands.size() - 1) // if expression
            // is last operand then index of last connector will be returned.
            {
                index = index - 1;
            } else if (index != 0)
            // if expression is not 1st & last, then index will depend upon the
            // immediate bracket surrounding that expression.
            {
                int preNesting = ((Connector) connectors.get(index - 1)).getNestingNumber();
                int postNesting = ((Connector) connectors.get(index)).getNestingNumber();
                if (postNesting == preNesting)
                // if nesting no are same, then there is not direct bracket
                // sorrounding operand.
                {
                    index = Expression.BOTH_LOGICAL_CONNECTOR;
                } else if (postNesting < preNesting) {
                    index--;
                }
            }
        } else {
            index = Expression.NO_LOGICAL_CONNECTOR;
        }

        return index;
    }

    /**
     * @see edu.wustl.common.querysuite.queryobject.IBaseExpression#nestingNumberOfOperand(int)
     */
    public int nestingNumberOfOperand(int i) {
        int leftNesting = getConnector(i - 1, i).getNestingNumber();
        int rightNesting = getConnector(i, i + 1).getNestingNumber();
        if (leftNesting < rightNesting) {
            return rightNesting;
        } else {
            return leftNesting;
        }
    }

    // for hibernate
    @SuppressWarnings("unused")
    private List<V> getExpressionOperands() {
        return expressionOperands;
    }

    protected void setExpressionOperands(List<V> expressionOperands) {
        this.expressionOperands = expressionOperands;
    }

    // for hibernate
    @SuppressWarnings("unused")
    private List<IConnector<P>> getConnectors() {
        return connectors;
    }

    @SuppressWarnings("unused")
    private void setConnectors(List<IConnector<P>> connectors) {
        this.connectors = connectors;
    }

    public Iterator<V> iterator() {
        return Collections.removalForbiddenIterator(expressionOperands);
    }

    public void addAll(IConnector<P> precedingConn, IBaseExpression<P, V> other) {
        int numOpnds = other.numberOfOperands();
        if (numOpnds == 0) {
            return;
        }
        addOperand(precedingConn, other.getOperand(0));
        if (numOpnds > 0) {
            for (int i = 1; i < numOpnds; i++) {
                addOperand(other.getConnector(i - 1, i), other.getOperand(i));
            }
        }
    }

    public IBaseExpression<P, V> subExpression(int startIdx, int endIdx) {
        IBaseExpression<P, V> res = createEmpty();
        res.addOperand(getOperand(startIdx));
        for (int i = startIdx + 1; i <= endIdx; i++) {
            res.addOperand(getConnector(i - 1, i), getOperand(i));
        }
        return res;
    }

    protected abstract IBaseExpression<P, V> createEmpty();

    public void clear() {
        connectors.clear();
        expressionOperands.clear();
    }

    public boolean containsOperand(V operand) {
        return expressionOperands.contains(operand);
    }
}
