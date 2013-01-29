/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.common.querysuite.queryobject.impl;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IOutputAttribute;

/**
 * @author chetan_patil
 * @created Sep 27, 2007, 3:44:18 PM
 * 
 * @hibernate.class table="QUERY_OUTPUT_ATTRIBUTE"
 * @hibernate.cache usage="read-write"
 */
public class OutputAttribute extends BaseQueryObject implements IOutputAttribute {
    private static final long serialVersionUID = 1L;

    private IExpression expression;

    private AttributeInterface attribute;

    /** Default Constructor */
    public OutputAttribute() {

    }

    /**
     * Parameterized Constructor
     * 
     * @param expression
     * @param attribute
     */
    public OutputAttribute(IExpression expression, AttributeInterface attribute) {
        this.expression = expression;
        this.attribute = attribute;
    }

    /**
     * Name: Abhishek Mehta Reviewer Name : Deepti Bug ID: 5661 Patch ID: 5661_3
     * See also: 1-7 Description : Making cascade none from cascade all
     */

    /**
     * @return the expression
     * 
     * @hibernate.many-to-one column="EXPRESSIONID_ID"
     *                        class="edu.wustl.common.querysuite.queryobject.impl.Expression"
     *                        cascade="none" lazy="false"
     */
    public IExpression getExpression() {
        return expression;
    }

    /**
     * @param expression the expression to set
     */
    public void setExpression(IExpression expression) {
        this.expression = expression;
    }

    /**
     * @return the attribute
     */
    public AttributeInterface getAttribute() {
        return attribute;
    }

    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(AttributeInterface attribute) {
        this.attribute = attribute;
    }

    /**
     * Returns the identifier assigned to BaseQueryObject.
     * 
     * @return a unique id assigned to the Condition.
     * 
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="OUTPUT_ATTRIBUTE_SEQ"
     */
    public Long getId() {
        return id;
    }

    /**
     * To check equality of the two object.
     * 
     * @param obj to be check for equality.
     * @return true if objects are equals.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;

        if (this == object) {
            isEqual = true;
        } else if (object != null && this.getClass() == object.getClass()) {
            OutputAttribute outputAtrribute = (OutputAttribute) object;
            IExpression expression = outputAtrribute.getExpression();
            
            if (this.getExpression().equals(expression) && this.getAttribute().equals(outputAtrribute.getAttribute())) {
                isEqual = true;
            }
        }

        return isEqual;
    }

    /**
     * To get the HashCode for the object.
     * 
     * @return The hash code value for the object.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
    	return new HashCodeBuilder().append(expression).append(attribute).toHashCode();
    }
}
