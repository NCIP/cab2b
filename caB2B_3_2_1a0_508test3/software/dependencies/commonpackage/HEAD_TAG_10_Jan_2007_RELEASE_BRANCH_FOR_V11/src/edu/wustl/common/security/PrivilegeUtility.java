/**
 * Utility class for methods related to CSM 
 */
package edu.wustl.common.security;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import edu.wustl.common.security.PrivilegeUtility;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.Identifiable;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.Application;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroupRoleContext;
import gov.nih.nci.security.dao.ApplicationSearchCriteria;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.PrivilegeSearchCriteria;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.exceptions.CSTransactionException;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.beans.SecurityDataBean;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.Constants;

/**
 * Utility class for methods related to CSM 
 * 
 * @author ravindra_jain
 *
 */
public class PrivilegeUtility 
{
	private static AuthorizationManager authorizationManager = null;
	private Class requestingClass = PrivilegeUtility.class;
	
	   
	/**
	 * This method creates protection elements corresponding to protection
	 * objects passed and associates them with static as well as dynamic
	 * protection groups that are passed. It also creates user group, role,
	 * protection group mapping for all the elements in authorization data
	 * 
	 * @param authorizationData
	 *            Vector of SecurityDataBean objects
	 * @param protectionObjects
	 *            Set of AbstractDomainObject instances
	 * @param dynamicGroups
	 *            Array of dynamic group names
	 * @throws SMException
	 */
	public void insertAuthorizationData(Vector authorizationData,
			Set protectionObjects, String[] dynamicGroups) throws SMException {

		Set protectionElements;

		try {

			/**
			 * Create protection elements corresponding to all protection
			 * objects
			 */
			protectionElements = createProtectionElementsFromProtectionObjects(protectionObjects);

			/**
			 * Create user group role protection group and their mappings if
			 * required
			 */
			if (authorizationData != null)
			{
				createUserGroupRoleProtectionGroup(authorizationData,
						protectionElements);
			}

			/**
			 * Assigning protection elements to dynamic groups
			 */
			assignProtectionElementsToGroups(protectionElements, dynamicGroups);

			Logger.out
			.debug("************** Inserted authorization Data ***************");

		} catch (CSException e) {
			Logger.out.fatal("The Security Service encountered "
					+ "a fatal exception.", e);
			throw new SMException(
					"The Security Service encountered a fatal exception.", e);
		}
	}


	/**
	 * This method creates protection elements from the protection objects
	 * passed and associate them with respective static groups they should be
	 * added to depending on their class name if the corresponding protection
	 * element does not already exist.
	 * 
	 * @param protectionObjects
	 * @return @throws
	 *         CSException
	 */
	private Set createProtectionElementsFromProtectionObjects(
			Set protectionObjects) throws CSException {
		ProtectionElement protectionElement;
		Set protectionElements = new HashSet();
		Identifiable protectionObject;
		Iterator it;

		UserProvisioningManager userProvisioningManager = getUserProvisioningManager();

		if (protectionObjects != null) {
			for (it = protectionObjects.iterator(); it.hasNext();) {
				protectionElement = new ProtectionElement();
				protectionObject = (Identifiable) it.next();
				protectionElement.setObjectId(protectionObject.getObjectId());

				populateProtectionElement(
						protectionElement, protectionObject, userProvisioningManager);
				protectionElements.add(protectionElement);
			}
		}
		return protectionElements;
	}


