/**
 * PrivilegeCache will cache all privileges for a specific user. 
 * An instance of PrivilegeCache will be created for every user who logs in.
 */

package edu.wustl.common.security;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.PrivilegeUtility;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.ObjectPrivilegeMap;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;

/**
 * @author ravindra_jain creation date : 14th April, 2008
 * @version 1.0
 */
public class PrivilegeCache
{

	/*
	 * login name of the user who has logged in
	 */
	private String loginName;

	/*
	 * the map of object id and corresponding permissions / privileges
	 */
	private Map<String, BitSet> privilegeMap;

	
	/**
	 * After initialization of the variables, a call to method 'initialize()'
	 * is made initialize() uses some ProtectionElementSearchCriterias & gets
	 * Protection Elements from the database
	 * 
	 * @throws Exception
	 */
	public PrivilegeCache(String loginName) throws Exception
	{
		privilegeMap = new HashMap<String, BitSet>();
		this.loginName = loginName;

		initialize();
	}

	/**
	 * This method gets Protection Elements we are interested in, from the
	 * database We achieve this by using the ProtectionElementSearchCriteria
	 * provided by CSM Then, a call to getPrivilegeMap is made & we get a
	 * Collection of ObjectPrivilegeMap for each call Here, for every passed
	 * ProtectionElement,the method looks for the privileges that User has on
	 * the ProtectionElement such ObjectPrivilegeMaps are then passed to
	 * 'populatePrivileges' method
	 * 
	 * @throws Exception
	 */
	private void initialize() throws Exception
	{
		/*
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		Set set = privilegeUtility.getUserProvisioningManager().getProtectionElementPrivilegeContextForUser(userId);
		
		
		// To populate the privilege map
		for (Object o : set)
		{
			ProtectionElementPrivilegeContext context = (ProtectionElementPrivilegeContext) o;
			
			String objectId = context.getProtectionElement().getObjectId();

			BitSet bitSet = new BitSet();

			for (Object privilege : context.getPrivileges())
			{
				bitSet.set(getBitNumber(((Privilege) privilege).getName()));
			}
			
			privilegeMap.put(objectId, bitSet);
		}
		*/

		for (String className : PrivilegeManager.getInstance().getClasses())
		{
			Collection objectPrivilegeMap = getObjectPrivilegeMap(className);
			populatePrivileges(objectPrivilegeMap);
		}

		for (String objectPattern : PrivilegeManager.getInstance().getEagerObjects())
		{
			Collection objectPrivilegeMap = getObjectPrivilegeMap(objectPattern);
			populatePrivileges(objectPrivilegeMap);
		}

	}

	private Collection getObjectPrivilegeMap(String protectionElementObjectId)
	{
		Collection objectPrivilegeMap = new ArrayList();

		try
		{
			PrivilegeUtility privilegeUtility = new PrivilegeUtility();
			ProtectionElement protectionElement = new ProtectionElement();
			protectionElement.setObjectId(protectionElementObjectId);
			ProtectionElementSearchCriteria protectionElementSearchCriteria = new ProtectionElementSearchCriteria(
					protectionElement);
			List list = privilegeUtility.getUserProvisioningManager().getObjects(
					protectionElementSearchCriteria);

			if (list.size() > 0)
			{
				objectPrivilegeMap = privilegeUtility.getUserProvisioningManager().getPrivilegeMap(
						loginName, list);
			}
		}
		catch (CSException excp)
		{
			Logger.out.error(excp.getMessage(), excp);
		}

		return objectPrivilegeMap;
	}

