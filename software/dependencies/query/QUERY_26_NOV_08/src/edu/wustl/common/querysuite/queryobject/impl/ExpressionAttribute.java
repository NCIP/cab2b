/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.TermType;

public class ExpressionAttribute extends ArithmeticOperand implements IExpressionAttribute {
    private static final long serialVersionUID = 2376055279144184693L;

    private IExpression expression;

    private AttributeInterface attribute;

    ExpressionAttribute() {
    // for hibernate
    }

    public ExpressionAttribute(IExpression expression, AttributeInterface attribute, TermType termType) {
        setExpression(expression);
        setAttribute(attribute);
        setTermType(termType);
    }

    @Override
    public void setTermType(TermType termType) {
        super.setTermType(termType);
    }

    public AttributeInterface getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeInterface attribute) {
        if (attribute == null) {
            throw new NullPointerException();
        }
        this.attribute = attribute;
    }

    public IExpression getExpression() {
        return expression;
    }

    public void setExpression(IExpression expression) {
        if (expression == null) {
            throw new NullPointerException();
        }
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "ExprId: " + expression.getExpressionId() + ", Attribute: " + attribute;
    }
}
