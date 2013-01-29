/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.cache;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.beans.MatchedClassEntry;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * This is an abstract class for caching metadata. 
 * It holds the data needed structures, methods to populate those and public methods to access cache.
 * 
 * @author Chandrakant Talele
 * @author gautam_shetty
 * @author Rahul Ner
 */
public abstract class AbstractEntityCache implements IEntityCache, Serializable {
    private static final long serialVersionUID = 1234567890L;

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(AbstractEntityCache.class);

    /**
     * List of all the categories loaded in caB2B local database.  
     */
    protected List<Category> categories = new ArrayList<Category>(0);

    /**
     * Set of all the entity groups loaded as metadata in caB2B.
     */
    private Set<EntityGroupInterface> cab2bEntityGroups;

    /**
     * The EntityCache object. Needed for singleton
     */
    protected static AbstractEntityCache entityCache = null;

    /**
     * Map with KEY as dynamic extension Entity's identifier and Value as Entity object
     */
    protected Map<Long, EntityInterface> idVsEntity = new HashMap<Long, EntityInterface>();

    /**
     * Map with KEY as dynamic extension Association's identifier and Value as Association object
     */
    protected Map<Long, AssociationInterface> idVsAssociation = new HashMap<Long, AssociationInterface>();

    /**
     * Map with KEY as dynamic extension Attribute's identifier and Value as Attribute object
     */
    protected Map<Long, AttributeInterface> idVsAttribute = new HashMap<Long, AttributeInterface>();

    /**
     * This map holds all the original association. Associations which are
     * replicated by cab2b are not present in this map Key : String to identify
     * a parent association uniquely.Generated by
     * {InheritanceUtil#generateUniqueId(AssociationInterface)} Value : Original
     * association for given string identifier
     */
    protected Map<String, AssociationInterface> originalAssociations = new HashMap<String, AssociationInterface>();

    /**
     * Map with KEY as a permissible value (PV) and VALUE as its Entity. This is
     * needed because there is no back pointer from PV to Entity
     */
    protected Map<PermissibleValueInterface, EntityInterface> permissibleValueVsEntity = new HashMap<PermissibleValueInterface, EntityInterface>();

    /**
     * This method gives the singleton cache object. If cache is not present then it throws {@link UnsupportedOperationException}
     * @return The singleton cache object.
     */
    public static AbstractEntityCache getCache() {
        if (entityCache == null) {
            throw new UnsupportedOperationException("Cache not present.");
        }
        return entityCache;
    }

    /**
     * Private default constructor. To restrict the user from instantiating
     * explicitly.
     * @throws RemoteException 
     */
    protected AbstractEntityCache() {
        refreshCache();
    }

    /**
     * Refreshes the entity cache.
     * @throws RemoteException 
     */
    public final void refreshCache() {
        logger.info("Initialising cache, this may take few minutes...");

        Collection<EntityGroupInterface> entityGroups = null;
        try {
            entityGroups = getCab2bEntityGroups();
        } catch (RemoteException e) {
            //TODO: Handle this execption properly.
            logger.error("Error while collecting caB2B entity groups. Error: " + e.getMessage());
        }
        createCache(entityGroups);
        
        logger.info("Initialising cache DONE");
    }

    /**
     * Initializes the data structures by processing one entity group at a time.
     * @param entityGroups Entitygroups to cache
     */
    private void createCache(Collection<EntityGroupInterface> entityGroups) {
        Set<EntityGroupInterface> entityGroupsSet = new HashSet<EntityGroupInterface>();
        for (EntityGroupInterface entityGroup : entityGroups) {
            entityGroupsSet.add(entityGroup);
            for (EntityInterface entity : entityGroup.getEntityCollection()) {
                addEntityToCache(entity);
            }
        }
        cab2bEntityGroups = Collections.unmodifiableSet(entityGroupsSet);
    }

    /**
     * Adds all attribute of given entity into cache
     * @param entity Entity to process
     */
    private void createAttributeCache(EntityInterface entity) {
        for (AttributeInterface attribute : entity.getAttributeCollection()) {
            idVsAttribute.put(attribute.getId(), attribute);
        }
    }

    /**
     * Adds all associations of given entity into cache
     * @param entity Entity to process
     */
    private void createAssociationCache(EntityInterface entity) {
        for (AssociationInterface association : entity.getAssociationCollection()) {
            idVsAssociation.put(association.getId(), association);
            if (!Utility.isInherited(association)) {
                originalAssociations.put(Utility.generateUniqueId(association), association);
            }
        }
    }

    /**
     * Adds permissible values of all the attributes of given entity into cache
     * @param entity Entity whose permissible values are to be processed
     */
    private void createPermissibleValueCache(EntityInterface entity) {
        for (AttributeInterface attribute : entity.getAttributeCollection()) {
            for (PermissibleValueInterface value : Utility.getPermissibleValues(attribute)) {
                permissibleValueVsEntity.put(value, entity);
            }
        }
    }

    /**
     * @see edu.wustl.cab2b.common.entityCache.IEntityCache#getEntityOnEntityParameters(java.util.Collection)
     */
    public MatchedClass getEntityOnEntityParameters(Collection<EntityInterface> patternEntityCollection) {
        MatchedClass matchedClass = new MatchedClass();
        for (EntityInterface cachedEntity : idVsEntity.values()) {
            for (EntityInterface patternEntity : patternEntityCollection) {
                MatchedClassEntry matchedClassEntry = CompareUtil.compare(cachedEntity, patternEntity);
                if (matchedClassEntry != null) {
                    matchedClass.addEntity(cachedEntity);
                    matchedClass.addMatchedClassEntry(matchedClassEntry);
                }
            }
        }
        return matchedClass;
    }

