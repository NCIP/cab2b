package edu.wustl.cab2b.common.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.wustl.cab2b.common.IdName;

/**
 * This is a DataList domain object. This will be mapped to "datalist" table.
 * 
 * @hibernate.joined-subclass table="CAB2B_DATA_LIST"
 * @hibernate.joined-subclass-key column="DL_ID"
 * 
 * @author chetan_bh
 */
public class DataListMetadata extends AdditionalMetadata implements Serializable {
    private static final long serialVersionUID = 1234567890L;

    private Set<Long> entityIds;
    
    private Long rootEntityId;

    private Set<IdName> entitiesNames;

    /**
     * boolean to indicate this data list is for the custom data category.
     */
    private boolean customDataCategory;

    public DataListMetadata() {
        customDataCategory = false;
    }

    /**
     * Returns the entity id of this datalist.
     * 
     * @hibernate.set name="entityIds" table="CAB2B_DATALIST_ENTITY" cascade ="none" 
     * @hibernate.collection-key column="DATALIST_METADATA_ID"
     * @hibernate.collection-element column="ENTITY_ID" type="long" not-null="true"
     * @return name of the domain object.
     */
    
     
    
    public Set<Long> getEntityIds() {
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

    public void addEntityInfo(Long id, String name, Long originalEntityId) {
        getEntitiesNames().add(new IdName(id, name, originalEntityId));
        addEntityId(id);
    }

    public Set<IdName> getEntitiesNames() {
        if (entitiesNames == null) {
            entitiesNames = new HashSet<IdName>();
        }
        return entitiesNames;
    }

    /**
     *@hibernate.property name="isCustomDataCategory" type="boolean" column="CUSTOM_DATA_CATEGORY"
     *@return Returns the isCustomDataCategory.
     */
    public boolean isCustomDataCategory() {
        return customDataCategory;
    }

    /**
     * @param isCustomDataCategory The isCustomDataCategory to set.
     */
    public void setCustomDataCategory(boolean isCustomDataCategory) {
        this.customDataCategory = isCustomDataCategory;
    }

	/**
	 * @return Returns the rootEntityId.
	 * @hibernate.property name="rootEntityId" type="long" column="ROOT_ENTITY_ID"
	 */
	public Long getRootEntityId() {
		return rootEntityId;
	}
	

	/**
	 * @param rootEntityId The rootEntityId to set.
	 */
	public void setRootEntityId(Long rootEntityId) {
		this.rootEntityId = rootEntityId;
	}

}