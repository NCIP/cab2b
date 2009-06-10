/**
 * <p>Title: EntityCacheBusinessInterface Interface>
 * <p>Description:	Entity Cache business interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.common.entityCache;

import java.util.Collection;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.beans.MatchedClass;

/**
 * Entity Cache business interface.
 * @author gautam_shetty
 */
public interface EntityCacheBusinessInterface extends BusinessInterface
{
    /**
     * Returns the Entity objects whose fields match with the respective not null 
     * fields in the passed entity object.
     * @param entity The entity object.
     * @return the Entity objects whose fields match with the respective not null 
     * fields in the passed entity object.
     */
    public MatchedClass getEntityOnEntityParameters(Collection entityCollection);
    
    /**
     * Returns the Entity objects whose Attribute fields match with the respective not null 
     * fields in the passed Attribute object.
     * @param entity The entity object.
     * @return the Entity objects whose Attribute fields match with the respective not null 
     * fields in the passed Attribute object.
     */
    public MatchedClass getEntityOnAttributeParameters(Collection attributeCollection);
}