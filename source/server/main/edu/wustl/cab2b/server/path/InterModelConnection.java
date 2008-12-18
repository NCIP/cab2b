package edu.wustl.cab2b.server.path;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * This stores all the inter model connections present between given pair of entities.
 * @author Chandrakant Talele
 */
class InterModelConnection {
    /**
     * Left side entity of the inter model connection 
     */
    private Long leftEntityId;

    /**
     * Right side entity of the inter model connection 
     */
    private Long rightEntityId;

    /**
     * Matching attribute from left side entity.
     */
    private Long leftAttributeId;

    /**
     * Matching attribute from right side entity.
     */
    private Long rightAttributeId;

    /**
     * 
     * @param leftAttribute  Left side entity
     * @param rightAttribute Right side entity
     */
    public InterModelConnection(AttributeInterface leftAttribute, AttributeInterface rightAttribute) {
        leftAttributeId = leftAttribute.getId();
        rightAttributeId = rightAttribute.getId();
        leftEntityId = leftAttribute.getEntity().getId();
        rightEntityId = rightAttribute.getEntity().getId();

    }

    private InterModelConnection(Long leftEntityId, Long leftAttributeId, Long rightEntityId, Long rightAttributeId) {
        this.leftEntityId = leftEntityId;
        this.rightEntityId = rightEntityId;
        this.leftAttributeId = leftAttributeId;
        this.rightAttributeId = rightAttributeId;
    }

    /**
     * @return the mirror (i.e. interchanged left and right attributes) inter-model connection
     */
    public InterModelConnection mirror() {
        return new InterModelConnection(rightEntityId, rightAttributeId, leftEntityId, leftAttributeId);
    }

    /**
     * @return Returns the leftAttributeId.
     */
    public Long getLeftAttributeId() {
        return leftAttributeId;
    }

    /**
     * @return Returns the leftEntityId.
     */
    public Long getLeftEntityId() {
        return leftEntityId;
    }

    /**
     * @return Returns the rightAttributeId.
     */
    public Long getRightAttributeId() {
        return rightAttributeId;
    }

    /**
     * @return Returns the rightEntityId.
     */
    public Long getRightEntityId() {
        return rightEntityId;
    }

    /**
     * To print something meaningful.
     * @return String form of object
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buff = new StringBuffer(70);
        buff.append("Left Entity : ").append(leftEntityId);
        buff.append("\tLeft Attribute : ").append(leftAttributeId);
        buff.append("\tRight Entity : ").append(rightEntityId);
        buff.append("\tRight Attribute : ").append(rightAttributeId);
        return buff.toString();
    }

    @Override
    /**
     * Returns whether this object is equal to o  
     * @return 
     */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InterModelConnection)) {
            return false;
        }
        InterModelConnection other = (InterModelConnection) o;
        return leftEntityId.equals(other.leftEntityId) && leftAttributeId.equals(other.leftAttributeId)
                && rightEntityId.equals(other.rightEntityId) && rightAttributeId.equals(other.rightAttributeId);
    }

    @Override
    /**
     * Returns hashcode
     * @return hashCode
     */
    public int hashCode() {
        return new HashCodeBuilder().append(leftEntityId).append(leftAttributeId).append(rightEntityId).append(
                                                                                                               rightAttributeId).toHashCode();
    }
}