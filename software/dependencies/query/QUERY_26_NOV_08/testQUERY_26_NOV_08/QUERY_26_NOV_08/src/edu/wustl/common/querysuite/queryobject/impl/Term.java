/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.IBaseExpression;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.utils.TermProcessor;
import edu.wustl.common.querysuite.utils.TermProcessor.TermString;

public class Term extends BaseExpression<ArithmeticOperator, IArithmeticOperand> implements ITerm {

    private static final long serialVersionUID = 6787015118573915634L;

    @Override
    protected IConnector<ArithmeticOperator> getUnknownOperator(int nestingNumber) {
        return QueryObjectFactory.createArithmeticConnector(ArithmeticOperator.Unknown, nestingNumber);
    }

    public String getStringRepresentation() {
        return process().getString();
    }

    public TermType getTermType() {
        return process().getTermType();
    }

    @Override
    public String toString() {
        return process().toString();
    }

    private TermString process() {
        return new TermProcessor().convertTerm(this);
    }

    @Override
    protected IBaseExpression<ArithmeticOperator, IArithmeticOperand> createEmpty() {
        return new Term();
    }
}
