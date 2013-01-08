/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author 
 *@version 1.0
 */ 
package edu.common.dynamicextensions.domain.integration;


import java.util.Date;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;


/**
 * @author sandeep_chinta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * @hibernate.class table="DYEXTN_ENTITY_MAP_RECORD"
 */

public class EntityMapRecord  extends DynamicExtensionBaseDomainObject
{
    
    /**Unique id of the object.*/
    private Long id;
    
    //private Long entityMapId;

    private Long staticEntityRecordId;
    
    private Long dynamicEntityRecordId;
    
    protected String createdBy;
    
    private Date createdDate;
    
    private Date modifiedDate;
    
    /**
     * Status of the link as attached detached.
     */
    private String linkStatus;
    
    private FormContext formContext;
    
    
    /**
     * 
     * @return
     * @hibernate.many-to-one column="FORM_CONTEXT_ID" class="edu.common.dynamicextensions.domain.integration.FormContext" constrained="true"
     */    
    
    public FormContext getFormContext()
    {
        return formContext;
    }
    
    public void setFormContext(FormContext formContext)
    {
        this.formContext = formContext;
    }
    /*
     * @hibernate.property name="entityMapId" column="ENTITY_MAP_ID" type="long" length="30"
     * @return Returns the entityMapId.
     
    public Long getEntityMapId()
    {
        return entityMapId;
    }
    
     * @param entityMapId The entityMapId to set.
     
    public void setEntityMapId(Long entityMapId)
    {
        this.entityMapId = entityMapId;
    }
    */
       
    public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
    {
        // TODO Auto-generated method stub
        
    }
   
    public Long getSystemIdentifier()
    {
        // TODO Auto-generated method stub
        return id;
    }
    
    public void setSystemIdentifier(Long systemIdentifier)
    {
        this.id = systemIdentifier;
    }

    /**
     * Returns the systemIdentifier assigned to user.
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_ENTITY_RECORD_SEQ"
     * @return Returns the systemIdentifier.
     */
    public Long getId()
    {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
  
    /**
     * @return
     * @hibernate.property name="staticEntityRecordId" column="STATIC_ENTITY_RECORD_ID" type="long" length="30"
     * 
     */
    public Long getStaticEntityRecordId()
    {
        return staticEntityRecordId;
    }
    /**
     * @param staticEntityRecordId The staticEntityRecordId to set.
     */
    public void setStaticEntityRecordId(
            Long staticEntityRecordId)
    {
        this.staticEntityRecordId = staticEntityRecordId;
    }
 
    /**
     * @hibernate.property name="linkStatus" type="string" column="STATUS" length="10"
     * @return Status of the link as attached/detached etc
     */
    public String getLinkStatus()
    {
        return this.linkStatus;
    }

    /**
     *  @param linkStatus : Status of the link as attached/detached
     */
    public void setLinkStatus(String linkStatus)
    {
        this.linkStatus = linkStatus;
    }
  
    /**
     * @return Returns the dynamicEntityRecordId.
     *  @hibernate.property name="dynamicEntityRecordId" column="DYNAMIC_ENTITY_RECORD_ID" type="long" length="30"
     */
    public Long getDynamicEntityRecordId()
    {
        return dynamicEntityRecordId;
    }
    /**
     * @param dynamicEntityRecordId The dynamicEntityRecordId to set.
     */
    public void setDynamicEntityRecordId(Long dynamicEntityRecordId)
    {
        this.dynamicEntityRecordId = dynamicEntityRecordId;
    }
    
    /**
     * @return Returns the modifiedDate.
     * @hibernate.property name="modifiedDate" type="date" column="MODIFIED_DATE"
     */
    public Date getModifiedDate()
    {
        return modifiedDate;
    }
    /**
     * @param createdDate The modifiedDate to set.
     */
    public void setModifiedDate(Date modifiedDate)
    {
        this.modifiedDate = modifiedDate;
    }
    
    /**
     * @return Returns the createdDate.
     * @hibernate.property name="createdDate" type="date" column="CREATED_DATE"
     */
    public Date getCreatedDate()
    {
        return createdDate;
    }
    /**
     * @param createdDate The createdDate to set.
     */
    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }
    /**
     * @return Returns the createdBy.
     * @hibernate.property name="createdBy" type="string" column="CREATED_BY" length="255"
     */
    public String getCreatedBy()
    {
        return createdBy;
    }
    /**
     * @param createdBy The createdBy to set.
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }
}
