/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

import java.util.Iterator;

/**
 * Represents a generic expression consisting of binary operators and operands
 * connected by these operators.<br>
 * The connectors are identified by the position of the operands on either side,
 * e.g.
 * 
 * <pre>
 *  given:  operand connector operand connector operand index:    0        0,
 * 1      1       1,2        2
 * </pre>
 * 
 * @author srinath_k
 * 
 * @param
 * <P>
 * the type of the binary operator
 * @param <V> the type of the operand
 * @see IExpression
 * @see ITerm
 */
public interface IBaseExpression<P extends IBinaryOperator, V extends IOperand> extends IBaseQueryObject, Iterable<V> {

    /**
     * To get the operand indexed by index in the operand list of Expression.
     * 
     * @param index index of the operand in the operand list of Expression.
     * @return The operand identified by the given index.
     */
    V getOperand(int index);

    /**
     * To set the operand in the Expression at index position
     * 
     * @param index the expected index of the operand in the Expression
     * @param operand The operand to be added in the Expression.
     */
    void setOperand(int index, V operand);

    /**
     * An operator is indexed by {left operand, right operand} i.e. if left
     * index is "i", value of right index must be "i+1" this is just to make the
     * method intuitively clearer...
     * 
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the right operand.
     * @return The reference to connector between who adjacent operands.
     */
    IConnector<P> getConnector(int leftOperandIndex, int rightOperandIndex);

    /**
     * To set connector between two adjescent operands.
     * 
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the right operand.
     * @param connector The reference to the connector to set.
     * 
     */
    void setConnector(int leftOperandIndex, int rightOperandIndex, IConnector<P> connector);

    /**
     * To add the Parenthesis around the operands specified by left & right
     * operand index. Increments nesting num of all the connectors in the
     * expression between the specified operands' indexes, both inclusive.
     * 
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the left operand.
     * 
     */
    void addParantheses(int leftOperandIndex, int rightOperandIndex);

    /**
     * Just calls addParantheses(0, size-1)
     */
    void addParantheses();

    /**
     * To remove the Parenthesis around the operands specified by left & right
     * operand index. Decrements nesting num of all the connectors in the
     * expression between the specified operands' indexes, both inclusive.
     * 
     * @param leftOperandIndex The index of the left operand.
     * @param rightOperandIndex The index of the left operand.
     */
    void removeParantheses(int leftOperandIndex, int rightOperandIndex);

    /**
     * Just calls removeParantheses(0, size-1)
     */
    void removeParantheses();

    /**
     * Adds an operand to the operands list.
     * 
     * @param operand The reference of the operand added.
     * @return index of the added operand.
     */
    int addOperand(V operand);

    /**
     * To add operand to the Expression with the specified connector. This
     * operand will be added as last operand in the operand list.
     * 
     * @param connector the connector by which the operand will be connected to
     *            the operand behind it.
     * @param operand The operand to be added in Expression.
     * @return index of the added operand.
     */
    int addOperand(IConnector<P> connector, V operand);

    /**
     * Inserts an operand with the connector in front of it.
     * 
     * @param index The index at which the operand to be inserted.
     * @param connector the connector by which the operand will be connected to
     *            the operand behind it.
     * @param operand The operand to be added in Expression.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 ||
     *             index > size()).
     */
    void addOperand(int index, IConnector<P> connector, V operand);

    /**
     * Inserts an operand with the connector behind it.
     * 
     * @param index The index at which the operand to be inserted.
     * @param operand The operand to be added in Expression.
     * @param connector the connector by which the operand will be connected
     *            operand in front of it.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 ||
     *             index > size()).
     */
    void addOperand(int index, V operand, IConnector<P> connector);

    /**
     * Removes the operand, and the appropriate connector. The adjacent logical
     * connector with the greater nesting number will be removed. If both
     * adjacent connectors are of same nesting number, it is undefined as to
     * which one will be removed.
     * 
     * @param index The index of operand to be removed.
     * @return reference to removed operand.
     */
    V removeOperand(int index);

    /**
     * Removes the operand, and the appropriate connector. The adjacent logical
     * connector with the greater nesting number will be removed. If both
     * adjacent connectors are of same nesting number, it is undefined as to
     * which one will be removed.
     * 
     * @param operand the operand to be removed.
     * @return true if the operand was found; false otherwise.
     */
    boolean removeOperand(V operand);

    /**
     * To get the index of the operand in the operand list of Expression.
     * 
     * @param operand the reference to IExpressionOperand, to be searched in the
     *            Expression.
     * @return The index of the given Expression operand.
     * @see java.util.List#indexOf(java.lang.Object)
     */
    int indexOfOperand(V operand);

    /**
     * To the the number of operand in the operand list of Expression.
     * 
     * @return the no. of operands in the expression.
     */
    int numberOfOperands();

    /**
     * Returns the nesting of the operand. The nesing of an operand is the
     * greater of the nesting of the connectors adjacent to the specified
     * operand.
     * 
     * @param i the index of the operand.
     * @return the nesting of the operand.
     */
    int nestingNumberOfOperand(int i);

    /**
     * DOES NOT support element removal.
     * 
     * @see java.lang.Iterable#iterator()
     */
    Iterator<V> iterator();

    void addAll(IConnector<P> precedingConn, IBaseExpression<P, V> other);

    /**
     * Both indices inclusive.
     * 
     * @param startIdx starting operand index.
     * @param endIdx ending operand index.
     * @return
     */
    IBaseExpression<P, V> subExpression(int startIdx, int endIdx);

    void clear();

    boolean containsOperand(V operand);
}
