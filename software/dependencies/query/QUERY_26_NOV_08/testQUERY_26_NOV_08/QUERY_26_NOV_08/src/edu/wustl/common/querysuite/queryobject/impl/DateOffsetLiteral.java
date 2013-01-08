/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

public class DateOffsetLiteral extends ArithmeticOperand implements IDateOffsetLiteral {
    private static final long serialVersionUID = -7510642736372664817L;

    private TimeInterval timeInterval;

    private String offset;

    private DateOffsetLiteral() {
        super(TermType.DSInterval);
    }

    public DateOffsetLiteral(TimeInterval<?> timeInterval) {
        super(TermType.termType(timeInterval));
        if (timeInterval == null) {
            throw new NullPointerException();
        }
        this.timeInterval = timeInterval;
    }

    public TimeInterval<?> getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval<?> timeInterval) {
        this.timeInterval = timeInterval;
        setTermType(TermType.termType(timeInterval));
    }

    public String getOffset() {
        if (offset == null) {
            offset = "";
        }
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }
}
