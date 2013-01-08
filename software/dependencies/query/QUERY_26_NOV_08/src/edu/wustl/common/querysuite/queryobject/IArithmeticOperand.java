/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

/**
 * Represents an operand in an {@link ITerm}. Each operand has a
 * {@link TermType}.
 * 
 * @author srinath_k
 * @see TermType
 * @see ITerm
 */
public interface IArithmeticOperand extends IOperand {
    TermType getTermType();
}
