/**
 * <p>Title: MatchedClass Class>
 * <p>Description:	This Class encapsulates the searched Entity classes and 
 * the matched attributes in advance search.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.common.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * This Class encapsulates the searched Entity classes and 
 * the matched attributes in advance search.
 * @author gautam_shetty
 */
public class MatchedClass implements Serializable, IMatchedClass
{
    private static final long serialVersionUID = 1234567890L;
    
    /**
     * Collection of matched entities.
     */
    Set<EntityInterface> entityCollection = new HashSet<EntityInterface>();
    
    /**
     * Collection of matched attributes.
     */
    Set<AttributeInterface> matchedAttributeCollection = new HashSet<AttributeInterface>();
    
    /**
     * @return Returns the entityCollection.
     */
    public Set<EntityInterface> getEntityCollection()
    {
        return entityCollection;
    }
    
    /**
     * @param entityCollection The entityCollection to set.
     */
    public void setEntityCollection(Set<EntityInterface> entityCollection)
    {
        this.entityCollection = entityCollection;
    }
    
    /**
     * @return Returns the matchedAttributeCollection.
     */
    public Set<AttributeInterface> getMatchedAttributeCollection()
    {
        return matchedAttributeCollection;
    }
    
    /**
     * @param matchedAttributeCollection The matchedAttributeCollection to set.
     */
    public void setMatchedAttributeCollection(Set<AttributeInterface> matchedAttributeCollection)
    {
        this.matchedAttributeCollection = matchedAttributeCollection;
    }
}