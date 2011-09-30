/**
 * 
 */
package edu.wustl.common.querysuite.metadata.associations.impl;

import edu.wustl.common.querysuite.metadata.associations.IAssociation;

/**
 * @author chetan_patil
 * @created Aug 10, 2007, 9:05:02 PM
 * 
 * @hibernate.class table="QUERY_MODEL_ASSOCIATION"
 * @hibernate.cache usage="read-write"
 */
public abstract class ModelAssociation implements IAssociation {

    /**
     * Internally generated identifier.
     */
    protected Long id;

    /**
     * Returns the identifier assigned to BaseQueryObject.
     * @return a unique id assigned to the Condition.
     * 
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="MODEL_ASSOCIATION_SEQ"
     */
    final public Long getId() {
        return id;
    }

    /**
     * Sets the given id to the domain object
     */
    final public void setId(Long id) {
        this.id = id;
    }
}
