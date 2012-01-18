
package edu.wustl.common.domain;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * @hibernate.class table="CATISSUE_AUDIT_EVENT"
 **/
public class AuditEvent implements java.io.Serializable
{
    
    private static final long serialVersionUID = 1234567890L;
    
	/**
     * System generated unique id.
     */
	protected Long id;
	
	/**
     * Date and time of the event.
     */
	protected Date timestamp = Calendar.getInstance().getTime();
	
	/**
     * User who performs the event.
     */
	protected Long userId;
	
	/**
     * Text comments on event.
     */
	protected String comments;
	
	/**
	 * IP address of the machine.
	 */
	protected String ipAddress;
	
	protected Collection auditEventLogCollection = new HashSet();
	
	/**
     * Returns System generated unique id.
     * @return System generated unique id.
     * @see #setId(Integer)
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_AUDIT_EVENT_PARAM_SEQ" 
     */
	public Long getId()
	{
		return id;
	}
	
	/**
     * Sets unique id.
     * @param id Identifier to be set.
     * @see #getId()
     */
	public void setId(Long id)
	{
		this.id = id;
	}
	
	/**
     * Returns date and time of the event. 
     * @return Date and time of the event.
     * @see #setTimestamp(Date)
     * @hibernate.property name="timestamp" type="timestamp" 
     * column="EVENT_TIMESTAMP"
     */
	public Date getTimestamp()
	{
		return timestamp;
	}
	
	/**
     * Sets date and time of the event.
     * @param timestamp Date and time of the event.
     * @see #getTimestamp()
     */
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}
	
    /**
     * @hibernate.property name="userId" type="long" column="USER_ID"
     * @return Returns the userId.
     */
    public Long getUserId()
    {
        return userId;
    }
    
    /**
     * @param userId The userId to set.
     */
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }
    
    /**
     * Returns text comments on this event. 
     * @return Text comments on this event.
     * @see #setComments(String)
     * @hibernate.property name="comments" type="string" 
     * column="COMMENTS" length="500"
     */
	public String getComments()
	{
		return comments;
	}
	
	/**
     * Sets text comments on this event.
     * @param comments text comments on this event.
     * @see #getComments()
     */
	public void setComments(String comments)
	{
		this.comments = comments;
	}
	
	/**
	 * @hibernate.property name="ipAddress" type="string"
     * column="IP_ADDRESS" length="20" 
	 **/
	public String getIpAddress()
	{
		return ipAddress;
	}
	
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	
	/**
	 * @hibernate.set name="auditEventLogCollection" table="CATISSUE_AUDIT_EVENT_LOG"
	 * @hibernate.collection-key column="AUDIT_EVENT_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.common.domain.AuditEventLog"
	 */
	public Collection getAuditEventLogCollection()
	{
		return auditEventLogCollection;
	}
	
	public void setAuditEventLogCollection(Collection auditEventLogCollection)
	{
		this.auditEventLogCollection = auditEventLogCollection;
	}
}