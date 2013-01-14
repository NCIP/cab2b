/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * This class represents a single entry that is matched while searching.
 * The criteria that is matched will have corresponding position field not null.
 * e.g. if attributeName is matched its attributeNamePosition will be having the position in the 
 * attribute name where the match occur.
 * 
 * @author Chandrakant Talele
 * @author rahul_ner
 */
public class MatchedClassEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private EntityInterface matchedEntity;

    private Map<MatchCause, Integer> causeVsPosition = new HashMap<MatchCause, Integer>();

    public MatchedClassEntry(EntityInterface matchedEntity) {
        this.matchedEntity = matchedEntity;
    }
//
//    /**
//     * @return Returns the attributeDescriptionPosition.
//     */
//    public Integer getAttributeDescriptionPosition() {
//        return positionOf(MatchCause.AttributeDescription);
//    }
//
//    /**
//     * @param attributeDescriptionPosition The attributeDescriptionPosition to set.
//     */
//    public void setAttributeDescriptionPosition(Integer attributeDescriptionPosition) {
//        setPositionOf(MatchCause.AttributeDescription, attributeDescriptionPosition);
//    }
//
//    /**
//     * @return Returns the attributeNamePosition.
//     */
//    public Integer getAttributeNamePosition() {
//        return positionOf(MatchCause.AttributeName);
//    }
//
//    /**
//     * @param attributeNamePosition The attributeNamePosition to set.
//     */
//    public void setAttributeNamePosition(Integer attributeNamePosition) {
//        setPositionOf(MatchCause.AttributeName, attributeNamePosition);
//    }
//
//    /**
//     * @return Returns the attributeSemanticPropertyPosition.
//     */
//    public Integer getAttributeSemanticPropertyPosition() {
//        return positionOf(MatchCause.AttributeSemanticProperty);
//    }
//
//    /**
//     * @param attributeSemanticPropertyPosition The attributeSemanticPropertyPosition to set.
//     */
//    public void setAttributeSemanticPropertyPosition(Integer attributeSemanticPropertyPosition) {
//        setPositionOf(MatchCause.AttributeSemanticProperty, attributeSemanticPropertyPosition);
//    }
//
//    /**
//     * @return Returns the entityDescriptionPosition.
//     */
//    public Integer getEntityDescriptionPosition() {
//        return positionOf(MatchCause.EntityDescription);
//    }
//
//    /**
//     * @param entityDescriptionPosition The entityDescriptionPosition to set.
//     */
//    public void setEntityDescriptionPosition(Integer entityDescriptionPosition) {
//        setPositionOf(MatchCause.EntityDescription, entityDescriptionPosition);
//    }
//
//    /**
//     * @return Returns the entityNamePosition.
//     */
//    public Integer getEntityNamePosition() {
//        return positionOf(MatchCause.EntityName);
//    }
//
//    /**
//     * @param entityNamePosition The entityNamePosition to set.
//     */
//    public void setEntityNamePosition(Integer entityNamePosition) {
//        setPositionOf(MatchCause.EntityName, entityNamePosition);
//    }
//
//    /**
//     * @return Returns the entitySemanticPropertyPosition.
//     */
//    public Integer getEntitySemanticPropertyPosition() {
//        return positionOf(MatchCause.EntitySemanticProperty);
//    }
//
//    /**
//     * @param entitySemanticPropertyPosition The entitySemanticPropertyPosition to set.
//     */
//    public void setEntitySemanticPropertyPosition(Integer entitySemanticPropertyPosition) {
//        setPositionOf(MatchCause.EntitySemanticProperty, entitySemanticPropertyPosition);
//    }
//
//    /**
//     * @return Returns the pvNamePosition.
//     */
//    public Integer getPvNamePosition() {
//        return positionOf(MatchCause.PermissibleValueName);
//    }
//
//    /**
//     * @param pvNamePosition The pvNamePosition to set.
//     */
//    public void setPvNamePosition(Integer pvNamePosition) {
//        setPositionOf(MatchCause.PermissibleValueName, pvNamePosition);
//    }
//
//    /**
//     * @return Returns the pvSemanticPropertyPosition.
//     */
//    public Integer getPvSemanticPropertyPosition() {
//        return positionOf(MatchCause.PermissibleSemanticProperty);
//    }
//
//    /**
//     * @param pvSemanticPropertyPosition The pvSemanticPropertyPosition to set.
//     */
//    public void setPvSemanticPropertyPosition(Integer pvSemanticPropertyPosition) {
//        setPositionOf(MatchCause.PermissibleSemanticProperty, pvSemanticPropertyPosition);
//    }

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
     * @param cause Cause 
     * @return Position
     */
    public Integer positionOf(MatchCause cause) {
        return causeVsPosition.get(cause);
    }
    /**
     * @param cause Cause 
     * @param position Position
     */
    public void setPositionOf(MatchCause cause, Integer position) {
        causeVsPosition.put(cause,position);
    }
    /**
     * Merges the two match entries (should be of the same entities).
     * It compare the position in the input entry and copies the minimum positions this matched entry.  
     * 
     * @param newMatchedClassEntry
     */
    public void merge(MatchedClassEntry newMatchedClassEntry) {

        for (MatchCause cause : MatchCause.values()) {

            Integer position1 = positionOf(cause);
            Integer position2 = newMatchedClassEntry.positionOf(cause);
            if (position1 == null || (position2 != null && position1 > position2)) {
                setPositionOf(cause, position2);
            }
        }

        //        if (this.entityDescriptionPosition == null
        //                || (newMatchedClassEntry.entityDescriptionPosition != null && this.entityDescriptionPosition > newMatchedClassEntry.entityDescriptionPosition)) {
        //            this.entityDescriptionPosition = newMatchedClassEntry.entityDescriptionPosition;
        //        }
        //
        //        if (this.entitySemanticPropertyPosition == null
        //                || (newMatchedClassEntry.entitySemanticPropertyPosition != null && this.entitySemanticPropertyPosition > newMatchedClassEntry.entitySemanticPropertyPosition)) {
        //            this.entitySemanticPropertyPosition = newMatchedClassEntry.entitySemanticPropertyPosition;
        //        }
        //
        //        if (this.attributeNamePosition == null
        //                || (newMatchedClassEntry.attributeNamePosition != null && this.attributeNamePosition > newMatchedClassEntry.attributeNamePosition)) {
        //            this.attributeNamePosition = newMatchedClassEntry.attributeNamePosition;
        //        }
        //
        //        if (this.attributeDescriptionPosition == null
        //                || (newMatchedClassEntry.attributeDescriptionPosition != null && this.attributeDescriptionPosition > newMatchedClassEntry.attributeDescriptionPosition)) {
        //            this.attributeDescriptionPosition = newMatchedClassEntry.attributeDescriptionPosition;
        //        }
        //
        //        if (this.attributeSemanticPropertyPosition == null
        //                || (newMatchedClassEntry.attributeSemanticPropertyPosition != null && this.attributeSemanticPropertyPosition > newMatchedClassEntry.attributeSemanticPropertyPosition)) {
        //            this.attributeSemanticPropertyPosition = newMatchedClassEntry.attributeSemanticPropertyPosition;
        //        }
        //
        //        if (this.pvNamePosition == null
        //                || (newMatchedClassEntry.pvNamePosition != null && this.pvNamePosition > newMatchedClassEntry.pvNamePosition)) {
        //            this.pvNamePosition = newMatchedClassEntry.pvNamePosition;
        //        }
        //
        //        if (this.pvSemanticPropertyPosition == null
        //                && (newMatchedClassEntry.pvSemanticPropertyPosition != null && this.pvSemanticPropertyPosition > newMatchedClassEntry.pvSemanticPropertyPosition)) {
        //            this.pvSemanticPropertyPosition = newMatchedClassEntry.pvSemanticPropertyPosition;
        //        }

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
     * Order of definition is important.
     * DON"T CHANGE THE ORDER. IT WILL BREAK METADATA SEARCH ALL-TOGETHER
     * @author chandrakant_talele
     */
    public enum MatchCause {
        EntityName,
        EntityDescription,
        EntitySemanticProperty,
        AttributeName,
        AttributeDescription,
        AttributeSemanticProperty,
        PermissibleValueName,
        PermissibleSemanticProperty;
    }
}
