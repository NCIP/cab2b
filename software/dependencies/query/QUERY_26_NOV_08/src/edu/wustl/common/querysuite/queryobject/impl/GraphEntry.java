/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.common.querysuite.queryobject.impl;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.queryobject.IExpression;

/**
 * 
 * @author chetan_patil
 * @created Aug 10, 2007, 5:27:53 PM
 * 
 * @hibernate.class table="QUERY_GRAPH_ENTRY"
 * @hibernate.cache usage="read-write"
 */
public class GraphEntry extends BaseQueryObject {
    private static final long serialVersionUID = 1L;

    private IExpression sourceExpression;

    private IExpression targetExpression;

    private IAssociation association;

    /**
     * Default Constructor
     */
    public GraphEntry() {

    }

    /**
     * Parameterized Constructor
     * 
     * @param sourceExpression
     * @param targetExpression
     * @param association
     */
    public GraphEntry(IExpression sourceExpression, IExpression targetExpression, IAssociation association) {
        super();
        this.sourceExpression = sourceExpression;
        this.targetExpression = targetExpression;
        this.association = association;
    }

    /**
     * Returns the identifier assigned to BaseQueryObject.
     * 
     * @return a unique id assigned to the Condition.
     * 
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="GRAPH_ENTRY_SEQ"
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the association
     * 
     * @hibernate.many-to-one column="QUERY_MODEL_ASSOCIATION_ID"
     *                        class="edu.wustl.common.querysuite.metadata.associations.impl.ModelAssociation"
     *                        cascade="save-update" lazy="false"
     */
    public IAssociation getAssociation() {
        return association;
    }

    /**
     * @param association the association to set
     */
    public void setAssociation(IAssociation association) {
        this.association = association;
    }

    /**
     * Name: Abhishek Mehta Reviewer Name : Deepti Bug ID: 5661 Patch ID: 5661_1
     * See also: 1-7 Description : Making cascade save-update from cascade all
     */
    /**
     * @return the sourceExpression
     * 
     * @hibernate.many-to-one column="SOURCE_EXPRESSIONID_ID"
     *                        class="edu.wustl.common.querysuite.queryobject.impl.Expression"
     *                        cascade="save-update" lazy="false"
     */
    public IExpression getSourceExpression() {
        return sourceExpression;
    }

    /**
     * @param sourceExpression the sourceExpression to set
     */
    public void setSourceExpression(IExpression sourceExpression) {
        this.sourceExpression = sourceExpression;
    }

    /**
     * Name: Abhishek Mehta Reviewer Name : Deepti Bug ID: 5661 Patch ID: 5661_2
     * See also: 1-7 Description : Making cascade save-update from cascade all
     */

    /**
     * @return the targetExpression
     * 
     * @hibernate.many-to-one column="TARGET_EXPRESSIONID_ID"
     *                        class="edu.wustl.common.querysuite.queryobject.impl.Expression"
     *                        cascade="save-update" lazy="false"
     */
    public IExpression getTargetExpression() {
        return targetExpression;
    }

    /**
     * @param targetExpression the targetExpression to set
     */
    public void setTargetExpression(IExpression targetExpression) {
        this.targetExpression = targetExpression;
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
            GraphEntry graphEntry = (GraphEntry) object;
            IExpression sourceExpression = graphEntry.getSourceExpression();
            IExpression targetExpression = graphEntry.getTargetExpression();
            IAssociation association = graphEntry.getAssociation();
            if (this.getSourceExpression().equals(sourceExpression)
                    && this.getTargetExpression().equals(targetExpression)
                    && this.getAssociation().equals(association)) {
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
        return new HashCodeBuilder().append(sourceExpression).append(targetExpression).append(association)
                .toHashCode();
    }

}
