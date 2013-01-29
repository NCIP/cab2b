/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

/**
 * Represents the binary operator in an {@link IBaseExpression} and the nesting
 * of that operator.
 * 
 * @author srinath_k
 * 
 * @param <T> the type of the operator
 */
public interface IConnector<T extends IBinaryOperator> {
    /**
     * To get the operator associated with this object.
     * 
     * @return the reference to the operator.
     */
    T getOperator();

    /**
     * To set the operator.
     * 
     * @param operator The operator to set.
     */
    void setOperator(T operator);

    /**
     * Denotes no. of parantheses around this operator
     * 
     * @return integer value, that represents no. of parantheses sorrounding
     *         this connector.
     */
    int getNestingNumber();
}