	/**
	 * populatePrivileges does the Mapping (inserts into Map) of the Object Id's
	 * and corresponding BitSets.
	 * 
	 * BitSet is used for the Mapping. There are total 10 possible Privileges /
	 * Permissions. So, we use 10 bits of bitset for storing these Permissions
	 * For every objectId in PrivilegeMap, a bit '1' in BitSet indicates user
	 * has permission on that Privilege & a bit '0' indicates otherwise So, in
	 * this method, we examine each Privilege returned by PrivilegeMap & set the
	 * BitSet corresponding to the objectId accordingly.
	 * 
	 * @param objectPrivilegeMapCollection
	 */
	private void populatePrivileges(Collection<ObjectPrivilegeMap> objectPrivilegeMapCollection)
	{
		// To populate the permissionMap
		for (ObjectPrivilegeMap objectPrivilegeMap : objectPrivilegeMapCollection)
		{
			String objectId = objectPrivilegeMap.getProtectionElement().getObjectId();

			BitSet bitSet = new BitSet();

			for (Object privilege : objectPrivilegeMap.getPrivileges())
			{
				bitSet.set(getBitNumber(((Privilege) privilege).getName()));
			}
			privilegeMap.put(objectId, bitSet);
		}
	}
	
	
	/**
	 * Simply checks if the user has any privilage on given object id
	 *  
	 * @param objectId
	 * @return
	 */
	public boolean hasPrivilege(String objectId)
	{
		BitSet bitSet = privilegeMap.get(objectId);
		if(bitSet != null && bitSet.cardinality()>0)
		{
			return true;
		}
		
		return false;
	}

	/**
	 * To check for 'Class' & 'Action' level Privilege Here, we take className
	 * as the objectId & retrieve its associated BitSet from the privilegeMap
	 * Then, we check whether User has Permission over the passed Privilege or
	 * no & return true if user has privilege, false otherwise.
	 * 
	 * @param classObj
	 * @param privilegeName
	 * @return
	 */
	public boolean hasPrivilege(Class classObj, String privilegeName)
	{
		return hasPrivilege(classObj.getName(), privilegeName);
	}

	/**
	 * To check for 'Object' level Privilege Here, we take objectId from Object &
	 * retrieve its associated BitSet from the privilegeMap Then, we check
	 * whether User has Permission over the passed Privilege or no & return true
	 * if user has privilege, false otherwise.
	 * 
	 * @param aDObject
	 * @param privilegeName
	 * @return
	 */
	public boolean hasPrivilege(AbstractDomainObject aDObject, String privilegeName)
	{
		return hasPrivilege(aDObject.getObjectId(), privilegeName);
	}

	/**
	 * To check for Privilege given the ObjectId Here, we take objectId &
	 * retrieve its associated BitSet from the privilegeMap Then, we check
	 * whether User has Permission over the passed Privilege or no & return true
	 * if user has privilege, false otherwise.
	 * 
	 * @param objectId
	 * @param privilegeName
	 * @return
	 */
	public boolean hasPrivilege(String objectId, String privilegeName)
	{
		boolean isAuthorized = false;
		
		if (objectId.contains("edu.wustl.catissuecore.domain.User_"))
		{
			System.out.println("checking privileges for " + objectId);
		}

		BitSet bitSet = privilegeMap.get(objectId);

		if (bitSet == null)
		{
			for (String objectIdPart : PrivilegeManager.getInstance().getLazyObjects())
			{
				if (objectId.startsWith(objectIdPart))
				{
					bitSet = getPrivilegesFromDatabase(objectId, privilegeName);

					break;
				}
			}
		}
		
		if(bitSet != null)
		{
			isAuthorized = bitSet.get(getBitNumber(privilegeName));
		}
		return isAuthorized;
	}

	private BitSet getPrivilegesFromDatabase(String objectId, String privilegeName)
	{
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();

		BitSet bitSet = privilegeMap.get(objectId);

		try
		{
			ProtectionElement protectionElement = new ProtectionElement();
			protectionElement.setObjectId(objectId);
			ProtectionElementSearchCriteria protectionElementSearchCriteria = new ProtectionElementSearchCriteria(
					protectionElement);
			List list = privilegeUtility.getUserProvisioningManager().getObjects(
					protectionElementSearchCriteria);
			Collection objectPrivilegeMap = privilegeUtility.getUserProvisioningManager()
					.getPrivilegeMap(loginName, list);
			populatePrivileges(objectPrivilegeMap);
			bitSet = privilegeMap.get(objectId);
		}
		catch (CSException excp)
		{
			Logger.out.error(excp.getMessage(), excp);
		}

		return bitSet;
	}

