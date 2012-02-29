/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.common.security;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import edu.wustl.common.audit.LoginAuditManager;
import edu.wustl.common.beans.LoginDetails;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.query.AbstractClient;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.SMTransactionException;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.Roles;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.ObjectPrivilegeMap;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSTransactionException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: (c) Washington University, School of Medicine 2005
 * </p>
 * <p>
 * Company: Washington University, School of Medicine, St. Louis.
 * </p>
 * 
 * @author Aarti Sharma
 * @version 1.0
 */

public class SecurityManager implements Permissions {

	private static AuthenticationManager authenticationManager = null;

	private static AuthorizationManager authorizationManager = null;

	private Class requestingClass = null;
	public static boolean initialized = false;
	public static String APPLICATION_CONTEXT_NAME = null; 
		
	public static HashMap<String, String> rolegroupNamevsId= new HashMap<String, String>();
	
	public static final String ADMINISTRATOR_GROUP = "ADMINISTRATOR_GROUP";
	public static final String SUPER_ADMINISTRATOR_GROUP = "SUPER_ADMINISTRATOR_GROUP";
	public static final String SUPERVISOR_GROUP = "SUPERVISOR_GROUP";
	public static final String TECHNICIAN_GROUP = "TECHNICIAN_GROUP";
	public static final String PUBLIC_GROUP = "PUBLIC_GROUP";
	
	public static final String CLASS_NAME = "CLASS_NAME";

	public static final String TABLE_NAME = "TABLE_NAME";

	public static final String TABLE_ALIAS_NAME = "TABLE_ALIAS_NAME";

	private static String securityDataPrefix = CLASS_NAME;


	/**
	 * @param class1
	 */
	public SecurityManager(Class class1) {
		super();
		requestingClass = class1;
		if(!initialized)
		{
			getApplicationContextName();
			initializeConstants();
		}
	}

