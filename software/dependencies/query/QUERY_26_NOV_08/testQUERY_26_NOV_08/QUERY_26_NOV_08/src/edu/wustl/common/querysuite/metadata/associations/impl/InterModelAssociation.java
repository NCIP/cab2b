/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.metadata.associations.impl;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;

/**
 * This stores all the inter model connections present between given pair of
 * entities.
 * 
 * @see edu.wustl.common.querysuite.metadata.associations.IAssociation
 * @author Chandrakant Talele
 * @hibernate.joined-subclass table="QUERY_INTER_MODEL_ASSOCIATION"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @hibernate.cache usage="read-write"
 */
public class InterModelAssociation extends ModelAssociation implements IInterModelAssociation {
    private static final long serialVersionUID = -3230037755747481080L;

    private String sourceServiceUrl;

    private String targetServiceUrl;

    private AttributeInterface sourceAttribute;

    private AttributeInterface targetAttribute;

    /**
     * Default Constructor
     */
    public InterModelAssociation() {

    }

    /**
     * Parameterized Constructor
     * 
     * @param sourceAttribute
     * @param targetAttribute
     */
    public InterModelAssociation(AttributeInterface sourceAttribute, AttributeInterface targetAttribute) {
        this.sourceAttribute = sourceAttribute;
        this.targetAttribute = targetAttribute;
    }

    /**
     * @see edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation#getSourceServiceUrl()
     * @hibernate.property name="sourceServiceUrl" column="SOURCE_SERVICE_URL"
     *                     type="string" length="1000" not-null="true"
     */
    public String getSourceServiceUrl() {
        return sourceServiceUrl;
    }

    /**
     * @see edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation#setSourceServiceUrl(java.lang.String)
     */
    public void setSourceServiceUrl(String sourceServiceUrl) {
        this.sourceServiceUrl = sourceServiceUrl;
    }

    /**
     * @see edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation#getTargetServiceUrl()
     * @hibernate.property name="targetServiceUrl" column="TARGET_SERVICE_URL"
     *                     type="string" length="1000" not-null="true"
     */
    public String getTargetServiceUrl() {
        return targetServiceUrl;
    }

    /**
     * @see edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation#setTargetServiceUrl(java.lang.String)
     */
    public void setTargetServiceUrl(String targetServiceUrl) {
        this.targetServiceUrl = targetServiceUrl;
    }

    /**
     * @see edu.wustl.common.querysuite.metadata.associations.IAssociation#getSourceEntity()
     */
    public EntityInterface getSourceEntity() {
        return sourceAttribute.getEntity();
    }

    /**
     * @see edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation#getSourceAttribute()
     */
    public AttributeInterface getSourceAttribute() {
        return sourceAttribute;
    }

    public void setSourceAttribute(AttributeInterface sourceAttribute) {
        this.sourceAttribute = sourceAttribute;
    }

    /**
     * @see edu.wustl.common.querysuite.metadata.associations.IAssociation#getTargetEntity()
     */
    public EntityInterface getTargetEntity() {
        return targetAttribute.getEntity();
    }

    /**
     * @see edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation#getTargetAttribute()
     */
    public AttributeInterface getTargetAttribute() {
        return targetAttribute;
    }

    public void setTargetAttribute(AttributeInterface targetAttribute) {
        this.targetAttribute = targetAttribute;
    }

    /**
     * @see edu.wustl.common.querysuite.metadata.associations.IAssociation#isBidirectional()
     */
    public boolean isBidirectional() {
        return true; // intermodel association is always bidirectional
    }

    /**
     * @return association with swapped source and target attributes, urls.
     * @author Srinath K.
     */
    public InterModelAssociation reverse() {
        InterModelAssociation interModelAssociation = new InterModelAssociation(getTargetAttribute(),
                getSourceAttribute());

        interModelAssociation.setSourceServiceUrl(getTargetServiceUrl());
        interModelAssociation.setTargetServiceUrl(getSourceServiceUrl());
        return interModelAssociation;
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("Source Attribute : " + sourceAttribute.getName());
        buff.append("\tSource Entity : " + getSourceEntity().getName());
        buff.append("\tTarget Attribute : " + targetAttribute.getName());
        buff.append("\tTarget Entity : " + getTargetEntity().getName());
        return buff.toString();
    }

    @Override
    public InterModelAssociation clone() {
        InterModelAssociation clone = new InterModelAssociation(sourceAttribute, targetAttribute);
        clone.setSourceServiceUrl(sourceServiceUrl);
        clone.setTargetServiceUrl(targetServiceUrl);
        return clone;
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
            InterModelAssociation interModelAssociation = (InterModelAssociation) object;
            AttributeInterface sourceAttribute = interModelAssociation.getSourceAttribute();
            AttributeInterface targetAttribute = interModelAssociation.getTargetAttribute();
            String sourceServiceURL = interModelAssociation.getSourceServiceUrl();
            String targetServiceURL = interModelAssociation.getTargetServiceUrl();

            if ((sourceAttribute != null && this.getSourceAttribute().equals(sourceAttribute))
                    && (targetAttribute != null && this.getTargetAttribute().equals(targetAttribute))
                    && (sourceServiceURL != null && this.getSourceServiceUrl().equals(sourceServiceURL))
                    && (targetServiceURL != null && this.getTargetServiceUrl().equals(targetServiceURL))) {
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
        return new HashCodeBuilder().append(sourceAttribute).append(targetAttribute).append(sourceServiceUrl).append(
                targetServiceUrl).toHashCode();
    }
}
