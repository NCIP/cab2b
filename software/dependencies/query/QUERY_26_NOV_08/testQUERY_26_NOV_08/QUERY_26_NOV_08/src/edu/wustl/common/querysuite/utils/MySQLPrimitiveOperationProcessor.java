/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;

public class MySQLPrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {
    public MySQLPrimitiveOperationProcessor() {
        super("%Y-%m-%d", "STR_TO_DATE");
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return "timestampdiff(SECOND, " + rightStr + ", " + leftStr + ")";
    }

    @Override
    String dateToTimestamp(String s) {
        return "timestamp(" + s + ")";
    }

    @Override
    String getTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator) {
        if (operator == ArithmeticOperator.Minus) {
            offsetStr = "-" + offsetStr;
        }
        return "timestampadd(SECOND, " + offsetStr + ", " + timeStr + ")";
    }
}