	public void updatePrivilege(Class classObj, String privilegeName, boolean value)
	{
		updatePrivilege(classObj.getName(), privilegeName, value);
	}

	public void updatePrivilege(AbstractDomainObject aDObject, String privilegeName, boolean value)
	{
		updatePrivilege(aDObject.getObjectId(), privilegeName, value);
	}

	public void updatePrivilege(String objectId, String privilegeName, boolean value)
	{
		BitSet bitSet = privilegeMap.get(objectId);

		if (privilegeName.equals("READ"))
			bitSet.set(getBitNumber("READ_DENIED"), !value);
		else
			bitSet.set(getBitNumber(privilegeName), value);
	}

	/**
	 * This method is used to refresh the Privilege Cache for the user A call to
	 * this method forces CSM to go to the database & get the ProtectionElements
	 * For more, please refer to the 'initialize' method above
	 * 
	 * @throws Exception
	 */
	public void refresh() throws Exception
	{
		initialize();
	}

	private int getBitNumber(String privilegeName)
	{
		if (Permissions.READ.equals(privilegeName))
		{
			return 0;
		}
		if (Permissions.READ_DENIED.equals(privilegeName))
		{
			return 1;
		}
		if (Permissions.UPDATE.equals(privilegeName))
		{
			return 2;
		}
		if (Permissions.DELETE.equals(privilegeName))
		{
			return 3;
		}
		if (Permissions.CREATE.equals(privilegeName))
		{
			return 4;
		}
		if (Permissions.EXECUTE.equals(privilegeName))
		{
			return 5;
		}
		if (Permissions.USE.equals(privilegeName))
		{
			return 6;
		}
		if (Permissions.ASSIGN_READ.equals(privilegeName))
		{
			return 7;
		}
		if (Permissions.ASSIGN_USE.equals(privilegeName))
		{
			return 8;
		}
		if (Permissions.IDENTIFIED_DATA_ACCESS.equals(privilegeName))
		{
			return 9;
		}
		if (Permissions.DEFINE_ANNOTATION.equals(privilegeName))
		{
			return 17;
		}
		if (Permissions.DISTRIBUTION.equals(privilegeName))
		{
			return 21;
		}
		if (Permissions.GENERAL_ADMINISTRATION.equals(privilegeName))
		{
			return 29;
		}
		if (Permissions.GENERAL_SITE_ADMINISTRATION.equals(privilegeName))
		{
			return 28;
		}
		if (Permissions.PARTICIPANT_SCG_ANNOTATION.equals(privilegeName))
		{
			return 24;
		}
		if (Permissions.PHI.equals(privilegeName))
		{
			return 23;
		}
		if (Permissions.PROTOCOL_ADMINISTRATION.equals(privilegeName))
		{
			return 16;
		}
		if (Permissions.QUERY.equals(privilegeName))
		{
			return 22;
		}
		if (Permissions.REGISTRATION.equals(privilegeName))
		{
			return 18;
		}
		if (Permissions.REPOSITORY_ADMINISTRATION.equals(privilegeName))
		{
			return 14;
		}
		if (Permissions.SPECIMEN_ACCESSION.equals(privilegeName))
		{
			return 20;
		}
		if (Permissions.SPECIMEN_ANNOTATION.equals(privilegeName))
		{
			return 25;
		}
		if (Permissions.SPECIMEN_PROCESSING.equals(privilegeName))
		{
			return 26;
		}
		if (Permissions.SPECIMEN_STORAGE.equals(privilegeName))
		{
			return 27;
		}
		if (Permissions.STORAGE_ADMINISTRATION.equals(privilegeName))
		{
			return 15;
		}
		if (Permissions.USER_PROVISIONING.equals(privilegeName))
		{
			return 13;
		}
		if (Permissions.SHIPMENT_PROCESSING.equals(privilegeName))
		{
			return 30;
		}
		if(Permissions.EXECUTE_QUERY.equals(privilegeName))
		{
			return 31;
		}
		return 0;
	}

