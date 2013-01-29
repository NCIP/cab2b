/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.util.global.Constants.InheritanceStrategy;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;

/**
 * @author prafull_kadam
 * @author chetan_patil
 * @created Aug 9, 2007, 4:02:13 PM
 * 
 * @hibernate.class table="QUERY_QUERY_ENTITY"
 * @hibernate.cache usage="read-write"
 */
public class QueryEntity extends BaseQueryObject implements IQueryEntity {
    private static final long serialVersionUID = 1L;

    protected EntityInterface entityInterface;

    /**
     * Default Constructor
     */
    public QueryEntity() {

    }

    /**
     * TO initialize entityInterface object for this object.
     * 
     * @param entityInterface The Dynamic Extension entity reference associated
     *            with this object.
     */
    public QueryEntity(EntityInterface entityInterface) {
        this.entityInterface = entityInterface;
    }

    /**
     * Returns the identifier assigned to BaseQueryObject.
     * 
     * @return a unique id assigned to the Condition.
     * 
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="QUERY_ENTITY_SEQ"
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the entityInterface
     */
    public EntityInterface getEntityInterface() {
        return entityInterface;
    }

    /**
     * @param entityInterface the entityInterface to set
     */
    public void setEntityInterface(EntityInterface entityInterface) {
        this.entityInterface = entityInterface;
    }

    /**
     * This method return the Dynamic Extension Entity reference.
     * 
     * @return The Dynamic Extension Entity reference corresponding to the
     *         QueryEntity.
     * @see edu.wustl.common.querysuite.queryobject.IQueryEntity#getDynamicExtensionsEntity()
     */
    public EntityInterface getDynamicExtensionsEntity() {
        return entityInterface;
    }

    public void setDynamicExtensionsEntity(EntityInterface entityInterface) {
        setEntityInterface(entityInterface);
    }

    /**
     * To check whether two objects are equal.
     * 
     * @param obj reference to the object to be checked for equality.
     * @return true if entityInterface of object is equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null && this.getClass() == obj.getClass()) {
            QueryEntity theObj = (QueryEntity) obj;
            if (entityInterface != null && entityInterface.equals(theObj.entityInterface)) {
                return true;
            }
        }
        return false;
    }

    /**
     * To get the HashCode for the object. It will be calculated based on
     * entityInterface.
     * 
     * @return The hash code value for the object.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(entityInterface).toHashCode();
    }

    /**
     * @return String representation of this object
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return entityInterface.getName();
    }

    /**
     * TO check whether the given query entity is having matching Entity. This
     * is used to identify Pseudo-AND condition. Returns true if the two
     * entities belongs to the same class heirarchy. i.e. class heirarchy having
     * TABLE_PER_HEIRARCHY as inheritance strategy.
     * 
     * @param queryEntity The QueryEntity to check.
     * @return true if the two entities can be pseudoAnded.
     */
    public boolean isPseudoAndedEntity(IQueryEntity queryEntity) {
        EntityInterface theEntityInterface = queryEntity.getDynamicExtensionsEntity();
        if (entityInterface.equals(theEntityInterface)) {
            return true;
        }
        // check for the Parent class heirarchy.
        // It will check whether two entities belongs to the same Class
        // heirarchy having TABLE_PER_HEIRARCHY as inheritance strategy.
        EntityInterface parentEntity = entityInterface.getParentEntity();
        EntityInterface theParentEntity = theEntityInterface.getParentEntity();
        if (parentEntity != null && theParentEntity != null) {
            Set<EntityInterface> parentHeirarchy = getParentHeirarchy(entityInterface);
            // retaining common parent entities
            parentHeirarchy.retainAll(getParentHeirarchy(theEntityInterface));

            if (!parentHeirarchy.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * TO get the parent heirarchy of the given entity. The returned set will
     * contains all the parent entities having TABLE_PER_HEIRARCHY as
     * Inheritance strategy for their derived entity.
     * 
     * @param entityInterface The reference to Entity.
     * @return the set of Parent entities with inheritance strategy as
     *         TABLE_PER_HEIRARCHY.
     */
    private Set<EntityInterface> getParentHeirarchy(EntityInterface entityInterface) {
        Set<EntityInterface> set = new HashSet<EntityInterface>();
        // Iterating on parent heirarchy till the inheritance strategy is
        // TABLE_PER_HEIRARCHY.
        do {
            set.add(entityInterface);
            if (entityInterface.getInheritanceStrategy().equals(InheritanceStrategy.TABLE_PER_HEIRARCHY)) {
                entityInterface = entityInterface.getParentEntity();
            } else {
                break;
            }

        } while (entityInterface != null);

        return set;
    }
}
