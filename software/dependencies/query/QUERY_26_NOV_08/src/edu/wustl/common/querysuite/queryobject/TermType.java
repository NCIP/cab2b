/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

/**
 * Enum of the possible types of {@link ITerm} and related utility methods.
 * 
 * @author srinath_k
 * @see ITerm
 */
public enum TermType {
    // TODO String?
    // TODO Boolean ??
    Date, Timestamp, YMInterval, DSInterval, Numeric, Invalid;

    /**
     * Returns the term type resulting from the specified arithmetic operation.
     * Following is the result term type for various cases : <br>
     * Date or timestamp (D), Timestamp(T), Numeric (N), DSInterval(DS),
     * YMInterval(YM), Interval (DS or YM).
     * 
     * <pre>
     * 
     * D (+,-) N  = T 
     * D (+,-) I  = T 
     * I   +   D  = T 
     * D   -   D  = DS
     * DS(+,-)DS  = DS 
     * N (any) N  = N
     * 
     * </pre>
     * 
     * All other operations result in {@link #Invalid}. Note in particular that
     * no operation results in <tt>YMInterval</tt>; also, a term should never
     * have a single operand of type <tt>YMInterval</tt>.<br>
     * Also, a date is converted to a timestamp; thus, if a term contains a
     * single date operand, the term's type is timestamp.
     * 
     * @param leftOpndType the type of the left operand.
     * @param rightOpndType the type of the right operand.
     * @param operator the operator.
     * @return the resultant term type.
     */
    public static TermType getResultTermType(TermType leftOpndType, TermType rightOpndType, ArithmeticOperator operator) {
        if (leftOpndType == Invalid || rightOpndType == Invalid) {
            return Invalid;
        }
        if (leftOpndType == Numeric && rightOpndType == Numeric) {
            return Numeric;
        }
        if (leftOpndType == Date) {
            leftOpndType = Timestamp;
        }
        if (rightOpndType == Date) {
            rightOpndType = Timestamp;
        }

        // rest is Temporal
        if (leftOpndType == Numeric) {
            // becomes DAY
            leftOpndType = DSInterval;
        }
        if (rightOpndType == Numeric) {
            // becomes DAY
            rightOpndType = DSInterval;
        }
        if (isInterval(leftOpndType) && isInterval(rightOpndType)) {
            if (operator == ArithmeticOperator.Plus || operator == ArithmeticOperator.Minus) {
                return DSInterval;
            }
            return Invalid;
        }
        if (leftOpndType == Timestamp && rightOpndType == Timestamp && operator == ArithmeticOperator.Minus) {
            return DSInterval;
        }

        if (leftOpndType == Timestamp && isInterval(rightOpndType)
                && (operator == ArithmeticOperator.Plus || operator == ArithmeticOperator.Minus)) {
            return Timestamp;
        }
        if (isInterval(leftOpndType) && rightOpndType == Timestamp && operator == ArithmeticOperator.Plus) {
            return Timestamp;
        }
        return Invalid;
    }

    /**
     * @return <tt>termType == DSInterval || termType == YMInterval</tt>
     */
    public static boolean isInterval(TermType termType) {
        return termType == DSInterval || termType == YMInterval;
    }

    public static boolean isDateTime(TermType termType) {
        return termType == Date || termType == Timestamp;
    }

    /**
     * @return <tt>timeInterval instanceof DSInterval ? DSInterval : YMInterval</tt>
     */
    public static TermType termType(TimeInterval<?> timeInterval) {
        if (timeInterval.primitiveEnum() instanceof DSInterval) {
            return DSInterval;
        } else if (timeInterval.primitiveEnum() instanceof YMInterval) {
            return YMInterval;
        } else {
            throw new RuntimeException("can't occur.");
        }
    }
}
