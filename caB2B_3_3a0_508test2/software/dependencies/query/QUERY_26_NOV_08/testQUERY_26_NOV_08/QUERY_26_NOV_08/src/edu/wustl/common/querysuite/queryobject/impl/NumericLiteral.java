package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.INumericLiteral;
import edu.wustl.common.querysuite.queryobject.TermType;

public class NumericLiteral extends ArithmeticOperand implements INumericLiteral {
    private static final long serialVersionUID = 3542370621181247228L;

    private String number;

    public NumericLiteral() {
        super(TermType.Numeric);
    }

    public String getNumber() {
        if (number == null) {
            number = "";
        }
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
