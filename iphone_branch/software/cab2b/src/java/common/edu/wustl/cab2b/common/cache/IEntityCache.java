/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.cache;

import java.rmi.RemoteException;
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
     * @throws RemoteException 
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
    public MatchedClass getCategories(Collection<EntityInterface> entityCollection);

    /**
     * Returns the Entity objects whose attributes's source classes fields match with the respective not null 
     * fields in the passed entity object.
     * @param entity The entity object.
     * @return the Entity objects whose attributes's source classes fields match with the respective not null 
     * fields in the passed entity object.
     */
    public MatchedClass getCategoriesAttributes(Collection<AttributeInterface> attributeCollection);
    
    
    /**
     * This method adds entity and its other details like associaion and permissible values into the cache.
     * 
     * @param entity entity to add
     */
    public void addEntityToCache(EntityInterface entity);
}