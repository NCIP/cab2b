package edu.wustl.cab2b.common.domain;

import java.io.Serializable;


/**
 * This is a DataList domain object. This will be mapped to "datalist" table.
 * 
 * @hibernate.joined-subclass table="datalist"
 * @hibernate.joined-subclass-key column="DL_ID"
 * 
 * @author chetan_bh
 */
public class DataListMetadata extends AdditionalMetadata implements Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	private Long entityId;

	/**
     * Returns the entity id of this datalist.
     * @hibernate.property name="entityId" type="long" column="ENT_ID"
     * @return name of the domain object.
     */
	public Long getEntityId()
	{
		return entityId;
	}

	
	public void setEntityId(Long entityId)
	{
		this.entityId = entityId;
	}
	
	
	
}