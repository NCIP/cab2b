package edu.wustl.common.querysuite.queryobject.impl;

import java.sql.Date;

import edu.wustl.common.querysuite.queryobject.IDateLiteral;
import edu.wustl.common.querysuite.queryobject.TermType;

public class DateLiteral extends ArithmeticOperand implements IDateLiteral {
    private static final long serialVersionUID = 5566821411890106081L;

    private Date date;

    public DateLiteral() {
        super(TermType.Date);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
