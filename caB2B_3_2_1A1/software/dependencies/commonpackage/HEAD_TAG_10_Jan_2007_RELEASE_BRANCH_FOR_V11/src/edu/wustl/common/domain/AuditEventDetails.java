
package edu.wustl.common.domain;


/**
 * @hibernate.class table="CATISSUE_AUDIT_EVENT_DETAILS"
 **/
public class AuditEventDetails implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	protected Long id;
	protected String elementName;
	protected String previousValue;
	protected String currentValue;

	protected AuditEventLog auditEventLog;
	/**
     * @return Long System generated unique identifier.
     * @see #setId(Long)
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native" 
     * @hibernate.generator-param name="sequence" value="CATISSUE_AUDIT_EVENT_DET_SEQ"
     */
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @hibernate.property name="elementName" type="string"
     * column="ELEMENT_NAME" length="150" 
	 **/
	public String getElementName()
	{
		return elementName;
	}

	public void setElementName(String elementName)
	{
		this.elementName = elementName;
	}

	/**
	 * @hibernate.property name="previousValue" type="string"
     * column="PREVIOUS_VALUE" length="150" 
	 **/
	public String getPreviousValue()
	{
		return previousValue;
	}

	public void setPreviousValue(String previousValue)
	{
		this.previousValue = previousValue;
	}

	/**
	 * @hibernate.property name="currentValue" type="string"
     * column="CURRENT_VALUE" length="500" 
	 **/
	public String getCurrentValue()
	{
		return currentValue;
	}

	public void setCurrentValue(String currentValue)
	{
		this.currentValue = currentValue;
	}
	
	/**
     * @hibernate.many-to-one column="AUDIT_EVENT_LOG_ID"  class="edu.wustl.common.domain.AuditEventLog" constrained="true"
	 * @see #setParticipant(Site)
     */
	public AuditEventLog getAuditEventLog() 
	{
		return auditEventLog;
	}
	/**
	 * @param auditEventLog The auditEventLog to set.
	 */
	public void setAuditEventLog(AuditEventLog auditEventLog) 
	{
		this.auditEventLog = auditEventLog;
	}
	
	public int hashCode()
	{
		int hashCode = 0;
	
		if(id!=null)
			hashCode += id.intValue(); 
		if(elementName!=null)	
			hashCode += elementName.hashCode(); 
		if(previousValue!=null)		
			hashCode +=  previousValue.hashCode();
		if(currentValue!=null)	
			hashCode +=  currentValue.hashCode();

		return hashCode;
	}
	
	public String toString()
	{
		return 
			"Id "+id+" "+
			"ElementName "+elementName +" "+
			"PreviousValue "+previousValue+" "+
			"CurrentValue "+currentValue+"\n";
	}
	
	public boolean equals(Object obj)
	{
//		if(obj instanceof AuditEventDetails)
//		{
//			AuditEventDetails auditEventDetails = (AuditEventDetails)obj;
//			if(this.id.equals(auditEventDetails.id) && 
//					this.elementName.equals(auditEventDetails.elementName) &&
//					this.previousValue.equals(auditEventDetails.previousValue) &&
//					this.currentValue.equals(auditEventDetails.currentValue))
//				return true;
//		}
		return false;
	}
}