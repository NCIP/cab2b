/**
 * <p>Title: MatchedClass Class>
 * <p>Description:  This Class encapsulates the searched Entity classes and 
 * the matched attributes in advance search.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * This Class encapsulates the searched Entity classes and 
 * the matched attributes in advance search.
 * @author gautam_shetty
 */
public class MatchedClass implements Serializable {
    private static final long serialVersionUID = 1234567890L;

    /**
     * Collection of matched entities.
     */
    Set<EntityInterface> entityCollection = new HashSet<EntityInterface>();
    
    
    List<MatchedClassEntry> matchedClassEntries = new ArrayList<MatchedClassEntry>();

    /**
     * Collection of matched attributes.
     */
    Set<AttributeInterface> attributeCollection = new HashSet<AttributeInterface>();

    /**
     * @return Returns the entityCollection.
     */
    public Set<EntityInterface> getEntityCollection() {
        return entityCollection;
    }
    
    /**
     * @return
     */
    public Set<EntityInterface> getSortedEntityCollection() {
        Collections.sort(matchedClassEntries,new MatchedClassEntryCompator());
        Set<EntityInterface> entities = new LinkedHashSet<EntityInterface>();
        for(MatchedClassEntry entry : matchedClassEntries) {
            entities.add(entry.getMatchedEntity());
        }
        return entities;
        
    }

    /**
     * @param entityCollection The entityCollection to set.
     */
    public void setEntityCollection(Set<EntityInterface> entityCollection) {
        this.entityCollection = entityCollection;
    }

    /**
     * @return Returns the matched Attribute Collection.
     */
    public Set<AttributeInterface> getAttributeCollection() {
        return attributeCollection;
    }

    /**
     * @param attributeCollection The attribute Collection to set.
     */
    public void setMatchedAttributeCollection(Set<AttributeInterface> attributeCollection) {
        this.attributeCollection = attributeCollection;
    }

    /**
     * @param entity Entity to add
     */
    public void addEntity(EntityInterface entity) {
        entityCollection.add(entity);
    }

    /**
     * @param attribute attribute to add
     */
    public void addAttribute(AttributeInterface attribute) {
        attributeCollection.add(attribute);
    }
    
    public void addMatchedClassEntry(MatchedClassEntry matchedClassEntry) {
        int index = matchedClassEntries.indexOf(matchedClassEntry);
        if (index  != -1 ) {
            MatchedClassEntry existingMatchedClassEntry = matchedClassEntries.get(index);
            existingMatchedClassEntry.merge(matchedClassEntry);
        } else {
            matchedClassEntries.add(matchedClassEntry);
        }
    }

    /**
     * @return Returns the matchedClassEntries.
     */
    public List<MatchedClassEntry> getMatchedClassEntries() {
        return matchedClassEntries;
    }

    /**
     * @param matchedClassEntries The matchedClassEntries to set.
     */
    public void setMatchedClassEntries(List<MatchedClassEntry> matchedClassEntries) {
        this.matchedClassEntries = matchedClassEntries;
    }
    
    
    
}