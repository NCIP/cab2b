package edu.wustl.cab2b.common.beans;

import java.io.Serializable;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * This class represents a single entry that is matched while searching.
 * The criteria that is matched will have corropsning postion field not null.
 * e.g. if attributeName is matched its attributeNamePosition will be having the position in the 
 * attribute name where the match occur.
 * 
 *  
 * @author rahul_ner
 *
 */
public class MatchedClassEntry implements Serializable {

    private EntityInterface matchedEntity;

    private Integer entityNamePosition;

    private Integer entityDescriptionPosition;

    private Integer entitySemanticPropertyPosition;

    private Integer attributeNamePosition;

    private Integer attributeDescriptionPosition;

    private Integer attributeSemanticPropertyPosition;

    private Integer pvNamePosition;

    private Integer pvSemanticPropertyPosition;

    public MatchedClassEntry(EntityInterface matchedEntity) {
        this.matchedEntity = matchedEntity;
    }

    /**
     * @return Returns the attributeDescriptionPosition.
     */
    public Integer getAttributeDescriptionPosition() {
        return attributeDescriptionPosition;
    }

    /**
     * @param attributeDescriptionPosition The attributeDescriptionPosition to set.
     */
    public void setAttributeDescriptionPosition(Integer attributeDescriptionPosition) {
        this.attributeDescriptionPosition = attributeDescriptionPosition;
    }

    /**
     * @return Returns the attributeNamePosition.
     */
    public Integer getAttributeNamePosition() {
        return attributeNamePosition;
    }

    /**
     * @param attributeNamePosition The attributeNamePosition to set.
     */
    public void setAttributeNamePosition(Integer attributeNamePosition) {
        this.attributeNamePosition = attributeNamePosition;
    }

    /**
     * @return Returns the attributeSemanticPropertyPosition.
     */
    public Integer getAttributeSemanticPropertyPosition() {
        return attributeSemanticPropertyPosition;
    }

    /**
     * @param attributeSemanticPropertyPosition The attributeSemanticPropertyPosition to set.
     */
    public void setAttributeSemanticPropertyPosition(Integer attributeSemanticPropertyPosition) {
        this.attributeSemanticPropertyPosition = attributeSemanticPropertyPosition;
    }

    /**
     * @return Returns the entityDescriptionPosition.
     */
    public Integer getEntityDescriptionPosition() {
        return entityDescriptionPosition;
    }

    /**
     * @param entityDescriptionPosition The entityDescriptionPosition to set.
     */
    public void setEntityDescriptionPosition(Integer entityDescriptionPosition) {
        this.entityDescriptionPosition = entityDescriptionPosition;
    }

    /**
     * @return Returns the entityNamePosition.
     */
    public Integer getEntityNamePosition() {
        return entityNamePosition;
    }

    /**
     * @param entityNamePosition The entityNamePosition to set.
     */
    public void setEntityNamePosition(Integer entityNamePosition) {
        this.entityNamePosition = entityNamePosition;
    }

    /**
     * @return Returns the entitySemanticPropertyPosition.
     */
    public Integer getEntitySemanticPropertyPosition() {
        return entitySemanticPropertyPosition;
    }

    /**
     * @param entitySemanticPropertyPosition The entitySemanticPropertyPosition to set.
     */
    public void setEntitySemanticPropertyPosition(Integer entitySemanticPropertyPosition) {
        this.entitySemanticPropertyPosition = entitySemanticPropertyPosition;
    }

    /**
     * @return Returns the matchedEntity.
     */
    public EntityInterface getMatchedEntity() {
        return matchedEntity;
    }

    /**
     * @param matchedEntity The matchedEntity to set.
     */
    public void setMatchedEntity(EntityInterface matchedEntity) {
        this.matchedEntity = matchedEntity;
    }

    /**
     * @return Returns the pvNamePosition.
     */
    public Integer getPvNamePosition() {
        return pvNamePosition;
    }

    /**
     * @param pvNamePosition The pvNamePosition to set.
     */
    public void setPvNamePosition(Integer pvNamePosition) {
        this.pvNamePosition = pvNamePosition;
    }

    /**
     * @return Returns the pvSemanticPropertyPosition.
     */
    public Integer getPvSemanticPropertyPosition() {
        return pvSemanticPropertyPosition;
    }

    /**
     * @param pvSemanticPropertyPosition The pvSemanticPropertyPosition to set.
     */
    public void setPvSemanticPropertyPosition(Integer pvSemanticPropertyPosition) {
        this.pvSemanticPropertyPosition = pvSemanticPropertyPosition;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof MatchedClassEntry)) {
            return false;
        }

        MatchedClassEntry otherEntry = (MatchedClassEntry) other;
        return this.matchedEntity.equals(otherEntry.matchedEntity);
    }

    /**
     * Merges the two match entries (should be of the same entities).
     * It compare the position in the input entry and copies the minimum positions this mathced entry.  
     * 
     * @param newMatchedClassEntry
     */
    public void merge(MatchedClassEntry newMatchedClassEntry) {
        if (this.entityNamePosition == null
                || (newMatchedClassEntry.entityNamePosition != null && this.entityNamePosition > newMatchedClassEntry.entityNamePosition)) {
            this.entityNamePosition = newMatchedClassEntry.entityNamePosition;
        }

        if (this.entityDescriptionPosition == null
                || (newMatchedClassEntry.entityDescriptionPosition != null && this.entityDescriptionPosition > newMatchedClassEntry.entityDescriptionPosition)) {
            this.entityDescriptionPosition = newMatchedClassEntry.entityDescriptionPosition;
        }

        if (this.entitySemanticPropertyPosition == null
                || (newMatchedClassEntry.entitySemanticPropertyPosition != null && this.entitySemanticPropertyPosition > newMatchedClassEntry.entitySemanticPropertyPosition)) {
            this.entitySemanticPropertyPosition = newMatchedClassEntry.entitySemanticPropertyPosition;
        }

        if (this.attributeNamePosition == null
                || (newMatchedClassEntry.attributeNamePosition != null && this.attributeNamePosition > newMatchedClassEntry.attributeNamePosition)) {
            this.attributeNamePosition = newMatchedClassEntry.attributeNamePosition;
        }

        if (this.attributeDescriptionPosition == null
                || (newMatchedClassEntry.attributeDescriptionPosition != null && this.attributeDescriptionPosition > newMatchedClassEntry.attributeDescriptionPosition)) {
            this.attributeDescriptionPosition = newMatchedClassEntry.attributeDescriptionPosition;
        }

        if (this.attributeSemanticPropertyPosition == null
                || (newMatchedClassEntry.attributeSemanticPropertyPosition != null && this.attributeSemanticPropertyPosition > newMatchedClassEntry.attributeSemanticPropertyPosition)) {
            this.attributeSemanticPropertyPosition = newMatchedClassEntry.attributeSemanticPropertyPosition;
        }

        if (this.pvNamePosition == null
                || (newMatchedClassEntry.pvNamePosition != null && this.pvNamePosition > newMatchedClassEntry.pvNamePosition)) {
            this.pvNamePosition = newMatchedClassEntry.pvNamePosition;
        }

        if (this.pvSemanticPropertyPosition == null
                && (newMatchedClassEntry.pvSemanticPropertyPosition != null && this.pvSemanticPropertyPosition > newMatchedClassEntry.pvSemanticPropertyPosition)) {
            this.pvSemanticPropertyPosition = newMatchedClassEntry.pvSemanticPropertyPosition;
        }

    }

}
