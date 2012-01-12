package edu.wustl.common.querysuite.queryobject;

import java.util.List;

import edu.wustl.common.querysuite.utils.CustomFormulaProcessor;

/**
 * Represents an arbitrarily complex custom-defined formula that can be a part
 * of an {@link IExpression}.<br>
 * A custom formula contains a {@link RelationalOperator} that splits it into
 * two parts - the left-hand side (LHS), and the right-hand side(RHS). The LHS
 * is represented by a single {@link ITerm}. Depending on the relational
 * operator, the RHS can contain any no. of {@link ITerm}s; e.g.
 * <tt>=, &lt;</tt> etc. need one RHS, <tt>Between</tt> needs two RHSes
 * while <tt>In</tt> and <tt>NotIn</tt> can have any no. of RHSes. Thus the
 * RHS is generically represented by a list of {@link ITerm}s.<br>
 * Note that the LHS of a custom formula should not be of
 * <tt>TermType YMInterval</tt>.
 * 
 * @author srinath_k
 * @see ITerm
 * @see CustomFormulaProcessor
 */
public interface ICustomFormula extends IExpressionOperand, IParameterizable {
    ITerm getLhs();

    void setLhs(ITerm lhs);

    List<ITerm> getAllRhs();

    void addRhs(ITerm rhs);

    RelationalOperator getOperator();

    void setOperator(RelationalOperator relationalOperator);

    boolean isValid();
}
