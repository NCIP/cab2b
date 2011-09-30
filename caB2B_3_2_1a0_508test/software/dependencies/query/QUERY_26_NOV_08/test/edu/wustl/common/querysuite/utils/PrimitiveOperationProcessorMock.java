package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.utils.TermProcessor.TermStringOpnd;

class PrimitiveOperationProcessorMock extends PrimitiveOperationProcessor {
    private boolean preFix;

    PrimitiveOperationProcessorMock(boolean preFix) {
        this.preFix = preFix;
    }

    String getResultString(TermStringOpnd leftTermStrOpnd, ArithmeticOperator operator,
            TermStringOpnd rightTermStrOpnd) {
        String leftOpndString = leftTermStrOpnd.getString();
        String rightOpndString = rightTermStrOpnd.getString();
        String termStr;
        if (preFix) {
            termStr = operator.mathString() + "[" + leftOpndString + ", " + rightOpndString + "]";
        } else {
            termStr = super.getResultString(leftTermStrOpnd, operator, rightTermStrOpnd);
        }
        return termStr;
    }
}
