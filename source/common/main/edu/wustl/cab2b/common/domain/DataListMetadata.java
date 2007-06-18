package edu.wustl.cab2b.common.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a DataList domain object. This will be mapped to "datalist" table.
 * 
 * @hibernate.joined-subclass table="datalist"
 * @hibernate.joined-subclass-key column="DL_ID"
 * 
 * @author chetan_bh
 */
public class DataListMetadata extends AdditionalMetadata implements Serializable {
    private static final long serialVersionUID = 1234567890L;

    private Set<Long> entityIds;

    /**
     * Returns the entity id of this datalist.
     * 
     * @hibernate.property name="entityId" type="long" column="ENT_ID"
     * @return name of the domain object.
     */
    public Set<Long> getEntityIds() {
        // TODO hbm and stuff...
        if (entityIds == null) {
            entityIds = new HashSet<Long>();
        }
        return entityIds;
    }

    public void setEntityIds(Set<Long> entityIds) {
        this.entityIds = entityIds;
    }

    public void addEntityId(Long entityId) {
        getEntityIds().add(entityId);
    }
}