	/**
	 * This method creates user group, role, protection group mappings in
	 * database for the passed authorizationData. It also adds protection
	 * elements to the protection groups for which mapping is made. For each
	 * element in authorization Data passed: User group is created and users are
	 * added to user group if one does not exist by the name passed. Similarly
	 * Protection Group is created and protection elements are added to it if
	 * one does not exist. Finally user group and protection group are
	 * associated with each other by the role they need to be associated with.
	 * If no role exists by the name an exception is thrown and the
	 * corresponding mapping is not created
	 * 
	 * @param authorizationData
	 * @param protectionElements
	 * @throws CSException
	 * @throws SMException
	 */ 
	private void createUserGroupRoleProtectionGroup(Vector authorizationData,
			Set protectionElements) throws CSException, SMException {
		ProtectionGroup protectionGroup = null;
		SecurityDataBean userGroupRoleProtectionGroupBean;
		RoleSearchCriteria roleSearchCriteria;
		Role role;
		String[] roleIds = null;
		List list;
		ProtectionGroupSearchCriteria protectionGroupSearchCriteria;
		Group group;
		GroupSearchCriteria groupSearchCriteria;
		Set userGroup;
		User user;
		Iterator it;
		UserProvisioningManager userProvisioningManager = getUserProvisioningManager();

		if (authorizationData != null) {
			Logger.out.debug(" UserGroupRoleProtectionGroup Size:"
					+ authorizationData.size());
			for (int i = 0; i < authorizationData.size(); i++) {
				Logger.out.debug(" authorizationData:" + i + " "
						+ authorizationData.get(i).toString());
				group = new Group();

				try {
					userGroupRoleProtectionGroupBean = (SecurityDataBean) authorizationData
					.get(i);

					group
					.setApplication(getApplication(SecurityManager.APPLICATION_CONTEXT_NAME));
					group.setGroupName(userGroupRoleProtectionGroupBean
							.getGroupName());
					groupSearchCriteria = new GroupSearchCriteria(group);
					/**
					 * If group already exists
					 */
					try {
						list = getObjects(groupSearchCriteria);
						Logger.out.debug("User group " + group.getGroupName()
								+ " already exists");
					}
					/**
					 * If group does not exist already
					 */
					catch (SMException ex) {
						Logger.out.debug("User group " + group.getGroupName()
								+ " does not exist");
						//                        group.setUsers(userGroupRoleProtectionGroupBean.getGroup());
						userProvisioningManager.createGroup(group);
						Logger.out.debug("User group " + group.getGroupName()
								+ " created");
						Logger.out.debug("Users added to group : "
								+ group.getUsers());
						list = getObjects(groupSearchCriteria);
					}
					group = (Group) list.get(0);

					/**
					 * Assigning group to users in userGroup
					 */
					userGroup = userGroupRoleProtectionGroupBean.getGroup();
					for (it = userGroup.iterator(); it.hasNext();) {
						user = (User) it.next();
						//                    userProvisioningManager.assignGroupsToUser(String.valueOf(user.getUserId()),new
						// String[] {String.valueOf(group.getGroupId())});
						assignAdditionalGroupsToUser(String.valueOf(user
								.getUserId()), new String[] { String
							.valueOf(group.getGroupId()) });
						Logger.out.debug("userId:" + user.getUserId()
								+ " group Id:" + group.getGroupId());
					}

					protectionGroup = new ProtectionGroup();
					protectionGroup
					.setApplication(getApplication(SecurityManager.APPLICATION_CONTEXT_NAME));
					protectionGroup
					.setProtectionGroupName(userGroupRoleProtectionGroupBean
							.getProtectionGroupName());
					protectionGroupSearchCriteria = new ProtectionGroupSearchCriteria(
							protectionGroup);
					/**
					 * If Protection group already exists add protection
					 * elements to the group
					 */
					try {
						list = getObjects(protectionGroupSearchCriteria);
						protectionGroup = (ProtectionGroup) list.get(0);
						Logger.out.debug(" From Database: "
								+ protectionGroup.toString());
					}
					/**
					 * If the protection group does not already exist create the
					 * protection group and add protection elements to it.
					 */
					catch (SMException sme) {
						protectionGroup
						.setProtectionElements(protectionElements);
						userProvisioningManager
						.createProtectionGroup(protectionGroup);
						Logger.out.debug("Protection group created: "
								+ protectionGroup.toString());
					}

					role = new Role();
					role
					.setName(userGroupRoleProtectionGroupBean
							.getRoleName());
					roleSearchCriteria = new RoleSearchCriteria(role);
					list = getObjects(roleSearchCriteria);
					roleIds = new String[1];
					roleIds[0] = String.valueOf(((Role) list.get(0)).getId());
					userProvisioningManager.assignGroupRoleToProtectionGroup(
							String.valueOf(protectionGroup
									.getProtectionGroupId()), String
									.valueOf(group.getGroupId()), roleIds);
					Logger.out.debug("Assigned Group Role To Protection Group "
							+ protectionGroup.getProtectionGroupId() + " "
							+ String.valueOf(group.getGroupId()) + " "
							+ roleIds);
				} catch (CSTransactionException ex) {
					Logger.out.error(
							"Error occured Assigned Group Role To Protection Group "
							+ protectionGroup.getProtectionGroupId()
							+ " " + String.valueOf(group.getGroupId())
							+ " " + roleIds, ex);
					throw new SMException (ex.getMessage(),ex);
				}
			}
		}
	}


