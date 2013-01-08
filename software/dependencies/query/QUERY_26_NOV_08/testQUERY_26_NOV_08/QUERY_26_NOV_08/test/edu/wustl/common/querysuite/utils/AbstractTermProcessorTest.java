/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.utils;

import static edu.wustl.common.querysuite.utils.DynExtnMockUtil.createAttribute;
import static edu.wustl.common.querysuite.utils.DynExtnMockUtil.createEntity;
import static edu.wustl.common.querysuite.utils.DynExtnMockUtil.createExpression;

import java.sql.Date;

import junit.framework.TestCase;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.DSInterval;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IDateLiteral;
import edu.wustl.common.querysuite.queryobject.IDateOffsetAttribute;
import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.INumericLiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;
import edu.wustl.common.querysuite.utils.TermProcessor.TermString;

public abstract class AbstractTermProcessorTest extends TestCase {
    private TermProcessor termProcessor;

    @Override
    protected void setUp() throws Exception {
        setTermProcessor(new TermProcessor());
    }

    protected final void setTermProcessor(TermProcessor termProcessor) {
        this.termProcessor = termProcessor;
    }

    protected void checkNumeric(ITerm term, String expected) {
        checkNumeric(term, expected, false);
    }

    protected void checkNumeric(ITerm term, String expectedStr, boolean preFix) {
        termProcessor.setPrimitiveOperationProcessor(new PrimitiveOperationProcessorMock(preFix));
        TermString actual = termProcessor.convertTerm(term);
        TermString expected = new TermString(expectedStr, TermType.Numeric);
        assertEquals(expected, actual);
    }

    protected void checkInvalid(ITerm term) {
        check(term, TermString.INVALID);
    }

    protected void check(ITerm term, String s, TermType termType) {
        check(term, new TermString(s, termType));
    }

    protected void check(ITerm term, TermString expected) {
        TermString actual = termProcessor.convertTerm(term);
        assertEquals(expected, actual);
    }

    protected INumericLiteral numericLiteral(String s) {
        return QueryObjectFactory.createNumericLiteral(s);
    }

    protected IDateLiteral dateLiteral(String s) {
        return QueryObjectFactory.createDateLiteral(Date.valueOf(s));
    }

    protected IDateOffsetLiteral dateOffsetLiteral(String s) {
        return QueryObjectFactory.createDateOffsetLiteral(s, TimeInterval.compoundEnum(DSInterval.Day));
    }

    protected <T extends Enum<?> & ITimeIntervalEnum> ILiteral dateOffsetLiteral(String s, T timeInterval) {
        return QueryObjectFactory.createDateOffsetLiteral(s, TimeInterval.compoundEnum(timeInterval));
    }

    // protected ILiteral literal(String s, TermType termType) {
    // return QueryObjectFactory.createLiteral(s, termType);
    // }

    protected void swapOperands(ITerm term, int i, int j) {
        IArithmeticOperand temp = term.getOperand(i);
        term.setOperand(i, term.getOperand(j));
        term.setOperand(j, temp);
    }

    protected IConnector<ArithmeticOperator> conn(ArithmeticOperator op, int n) {
        return QueryObjectFactory.createArithmeticConnector(op, n);
    }

    protected ITerm newTerm() {
        return QueryObjectFactory.createTerm();
    }

    protected IExpressionAttribute createNumericExpressionAttribute(String attrName, String entityName) {
        return createExpressionAttribute(attrName, entityName, TermType.Numeric);
    }

    protected IExpressionAttribute createDateExpressionAttribute(String attrName, String entityName) {
        return createExpressionAttribute(attrName, entityName, TermType.Date);
    }

    protected IExpressionAttribute createTimestampExpressionAttribute(String attrName, String entityName) {
        return createExpressionAttribute(attrName, entityName, TermType.Timestamp);
    }

    protected <T extends Enum<?> & ITimeIntervalEnum> IDateOffsetAttribute createDateOffsetExpressionAttribute(
            String attrName, String entityName, T timeInterval) {
        return QueryObjectFactory.createDateOffsetAttribute(createExpression(1), createAttribute(attrName,
                createEntity(entityName)), TimeInterval.compoundEnum(timeInterval));
    }

    private IExpressionAttribute createExpressionAttribute(String attrName, String entityName, TermType termType) {
        return QueryObjectFactory.createExpressionAttribute(createExpression(1), createAttribute(attrName,
                createEntity(entityName), termType));
    }
}