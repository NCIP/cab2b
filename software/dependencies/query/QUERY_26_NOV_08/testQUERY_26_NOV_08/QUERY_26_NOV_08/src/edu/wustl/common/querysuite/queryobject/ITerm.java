/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

import edu.wustl.common.querysuite.utils.TermProcessor;

/**
 * A list of arithmetic operands connected by arithmetic operators. Every term
 * has a {@link TermType} that is determined by the term types of the operands
 * in the term and the operators connecting them; see
 * {@link TermType#getResultTermType(TermType, TermType, ArithmeticOperator)}.
 * 
 * @see IBaseExpression
 * @see ArithmeticOperator
 * @see IArithmeticOperand
 * @see TermProcessor
 * @see TermType
 * @see ICustomFormula
 * @author srinath_k
 */
public interface ITerm extends IBaseExpression<ArithmeticOperator, IArithmeticOperand> {
    /**
     * @return the string representation of this term.
     * @see TermProcessor#convertTerm(ITerm)
     */
    String getStringRepresentation();

    TermType getTermType();
}
