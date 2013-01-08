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


import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;


/**
 * @author shital_lawhale
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * @hibernate.class table="DYEXTN_ENTITY_MAP_CONDNS"
 */

public class EntityMapCondition  extends DynamicExtensionBaseDomainObject
{
    
    /**Unique id of the object.*/
    private Long id;      
    private Long typeId;    
    private Long staticRecordId;
    private FormContext formContext ;

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
     * @hibernate.generator-param name="sequence" value="DYEXTN_ENTITY_MAP_CONDN_SEQ"
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
     * @hibernate.property name="staticRecordId" column="STATIC_RECORD_ID" type="long" length="30"
     * 
     */
    public Long getStaticRecordId()
    {
        return staticRecordId;
    }
    /**
     * @param collectionProtocol The collectionProtocol to set.
     */
    public void setStaticRecordId(
            Long collectionProtocol)
    {
        this.staticRecordId = collectionProtocol;
    }
    
    /**
     * @return Returns the typeId.
     *  @hibernate.property name="typeId" column="TYPE_ID" type="long" length="30"
     */ 
   
    public Long getTypeId()
    {
        return typeId;
    }
    /**
     * @param typeId The typeId to set.
     */
    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

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
 
}
