/**
 * 
 */
package edu.wustl.common.querysuite.queryobject;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * @author chetan_patil
 * @created Oct 8, 2007, 3:04:09 PM
 */
public interface IOutputAttribute extends IBaseQueryObject {
    IExpression getExpression();

    AttributeInterface getAttribute();
}
