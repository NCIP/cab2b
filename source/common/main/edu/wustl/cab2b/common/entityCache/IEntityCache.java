/**
 * <p>Title: IEntityCache Interface>
 * <p>Description:  Entity Cache interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author hrishikesh_rajpathak
 */
package edu.wustl.cab2b.common.entityCache;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.cab2b.common.beans.MatchedClass;

/**
 * @author gautam_shetty
 */
public interface IEntityCache {
    /**
     * Refreshes the entity cache.
     */
    public void refreshCache();

    /**
     * Returns the Entity objects whose fields match with the respective not null 
     * fields in the passed entity object.
     * @param entity The entity object.
     * @return the Entity objects whose fields match with the respective not null 
     * fields in the passed entity object.
     */
    public MatchedClass getEntityOnEntityParameters(Collection<EntityInterface> entityCollection);

    /**
     * Returns the Entity objects whose Attribute fields match with the respective not null 
     * fields in the passed Attribute object.
     * @param entity The entity object.
     * @return the Entity objects whose Attribute fields match with the respective not null 
     * fields in the passed Attribute object.
     */
    public MatchedClass getEntityOnAttributeParameters(Collection<AttributeInterface> attributeCollection);

    /**
     * Returns the Entity objects whose Permissible Value fields match with the respective not null 
     * fields in the passed Attribute object.
     * @param entity The entity object.
     * @return the Entity objects whose Permissible Value fields match with the respective not null 
     * fields in the passed Permissible Value object.
     */
    public MatchedClass getEntityOnPermissibleValueParameters(Collection<PermissibleValueInterface> PVCollection);

    /**
     * Returns the Entity objects whose source classes fields match with the respective not null 
     * fields in the passed entity object.
     * @param entity The entity object.
     * @return the Entity objects whose source classes fields match with the respective not null 
     * fields in the passed entity object.
     */
    public MatchedClass getCategories(Collection <EntityInterface> entityCollection);

    /**
     * Returns the Entity objects whose attributes's source classes fields match with the respective not null 
     * fields in the passed entity object.
     * @param entity The entity object.
     * @return the Entity objects whose attributes's source classes fields match with the respective not null 
     * fields in the passed entity object.
     */
    public MatchedClass getCategoriesAttributes(Collection<AttributeInterface> attributeCollection);
}