	public String getLoginName()
	{
		return loginName;
	}

	public void addObject(String objectId, Collection<Privilege> privileges)
	{
		BitSet bitSet = new BitSet();

		for (Privilege privilege : privileges)
		{
			bitSet.set(getBitNumber(privilege.getName()));
		}
		privilegeMap.put(objectId, bitSet);
	}

	public void updateUserPrivilege(String privilegeName, Class objectType, Long[] objectIds,
			Long userId, boolean assignOperation) throws Exception
	{
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		Collection<PrivilegeCache> listOfPrivilegeCaches = null;

		listOfPrivilegeCaches = PrivilegeManager.getInstance().getPrivilegeCaches();

		for (PrivilegeCache privilegeCache : listOfPrivilegeCaches)
		{
			if (privilegeCache.getLoginName().equals(
					privilegeUtility.getUserById(userId.toString()).getLoginName()))
			{
				for (Long objectId : objectIds)
				{
					updatePrivilege(objectType.getName() + "_" + objectId, privilegeName,
							assignOperation);
				}
			}
		}
		assignPrivilegeToUser(privilegeName, objectType, objectIds, userId, assignOperation);
	}

	/**
	 * This method assigns privilege by privilegeName to the user identified by
	 * userId on the objects identified by objectIds
	 * 
	 * @param privilegeName
	 * @param objectIds
	 * @param userId
	 * @throws SMException
	 */
	private void assignPrivilegeToUser(String privilegeName, Class objectType, Long[] objectIds,
			Long userId, boolean assignOperation) throws SMException
	{
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();

		Logger.out.debug("In assignPrivilegeToUser...");
		Logger.out.debug("privilegeName:" + privilegeName + " objectType:" + objectType
				+ " objectIds:" + edu.wustl.common.util.Utility.getArrayString(objectIds)
				+ " userId:" + userId);

		if (privilegeName == null || objectType == null || objectIds == null || userId == null)
		{
			Logger.out.debug("Cannot assign privilege to user. One of the parameters is null.");
		}
		else
		{
			String protectionGroupName = null;
			String roleName;
			Role role;
			ProtectionGroup protectionGroup;

			try
			{
				// Getting Appropriate Role
				// role name is generated as <<privilegeName>>_ONLY
				if (privilegeName.equals(Permissions.READ))
					roleName = Permissions.READ_DENIED;
				else
					roleName = privilegeName + "_ONLY";

				role = privilegeUtility.getRole(roleName);
				Logger.out.debug("Operation>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
						+ (assignOperation == true ? "Remove READ_DENIED" : "Add READ_DENIED"));

				Set roles = new HashSet();
				roles.add(role);

				if (privilegeName.equals(Permissions.USE))
				{
					protectionGroupName = "PG_" + userId + "_ROLE_" + role.getId();

					if (assignOperation == Constants.PRIVILEGE_ASSIGN)
					{
						Logger.out.debug("Assign Protection elements");

						protectionGroup = privilegeUtility.getProtectionGroup(protectionGroupName);

						// Assign Protection elements to Protection Group
						privilegeUtility.assignProtectionElements(protectionGroup
								.getProtectionGroupName(), objectType, objectIds);

						privilegeUtility.assignUserRoleToProtectionGroup(userId, roles,
								protectionGroup, assignOperation);
					}
					else
					{
						Logger.out.debug("De Assign Protection elements");
						Logger.out.debug("protectionGroupName : " + protectionGroupName
								+ " objectType : " + objectType + " objectIds : "
								+ edu.wustl.common.util.Utility.getArrayString(objectIds));
						privilegeUtility.deAssignProtectionElements(protectionGroupName,
								objectType, objectIds);
					}
				}
				else
				{
					// In case of assign remove the READ_DENIED privilege of the
					// user
					// and in case of de-assign add the READ_DENIED privilege to
					// the user.
					assignOperation = !assignOperation;

					for (int i = 0; i < objectIds.length; i++)
					{
						// Getting Appropriate Group
						// Protection Group Name is generated as
						// PG_<<userID>>_ROLE_<<roleID>>

						Logger.out.debug("objectType............................" + objectType);
						// changed by ajay

						if (objectType.getName().equals(Constants.COLLECTION_PROTOCOL_CLASS_NAME))
							protectionGroupName = Constants
									.getCollectionProtocolPGName(objectIds[i]);
						else if (objectType.getName().equals(
								Constants.DISTRIBUTION_PROTOCOL_CLASS_NAME))
							protectionGroupName = Constants
									.getDistributionProtocolPGName(objectIds[i]);

						protectionGroup = privilegeUtility.getProtectionGroup(protectionGroupName);

						Logger.out.debug("Assign User Role To Protection Group");

						// Assign User Role To Protection Group
						privilegeUtility.assignUserRoleToProtectionGroup(userId, roles,
								protectionGroup, assignOperation);
					}
				}
			}
			catch (CSException csex)
			{
				throw new SMException(csex);
			}
		}
	}
	
