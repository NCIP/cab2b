
package edu.wustl.common.beans;

/**
 * This class acts as a POJO for all the Login Attempt related information.
 * An instance of this class is passed to the LoginAuditManager's audit() method,
 * to audit the login attempt 
 * @author niharika_sharma
 */
public class LoginDetails
{

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

	/**
	 * Status of the login attempt, success or failure
	 */
	protected boolean isLoginSuccessful;

	/**
	 * Constructor accepting the details required to create the LoginDetails object
	 * @param userLoginId
	 * @param sourceId
	 * @param ipAddress
	 */
	public LoginDetails(String userLoginId, Long sourceId, String ipAddress)
	{
		this.ipAddress = ipAddress;
		this.userLoginId = userLoginId;
		this.sourceId = sourceId;
	}

	/**
	 * Constructor accepting the details required to create the LoginDetails object 
	 * along with login status 
	 * @param userLoginId
	 * @param sourceId
	 * @param ipAddress
	 * @param isLoginSuccessful
	 */
	public LoginDetails(String userLoginId, Long sourceId, String ipAddress,
			boolean isLoginSuccessful)
	{
		this(userLoginId, sourceId, ipAddress);
		this.isLoginSuccessful = isLoginSuccessful;
	}

	/**
	 * Returns the userLoginId
	 * @return
	 */
	public String getUserLoginId()
	{
		return userLoginId;
	}

	/**
	 * Sets the userLoginId
	 * @param userLoginId
	 */
	public void setUserLoginId(String userLoginId)
	{
		this.userLoginId = userLoginId;
	}

	/**
	 * Returns the sourceId
	 * @return
	 */
	public Long getSourceId()
	{
		return sourceId;
	}

	/**
	 * Sets the sourceId
	 * @param sourceId
	 */
	public void setSourceId(Long sourceId)
	{
		this.sourceId = sourceId;
	}

	/**
	 * Returns the ipAddress
	 * @return
	 */
	public String getIpAddress()
	{
		return ipAddress;
	}

	/**
	 * Sets the ipAddress
	 * @param ipAddress
	 */
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	/**
	 * Returns the isLoginSuccessful
	 * @return
	 */
	public boolean isLoginSuccessful()
	{
		return isLoginSuccessful;
	}

	/**
	 * Sets the isLoginSuccessful
	 * @param isLoginSuccessful
	 */
	public void setLoginSuccessful(boolean isLoginSuccessful)
	{
		this.isLoginSuccessful = isLoginSuccessful;
	}
}
