package edu.wustl.common.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @hibernate.class table="LOGIN_EVENT"
 **/
public class LoginEvent implements Serializable
{
	
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -7085963264585176596L;

	/**
     * System generated unique id.
     */
	protected Long id;
	
	/**
     * Date and time of the event.
     */
	protected Date timestamp = Calendar.getInstance().getTime();
	
	/**
     * User's login id
     */
	protected String userLoginId;
	
	/**
     * User's source or domain he belongs to
     */
	protected Long sourceId;
	
	/**
	 * IP address of the machine.
	 */
	protected String ipAddress;
	
	
	protected boolean isLoginSuccessful;
	
	/**
     * Returns user's login Status
     * @return user's login Status
     * @hibernate.property name="isLoginSuccessful" type="boolean" 
     * column="LOGIN_SUCCESS"
     */
	public boolean getIsLoginSuccessful()
	{
		return isLoginSuccessful;
	}
	
	public void setIsLoginSuccessful(boolean loginSuccessful)
	{
		this.isLoginSuccessful = loginSuccessful;
	}
	
	/**
     * Returns user's login id 
     * @return user's login id
     * @hibernate.property name="userLoginId" type="string" 
     * column="USER_LOGIN_ID" length="50"
     */
	public String getUserLoginId()
	{
		return userLoginId;
	}
	
	public void setUserLoginId(String userLoginId)
	{
		this.userLoginId = userLoginId;
	}
	
	
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
     * @hibernate.property name="sourceId" type="long" column="SOURCE_ID"
     * @return Returns the institutionId.
     */
    public Long getSourceId()
    {
        return sourceId;
    }
    
    /**
     * @param userId The userId to set.
     */
    public void setSourceId(Long sourceId)
    {
    	this.sourceId=sourceId;
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
}
