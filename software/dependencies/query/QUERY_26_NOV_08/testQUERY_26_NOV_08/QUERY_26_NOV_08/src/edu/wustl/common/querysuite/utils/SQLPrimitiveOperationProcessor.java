/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IDateLiteral;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;
import edu.wustl.common.querysuite.utils.TermProcessor.TermStringOpnd;

/**
 * Provides SQL database specific string representations of primitive
 * operations.
 * 
 * @author srinath_k
 * @see PrimitiveOperationProcessor
 */
abstract class SQLPrimitiveOperationProcessor extends PrimitiveOperationProcessor {
    private final String dateFormat;

    private final String strToDateFunc;

    SQLPrimitiveOperationProcessor(String dateFormat, String strToDateFunc) {
        if (dateFormat == null) {
            throw new NullPointerException("date format is null.");
        }
        if (strToDateFunc == null) {
            throw new NullPointerException("str-to-date function is null.");
        }
        this.dateFormat = dateFormat;
        this.strToDateFunc = strToDateFunc;
    }

    /**
     * Customizes the result string when the operation is temporal. For
     * non-temporal operations, the value obtained from
     * {@link PrimitiveOperationProcessor#getResultString(String, ArithmeticOperator, String)}
     * is returned.
     */
    @Override
    String getResultString(TermStringOpnd leftTermStrOpnd, ArithmeticOperator operator, TermStringOpnd rightTermStrOpnd) {
        TermType leftType = leftTermStrOpnd.getTermType();
        TermType rightType = rightTermStrOpnd.getTermType();

        String leftStr = leftTermStrOpnd.getString();
        String rightStr = rightTermStrOpnd.getString();
        if (leftType == TermType.Date) {
            leftStr = dateToTimestamp(leftStr);
            leftType = TermType.Timestamp;
        }
        if (rightType == TermType.Date) {
            rightStr = dateToTimestamp(rightStr);
            rightType = TermType.Timestamp;
        }

        if (leftType == TermType.Timestamp && rightType == TermType.Timestamp) {
            checkMinus(operator);
            return getDateDiffString(leftStr, rightStr);
        }
        if (leftType == TermType.DSInterval && rightType == TermType.DSInterval) {
            checkPlusOrMinus(operator);
            return getIntervalOp(leftStr, operator, rightStr);
        }
        if (leftType == TermType.Timestamp && TermType.isInterval(rightType)) {
            checkPlusOrMinus(operator);
            return getTimeOffsetOpString(leftStr, rightStr, operator);
        }
        if (TermType.isInterval(leftType) && rightType == TermType.Timestamp) {
            checkPlus(operator);
            return getTimeOffsetOpString(rightStr, leftStr, operator);
        }
        return super.getResultString(leftStr, operator, rightStr);
    }

    private String getIntervalOp(String leftStr, ArithmeticOperator operator, String rightStr) {
        return super.getResultString(leftStr, operator, rightStr);
    }

    @Override
    protected String modifyDateLiteral(IDateLiteral s) {
        return strToDateFunc + "('" + standardDateFormat(s) + "', '" + dateFormat + "')";
    }

    protected String standardDateFormat(IDateLiteral s) {
        return s.getDate().toString();
    }

    private void checkPlusOrMinus(ArithmeticOperator operator) {
        if (!(operator == ArithmeticOperator.Plus || operator == ArithmeticOperator.Minus)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkPlus(ArithmeticOperator operator) {
        if (operator != ArithmeticOperator.Plus) {
            throw new IllegalArgumentException();
        }
    }

    private void checkMinus(ArithmeticOperator operator) {
        if (operator != ArithmeticOperator.Minus) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected String getIntervalString(String s, TimeInterval<?> timeInterval) {
        return "(" + s + ")*" + timeInterval.numSeconds();
    }

    /**
     * @param s the date string
     * @return the SQL string that converts/casts the specified date string to a
     *         timestamp.
     */
    @Override
    abstract String dateToTimestamp(String s);

    /**
     * Returns the SQL string denoting difference (in seconds) between the two
     * dates.
     * 
     * @return SQL for (leftStr - rightStr) in seconds.
     */
    abstract String getDateDiffString(String leftStr, String rightStr);

    /**
     * Returns the SQL string (of type timestamp) denoting the operation
     * <tt>time +/- offset<tt>. 
     * 
     * @return SQL for <tt>time +/- offset<tt> of type timestamp.
     */
    abstract String getTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator);

    // for testing
    final String getDateFormat() {
        return dateFormat;
    }
}
