/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;

public class OraclePrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {

    public OraclePrimitiveOperationProcessor() {
        super("YYYY-MM-DD", "TO_DATE");
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return numSecs(leftStr + " - " + rightStr);
    }

    private String numSecs(String s) {
        return "extract(day from " + s + ")*86400 + extract(hour from " + s + ")*3600 + extract(minute from " + s
                + ")*60 + extract(second from " + s + ")";
    }

    @Override
    String dateToTimestamp(String s) {
        // casting to timestamp is needed in view of further interval
        // arithmetic.
        return "cast(" + s + " as timestamp)";
    }

    @Override
    String getTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator) {
        return super.getResultString(timeStr, operator, "NUMTODSINTERVAL(" + offsetStr + ", 'second')");
    }
}