	/**
	 * @param class1
	 * @return
	 */
	public static final SecurityManager getInstance(Class class1) {
		Class className = null;
        try
        {
            InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream(
                    Constants.SECURITY_MANAGER_PROP_FILE);
            Properties p = new Properties();
            p.load(inputStream);
            inputStream.close();
            String securityManagerClass = p.getProperty(Constants.SECURITY_MANAGER_CLASSNAME);
            if(securityManagerClass!=null)
            	className = Class.forName(securityManagerClass);
            if(className != null)
            {
            	Constructor[] cons = className.getConstructors();
            	return (SecurityManager)cons[0].newInstance(class1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
       	return new SecurityManager(class1);
	}
	
	 public static String getApplicationContextName()
	    {
	        String applicationName = "";
	        try
	        {
	            InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream(
	                    Constants.SECURITY_MANAGER_PROP_FILE);
	            Properties p = new Properties();
	            p.load(inputStream);
	            inputStream.close();
	            applicationName = p.getProperty(Constants.APPLN_CONTEXT_NAME);
	            APPLICATION_CONTEXT_NAME = applicationName;       
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        
	        return applicationName; 
	    }
	
	 /**
     * Returns group id from Group name
     * @param groupName
     * @return
     * @throws CSException 
     * @throws SMException 
     */
    private String getGroupID(String groupName) throws CSException, SMException
    {
        Group group = new Group();
        group.setGroupName(groupName);
        UserProvisioningManager userProvisioningManager;
        SearchCriteria searchCriteria = new GroupSearchCriteria(group);
        List list;
        userProvisioningManager = getUserProvisioningManager();
        group.setApplication(userProvisioningManager
                .getApplication(APPLICATION_CONTEXT_NAME));
        list = getObjects(searchCriteria);
        if (list.isEmpty() == false)
        {
            group = (Group) list.get(0);
            return group.getGroupId().toString();
        }

        return null;
    }
    
    /**
     * Returns role id from role name
     * @param roleName
     * @return
     */
    private String getRoleID(String roleName)  throws CSException, SMException
    {
        Role role = new Role();
        role.setName(roleName);
        UserProvisioningManager userProvisioningManager;
        SearchCriteria searchCriteria = new RoleSearchCriteria(role);
        List list;
        userProvisioningManager = getUserProvisioningManager();
        role.setApplication(userProvisioningManager
                .getApplication(APPLICATION_CONTEXT_NAME));
        list = getObjects(searchCriteria);
        if (list.isEmpty() == false)
        {
            role = (Role) list.get(0);
            return role.getId().toString();
        }
        return null;
    }
    
    
    public void initializeConstants()
    {
    	try
    	{
 		
    		rolegroupNamevsId.put(Constants.ADMINISTRATOR_ROLE, getRoleID(Constants.ROLE_ADMINISTRATOR));
    		rolegroupNamevsId.put(Constants.PUBLIC_ROLE,getRoleID(Constants.SCIENTIST));
    		rolegroupNamevsId.put(Constants.TECHNICIAN_ROLE,getRoleID(Constants.TECHNICIAN));
    		rolegroupNamevsId.put(Constants.SUPERVISOR_ROLE,getRoleID(Constants.SUPERVISOR));
    		rolegroupNamevsId.put(Constants.ADMINISTRATOR_GROUP_ID, getGroupID(ADMINISTRATOR_GROUP));
    		rolegroupNamevsId.put(Constants.PUBLIC_GROUP_ID,getGroupID(PUBLIC_GROUP));
    		rolegroupNamevsId.put(Constants.TECHNICIAN_GROUP_ID,getGroupID(TECHNICIAN_GROUP));
    		rolegroupNamevsId.put(Constants.SUPERVISOR_GROUP_ID,getGroupID(SUPERVISOR_GROUP));
    		rolegroupNamevsId.put(Constants.SUPER_ADMINISTRATOR_ROLE, getRoleID(Constants.ROLE_SUPER_ADMINISTRATOR));
    		rolegroupNamevsId.put(Constants.SUPER_ADMINISTRATOR_GROUP_ID, getRoleID(SUPER_ADMINISTRATOR_GROUP));
        	initialized=true;
	   	}
    	catch(CSException e)
    	{
    		e.printStackTrace();
    	}
    	catch(SMException e)
    	{
    		e.printStackTrace();
    	}
    }
	/**
	 * Returns the AuthenticationManager for the caTISSUE Core. This method
	 * follows the singleton pattern so that only one AuthenticationManager is
	 * created for the caTISSUE Core.
	 * 
	 * @return @throws
	 *         CSException
	 */
	protected AuthenticationManager getAuthenticationManager()
	throws CSException {
		if (authenticationManager == null) {
			synchronized (requestingClass) {
				if (authenticationManager == null) {
					authenticationManager = SecurityServiceProvider
					.getAuthenticationManager(APPLICATION_CONTEXT_NAME);
				}
			}
		}

		return authenticationManager;

	}

	/**
	 * Returns the Authorization Manager for the caTISSUE Core. This method
	 * follows the singleton pattern so that only one AuthorizationManager is
	 * created.
	 * 
	 * @return @throws
	 *         CSException
	 */
	protected AuthorizationManager getAuthorizationManager() throws CSException {

		if (authorizationManager == null) {
			synchronized (requestingClass) {
				if (authorizationManager == null) {
					authorizationManager = SecurityServiceProvider
					.getAuthorizationManager(APPLICATION_CONTEXT_NAME);
				}
			}
		}

		return authorizationManager;

	}

	/**
	 * Returns the UserProvisioningManager singleton object.
	 * 
	 * @return @throws
	 *         CSException
	 */
	public UserProvisioningManager getUserProvisioningManager()
	throws CSException {
		UserProvisioningManager userProvisioningManager = (UserProvisioningManager) getAuthorizationManager();

		return userProvisioningManager;
	}

	/**
	 * Returns true or false depending on the person gets authenticated or not.
	 * @param requestingClass
	 * @param loginName login name
	 * @param password password
	 * @return @throws CSException
	 */
	public boolean login(String loginName, String password) throws SMException 
	{
		boolean loginSuccess = false;
		try 
		{
			Logger.out.debug("login name: " + loginName + " passowrd: " + password);
			AuthenticationManager authMngr = getAuthenticationManager();
			loginSuccess = authMngr.login(loginName, password);
		} 
		catch (CSException ex) 
		{
			Logger.out.debug("Authentication|"
					+ requestingClass
					+ "|"
					+ loginName
					+ "|login|Success| Authentication is not successful for user "
					+ loginName + "|" + ex.getMessage());
			throw new SMException(ex.getMessage(), ex);
		}
		return loginSuccess;
	}
	
	/**
	 * Returns true or false depending on the person gets authenticated or not.
	 * Also audits the login attempt
	 * @param requestingClass
	 * @param loginName login name
	 * @param loginEvent
	 * @param password password
	 * @return @throws CSException
	 */
	public boolean login(String loginName, String password,LoginDetails loginDetails) throws SMException 
	{
		boolean loginSuccess = false;
		LoginAuditManager loginAuditManager=new LoginAuditManager(loginDetails);
		try 
		{
			Logger.out.debug("login name: " + loginName + " passowrd: " + password);
			AuthenticationManager authMngr = getAuthenticationManager();
			loginSuccess = authMngr.login(loginName, password);
		} 
		catch (CSException ex) 
		{
			Logger.out.debug("Authentication|"
					+ requestingClass
					+ "|"
					+ loginName
					+ "|login|Success| Authentication is not successful for user "
					+ loginName + "|" + ex.getMessage());
			throw new SMException(ex.getMessage(), ex);
		}
		finally
		{
			System.out.println("-------------------------loginSuccess="+loginSuccess);
			loginAuditManager.audit(loginSuccess);
		}
		return loginSuccess;
	}

	/**
	 * This method creates a new User in the database based on the data passed
	 * 
	 * @param user
	 *            user to be created
	 * @throws SMTransactionException
	 *             If there is any exception in creating the User
	 */
	public void createUser(User user) throws SMTransactionException {
		try {
			getUserProvisioningManager().createUser(user);
		} catch (CSTransactionException e) {
			Logger.out.debug("Unable to create user: Exception: "
					+ e.getMessage());
			throw new SMTransactionException(e.getMessage(), e);
		} catch (CSException e) {
			Logger.out.debug("Unable to create user: Exception: " + e);
		}
	}

	/**
	 * This method returns the User object from the database for the passed
	 * User's Login Name. If no User is found then null is returned
	 * 
	 * @param loginName
	 *            Login name of the user
	 * @return @throws
	 *         SMException
	 */
	public User getUser(String loginName) throws SMException {
		try {
			return getAuthorizationManager().getUser(loginName);
		} catch (CSException e) {
			Logger.out
			.debug("Unable to get user: Exception: " + e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * This method returns array of CSM user id of all users who are administrators
	 * @return
	 * @throws SMException
	 */
	public Long[] getAllAdministrators() throws SMException 
	{
		try {
			Group group = new Group();
			group.setGroupName(ADMINISTRATOR_GROUP);
			GroupSearchCriteria groupSearchCriteria= new GroupSearchCriteria(group);
			List list = getObjects(groupSearchCriteria);
			Logger.out.debug("Group Size: "+list.size());
			group = (Group) list.get(0);
			Logger.out.debug("Group : "+group.getGroupName());
			Set users = group.getUsers();
			Logger.out.debug("Users : "+users);
			Long[] userId= new Long[users.size()];
			Iterator it= users.iterator();
			for(int i=0; i<users.size(); i++)
			{
				userId[i] =  ((User)it.next()).getUserId();
			}
			return userId;
		} catch (CSException e) {
			Logger.out.debug("Unable to get users: Exception: " + e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * This method checks whether a user exists in the database or not
	 * 
	 * @param loginName
	 *            Login name of the user
	 * @return TRUE is returned if a user exists else FALSE is returned
	 * @throws SMException
	 */
	public boolean userExists(String loginName) throws SMException {
		boolean userExists = true;
		try {
			if (getUser(loginName) == null) {
				userExists = false;
			}
		} catch (SMException e) {
			Logger.out
			.debug("Unable to get user: Exception: " + e.getMessage());
			throw e;
		}
		return userExists;
	}

	public void removeUser(String userId) throws SMException
	{
		try {
			getUserProvisioningManager().removeUser(userId);
		} catch (CSTransactionException ex) {
			Logger.out
			.debug("Unable to get user: Exception: " + ex.getMessage());
			throw new SMTransactionException("Failed to find this user with userId:"+userId,ex);
		} catch (CSException e) {
			Logger.out
			.debug("Unable to obtain Authorization Manager: Exception: " + e.getMessage());
			throw new SMException("Failed to find this user with userId:"+userId,e);
		}
	}

	/**
	 * This method returns Vactor of all the role objects defined for the
	 * application from the database
	 * 
	 * @return @throws
	 *         SMException
	 */
	public Vector getRoles() throws SMException {
		Vector roles = new Vector();
		UserProvisioningManager userProvisioningManager = null;
		try {
			userProvisioningManager = getUserProvisioningManager();
			roles.add(userProvisioningManager.getRoleById(rolegroupNamevsId.get(Constants.SUPER_ADMINISTRATOR_ROLE)));
			roles.add(userProvisioningManager.getRoleById(rolegroupNamevsId.get(Constants.ADMINISTRATOR_ROLE)));
			roles.add(userProvisioningManager.getRoleById(rolegroupNamevsId.get(Constants.SUPERVISOR_ROLE)));
			roles.add(userProvisioningManager.getRoleById(rolegroupNamevsId.get(Constants.TECHNICIAN_ROLE)));
			roles.add(userProvisioningManager.getRoleById(rolegroupNamevsId.get(Constants.PUBLIC_ROLE)));
			
		} catch (CSException e) {
			Logger.out.debug("Unable to get roles: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
		return roles;
	}

	/**
	 * Assigns a Role to a User
	 * 
	 * @param userName -
	 *            the User Name to to whom the Role will be assigned
	 * @param roleID -
	 *            The id of the Role which is to be assigned to the user
	 * @throws SMException
	 */
	public void assignRoleToUser(String userID, String roleID)
	throws SMException {
		Logger.out.debug("UserName: " + userID + " Role ID:" + roleID);
		UserProvisioningManager userProvisioningManager = null;
		User user;
		String groupId;
		try {
			userProvisioningManager = getUserProvisioningManager();
			//user = userProvisioningManager.getUser(userName);
			user = userProvisioningManager.getUserById(userID);

			//Remove user from any other role if he is assigned some
			userProvisioningManager.removeUserFromGroup(rolegroupNamevsId.get(Constants.ADMINISTRATOR_GROUP_ID),
					String.valueOf(user.getUserId()));
			userProvisioningManager.removeUserFromGroup(rolegroupNamevsId.get(Constants.SUPERVISOR_GROUP_ID), String
					.valueOf(user.getUserId()));
			userProvisioningManager.removeUserFromGroup(rolegroupNamevsId.get(Constants.TECHNICIAN_GROUP_ID), String
					.valueOf(user.getUserId()));
			userProvisioningManager.removeUserFromGroup(rolegroupNamevsId.get(Constants.PUBLIC_GROUP_ID), String
					.valueOf(user.getUserId()));

			//Add user to corresponding group
			groupId = getGroupIdForRole(roleID);
			if (groupId == null) {
				Logger.out.debug(" User assigned no role");
			} else {
				assignAdditionalGroupsToUser(String.valueOf(user.getUserId()),
						new String[] { groupId });
				Logger.out.debug(" User assigned role:" + groupId);
			}

		} catch (CSException e) {
			Logger.out.debug("UNABLE TO ASSIGN ROLE TO USER: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	private String getGroupIdForRole(String roleID) {
		if (roleID.equals(rolegroupNamevsId.get(Constants.ADMINISTRATOR_ROLE))) {
			Logger.out.debug(" role corresponds to Administrator group");
			return rolegroupNamevsId.get(Constants.ADMINISTRATOR_GROUP_ID);
		} else if (roleID.equals(rolegroupNamevsId.get(Constants.SUPERVISOR_ROLE))) {
			Logger.out.debug(" role corresponds to Supervisor group");
			return rolegroupNamevsId.get(Constants.SUPERVISOR_GROUP_ID);
		} else if (roleID.equals(rolegroupNamevsId.get(Constants.TECHNICIAN_ROLE))) {
			Logger.out.debug(" role corresponds to Technician group");
			return rolegroupNamevsId.get(Constants.TECHNICIAN_GROUP_ID);
		} else if (roleID.equals(rolegroupNamevsId.get(Constants.PUBLIC_ROLE))) {
			Logger.out.debug(" role corresponds to public group");
			return rolegroupNamevsId.get(Constants.PUBLIC_GROUP_ID);
		} else {
			Logger.out.debug("role corresponds to no group");
			return null;
		}
	}


	public Role getUserRole(long userID) throws SMException {
		Set groups;
		UserProvisioningManager userProvisioningManager = null;
		Iterator it;
		Group group;
		Role role = null;
		try {
			userProvisioningManager = getUserProvisioningManager();
			groups = userProvisioningManager.getGroups(String.valueOf(userID));
			it = groups.iterator();
			while (it.hasNext()) {
				group = (Group) it.next();
				if (group.getApplication().getApplicationName().equals(APPLICATION_CONTEXT_NAME))
				{
					if (group.getGroupName().equals(ADMINISTRATOR_GROUP)) {
						role = userProvisioningManager
						.getRoleById(rolegroupNamevsId.get(Constants.ADMINISTRATOR_ROLE));
						return role;
					} else if (group.getGroupName().equals(SUPERVISOR_GROUP)) {
						role = userProvisioningManager
						.getRoleById(rolegroupNamevsId.get(Constants.SUPERVISOR_ROLE));
						return role;
					} else if (group.getGroupName().equals(TECHNICIAN_GROUP)) {
						role = userProvisioningManager
						.getRoleById(rolegroupNamevsId.get(Constants.TECHNICIAN_ROLE));
						return role;
					} else if (group.getGroupName().equals(PUBLIC_GROUP)) {
						role = userProvisioningManager
						.getRoleById(rolegroupNamevsId.get(Constants.PUBLIC_ROLE));
						return role;
					}
				}
			}
		} catch (CSException e) {
			Logger.out.debug("Unable to get roles: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
		return role;

	}
	/**
	 * Name : Virender Mehta
	 * Reviewer: Sachin Lale
	 * Bug ID: 3842
	 * Patch ID: 3842_2
	 * See also: 3842_1
	 * Description: This function will return the Role name(Administrator, Scientist, Technician, Supervisor )
	 * @param userID
	 * @return Role Name
	 * @throws SMException
	 */
	public String getUserGroup(long userID) throws SMException
	{
		Set groups;
		UserProvisioningManager userProvisioningManager = null;
		Iterator it;
		Group group;
		try
		{
			userProvisioningManager = getUserProvisioningManager();
			groups = userProvisioningManager.getGroups(String.valueOf(userID));
			it = groups.iterator();
			while (it.hasNext())
			{
				group = (Group) it.next();
				if (group.getApplication().getApplicationName().equals(APPLICATION_CONTEXT_NAME))
				{
					if (group.getGroupName().equals(ADMINISTRATOR_GROUP) ) 
					{
						return Roles.ADMINISTRATOR;
					}
					else if (group.getGroupName().equals(SUPERVISOR_GROUP)) 
					{
						return Roles.SUPERVISOR;
					}
					else if (group.getGroupName().equals(TECHNICIAN_GROUP)) 
					{
						return Roles.TECHNICIAN;
					}
					else if (group.getGroupName().equals(PUBLIC_GROUP)) 
					{
						return Roles.SCIENTIST;
					}
				}
			}
		}
		catch (CSException e) 
		{
			Logger.out.debug("Unable to get roles: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
		return "";
	}

	/**
	 * Modifies an entry for an existing User in the database based on the data
	 * passed
	 * 
	 * @param user -
	 *            the User object that needs to be modified in the database
	 * @throws SMException
	 *             if there is any exception in modifying the User in the
	 *             database
	 */
	public void modifyUser(User user) throws SMException {
		try {
			getUserProvisioningManager().modifyUser(user);
		} catch (CSException e) {
			Logger.out.debug("Unable to modify user: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * Returns the User object for the passed User id
	 * 
	 * @param userId -
	 *            The id of the User object which is to be obtained
	 * @return The User object from the database for the passed User id
	 * @throws SMException
	 *             if the User object is not found for the given id
	 */
	public User getUserById(String userId) throws SMException {
		Logger.out.debug("user Id: " + userId);
		try {
			User user = getUserProvisioningManager().getUserById(userId);
			Logger.out.debug("User returned: " + user.getLoginName());
			return user;
		} catch (CSException e) {
			Logger.out.debug("Unable to get user by Id: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * Returns list of the User objects for the passed email address
	 * 
	 * @param emailAddress -
	 *            Email Address for which users need to be searched
	 * @return @throws
	 *         SMException if there is any exception while querying the database
	 */
	public List getUsersByEmail(String emailAddress) throws SMException {
		try {
			User user = new User();
			user.setEmailId(emailAddress);
			SearchCriteria searchCriteria = new UserSearchCriteria(user);
			return getUserProvisioningManager().getObjects(searchCriteria);
		} catch (CSException e) {
			Logger.out.debug("Unable to get users by emailAddress: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * @throws SMException
	 *  
	 */
	public List getUsers() throws SMException {
		try {
			User user = new User();
			SearchCriteria searchCriteria = new UserSearchCriteria(user);
			return getUserProvisioningManager().getObjects(searchCriteria);
		} catch (CSException e) {
			Logger.out.debug("Unable to get all users: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * Returns list of objects corresponding to the searchCriteria passed
	 * 
	 * @param searchCriteria
	 * @return List of resultant objects
	 * @throws SMException
	 *             if searchCriteria passed is null or if search results in no
	 *             results
	 * @throws CSException
	 */
	public List getObjects(SearchCriteria searchCriteria) throws SMException,
	CSException {
		if (null == searchCriteria) {
			Logger.out.debug(" Null Parameters passed");
			throw new SMException("Null Parameters passed");
		}
		UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
		List list = userProvisioningManager.getObjects(searchCriteria);
		if (null == list || list.size() <= 0) {
			// Logger.out.debug("Search resulted in no results");
			// throw new SMException("Search resulted in no results");
		}
		return list;
	}

	public void assignUserToGroup(String userGroupname, String userId)throws SMException
	{
		Logger.out.debug(" userId: " + userId + " userGroupname:" + userGroupname);

		if (userId == null || userGroupname == null)
		{
			Logger.out.debug(" Null or insufficient Parameters passed");
			throw new SMException("Null or insufficient Parameters passed");
		}

		try
		{
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();

			Group group = getUserGroup(userGroupname);
			if (group != null)
			{
				String[] groupIds = {group.getGroupId().toString()};

				assignAdditionalGroupsToUser(userId, groupIds);
			}
			else
			{
				Logger.out.debug("No user group with name "+userGroupname+" is present");
			}
		}
		catch(CSException ex)
		{
			Logger.out.fatal("The Security Service encountered "
					+ "a fatal exception.", ex);
			throw new SMException(
					"The Security Service encountered a fatal exception.", ex);
		}
	}

	public void removeUserFromGroup(String userGroupname, String userId)throws SMException
	{
		Logger.out.debug(" userId: " + userId + " userGroupname:" + userGroupname);

		if (userId == null || userGroupname == null)
		{
			Logger.out.debug(" Null or insufficient Parameters passed");
			throw new SMException("Null or insufficient Parameters passed");
		}

		try
		{
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();

			Group group = getUserGroup(userGroupname);

			if (group != null)
			{
				userProvisioningManager.removeUserFromGroup(group.getGroupId().toString(), userId);
			}
			else
			{
				Logger.out.debug("No user group with name "+userGroupname+" is present");
			}
		}
		catch(CSException ex)
		{
			Logger.out.fatal("The Security Service encountered "
					+ "a fatal exception.", ex);
			throw new SMException(
					"The Security Service encountered a fatal exception.", ex);
		}
	}

	/**
	 * @param userGroupname
	 * @return
	 * @throws SMException
	 * @throws CSException
	 */
	private Group getUserGroup(String userGroupname) throws SMException, CSException
	{
		Group group = new Group();
		group.setGroupName(userGroupname);
		SearchCriteria searchCriteria = new GroupSearchCriteria(group);
		List list = getObjects(searchCriteria);
		if (list.isEmpty() == false)
		{
			Logger.out.debug("list size********************"+list.size());
			group = (Group) list.get(0);

			return group;
		}

		return null;
	}

	public void assignAdditionalGroupsToUser(String userId, String[] groupIds)
	throws SMException {
		if (userId == null || groupIds == null || groupIds.length < 1) {
			Logger.out.debug(" Null or insufficient Parameters passed");
			throw new SMException("Null or insufficient Parameters passed");
		}

		Logger.out.debug(" userId: " + userId + " groupIds:" + groupIds);

		Set consolidatedGroupIds = new HashSet();
		Set consolidatedGroups;
		String[] finalUserGroupIds;
		UserProvisioningManager userProvisioningManager;
		Group group =null;
		
		try {
			userProvisioningManager = getUserProvisioningManager();

			consolidatedGroups = userProvisioningManager.getGroups(userId);
			
			if (null != consolidatedGroups) {
				Iterator it = consolidatedGroups.iterator();
				while (it.hasNext()) {
					group = (Group) it.next();
					consolidatedGroupIds
					.add(String.valueOf(group.getGroupId()));
				}
			}

			/**
			 * Consolidating all the Groups
			 */

			for (int i = 0; i < groupIds.length; i++) {
				consolidatedGroupIds.add(groupIds[i]);
			}

			finalUserGroupIds = new String[consolidatedGroupIds.size()];
			Iterator it = consolidatedGroupIds.iterator();

			for (int i = 0; it.hasNext(); i++) {
				finalUserGroupIds[i] = (String) it.next();
				Logger.out.debug("Group user is assigned to: "
						+ finalUserGroupIds[i]);
			}			

			/**
			 * Setting groups for user and updating it
			 */
			userProvisioningManager.assignGroupsToUser(userId,
					finalUserGroupIds);

		} catch (CSException ex) {
			Logger.out.fatal("The Security Service encountered "
					+ "a fatal exception.", ex);
			throw new SMException(
					"The Security Service encountered a fatal exception.", ex);
		}

	}


	public boolean isAuthorized(String userName, String objectId,
			String privilegeName) throws SMException {
		try {
			boolean isAuthorized = getAuthorizationManager().checkPermission(
					userName, objectId, privilegeName);
			Logger.out.debug(" User:" + userName + " objectId:" + objectId
					+ " privilegeName:" + privilegeName + " isAuthorized:"
					+ isAuthorized);
			return isAuthorized;
		} catch (CSException e) {
			Logger.out.debug("Unable to get all users: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
	}

	
	public boolean checkPermission(String userName, String objectType,
			String objectIdentifier, String privilegeName) throws SMException 
			{
		if(Boolean.parseBoolean(XMLPropertyHandler.getValue(Constants.ISCHECKPERMISSION)))
		{
			try {
				Logger.out.debug(" User:" + userName + "objectType:" + objectType
						+ " objectId:" + objectIdentifier + " privilegeName:"
						+ privilegeName);

//				String protectionElementName = objectType + "_" + objectIdentifier;

//				ProtectionElement protectionElement = getAuthorizationManager().getProtectionElement(
//				protectionElementName);

//				List peList = new ArrayList();
//				peList.add(protectionElement);

//				Collection pMap = getAuthorizationManager().getPrivilegeMap(userName, peList);
//				Iterator it1 = pMap.iterator();
//				while (it1.hasNext())
//				{
//				ObjectPrivilegeMap map = (ObjectPrivilegeMap) it1.next();
//				Logger.out.debug("PE Privileges>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+map.getPrivileges().toString());
//				Logger.out.debug("PE Privileges Size>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+map.getPrivileges().size());
//				Iterator it2 = map.getPrivileges().iterator();
//				while (it2.hasNext())
//				{
//				Privilege pr = (Privilege)it2.next();
//				Logger.out.debug("Privilege ****************************"+pr.getName());
//				}
//				}

//				Set protectionGroups = getAuthorizationManager().getProtectionGroups(
//				protectionElement.getProtectionElementId().toString());
//				Iterator it = protectionGroups.iterator();
//				while (it.hasNext()) {
//				ProtectionGroup protectionGroup = (ProtectionGroup) it.next();
//				String name = protectionGroup.getProtectionGroupName();
//				Logger.out.debug("Protection group Name : ############################"+name);
//				}

				boolean isAuthorized = getAuthorizationManager().checkPermission(
						userName, objectType + "_" + objectIdentifier,
						privilegeName);

				Logger.out.debug(" User:" + userName + "objectType:" + objectType
						+ " objectId:" + objectIdentifier + " privilegeName:"
						+ privilegeName + " isAuthorized:" + isAuthorized);

				return isAuthorized;
			} catch (CSException e) {
				Logger.out.debug("Unable to get all users: Exception: "
						+ e.getMessage());
				throw new SMException(e.getMessage(), e);
			}
		}
		return true;
			}

	/**
	 * This method returns name of the Protection groupwhich consists of obj as
	 * Protection Element and whose name consists of string nameConsistingOf
	 * 
	 * @param obj
	 * @param nameConsistingOf
	 * @return @throws
	 *         SMException
	 */
	public String getProtectionGroupByName(AbstractDomainObject obj,
			String nameConsistingOf) throws SMException {
		Set protectionGroups;
		Iterator it;
		ProtectionGroup protectionGroup;
		ProtectionElement protectionElement;
		String name = null;
		String protectionElementName = obj.getObjectId();
		try {
			AuthorizationManager authManager = getAuthorizationManager();
			protectionElement = authManager.getProtectionElement(
					protectionElementName);
			protectionGroups = authManager.getProtectionGroups(
					protectionElement.getProtectionElementId().toString());
			it = protectionGroups.iterator();
			while (it.hasNext()) {
				protectionGroup = (ProtectionGroup) it.next();
				name = protectionGroup.getProtectionGroupName();
				if (name.indexOf(nameConsistingOf) != -1) {
					Logger.out.debug("protection group by name "
							+ nameConsistingOf + " for Protection Element "
							+ protectionElementName + " is " + name);
					return name;
				}
			}
		} catch (CSException e) {
			Logger.out.debug("Unable to get protection group by name "
					+ nameConsistingOf + " for Protection Element "
					+ protectionElementName + e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
		return name;

	}

	/**
	 * This method returns name of the Protection groupwhich consists of obj as
	 * Protection Element and whose name consists of string nameConsistingOf
	 * 
	 * @param obj
	 * @param nameConsistingOf
	 * @return @throws
	 *         SMException
	 */
	public String[] getProtectionGroupByName(AbstractDomainObject obj
	) throws SMException {
		Set protectionGroups;
		Iterator it;
		ProtectionGroup protectionGroup;
		ProtectionElement protectionElement;
		String name = null;
		String[] names = null;
		String protectionElementName = obj.getObjectId();
		try {
			AuthorizationManager authManager = getAuthorizationManager();
			protectionElement = authManager.getProtectionElement(
					protectionElementName);
			protectionGroups = authManager.getProtectionGroups(
					protectionElement.getProtectionElementId().toString());
			it = protectionGroups.iterator();
			names = new String[protectionGroups.size()];
			int i=0;
			while (it.hasNext()) {
				protectionGroup = (ProtectionGroup) it.next();
				names[i++] = protectionGroup.getProtectionGroupName();

			}
		} catch (CSException e) {
			Logger.out.debug("Unable to get protection group by name "
					+  " for Protection Element "
					+ protectionElementName + e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
		return names;

	}

	/**
	 * Returns name value beans corresponding to all privileges that can be
	 * assigned for Assign Privileges Page
	 * 
	 * @param roleName
	 *            role name of user logged in
	 * @return
	 */
	public Vector getPrivilegesForAssignPrivilege(String roleName) {
		Vector privileges = new Vector();
		NameValueBean nameValueBean;
		nameValueBean = new NameValueBean(Permissions.READ, Permissions.READ);
		privileges.add(nameValueBean);
		//Use privilege only provided to Administrtor in Assing privileges page.
		if(roleName.equals(Constants.ADMINISTRATOR))
		{
			nameValueBean = new NameValueBean(Permissions.USE, Permissions.USE);
			privileges.add(nameValueBean);
		}
		return privileges;
	}

	/**
	 * This method returns NameValueBeans for all the objects of type objectType
	 * on which user with identifier userID has privilege ASSIGN_ <
	 * <privilegeName>>.
	 * 
	 * @param userID
	 * @param objectType
	 * @param privilegeName
	 * @return @throws
	 *         SMException thrown if any error occurs while retreiving
	 *         ProtectionElementPrivilegeContextForUser
	 */
	private Set getObjectsForAssignPrivilege(Collection privilegeMap,
			String objectType, String privilegeName) throws SMException {
		Logger.out.debug(" objectType:" + objectType + " privilegeName:"
				+ privilegeName);
		Set objects = new HashSet();
		NameValueBean nameValueBean;

		ObjectPrivilegeMap objectPrivilegeMap;

		Collection privileges;
		Iterator iterator;
		String objectId;
		Privilege privilege;

		if (privilegeMap != null) {
			iterator = privilegeMap.iterator();
			while (iterator.hasNext()) {
				objectPrivilegeMap = (ObjectPrivilegeMap) iterator.next();
				objectId = objectPrivilegeMap.getProtectionElement()
				.getObjectId();
				Logger.out.debug("PE Name .................."+objectPrivilegeMap.getProtectionElement().getProtectionElementName());
				Logger.out.debug("PE objectId : "+objectId);
				if (objectId.indexOf(objectType + "_") != -1) {
					privileges = objectPrivilegeMap.getPrivileges();
					Logger.out.debug("Privileges:" + privileges.size());
					Iterator it = privileges.iterator();
					while (it.hasNext()) {
						privilege = (Privilege) it.next();
						Logger.out.debug(" Privilege:" + privilege.getName());

						if (privilege.getName().equals(
								"ASSIGN_" + privilegeName)) {
							nameValueBean = new NameValueBean(objectId
									.substring(objectId.lastIndexOf("_") + 1),
									objectId.substring(objectId
											.lastIndexOf("_") + 1));
							objects.add(nameValueBean);
							Logger.out.debug(nameValueBean);
							break;
						}
					}
				}
			}
		}

		return objects;
	}

	/**
	 * This method returns name value beans of the object ids for types
	 * identified by objectTypes on which user can assign privileges identified
	 * by privilegeNames User needs to have ASSIGN_ < <privilegeName>>privilege
	 * on these objects to assign corresponding privilege on them identified by
	 * userID has
	 * 
	 * @param userID
	 * @param objectTypes
	 * @param privilegeNames
	 * @return @throws
	 *         SMException
	 */
	public Set getObjectsForAssignPrivilege(String userID,
			String[] objectTypes, String[] privilegeNames) throws SMException {
		Set objects = new HashSet();
		AuthorizationManager authorizationManager;
		Collection privilegeMap;
		List list;
		try {
			if (objectTypes == null || privilegeNames == null) {
				return objects;
			}
			authorizationManager = getAuthorizationManager();

			ProtectionElement protectionElement;
			ProtectionElementSearchCriteria protectionElementSearchCriteria;
			User user = new User();
			user = getUserById(userID);
			if (user == null) {
				Logger.out.debug(" User not found");
				return objects;
			}
			Logger.out.debug("user login name:" + user.getLoginName());

			for (int i = 0; i < objectTypes.length; i++) {
				for (int j = 0; j < privilegeNames.length; j++) {

					try {
						Logger.out.debug("objectType:" + objectTypes[i]);
						protectionElement = new ProtectionElement();
						protectionElement.setObjectId(objectTypes[i] + "_*");
						protectionElementSearchCriteria = new ProtectionElementSearchCriteria(
								protectionElement);
						list = getObjects(protectionElementSearchCriteria);
						privilegeMap = authorizationManager.getPrivilegeMap(
								user.getLoginName(), list);
						for (int k = 0; k < list.size(); k++) {
							protectionElement = (ProtectionElement) list.get(k);
							Logger.out.debug(protectionElement.getObjectId()
									+ " " + protectionElement.getAttribute());
						}

						objects
						.addAll(getObjectsForAssignPrivilege(
								privilegeMap, objectTypes[i],
								privilegeNames[j]));
					} catch (SMException smex) {
						Logger.out.debug(" Exception:", smex);
					}
				}
			}
		} catch (CSException e) {
			Logger.out.debug("Unable to get objects: Exception: "
					+ e.getMessage());
			throw new SMException(e.getMessage(), e);
		}
		return objects;

	}

	
	/**
	 * @param sessionDataBean
	 * @param queryResultObjectDataMap
	 * @param aList
	 */
	@Deprecated
	public void filterRow(SessionDataBean sessionDataBean,
			Map queryResultObjectDataMap, List aList) 
	{    
		// boolean that indicated whether user has privilege on main object
		boolean isAuthorizedForMain = false;

		// boolean that indicated whether user has privilege on related object
		boolean isAuthorizedForRelated = false;

		// boolean that indicates whether user has privilege on identified data
		boolean hasPrivilegeOnIdentifiedData = false;

		Vector objectColumnIds;
		Set keySet = queryResultObjectDataMap.keySet();
		Iterator keyIterator = keySet.iterator();
		QueryResultObjectData queryResultObjectData2;
		QueryResultObjectData queryResultObjectData3;
		Vector queryObjects;
		Map columnIdsMap = new HashMap();

		//Aarti: For all objects in objectIdentifiers check permission on the
		// objects
		//In case user is not authorized to access an object make
		//value of all the columns that are dependent on this object ##
		//		for(int j=0; j< objectIdentifiers.length; j++)
		//		{
		//			isAuthorized =
		// checkPermission(sessionDataBean.getUserName(),objectIdentifiers[j][0],aList.get(Integer.parseInt(objectIdentifiers[j][1])));
		//			if(!isAuthorized)
		//			{
		//				objectColumnIds = (Vector)columnIdsMap.get(objectIdentifiers[j][0]);
		//				if(objectColumnIds!=null)
		//				{
		//					for(int k=0; k<objectColumnIds.size();k++)
		//					{
		//						aList.set(((Integer)objectColumnIds.get(k)).intValue()-1,"##");
		//					}
		//				}
		//			}
		//		}

		for (; keyIterator.hasNext();) 
		{
			queryResultObjectData2 = (QueryResultObjectData) queryResultObjectDataMap
			.get(keyIterator.next());
			isAuthorizedForMain = checkPermission(sessionDataBean.getUserName(), queryResultObjectData2
					.getAliasName(), aList.get(queryResultObjectData2
							.getIdentifierColumnId()), Permissions.READ_DENIED);

			isAuthorizedForMain = !isAuthorizedForMain;
			Logger.out.debug("Main object:"
					+ queryResultObjectData2.getAliasName()
					+ " isAuthorizedForMain:" + isAuthorizedForMain);

			//Remove the data from the fields directly related to main object
			if (!isAuthorizedForMain) 
			{
				Logger.out.debug("Removed Main Object Fields...................");
				removeUnauthorizedFieldsData(aList, queryResultObjectData2, false);
			} 
			else
			{
				Logger.out.debug("For Identified Data : User : "+sessionDataBean.getUserName()+" Alias Name : "
						+queryResultObjectData2.getAliasName()+"Identifed Column : "+aList.get(queryResultObjectData2.getIdentifierColumnId()));
				hasPrivilegeOnIdentifiedData = checkPermission(sessionDataBean
						.getUserName(), queryResultObjectData2.getAliasName(),
						aList.get(queryResultObjectData2.getIdentifierColumnId()),
						Permissions.IDENTIFIED_DATA_ACCESS);

				Logger.out.debug("hasPrivilegeOnIdentifiedData:"
						+ hasPrivilegeOnIdentifiedData);

				if (!hasPrivilegeOnIdentifiedData) 
				{
					removeUnauthorizedFieldsData(aList, queryResultObjectData2, true);
				}
			}

			Logger.out.debug("isAuthorizedForMain***********************"+isAuthorizedForMain);
			// Check the privilege on related objects when the privilege on main object is de-assigned.
			Logger.out.debug("Check Permission of Related Objects..................");
			queryObjects = queryResultObjectData2.getRelatedQueryResultObjects();
			for (int j = 0; j < queryObjects.size(); j++) 
			{
				queryResultObjectData3 = (QueryResultObjectData) queryObjects.get(j);

				//If authorized to see the main object then check for
				// authorization on dependent object
				if (isAuthorizedForMain) 
				{
					isAuthorizedForRelated = checkPermission(sessionDataBean
							.getUserName(), queryResultObjectData3
							.getAliasName(), aList.get(queryResultObjectData3
									.getIdentifierColumnId()), Permissions.READ_DENIED);
					isAuthorizedForRelated = !isAuthorizedForRelated;
				}
				//else set it false
				else 
				{
					isAuthorizedForRelated = false;
				}

				Logger.out.debug("Related object:"
						+ queryResultObjectData3.getAliasName()
						+ " isAuthorizedForRelated:" + isAuthorizedForRelated);

				//If not authorized to see related objects
				//remove the data from the fields directly related to related
				// object
				if (!isAuthorizedForRelated) 
				{
					removeUnauthorizedFieldsData(aList, queryResultObjectData3,
							false);
				} 
				else 
				{
					hasPrivilegeOnIdentifiedData = checkPermission(
							sessionDataBean.getUserName(),
							queryResultObjectData3.getAliasName(), aList
							.get(queryResultObjectData3.getIdentifierColumnId()),
							Permissions.IDENTIFIED_DATA_ACCESS);

					if (!hasPrivilegeOnIdentifiedData) 
					{
						removeUnauthorizedFieldsData(aList,queryResultObjectData3, true);
					}
				}
			}
		}
	}

	/**
	 * This method removes data from list aList.
	 * It could be all data related to QueryResultObjectData
	 * or only the identified fields depending on 
	 * the value of boolean removeOnlyIdentifiedData
	 * user
	 * @param aList
	 * @param queryResultObjectData3
	 * @param removeOnlyIdentifiedData
	 */
	@Deprecated
	private void removeUnauthorizedFieldsData(List aList,
			QueryResultObjectData queryResultObjectData3,
			boolean removeOnlyIdentifiedData) {

		Logger.out.debug(" Table:" + queryResultObjectData3.getAliasName()
				+ " removeOnlyIdentifiedData:" + removeOnlyIdentifiedData);
		Vector objectColumnIds;

		//If removeOnlyIdentifiedData is true then get Identified data column
		// ids
		//else get all column Ids to remove them
		if (removeOnlyIdentifiedData) {
			objectColumnIds = queryResultObjectData3
			.getIdentifiedDataColumnIds();
		} else {
			objectColumnIds = queryResultObjectData3.getDependentColumnIds();
		}
		Logger.out.debug("objectColumnIds:" + objectColumnIds);
		if (objectColumnIds != null) {
			for (int k = 0; k < objectColumnIds.size(); k++) {
				aList.set(((Integer) objectColumnIds.get(k)).intValue() - 1, "##");
			}
		}
	}


	/**
	 * This method checks whether user identified by userName has given
	 * permission on object identified by identifier of table identified by
	 * tableAlias
	 * 
	 * @param userName
	 * @param tableAlias
	 * @param identifier
	 * @param permission
	 * @return
	 */
	@Deprecated
	public boolean checkPermission(String userName, String tableAlias,
			Object identifier, String permission) 
	{   
		if(Boolean.parseBoolean(XMLPropertyHandler.getValue(Constants.ISCHECKPERMISSION)))
		{
			boolean isAuthorized = false;
			String tableName = (String) AbstractClient.objectTableNames.get(tableAlias);
			Logger.out.debug(" AliasName:" + tableAlias + " tableName:" + tableName
					+ " Identifier:" + identifier + " Permission:" + permission + " userName" + userName);

			String securityDataPrefixForTable;

			//Aarti: Security Data in database might be on the basis of classname/table name/table alias name
			//Depending on the option that an application chooses corresponding prefix is used to check permissions
			if(securityDataPrefix.equals(CLASS_NAME))
			{
				securityDataPrefixForTable = HibernateMetaData.getClassName(tableName);
				if(tableName.equals(Constants.CATISSUE_SPECIMEN))
				{
					try
					{
						Class classObject = Class.forName(securityDataPrefixForTable);
						securityDataPrefixForTable = classObject.getSuperclass().getName();
					}
					catch (ClassNotFoundException classNotExp)
					{
						Logger.out.debug("Class "+securityDataPrefixForTable+" not present.");
					}
				}

				//Get classname mapping to tableAlias
				if (securityDataPrefixForTable == null) {
					return isAuthorized;
				}
			}
			else if(securityDataPrefix.equals(TABLE_ALIAS_NAME))
			{
				securityDataPrefixForTable = tableAlias;
			}
			else if(securityDataPrefix.equals(TABLE_NAME))
			{
				securityDataPrefixForTable = tableName;
			}
			else
			{
				securityDataPrefixForTable = "";
			}

			//checking privilege type on class.
			//whether it is class level / object level / no privilege
			int privilegeType = Integer.parseInt((String) AbstractClient.privilegeTypeMap.get(tableAlias));
			Logger.out.debug(" privilege type:" + privilegeType);

			try {
				//If type of privilege is class level check user's privilege on
				// class
				if (privilegeType == Constants.CLASS_LEVEL_SECURE_RETRIEVE) {
					isAuthorized = SecurityManager.getInstance(this.getClass())
					.isAuthorized(userName, securityDataPrefixForTable, permission);
				}
				//else if it is object level check user's privilege on object
				// identifier
				else if (privilegeType == Constants.OBJECT_LEVEL_SECURE_RETRIEVE) {
					isAuthorized = SecurityManager.getInstance(this.getClass())
					.checkPermission(userName, securityDataPrefixForTable,
							String.valueOf(identifier), permission);
				}
				//else no privilege needs to be checked
				else if (privilegeType == Constants.INSECURE_RETRIEVE) 
				{
					isAuthorized = true;
				}

			} catch (SMException e) {
				Logger.out.debug(" Exception while checking permission:"
						+ e.getMessage(), e);
				return isAuthorized;
			}
			return isAuthorized;
		}
		return true;
	}

	/**
	 * This method returns true if user has privilege on identified data in list
	 * else false
	 * @author aarti_sharma
	 * @param sessionDataBean
	 * @param queryResultObjectDataMap
	 * @param list
	 * @return
	 */
	@Deprecated
	public boolean hasPrivilegeOnIdentifiedData(
			SessionDataBean sessionDataBean, Map queryResultObjectDataMap,
			List aList) {
		// boolean that indicates whether user has privilege on identified data
		boolean hasPrivilegeOnIdentifiedData = true;

		Set keySet = queryResultObjectDataMap.keySet();
		Iterator keyIterator = keySet.iterator();
		QueryResultObjectData queryResultObjectData2;

		for (; keyIterator.hasNext();) {

			queryResultObjectData2 = (QueryResultObjectData) queryResultObjectDataMap
			.get(keyIterator.next());

			if (hasAssociatedIdentifiedData(queryResultObjectData2.getAliasName())) {

				hasPrivilegeOnIdentifiedData = checkPermission(sessionDataBean
						.getUserName(), queryResultObjectData2.getAliasName(),
						aList.get(queryResultObjectData2
								.getIdentifierColumnId()),
								Permissions.IDENTIFIED_DATA_ACCESS);
				//if user does not have privilege on even a single identified
				// data in row
				//user does not have privilege on all the identified data in
				// that row
				if (!hasPrivilegeOnIdentifiedData) {
					hasPrivilegeOnIdentifiedData = false;
					return hasPrivilegeOnIdentifiedData;
				}
			}
		}

		return hasPrivilegeOnIdentifiedData;
	}

	/**
	 * Checks whether an object type has any identified data associated with
	 * it or not
	 * @param aliasName
	 * @return
	 */
	private boolean hasAssociatedIdentifiedData(String aliasName) {
		boolean hasIdentifiedData = false;
		String dataElementTableName;
		Vector identifiedData = new Vector();
		Logger.out.debug(this);
		Logger.out.debug(aliasName);

		identifiedData = (Vector) AbstractClient.identifiedDataMap.get(aliasName);
		Logger.out.debug("Table:" + aliasName + " Identified Data:"
				+ identifiedData);
		if (identifiedData != null) {
			Logger.out.debug(" identifiedData not null..." + identifiedData);
			hasIdentifiedData = true;
		}
		return hasIdentifiedData;
	}

	public static String getSecurityDataPrefix() {
		return securityDataPrefix;
	}
	public static void setSecurityDataPrefix(String securityDataPrefix) {
		SecurityManager.securityDataPrefix = securityDataPrefix;
	}

	/**
	 * Name : Aarti Sharma
	 * Reviewer: Sachin Lale
	 * Bug ID: 4111
	 * Patch ID: 4111_2
	 * See also: 4111_1
	 * Description: This method checks user's privilege on identified data
	 * @param userId User's Identifier
	 * @return true if user has privilege on identified data else false
	 * @throws SMException
	 */
	public boolean hasIdentifiedDataAccess(Long userId) throws SMException
	{
		boolean hasIdentifiedDataAccess = false;
		try
		{
			//Get user's role
			Role role = getUserRole(userId.longValue());
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();

			//Get privileges the user has based on his role
			Set privileges = userProvisioningManager.getPrivileges(String.valueOf(role.getId()));
			Iterator privIterator = privileges.iterator();
			Privilege privilege;

			// If user has Identified data access set hasIdentifiedDataAccess true
			for(int i=0; i< privileges.size(); i++)
			{
				privilege = (Privilege) privIterator.next();
				if(privilege.getName().equals(Permissions.IDENTIFIED_DATA_ACCESS))
				{
					hasIdentifiedDataAccess = true;
					break;
				}
			}
		}
		catch (CSException e)
		{
			throw new SMException(e.getMessage(), e);
		}
		return hasIdentifiedDataAccess;

	}
		
//	public static void main(String[] args)
//	{		
//	try
//	{
//	boolean isAuthorized = getAuthorizationManager().checkPermission(
//	"admin@admin.com", "ParticipantMedicalIdentifier" + "_" + "1",
//	"READ");
//	}
//	catch (CSException e)
//	{
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//	}
//	}
	
	
}