    /**
     * Returns the Entity objects whose Attribute fields match with the
     * respective not null fields in the passed Attribute object.
     * 
     * @param entity The entity object.
     * @return the Entity objects whose Attribute fields match with the
     *         respective not null fields in the passed Attribute object.
     */
    public MatchedClass getEntityOnAttributeParameters(Collection<AttributeInterface> patternAttributeCollection) {
        MatchedClass matchedClass = new MatchedClass();
        for (EntityInterface entity : idVsEntity.values()) {
            for (AttributeInterface cachedAttribute : entity.getAttributeCollection()) {
                for (AttributeInterface patternAttribute : patternAttributeCollection) {
                    MatchedClassEntry matchedClassEntry = CompareUtil.compare(cachedAttribute, patternAttribute);
                    if (matchedClassEntry != null) {
                        matchedClass.addMatchedClassEntry(matchedClassEntry);
                        matchedClass.addAttribute(cachedAttribute);
                        matchedClass.addEntity(cachedAttribute.getEntity());
                    }
                }
            }
        }
        return matchedClass;
    }

    /**
     * Returns the Entity objects whose Permissible value fields match with the
     * respective not null fields in the passed Permissible value object.
     * 
     * @param entity The entity object.
     * @return the Entity objects whose Permissible value fields match with the
     *         respective not null fields in the passed Permissible value
     *         object.
     */
    public MatchedClass getEntityOnPermissibleValueParameters(
                                                              Collection<PermissibleValueInterface> patternPermissibleValueCollection) {
        MatchedClass matchedClass = new MatchedClass();
        for (PermissibleValueInterface cachedPermissibleValue : permissibleValueVsEntity.keySet()) {
            for (PermissibleValueInterface patternPermissibleValue : patternPermissibleValueCollection) {
                EntityInterface cachedEntity = permissibleValueVsEntity.get(cachedPermissibleValue);
                MatchedClassEntry matchedClassEntry = CompareUtil.compare(cachedPermissibleValue,
                                                                          patternPermissibleValue, cachedEntity);
                if (matchedClassEntry != null) {
                    matchedClass.addEntity(cachedEntity);
                    matchedClass.addMatchedClassEntry(matchedClassEntry);
                }
            }
        }
        return matchedClass;

    }

    /**
     * Returns the Entity for given Identifier
     * 
     * @param id Id of the entity
     * @return Actual Entity for given id.
     */
    public EntityInterface getEntityById(Long id) {
        EntityInterface entity = idVsEntity.get(id);
        if (entity == null) {
            throw new RuntimeException("Entity with given id is not present in cache : " + id);
        }
        return entity;
    }

    /**
     * Checks if entity with given id is present in cache.
     * 
     * @param id the entity id
     * @return <code>true</code> - if entity with given id is present in
     *         cache; <code>false</code> otherwise.
     */
    public boolean isEntityPresent(Long id) {
        return idVsEntity.containsKey(id);
    }

    /**
     * Returns the Attribute for given Identifier
     * 
     * @param id Id of the Attribute
     * @return Actual Attribute for given id.
     */
    public AttributeInterface getAttributeById(Long id) {
        AttributeInterface attribute = idVsAttribute.get(id);
        if (attribute == null) {
            throw new RuntimeException("Attribute with given id is not present in cache : " + id);
        }
        return attribute;
    }

    /**
     * Returns the Association for given Identifier
     * 
     * @param id Id of the Association
     * @return Actual Association for given id.
     */
    public AssociationInterface getAssociationById(Long id) {
        AssociationInterface association = idVsAssociation.get(id);
        if (association == null) {
            throw new RuntimeException("Association with given id is not present in cache : " + id);
        }
        return association;
    }

    /**
     * Returns the Association for given string. 
     * Passed string MUST be of format specified in {@link Utility#generateUniqueId(AssociationInterface)}
     * @param uniqueStringIdentifier unique String Identifier
     * @return Actual Association for given string identifier.
     */
    public AssociationInterface getAssociationByUniqueStringIdentifier(String uniqueStringIdentifier) {
        AssociationInterface association = originalAssociations.get(uniqueStringIdentifier);
        if (association == null) {
            throw new RuntimeException("Association with given uniqueStringIdentifier is not present in cache : "
                    + uniqueStringIdentifier);
        }
        return association;
    }

    /**
     * @see edu.wustl.cab2b.common.entityCache.IEntityCache#addEntity(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    public void addEntityToCache(EntityInterface entity) {
        idVsEntity.put(entity.getId(), entity);
        createAttributeCache(entity);
        createAssociationCache(entity);
        createPermissibleValueCache(entity);
    }

    /**
     * Returns all the entity groups registered with the instance of cache.
     * @return Returns all the registered entity groups
     */
    public Collection<EntityGroupInterface> getEntityGroups() {
        return cab2bEntityGroups;
    }

    /**
     * This method returns the entity group of given name from cache.
     * @param name name of the entity group
     * @return enttity group
     */
    public EntityGroupInterface getEntityGroupByName(String name) {
        for (EntityGroupInterface group : cab2bEntityGroups) {
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }

    /**
     * This method returns all the entity groups which are to be cached. 
     * These will typically be the metadata entitygroups present is local caB2B database. 
     * It should not return entitygroups for datalist or experiment
     * @return Returns the entity groups
     * @throws RemoteException 
     */
    protected abstract Collection<EntityGroupInterface> getCab2bEntityGroups() throws RemoteException;
}