	/**
	 * get the ids and privileges where ids start with the given prefix
	 * 
	 * @param prefix
	 * @return
	 */
	public Map<String, List<NameValueBean>> getPrivilegesforPrefix(String prefix)
	{
		Map<String, List<NameValueBean>> map = new HashMap<String, List<NameValueBean>>();
		
		for(Entry<String, BitSet> entry : privilegeMap.entrySet())
		{
			if(entry.getKey().startsWith(prefix))
			{
				List<NameValueBean> privileges = getPrivilegeNames(entry.getValue());
				if(!privileges.isEmpty())
				{
					map.put(entry.getKey(), privileges);
				}
			}
		}
		
		return map;
		
	}

	
	/**
	 * convert the given bitset into a set of privilege names
	 * 
	 * @param value
	 * @return
	 */
	private List<NameValueBean> getPrivilegeNames(BitSet value)
	{
		List<NameValueBean> privilegeNames = new ArrayList<NameValueBean>();
		
		for(int i=0; i<value.size();i++)
		{
			if(value.get(i))
			{
				NameValueBean nmv = new NameValueBean();
				nmv.setName("DUMMY");
				
				switch(i)
				{
					/*case 17:
						nmv.setName(Permissions.DEFINE_ANNOTATION);
						break;*/
										
					case 21:
						nmv.setName(Permissions.DISTRIBUTION);
						break;
						
					/*case 29:
						nmv.setName(Permissions.GENERAL_ADMINISTRATION);
						break;*/
					
						/*
					case 24:
						nmv.setName(Permissions.PARTICIPANT_SCG_ANNOTATION);
						break;
						*/
						
					case 16:
						nmv.setName(Permissions.PROTOCOL_ADMINISTRATION);
						break;
						
					/*case 22:
						nmv.setName(Permissions.QUERY);
						break;*/
						
					case 18:
						nmv.setName(Permissions.REGISTRATION);
						break;
						
					/*case 14:
						nmv.setName(Permissions.REPOSITORY_ADMINISTRATION);
						break;*/
					/*	
					case 25:
						nmv.setName(Permissions.SPECIMEN_ANNOTATION);
						break;
					*/
						
					case 26:
						nmv.setName(Permissions.SPECIMEN_PROCESSING);
						break;
					/*	
					case 27:
						nmv.setName(Permissions.SPECIMEN_STORAGE);
						break;
					*/
						
					case 15:
						nmv.setName(Permissions.STORAGE_ADMINISTRATION);
						break;
						
					case 13:
						nmv.setName(Permissions.USER_PROVISIONING);
						break;
					
					case 30:
						nmv.setName(Permissions.SHIPMENT_PROCESSING);
						break;
						
					case 1:
						nmv.setName(Permissions.READ_DENIED);
						break;
				}
				
				for(Object o :Utility.getAllPrivileges())
				{
					NameValueBean privilege = (NameValueBean) o;
					if(privilege.getName().equals(nmv.getName()))
					{
						nmv.setValue(privilege.getValue());
						privilegeNames.add(nmv);
						break;
					}
				}
			}
		}
		
		return privilegeNames;
	}
	
}