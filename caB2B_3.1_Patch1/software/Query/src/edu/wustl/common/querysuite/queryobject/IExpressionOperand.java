package edu.wustl.common.querysuite.queryobject;

/**
 * A marker interface for an operand. An operand is either a subexpression (in
 * which case, the corresponding expression id is added), a rule or a custom
 * formula.
 * 
 * @version 1.0
 * @updated 11-Oct-2006 02:56:16 PM
 * @see IExpressionId
 * @see ICustomFormula
 * @see IRule
 */
public interface IExpressionOperand extends IBaseQueryObject, IOperand {

}
