package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.DSInterval;
import edu.wustl.common.querysuite.queryobject.IArithmeticOperand;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.YMInterval;
import static edu.wustl.common.querysuite.queryobject.RelationalOperator.*;

public class CustomFormulaProcessorTest extends AbstractTermProcessorTest {
    private CustomFormulaProcessor customFormulaProcessor;

    private ICustomFormula customFormula;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        customFormulaProcessor = new CustomFormulaProcessor();
        customFormula = QueryObjectFactory.createCustomFormula();
        customFormula.setLhs(newTerm(1));
    }

    public void testNoOperator() {
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testInvalidLHS() {
        customFormula.getLhs().addOperand(conn(ArithmeticOperator.Minus, 0), dateLiteral("2008-01-01"));
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testInvalidRHS() {
        addRhs(dateOffsetLiteral("off", YMInterval.Month));
        setOperator(Equals);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testIncompatibleRHS() {
        addRhs(dateLiteral("2008-01-01"));
        setOperator(Equals);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    public void testIncompatibleRHSBetweenIn() {
        addRhs(2);
        addRhs(dateLiteral("2008-01-01"));
        setOperator(Between);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
        customFormula.getAllRhs().add(1, newTerm(3));
        setOperator(In);
        try {
            customFormulaProcessor.asString(customFormula);
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    private ITerm newTerm(int opnd) {
        return newTerm(QueryObjectFactory.createNumericLiteral(String.valueOf(opnd)));
    }

    private ITerm newTerm(IArithmeticOperand opnd) {
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(opnd);
        return term;
    }

    public void testNoRHS() {
        RelationalOperator o = IsNull;
        setOperator(o);
        check("1 " + sql(o));
        o = IsNotNull;
        setOperator(o);
        check("1 " + sql(o));
    }

    private String getResult() {
        return customFormulaProcessor.asString(customFormula);
    }

    public void testOneRHS() {
        setOperator(Equals);
        checkIllegal();
        addRhs(2);
        check("1 = 2");
        setOperator(LessThan);
        check("1 < 2");
    }

    public void testBetween() {
        setOperator(Between);
        checkIllegal();
        addRhs(2);
        checkIllegal();
        addRhs(3);
        check("((1 >= 2) and (1 <= 3)) or ((1 >= 3) and (1 <= 2))");
        addRhs(4);
        checkIllegal();
    }

    public void testNotBetween() {
        setOperator(NotBetween);
        checkIllegal();
        addRhs(2);
        checkIllegal();
        addRhs(3);
        check("((1 < 2) or (1 > 3)) and ((1 < 3) or (1 > 2))");
        addRhs(4);
        checkIllegal();
    }

    public void testIn() {
        setOperator(In);
        checkIllegal();
        addRhs(2);
        check("(1 = 2)");
        addRhs(3);
        check("(1 = 2) or (1 = 3)");
        addRhs(4);
        check("(1 = 2) or (1 = 3) or (1 = 4)");
    }

    public void testNotIn() {
        setOperator(NotIn);
        checkIllegal();
        addRhs(2);
        check("(1 != 2)");
        addRhs(3);
        check("(1 != 2) and (1 != 3)");
        addRhs(4);
        check("(1 != 2) and (1 != 3) and (1 != 4)");
    }

    public void testTemporalIllegal() {
        customFormula.setLhs(newTerm(dateOffsetLiteral("1", YMInterval.Month)));
        setOperator(Equals);
        addRhs(newTerm(dateOffsetLiteral("1", YMInterval.Month)));

        customFormula.setLhs(newTerm(dateOffsetLiteral("1", DSInterval.Day)));
        customFormula.getLhs().addOperand(conn(ArithmeticOperator.Minus, 0), dateLiteral("2008-01-01"));
        // 1day - '2008-01-01' = 1month
        checkIllegal();

        customFormula.getLhs().setConnector(0, 1, conn(ArithmeticOperator.Plus, 0));
        // 1day + '2008-01-01' = 1month
        checkIllegal();

        customFormula.getLhs().setOperand(0, dateLiteral("2008-01-02"));
        // '2008-01-02 + '2008-01-01' = 1month
        checkIllegal();

        customFormula.setLhs(newTerm(1));
        // 1 = 1Month
        checkIllegal();
    }

    public void testTemporalNoRhs() {
        setOperator(IsNull);
        customFormula.setLhs(newTerm(dateOffsetLiteral("1", YMInterval.Month)));
        // 1month isnull
        check("1Month is NULL");
        setOperator(Equals);
    }

    public void testTemporalRounding() {
        setOperator(Equals);
        customFormula.setLhs(newTerm(dateLiteral("2008-01-02")));
        customFormula.getLhs().addOperand(conn(ArithmeticOperator.Minus, 0), dateLiteral("2008-01-01"));
        addRhs(newTerm(dateOffsetLiteral("1", YMInterval.Month)));
        // d1 - d2 = 1Month
        check("('2008-01-02' - '2008-01-01' >= 1Month - 15Day) and ('2008-01-02' - '2008-01-01' <= 1Month + 15Day)");

        setOperator(NotEquals);
        check("('2008-01-02' - '2008-01-01' < 1Month - 15Day) or ('2008-01-02' - '2008-01-01' > 1Month + 15Day)");

        setOperator(LessThan);
        check("'2008-01-02' - '2008-01-01' < 1Month");

        setOperator(GreaterThan);
        check("'2008-01-02' - '2008-01-01' > 1Month");

        setOperator(LessThanOrEquals);
        check("'2008-01-02' - '2008-01-01' <= 1Month + 15Day");

        setOperator(GreaterThanOrEquals);
        check("'2008-01-02' - '2008-01-01' >= 1Month - 15Day");

        setOperator(Between);
        addRhs(newTerm(dateOffsetLiteral("2", DSInterval.Day)));
        check("(('2008-01-02' - '2008-01-01' >= 1Month - 15Day) and ('2008-01-02' - '2008-01-01' <= 2Day + 12Hour)) or (('2008-01-02' - '2008-01-01' >= 2Day - 12Hour) and ('2008-01-02' - '2008-01-01' <= 1Month + 15Day))");

        setOperator(In);
        check("(('2008-01-02' - '2008-01-01' >= 1Month - 15Day) and ('2008-01-02' - '2008-01-01' <= 1Month + 15Day)) or (('2008-01-02' - '2008-01-01' >= 2Day - 12Hour) and ('2008-01-02' - '2008-01-01' <= 2Day + 12Hour))");
        
        setOperator(NotIn);
        check("(('2008-01-02' - '2008-01-01' < 1Month - 15Day) or ('2008-01-02' - '2008-01-01' > 1Month + 15Day)) and (('2008-01-02' - '2008-01-01' < 2Day - 12Hour) or ('2008-01-02' - '2008-01-01' > 2Day + 12Hour))");

        setOperator(Equals);
        customFormula.getAllRhs().remove(1);
        customFormula.getAllRhs().get(0).addOperand(conn(ArithmeticOperator.Plus, 0), dateOffsetLiteral("2", DSInterval.Week));
        check("('2008-01-02' - '2008-01-01' >= (1Month + 2Week) - 15Day) and ('2008-01-02' - '2008-01-01' <= (1Month + 2Week) + 15Day)");
    }

    private void addRhs(int i) {
        addRhs(newTerm(i));
    }

    private void addRhs(IArithmeticOperand opnd) {
        addRhs(newTerm(opnd));
    }

    private void addRhs(ITerm term) {
        customFormula.addRhs(term);
    }

    private void setOperator(RelationalOperator operator) {
        customFormula.setOperator(operator);
    }

    private String sql(RelationalOperator o) {
        return getSQL(o);
    }

    private void check(String expected) {
        assertEquals(expected, getResult());
        assertTrue(customFormula.isValid());
    }

    private void checkIllegal() {
        assertFalse(customFormula.isValid());
        try {
            getResult();
            fail();
        } catch (IllegalArgumentException e) {

        }
    }
}
