/*L
 * Copyright Georgetown University, Washington University.
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



import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;


/**
 * 
 * @author shital_lawhale
 *@hibernate.class table="DYEXTN_FORM_CONTEXT"
 */
public class FormContext  extends DynamicExtensionBaseDomainObject
{
    
    private static final long serialVersionUID = 1L;
    
    protected Long id;
    protected String studyFormLabel;
    protected Integer noOfEntries;
   
    protected Collection entityMapConditionCollection = new HashSet();
    protected Collection entityMapRecordCollection = new HashSet();
    protected  EntityMap entityMap ;
    
  protected Boolean isInfiniteEntry;  
    

    /**
     * @return Returns the studyFormLabel.
     *  @hibernate.property name="isInfiniteEntry" column="IS_INFINITE_ENTRY" type="boolean" length="5" 
     */ 
       
    public Boolean getIsInfiniteEntry()
    {
        return isInfiniteEntry;
    }

    
    public void setIsInfiniteEntry(Boolean isInfiniteEntry)
    {
        this.isInfiniteEntry = isInfiniteEntry;
    }
    /**
     * @return 
     * @hibernate.many-to-one column="ENTITY_MAP_ID" class="edu.common.dynamicextensions.domain.integration.EntityMap" constrained="true"
     */
    
    public EntityMap getEntityMap()
    {
        return entityMap;
    }
    
    public void setEntityMap(EntityMap entityMap)
    {
        this.entityMap = entityMap;
    }
     
    
    /**
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_FORM_CONTEXT_SEQ"
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
     * @return Returns the studyFormLabel.
     *  @hibernate.property name="studyFormLabel" column="STUDY_FORM_LABEL" type="string" length="255"
     */ 
   
    public String getStudyFormLabel()
    {
        return studyFormLabel;
    }

    
    public void setStudyFormLabel(String studyFormLabel)
    {
        this.studyFormLabel = studyFormLabel;
    }
    

    /**
     * @return Returns the typeId.
     *  @hibernate.property name="noOfEntries" column="NO_OF_ENTRIES" type="int" length="30"
     */ 
   
    public Integer getNoOfEntries()
    {
        return noOfEntries;
    }

    
    public void setNoOfEntries(Integer noOfEntries)
    {
        this.noOfEntries = noOfEntries;
    }
    
    
    /**
     * @return Returns the entityMapConditionCollection.
     * @hibernate.set name="entityMapConditionCollection" table="DYEXTN_ENTITY_MAP_CONDNS" cascade="save-update"
     * inverse="true" lazy="false"
     * @hibernate.collection-key column="FORM_CONTEXT_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.integration.EntityMapCondition"
     */
    
    public Collection getEntityMapConditionCollection()
    {
        return entityMapConditionCollection;
    }
    
    public void setEntityMapConditionCollection(
            Collection entityMapConditionCollection)
    {
        this.entityMapConditionCollection = entityMapConditionCollection;
    }

    
    public void setAllValues(AbstractActionForm arg0) throws AssignDataException 
    {
        
    }

    /**
     * @return Returns the entityMapRecordCollection.
     * @hibernate.set name="entityMapRecordCollection" table="DYEXTN_ENTITY_MAP_RECORD" cascade="save-update"
     * inverse="true" lazy="false"
     * @hibernate.collection-key column="FORM_CONTEXT_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.integration.EntityMapRecord"
     * @return
     */
    public Collection getEntityMapRecordCollection()
    {
        return entityMapRecordCollection;
    }

    
    public void setEntityMapRecordCollection(Collection entityMapRecordCollection)
    {
        this.entityMapRecordCollection = entityMapRecordCollection;
    }
    
    
    

}
