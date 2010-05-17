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

    /**
     * Constructor
     * @param matchedEntity matched entity
     */
    public MatchedClassEntry(EntityInterface matchedEntity) {
        this.matchedEntity = matchedEntity;
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
    }
    /**
     * @param object to compare
     * @return TRUE if passed object is equal to passed object
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