	/**
	 * This method assigns Protection Elements passed to the Protection group
	 * names passed.
	 * 
	 * @param protectionElements
	 * @param groups
	 * @throws CSException
	 */
	private void assignProtectionElementsToGroups(Set protectionElements,
			String[] groups) {
		ProtectionElement protectionElement;
		Iterator it;
		if (groups != null) {
			for (int i = 0; i < groups.length; i++) {
				for (it = protectionElements.iterator(); it.hasNext();) {
					protectionElement = (ProtectionElement) it.next();
					assignProtectionElementToGroup(protectionElement, groups[i]);
				}
			}
		}
	}


	/**
	 * @param protectionElement
	 * @param protectionObject
	 * @return
	 * @throws CSException
	 */
	private void populateProtectionElement(
			ProtectionElement protectionElement,
			Identifiable protectionObject, 
			UserProvisioningManager userProvisioningManager) throws CSException {
		try
		{
			protectionElement
			.setApplication(getApplication(SecurityManager.APPLICATION_CONTEXT_NAME));
			protectionElement
			.setProtectionElementDescription(protectionObject
					.getClass().getName()
					+ " object");
			protectionElement
			.setProtectionElementName(protectionObject.getObjectId());
			/**
			 * Adding protection elements to static groups they
			 * should be added to
			 */
			String [] staticGroups = (String[]) Constants.STATIC_PROTECTION_GROUPS_FOR_OBJECT_TYPES
			.get(protectionObject.getClass().getName());

			setProtectGroups(protectionElement,
					staticGroups);

			userProvisioningManager
			.createProtectionElement(protectionElement);

		} catch (CSTransactionException ex) {
			Logger.out.warn(
					ex.getMessage() + "Error occured while creating Potection Element "
					+ protectionElement.getProtectionElementName());
			throw new CSException (ex.getMessage(),ex);
		}

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
		Group group = new Group();
	
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
			Logger.out.debug("Search resulted in no results");
			throw new SMException("Search resulted in no results");
		}
		return list;
	}


	/**
	 * @param protectionElement
	 * @param userProvisioningManager
	 * @param dynamicGroups
	 * @param i
	 * @throws CSException
	 */
	private void assignProtectionElementToGroup(
			ProtectionElement protectionElement, String GroupsName)
	{
		try {
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
			userProvisioningManager.assignProtectionElement(GroupsName,
					protectionElement.getObjectId());
			Logger.out.debug("Associated protection group: " + GroupsName
					+ " to protectionElement"
					+ protectionElement.getProtectionElementName());
		}

		catch (CSException e) {
			Logger.out
			.error(
					"The Security Service encountered an error while associating protection group: "
					+ GroupsName
					+ " to protectionElement"
					+ protectionElement
					.getProtectionElementName());
		}
	}


	/**
	 * @param protectionElement
	 * @param staticGroups
	 * @param protectionGroups
	 * @return
	 * @throws CSException
	 */
	private void setProtectGroups(ProtectionElement protectionElement,
			String[] staticGroups) throws CSException {
		ProtectionGroup protectionGroup;
		Set protectionGroups = null;
		ProtectionGroupSearchCriteria protectionGroupSearchCriteria;
		if (staticGroups != null) {
			protectionGroups = new HashSet();
			for (int i = 0; i < staticGroups.length; i++) {
				Logger.out.debug(" group name " + i + " "
						+ staticGroups[i]);
				protectionGroup = new ProtectionGroup();
				protectionGroup
				.setProtectionGroupName(staticGroups[i]);
				protectionGroupSearchCriteria = new ProtectionGroupSearchCriteria(
						protectionGroup);
				try {
					List list = getObjects(protectionGroupSearchCriteria);
					protectionGroup = (ProtectionGroup) list
					.get(0);
					Logger.out.debug(" From Database: "
							+ protectionGroup.toString());
					protectionGroups.add(protectionGroup);
				} catch (SMException sme) {
					Logger.out.warn(
							"Error occured while retrieving "
							+ staticGroups[i]
							               + "  From Database: ");
				}
			}
			protectionElement
			.setProtectionGroups(protectionGroups);
		}
	}


	public Application getApplication(String applicationName)
	throws CSException {
		Application application = new Application();
		application.setApplicationName(applicationName);
		ApplicationSearchCriteria applicationSearchCriteria = new ApplicationSearchCriteria(
				application);
		application = (Application) getUserProvisioningManager().getObjects(
				applicationSearchCriteria).get(0);
		return application;
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
	 * Returns the Authorization Manager for the caTISSUE Core. This method
	 * follows the singleton pattern so that only one AuthorizationManager is
	 * created.
	 * 
	 * @return @throws
	 *         CSException
	 */
	protected AuthorizationManager getAuthorizationManager() throws CSException {
		//Creating instance of security manager to initialize and set constants default value
		SecurityManager securityManager = SecurityManager.getInstance(this.getClass());
		if (authorizationManager == null) {
			synchronized (requestingClass) {
				if (authorizationManager == null) {
					authorizationManager = SecurityServiceProvider
					.getAuthorizationManager(SecurityManager.APPLICATION_CONTEXT_NAME);
				}
			}
		}
		return authorizationManager;
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
	 * This method returns role corresponding to the rolename passed
	 * 
	 * @param privilegeName
	 * @param list
	 * @return @throws
	 *         CSException
	 * @throws SMException
	 */
	public Role getRole(String roleName) throws CSException, SMException {
		Logger.out.debug(" roleName:" + roleName);
		if (roleName == null) {
			Logger.out.debug("Rolename passed is null");
			throw new SMException("No role of name null");
		}

		//Search for role by the name roleName
		RoleSearchCriteria roleSearchCriteria;
		Role role;
		role = new Role();
		role.setName(roleName);
		role.setApplication(getApplication(SecurityManager.APPLICATION_CONTEXT_NAME));
		roleSearchCriteria = new RoleSearchCriteria(role);
		List list;
		try {
			list = getObjects(roleSearchCriteria);
		} catch (SMException e) {
			Logger.out.debug("Role not found by name " + roleName);
			throw new SMException("Role not found by name " + roleName, e);
		}
		role = (Role) list.get(0);
		Logger.out.debug(" RoleId of role " + role.getName() + " is "
				+ role.getId());
		return role;
	}
	
	public Set<Privilege> getRolePrivileges(String id) throws CSObjectNotFoundException, CSException
	{
		
	  return getUserProvisioningManager().getPrivileges(id);
		
	}
	
	
	/**
	 * This method returns protection group corresponding to the naem passed. In
	 * case it does not exist it creates one and returns that.
	 * 
	 * @param protectionGroupName
	 * @return @throws
	 *         CSException
	 * @throws CSTransactionException
	 * @throws SMException
	 */
	public ProtectionGroup getProtectionGroup(String protectionGroupName)
	throws CSException, CSTransactionException, SMException {
		Logger.out.debug(" protectionGroupName:" + protectionGroupName);
		if (protectionGroupName == null) {
			Logger.out.debug("protectionGroupName passed is null");
			throw new SMException("No protectionGroup of name null");
		}

		//Search for Protection Group of the name passed
		ProtectionGroupSearchCriteria protectionGroupSearchCriteria;
		ProtectionGroup protectionGroup;
		protectionGroup = new ProtectionGroup();
		protectionGroup.setProtectionGroupName(protectionGroupName);
		protectionGroupSearchCriteria = new ProtectionGroupSearchCriteria(
				protectionGroup);
		UserProvisioningManager userProvisioningManager = null;
		List list;
		try {
			userProvisioningManager = getUserProvisioningManager();
			list = getObjects(protectionGroupSearchCriteria);
		} catch (SMException e) {
			Logger.out.debug("Protection Group not found by name "
					+ protectionGroupName);
			userProvisioningManager.createProtectionGroup(protectionGroup);
			list = getObjects(protectionGroupSearchCriteria);
		}
		protectionGroup = (ProtectionGroup) list.get(0);

		Logger.out.debug(" ID of ProtectionGroup "
				+ protectionGroup.getProtectionGroupName() + " is "
				+ protectionGroup.getProtectionGroupId());
		return protectionGroup;
	}
	
	
	/**
	 * This method assigns additional protection Elements identified by
	 * protectionElementIds to the protection Group identified by
	 * protectionGroupName
	 * 
	 * @param protectionGroupName
	 * @param objectIds
	 * @throws SMException
	 */
	public void assignProtectionElements(String protectionGroupName,
			Class objectType, Long[] objectIds) throws SMException {
		try {
			Logger.out.debug("Protection Group Name:" + protectionGroupName
					+ " objectType:" + objectType + " protectionElementIds:"
					+ edu.wustl.common.util.Utility.getArrayString(objectIds));

			if (protectionGroupName == null || objectType == null
					|| objectIds == null) {
				Logger.out.debug(" One of the parameters is null");
				throw new SMException(
				"Could not assign Protection elements to protection group. One or more parameters are null");
			}

			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
			for (int i = 0; i < objectIds.length; i++) {
				try {
					userProvisioningManager.assignProtectionElement(
							protectionGroupName, objectType.getName() + "_"
							+ objectIds[i]);
				} catch (CSTransactionException txex) //thrown when association
				// already exists
				{
					Logger.out.debug("Exception:" + txex.getMessage());
				}
			}
		} catch (CSException csex) {
			Logger.out.debug(
					"Could not assign Protection elements to protection group",
					csex);
			throw new SMException(
					"Could not assign Protection elements to protection group",
					csex);
		}
	}
	
	
	/**
	 * This method assigns user identified by userId, roles identified by roles
	 * on protectionGroup
	 * 
	 * @param userId
	 * @param roles
	 * @param protectionGroup
	 * @throws SMException
	 */
	public void assignUserRoleToProtectionGroup(Long userId, Set roles,
			ProtectionGroup protectionGroup, boolean assignOperation) throws SMException {
		Logger.out.debug("userId:" + userId + " roles:" + roles
				+ " protectionGroup:" + protectionGroup);
		if (userId == null || roles == null || protectionGroup == null) {
			Logger.out
			.debug("Could not assign user role to protection group. One or more parameters are null");
			throw new SMException(
			"Could not assign user role to protection group");
		}
		Set protectionGroupRoleContextSet;
		ProtectionGroupRoleContext protectionGroupRoleContext;
		Iterator it;
		Set aggregatedRoles = new HashSet();
		String[] roleIds = null;
		try {
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
			protectionGroupRoleContextSet = userProvisioningManager
			.getProtectionGroupRoleContextForUser(String
					.valueOf(userId));

			//get all the roles that user has on this protection group
			it = protectionGroupRoleContextSet.iterator();
			while (it.hasNext()) {
				protectionGroupRoleContext = (ProtectionGroupRoleContext) it
				.next();
				if (protectionGroupRoleContext.getProtectionGroup()
						.getProtectionGroupId().equals(
								protectionGroup.getProtectionGroupId())) {
					aggregatedRoles.addAll(protectionGroupRoleContext
							.getRoles());

					break;
				}
			}

			// if the operation is assign, add the roles to be assigned.
			if (assignOperation == Constants.PRIVILEGE_ASSIGN)
			{
				aggregatedRoles.addAll(roles);
			}
			else // if the operation is de-assign, remove the roles to be de-assigned.
			{
				Set newaggregateRoles = removeRoles(aggregatedRoles, roles);
				aggregatedRoles = newaggregateRoles;
			}

			roleIds = new String[aggregatedRoles.size()];
			Iterator roleIt = aggregatedRoles.iterator();

			for (int i = 0; roleIt.hasNext(); i++) {
				roleIds[i] = String.valueOf(((Role) roleIt.next()).getId());
			}

			userProvisioningManager.assignUserRoleToProtectionGroup(String
					.valueOf(userId), roleIds, String.valueOf(protectionGroup
							.getProtectionGroupId()));

		} catch (CSException csex) {
			Logger.out.debug("Could not assign user role to protection group",
					csex);
			throw new SMException(
					"Could not assign user role to protection group", csex);
		}
	}
	
	
	private Set removeRoles(Set fromSet, Set toSet)
	{
		Set differnceRoles = new HashSet();
		Iterator fromSetiterator = fromSet.iterator();
		while (fromSetiterator.hasNext())
		{
			Role role1  = (Role) fromSetiterator.next();

			Iterator toSetIterator = toSet.iterator();
			while (toSetIterator.hasNext())
			{
				Role role2 = (Role) toSetIterator.next();

				if (role1.getId().equals(role2.getId()) == false)
				{
					differnceRoles.add(role1);
				}
			}
		}
		return differnceRoles;
	}
	
	
	/**
	 * @param protectionGroupName
	 * @param objectType
	 * @param objectIds
	 * @throws SMException
	 */
	public void deAssignProtectionElements(String protectionGroupName,
			Class objectType, Long[] objectIds) throws SMException {
		try {
			Logger.out.debug("Protection Group Name:" + protectionGroupName
					+ " protectionElementIds:"
					+ edu.wustl.common.util.Utility.getArrayString(objectIds));
			if (protectionGroupName == null || objectType == null
					|| objectIds == null) {
				Logger.out
				.debug("Cannot disassign protection elements. One of the parameters is null.");
				throw new SMException(
				"Could not deassign Protection elements to protection group. One of the parameters is null.");
			}
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
			for (int i = 0; i < objectIds.length; i++) {
				try {
					Logger.out.debug(" protectionGroupName:"
							+ protectionGroupName + " objectId:"
							+ objectType.getName() + "_" + objectIds[i]);
					userProvisioningManager.deAssignProtectionElements(
							protectionGroupName, objectType.getName() + "_"
							+ objectIds[i]);
				} catch (CSTransactionException txex) //thrown when no
				// association exists
				{
					Logger.out.debug("Exception:" + txex.getMessage(), txex);
				}
			}
		} catch (CSException csex) {
			Logger.out
			.debug(
					"Could not deassign Protection elements to protection group",
					csex);
			throw new SMException(
					"Could not deassign Protection elements to protection group",
					csex);
		}
	}
		
	
	public String getGroupIdForRole(String roleID) {
		if (roleID.equals(SecurityManager.rolegroupNamevsId.get(Constants.ADMINISTRATOR_ROLE))) {
			Logger.out.debug(" role corresponds to Administrator group");
			return SecurityManager.rolegroupNamevsId.get(Constants.ADMINISTRATOR_GROUP_ID);
		} else if (roleID.equals(SecurityManager.rolegroupNamevsId.get(Constants.SUPERVISOR_ROLE))) {
			Logger.out.debug(" role corresponds to Supervisor group");
			return SecurityManager.rolegroupNamevsId.get(Constants.SUPERVISOR_GROUP_ID);
		} else if (roleID.equals(SecurityManager.rolegroupNamevsId.get(Constants.TECHNICIAN_ROLE))) {
			Logger.out.debug(" role corresponds to Technician group");
			return SecurityManager.rolegroupNamevsId.get(Constants.TECHNICIAN_GROUP_ID);
		} else if (roleID.equals(SecurityManager.rolegroupNamevsId.get(Constants.PUBLIC_ROLE))) {
			Logger.out.debug(" role corresponds to public group");
			return SecurityManager.rolegroupNamevsId.get(Constants.PUBLIC_GROUP_ID);
		} else {
			Logger.out.debug("role corresponds to no group");
			return null;
		}
	}
	
	
	/**
	 * This method assigns user group identified by groupId, roles identified by
	 * roles on protectionGroup
	 * 
	 * @param groupId
	 * @param roles
	 * @param protectionGroup
	 * @throws SMException
	 */
	public void assignGroupRoleToProtectionGroup(Long groupId, Set roles,
			ProtectionGroup protectionGroup, boolean assignOperation) throws SMException {
		Logger.out.debug("userId:" + groupId + " roles:" + roles
				+ " protectionGroup:"
				+ protectionGroup.getProtectionGroupName());

		if (groupId == null || roles == null || protectionGroup == null) {
			Logger.out
			.debug("Could not assign group role to protection group. One or more parameters are null");
			throw new SMException(
			"Could not assign group role to protection group. One or more parameters are null");
		}
		Set protectionGroupRoleContextSet = null;
		ProtectionGroupRoleContext protectionGroupRoleContext = null;
		Iterator it;
		Set aggregatedRoles = new HashSet();
		String[] roleIds = null;
		Role role;
		try {
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();

			/**
			 * Name : Aarti Sharma
			 * Reviewer: Sachin Lale
			 * Bug ID: 4418
			 * Description: CSM API getProtectionGroupRoleContextForGroup throws exception
			 * CSObjectNotFoundException when called on oracle database thus leading to this problem.
			 * Check is made for this exception now so that the method works for oracle as well.
			 */
			try
			{
				protectionGroupRoleContextSet = userProvisioningManager
				.getProtectionGroupRoleContextForGroup(String
						.valueOf(groupId));
			}
			catch (CSObjectNotFoundException e) {
				Logger.out.debug("Could not find Role Context for the Group: "+
						e.toString());
			}

			if(protectionGroupRoleContext!=null)
			{

				it = protectionGroupRoleContextSet.iterator();
				while (it.hasNext()) {
					protectionGroupRoleContext = (ProtectionGroupRoleContext) it
					.next();
					if (protectionGroupRoleContext.getProtectionGroup()
							.getProtectionGroupId().equals(
									protectionGroup.getProtectionGroupId())) {
						aggregatedRoles.addAll(protectionGroupRoleContext
								.getRoles());

						break;
					}
				}
			}

			// if the operation is assign, add the roles to be assigned.
			if (assignOperation == Constants.PRIVILEGE_ASSIGN)
			{
				aggregatedRoles.addAll(roles);
			}
			else // if the operation is de-assign, remove the roles to be de-assigned.
			{
				Set newaggregateRoles = removeRoles(aggregatedRoles, roles);
				aggregatedRoles = newaggregateRoles;
			}

			roleIds = new String[aggregatedRoles.size()];
			Iterator roleIt = aggregatedRoles.iterator();

			for (int i = 0; roleIt.hasNext(); i++) {
				role = (Role) roleIt.next();
				Logger.out.debug(" Role " + i + 1 + " " + role.getName());
				roleIds[i] = String.valueOf(role.getId());
			}

			userProvisioningManager.assignGroupRoleToProtectionGroup(String
					.valueOf(protectionGroup.getProtectionGroupId()), String
					.valueOf(groupId), roleIds);

		} catch (CSException csex) {
			Logger.out.debug("Could not assign user role to protection group",
					csex);
			throw new SMException(
					"Could not assign user role to protection group", csex);
		}
	}
	 
	
	/*
	public List<Privilege> getPrivilegeList() throws CSException
	{
		Privilege privilege = new Privilege();
		privilege.setName("*");
		PrivilegeSearchCriteria privilegeSearchCriteria = new PrivilegeSearchCriteria(privilege);
		List<Privilege> prvilegeList =  getUserProvisioningManager().getObjects(privilegeSearchCriteria);
		return prvilegeList;
	} */
	
	public Privilege getPrivilegeById(String privilegeId) throws CSException
	{ 
		return getUserProvisioningManager().getPrivilegeById(privilegeId);
		
	}
}
