/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IDateLiteral;
import edu.wustl.common.querysuite.queryobject.TimeInterval;
import edu.wustl.common.querysuite.utils.TermProcessor.TermStringOpnd;

/**
 * 
 * Provides string representation of a primitive operation in a term. A
 * "primitive operation" is currently defined by a binary operator and its two
 * operands.
 * 
 * @author srinath_k
 * 
 */
class PrimitiveOperationProcessor {
    /**
     * @param leftTermStrOpnd the left operand
     * @param operator the operator
     * @param rightTermStrOpnd the right operand
     * @return string representation of the operation
     */
    String getResultString(TermStringOpnd leftTermStrOpnd, ArithmeticOperator operator, TermStringOpnd rightTermStrOpnd) {
        return getResultString(leftTermStrOpnd.getString(), operator, rightTermStrOpnd.getString());
    }

    String modifyDateLiteral(IDateLiteral literal) {
        return "'" + literal.getDate().toString() + "'";
    }

    String getIntervalString(String s, TimeInterval<?> timeInterval) {
        return s + timeInterval;
    }

    String dateToTimestamp(String s) {
        return s;
    }

    /**
     * @return default string representation of the operation as
     *         <tt>leftStr + " " + operator.mathString() + " " + rightStr</tt>
     */
    final String getResultString(String leftStr, ArithmeticOperator operator, String rightStr) {
        return leftStr + " " + operator.mathString() + " " + rightStr;
    }
}
