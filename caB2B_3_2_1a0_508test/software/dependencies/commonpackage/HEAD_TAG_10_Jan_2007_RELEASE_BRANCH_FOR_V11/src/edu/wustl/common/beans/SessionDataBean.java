/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.common.beans;



/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Poornima Govindrao
 *@version 1.0
 */

public class SessionDataBean 
{
	private String firstName = new String();
	private String lastName  = new String();
	
	private String csmUserId = new String();
    private String userName=new String();
    private String ipAddress=new String();
    private Long userId = null;
    private boolean securityRequired=new Boolean(false);
    private boolean isAdmin = false;
    
    
	public boolean isAdmin()
	{
		return isAdmin;
	}
	
	public void setAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
	}
	/**
     * @return Returns the userName.
     */
    public String getUserName()
    {
        return userName;
    }
    /**
     * @param userName The userName to set.
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    /**
     * @return Returns the csmUserId.
     */
    public String getCsmUserId()
    {
        return csmUserId;
    }
    
    /**
     * @param csmUserId The csmUserId to set.
     */
    public void setCsmUserId(String csmUserId)
    {
        this.csmUserId = csmUserId;
    }
    
    /**
     * @return Returns the ipAddress.
     */
    public String getIpAddress()
    {
        return ipAddress;
    }
    /**
     * @param ipAddress The ipAddress to set.
     */
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }
    /**
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
    

	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	/**
	 * This method returns boolean value for securityRequired
	 * @return
	 */
	public boolean isSecurityRequired() 
	{
		return securityRequired;
	}
	/**
	 * This method sets the boolen value of securityRequired
	 * @param securityRequired
	 */
	public void setSecurityRequired(boolean securityRequired) 
	{
		this.securityRequired = securityRequired;
	}
}
