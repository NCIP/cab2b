package edu.wustl.cab2b.server.path;

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
     * @param leftEntity  Left side entity
     * @param rightEntity Right side entity
     * @param allMatchingAttributePairs list of matching attribute pairs.
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
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("Left Entity : " + leftEntityId);
        buff.append("\tLeft Attribute : " + leftAttributeId);
        buff.append("\tRight Entity : " + rightEntityId);
        buff.append("\tRight Attribute : " + rightAttributeId);
        return buff.toString();
    }
}