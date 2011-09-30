package edu.wustl.common.querysuite.queryobject;

import java.sql.Date;

public interface IDateLiteral extends ILiteral {
    void setDate(Date date);

    Date getDate();
}
