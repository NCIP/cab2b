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
