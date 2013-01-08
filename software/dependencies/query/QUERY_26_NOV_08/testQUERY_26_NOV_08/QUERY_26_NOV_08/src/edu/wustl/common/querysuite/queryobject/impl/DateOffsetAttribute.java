/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.queryobject.IDateOffsetAttribute;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

public class DateOffsetAttribute extends ExpressionAttribute implements IDateOffsetAttribute {
    private static final long serialVersionUID = 3883684246378982941L;

    private TimeInterval<?> timeInterval;

    protected DateOffsetAttribute() {

    }

    public DateOffsetAttribute(IExpression expression, AttributeInterface attribute, TimeInterval timeInterval) {
        super(expression, attribute, TermType.termType(timeInterval));
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

}
