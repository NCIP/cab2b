package edu.wustl.cab2b.common.domain;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

 /**
  * A top level base class for all different kinds of Experiments, and Categories.
  *  
  * @hibernate.class table="CAB2B_ABSTRACT_DOMAIN_OBJECT"
  * @author 
  */
public class AbstractDomainObject implements java.io.Serializable {
    
	private static final long serialVersionUID = 1234567890L;

    /**
     * A unique identifier.
     */
    protected java.lang.Long id;
    
    
    /**
     * Returns the identifier assigned to AbstractDomainObject.
     * 
     * @hibernate.id name="id" column="ADO_ID" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="ADO_SEQ"
     * @return a unique id assigned to the domain object.
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the Identifier.
     * @param id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }

    /**
     * Activity status of the domain object.
     */
    protected java.lang.String activityStatus;
    
    /**
     * Returns the activity status of the object.
     * 
     * @hibernate.property name="activityStatus" type="string" column="ADO_ACTIVITY_STATUS" length="50"
     * @return activity status of the object.
     */
    public java.lang.String getActivityStatus() {
        return activityStatus;
    }
    
    /**
     * Sets the activity status.
     * @param activityStatus
     * 
     * 
     * 
     * 
     */
    public void setActivityStatus(java.lang.String activityStatus) {
        this.activityStatus = activityStatus;
    }


    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj instanceof AbstractDomainObject) {
            AbstractDomainObject c = (AbstractDomainObject)obj;
            Long thisId = getId();

            if (thisId != null && thisId.equals(c.getId())) {
                eq = true;
            }

        }
        return eq;
    }

    public int hashCode() {
        int h = 0;

        if (getId() != null) {
            h += getId().hashCode();
        }

        return h;
    }
}
