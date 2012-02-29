/**
 * 
 */
package edu.wustl.common.querysuite.security.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.query.AbstractClient;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * @author supriya_dankh
 * This class is a cache manager class for CsmCache.This class will add CP and privileges related to it in CsmCache 
 * for Read as well as Identified Data Access.Also it filters results by using checkPermision() method of SecurityManager. 
 */
public class CsmCacheManager
{
	private Connection connection;
	private IValidator validator;
	
	public CsmCacheManager(Connection connection)
	{
		this.connection = connection;
		validator = getValidatorInstance();
	}
	
	private IValidator getValidatorInstance() 
	{
		if(Variables.validatorClassname == null || Variables.validatorClassname.length()==0)
		{
			return null;
		}
		else
		{
			return (IValidator) Utility.getObject(Variables.validatorClassname);
		}
	}

	public CsmCache getNewCsmCacheObject()
	{
		CsmCache csmCache = new CsmCache();
		return csmCache;
	}
	
	/**
	 * This method checks user's permissions (Read, Identified data access) for every entity in query result 
	 * and filters result accordingly.
	 * @param sessionDataBean 
	 * @param queryResultObjectDataMap
	 * @param aList
	 * @param cache
	 */
	public void filterRow(SessionDataBean sessionDataBean,
			Map<String,QueryResultObjectDataBean> queryResultObjectDataMap, List aList ,CsmCache cache)
	{     
		Boolean isAuthorisedUser = true;
		Boolean hasPrivilegeOnIdentifiedData = true;
		if (queryResultObjectDataMap != null)
		{ 
			Set keySet = queryResultObjectDataMap.keySet();

			for (Object key : keySet)
			{
				QueryResultObjectDataBean queryResultObjectDataBean = queryResultObjectDataMap
						.get(key);
				String entityName = queryResultObjectDataBean.getCsmEntityName();
				int mainEntityId = -1;
				if(queryResultObjectDataBean
						.getMainEntityIdentifierColumnId()!=-1)
				{
					mainEntityId = Integer.parseInt((String)aList.get(queryResultObjectDataBean
						.getMainEntityIdentifierColumnId()));
				}
				
				//Check if user has read privilege on particular object or not.
				if ((mainEntityId != -1) && (queryResultObjectDataBean.isReadDeniedObject()))
				{
					List<List<String>> cpIdsList = getCpIdsListForGivenEntityId(sessionDataBean,
							entityName, mainEntityId);
					if(cpIdsList.size()==0)//if this object is not associated to any CP then user will not have identified privilege on it.
					{
						hasPrivilegeOnIdentifiedData = checkPermissionOnGlobalParticipant(sessionDataBean);
					}
					else
					{
						List<Boolean> readPrivilegeList = new ArrayList<Boolean>();
						List<Boolean> IdentifiedPrivilegeList = new ArrayList<Boolean>();

						for (int i = 0; i < cpIdsList.size(); i++)
						{
							List<String> cpIdList = cpIdsList.get(i);
							updatePrivilegeList(sessionDataBean,
									cache, readPrivilegeList, IdentifiedPrivilegeList,
									cpIdList);
						}
					   isAuthorisedUser = isAuthorizedUser(readPrivilegeList,true);
					   hasPrivilegeOnIdentifiedData = isAuthorizedUser(IdentifiedPrivilegeList,false);
					}
				
				 	
		 		//If user is not authorized to read the data then remove all data related to this particular from row.
				Vector identifiedColumnIdentifiers = queryResultObjectDataBean.getIdentifiedDataColumnIds();
				Vector objectColumnIdentifiers = queryResultObjectDataBean.getObjectColumnIds();
				removeUnauthorizedData(aList, isAuthorisedUser, hasPrivilegeOnIdentifiedData,queryResultObjectDataBean);
				}
			} 
		}
	}
	
	/**
	 * Checks if the user has privilege on identified data
	 * @param sessionDataBean
	 * @param queryResultObjectDataMap
	 * @param aList
	 * @param cache
	 * @return <CODE>true</CODE> user has privilege on identified data, 
      * <CODE>false</CODE> otherwise
	 */
	public boolean hasPrivilegeOnIdentifiedData(SessionDataBean sessionDataBean,
			Map<String,QueryResultObjectDataBean> queryResultObjectDataMap, List aList ,CsmCache cache)
	{
		Boolean hasPrivilegeOnIdentifiedData = true;
		if (queryResultObjectDataMap != null)
		{ 
			Set keySet = queryResultObjectDataMap.keySet();

			for (Object key : keySet)
			{
				QueryResultObjectDataBean queryResultObjectDataBean = queryResultObjectDataMap
						.get(key);
				String entityName = queryResultObjectDataBean.getCsmEntityName();
				int mainEntityId = -1;
				if(queryResultObjectDataBean
						.getMainEntityIdentifierColumnId()!=-1)
				{
					mainEntityId = Integer.parseInt((String)aList.get(queryResultObjectDataBean
						.getMainEntityIdentifierColumnId()));
				}
				
				//Check if user has identified data access on particular object or not.
				if (mainEntityId != -1)
				{
					List<List<String>> cpIdsList = getCpIdsListForGivenEntityId(sessionDataBean,
							entityName, mainEntityId);
					if(cpIdsList.size()==0)//if this object is not associated to any CP then user will not have identified privilege on it.
					{
						hasPrivilegeOnIdentifiedData = checkPermissionOnGlobalParticipant(sessionDataBean);
					}
					else
					{
						List<Boolean> IdentifiedPrivilegeList = new ArrayList<Boolean>();

						for (int i = 0; i < cpIdsList.size(); i++)
						{
							List<String> cpIdList = cpIdsList.get(i);
							updatePrivilegeList(sessionDataBean, cache, null, IdentifiedPrivilegeList, cpIdList);
						}
						hasPrivilegeOnIdentifiedData = isAuthorizedUser(IdentifiedPrivilegeList,false);
					}	
				}
			}
		}
		return hasPrivilegeOnIdentifiedData;
	}
	
	/**
	 * Checks user's permissions (Read, Identified data access) for every entity in query result 
	 * and filters result accordingly for simple search.
	 * @param sessionDataBean
	 * @param queryResultObjectDataMap
	 * @param aList
	 * @param cache
	 */
	public void filterRowForSimpleSearch(SessionDataBean sessionDataBean, Map queryResultObjectDataMap,
			List aList, CsmCache cache)
	{
		Boolean isAuthorisedUser = true;
		Boolean hasPrivilegeOnIdentifiedData = true;

		Set keySet = queryResultObjectDataMap.keySet();
		Iterator keyIterator = keySet.iterator();
		QueryResultObjectData queryResultObjectData;

		for (; keyIterator.hasNext();) 
		{
			queryResultObjectData = (QueryResultObjectData) queryResultObjectDataMap
			.get(keyIterator.next());

			int entityId = Integer.parseInt(aList.get(queryResultObjectData.getIdentifierColumnId()).toString());
			String entityName = getEntityName(queryResultObjectData);
			
			List<List<String>> cpIdsList = getCpIdsListForGivenEntityId(sessionDataBean,
					entityName, entityId);
			if(cpIdsList.size()==0)//if this object is not associated to any CP then user will not have identified privilege on it.
			{
				hasPrivilegeOnIdentifiedData = checkPermissionOnGlobalParticipant(sessionDataBean);
			}
			else
			{
				List<Boolean> readPrivilegeList = new ArrayList<Boolean>();
				List<Boolean> IdentifiedPrivilegeList = new ArrayList<Boolean>();

				for (int i = 0; i < cpIdsList.size(); i++)
				{
					List<String> cpIdList = cpIdsList.get(i);
					updatePrivilegeList(sessionDataBean,
							cache, readPrivilegeList, IdentifiedPrivilegeList,
							cpIdList);
				}
				isAuthorisedUser = isAuthorizedUser(readPrivilegeList,true);
				hasPrivilegeOnIdentifiedData = isAuthorizedUser(IdentifiedPrivilegeList,false);
			}
			//If user is not authorized to read the data then remove all data related to this particular from row.
			Vector identifiedColumnIdentifiers = queryResultObjectData.getIdentifiedDataColumnIds();
			Vector objectColumnIdentifiers = queryResultObjectData.getDependentColumnIds();
			removeUnauthorizedData(aList, isAuthorisedUser, hasPrivilegeOnIdentifiedData,
					identifiedColumnIdentifiers, objectColumnIdentifiers, true);
		}
	}
	
	/**
	 * Checks if the user has privilege on identified data for simple search 
	 * @param sessionDataBean
	 * @param queryResultObjectDataMap
	 * @param aList
	 * @param cache
	 * @return <CODE>true</CODE> user has privilege on identified data, 
      * <CODE>false</CODE> otherwise
	 */
	public boolean hasPrivilegeOnIdentifiedDataForSimpleSearch(SessionDataBean sessionDataBean, Map queryResultObjectDataMap,
			List aList, CsmCache cache)
	{
		// boolean that indicates whether user has privilege on identified data
		boolean hasPrivilegeOnIdentifiedData = true;

		Set keySet = queryResultObjectDataMap.keySet();
		Iterator keyIterator = keySet.iterator();
		QueryResultObjectData queryResultObjectData;

		for (; keyIterator.hasNext();) 
		{
			queryResultObjectData = (QueryResultObjectData) queryResultObjectDataMap
			.get(keyIterator.next());

			int entityId = Integer.parseInt(aList.get(queryResultObjectData.getIdentifierColumnId()).toString());
			
			String entityName = getEntityName(queryResultObjectData);
			
			List<List<String>> cpIdsList = getCpIdsListForGivenEntityId(sessionDataBean,
					entityName, entityId);
			if(cpIdsList.size()==0)//if this object is not associated to any CP then user will not have identified privilege on it.
			{
				hasPrivilegeOnIdentifiedData = checkPermissionOnGlobalParticipant(sessionDataBean);
			}
			else
			{
				List<Boolean> IdentifiedPrivilegeList = new ArrayList<Boolean>();

				for (int i = 0; i < cpIdsList.size(); i++)
				{
					List<String> cpIdList = cpIdsList.get(i);
					updatePrivilegeList(sessionDataBean, cache, null, IdentifiedPrivilegeList, cpIdList);
				}	
				hasPrivilegeOnIdentifiedData = isAuthorizedUser(IdentifiedPrivilegeList,false);
			}
		}
		return hasPrivilegeOnIdentifiedData;			
	}
	
	/**
	 * Updates the privilege list
	 * @param sessionDataBean
	 * @param cache
	 * @param readPrivilegeList
	 * @param IdentifiedPrivilegeList
	 * @param cpIdList
	 */
	private void updatePrivilegeList(SessionDataBean sessionDataBean,
			CsmCache cache, List<Boolean> readPrivilegeList,
			List<Boolean> IdentifiedPrivilegeList, List<String> cpIdList)
	{
		Boolean isAuthorisedUser;
		Boolean hasPrivilegeOnIdentifiedData;
		String entityName;
		Long cpId = cpIdList.get(0) != null ? Long.parseLong(cpIdList.get(0)) : -1;
		entityName = Variables.mainProtocolObject;
			
		if(readPrivilegeList!=null)
		{
			isAuthorisedUser = checkReadDenied(sessionDataBean, cache,
					entityName, cpId);
			readPrivilegeList.add(isAuthorisedUser);
		
			//If user is authorized to read data then check for identified data access.
			if (isAuthorisedUser)
			{
				hasPrivilegeOnIdentifiedData = checkIdentifiedDataAccess(
						sessionDataBean, cache, entityName,
						cpId);
				IdentifiedPrivilegeList.add(hasPrivilegeOnIdentifiedData);
			}
		}
		else
		{
			hasPrivilegeOnIdentifiedData = checkIdentifiedDataAccess(
					sessionDataBean, cache, entityName,
					cpId);
			IdentifiedPrivilegeList.add(hasPrivilegeOnIdentifiedData);
		}
	}

	/**
	 * To retrieve the entity name
	 * @param queryResultObjectData
	 * @return entityName
	 */
	private String getEntityName(QueryResultObjectData queryResultObjectData) 
	{ 
		String tableName = (String) AbstractClient.objectTableNames.get(queryResultObjectData.getAliasName());
		String entityName =HibernateMetaData.getClassName(tableName);
//		if(tableName.equals(Constants.CATISSUE_SPECIMEN))
//		{
//			try
//			{
//				Class classObject = Class.forName(entityName);
//				entityName = classObject.getSuperclass().getName();
//			}
//			catch (ClassNotFoundException classNotExp)
//			{
//				Logger.out.debug("Class "+entityName+" not present.");
//			}
//		}
		return entityName;
	}

	/**
	 * If a object say participant-1 is registered to CP-1, CP-2 this method will check what are privileges of the 
	 * user on both CPs and will return true if user is having privilege on any one CP.
	 * @param PrivilegeList List of privileges that a object id is having for every CP to which this object is registered.
	 * @param isReadDenied 
	 * @return <CODE>true</CODE> User is authorized, 
      * <CODE>false</CODE> otherwise
	 */
	private Boolean isAuthorizedUser(List<Boolean> PrivilegeList, boolean isReadDenied)
	{
		for (int i = 0; i < PrivilegeList.size(); i++)
		{
			Boolean isAuthorized = PrivilegeList.get(i);
			if(isReadDenied &&!(isAuthorized))
				return false;
			else if(!(isReadDenied) && isAuthorized)
				return true;
		}
		if(isReadDenied)
			return true;
		else
			return false;
	}
	
	private void removeUnauthorizedData(List aList, Boolean isAuthorisedUser,
			Boolean hasPrivilegeOnIdentifiedData,QueryResultObjectDataBean queryResultObjectDataBean)
	{  
		if (!isAuthorisedUser)
		{
			removeUnauthorizedFieldsData(aList,false,queryResultObjectDataBean);
		}
		else
		{
			//If user is not authorized to see identified data then replace identified column values by ##
			if (!hasPrivilegeOnIdentifiedData)
			{
				removeUnauthorizedFieldsData(aList,true,queryResultObjectDataBean);
			}
		}
	}
	
	

	/**
	 * This method will internally call removeUnauthorizedFieldsData depending on the value of isAuthorisedUser 
	 * and hasPrivilegeOnIdentifiedData.
	 * @param aList
	 * @param isAuthorisedUser
	 * @param hasPrivilegeOnIdentifiedData
	 * @param queryResultObjectDataBean
	 */
	private void removeUnauthorizedData(List aList, Boolean isAuthorisedUser,
			Boolean hasPrivilegeOnIdentifiedData,
			Vector identifiedColumnIdentifiers, Vector objectColumnIdentifiers, boolean isSimpleSearch)
	{  
		if (!isAuthorisedUser)
		{
			removeUnauthorizedFieldsData(aList, identifiedColumnIdentifiers, objectColumnIdentifiers, false, isSimpleSearch);
		}
		else
		{
			//If user is not authorized to see identified data then replace identified column values by ##
			if (!hasPrivilegeOnIdentifiedData)
			{
				removeUnauthorizedFieldsData(aList, identifiedColumnIdentifiers, objectColumnIdentifiers, true, isSimpleSearch);
			}
		}
	}

	/**
	 * Check if user is having Identified data access on a object id passed to method.And update cache accordingly.
	 * @param sessionDataBean A data bean that contains information related to user logged in. 
	 * @param cache cache object that maintains information related to permissions of user on every CP object.  
	 * @param queryResultObjectDataBean A metadata object required for CSM filtering.
	 * @param entityName Name of entity for which identified data access is to be checked.
	 * @param cpId CP id 
	 * @return
	 */
	private Boolean checkIdentifiedDataAccess(SessionDataBean sessionDataBean, CsmCache cache,
			 String entityName,
			Long entityId)
	{ 
		Boolean hasPrivilegeOnIdentifiedData;
		if (cache.isIdentifedDataAccess(entityId) == null)
		{
			hasPrivilegeOnIdentifiedData = 
				((checkPermission(sessionDataBean, entityName, entityId, 
						Permissions.PHI)) ||
				(checkPermission(sessionDataBean, entityName, entityId,
					    Permissions.REGISTRATION))
					);

			cache.addNewObjectInIdentifiedDataAccsessMap(entityId,
					hasPrivilegeOnIdentifiedData);
		}
		else
			hasPrivilegeOnIdentifiedData = cache.isIdentifedDataAccess(entityId);
		return hasPrivilegeOnIdentifiedData;
	}

	/**
	 * Check if user is having Read privilege on a object id passed to method.And update cache accordingly.
	 * @param sessionDataBean
	 * @param cache
	 * @param queryResultObjectDataBean
	 * @param entityName
	 * @param cpId
	 * @return
	 */
	private Boolean checkReadDenied(SessionDataBean sessionDataBean, CsmCache cache, String entityName, Long cpId)
	{
		Boolean isAuthorisedUser;
		if (cache.isReadDenied(cpId) == null)
		{
			isAuthorisedUser = checkPermission(sessionDataBean, entityName, cpId,
					Permissions.READ_DENIED);
			cache.addNewObjectInReadPrivilegeMap(cpId, isAuthorisedUser);
		}
		else
			isAuthorisedUser = cache.isReadDenied(cpId);
		return isAuthorisedUser;
	}
	
	/**
	 * This method will fire a query on catissue database to get CP ids related to a entity Id passed to this method.
	 * @param sessionDataBean
	 * @param entityName 
	 * @param entityId
	 * @return
	 */
	private List<List<String>> getCpIdsListForGivenEntityId(SessionDataBean sessionDataBean,
			String entityName, int entityId)
	{
		String sql = Variables.entityCPSqlMap.get(entityName);
		List<List<String>> cpIdsList = new ArrayList<List<String>>();
		if (sql != null)
		{
			sql = sql + entityId;
			try
			{
				cpIdsList = executeQuery(sessionDataBean, sql);
			}
			catch (Exception e)
			{
				Logger.out.error("Error occured while getting CP ids for entity : "+entityName);
				e.printStackTrace();
			}
		}else if (entityName.equals(Variables.mainProtocolObject))
		{
		    List<String> cpIdList = new ArrayList<String>();
		    cpIdList.add(String.valueOf(entityId));
			cpIdsList.add(cpIdList);
		}
		return cpIdsList;
	} 

	/**
	 * @param entityName
	 * @param entityId
	 * @return
	 */
	public static String getQueryStringForCP(String entityName, int entityId)
	{
		if (entityName == null || entityId == 0)
		{
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Variables.entityCPSqlMap.get(entityName));
		sb.append(entityId);
		return sb.toString();
	}
	
	/**
	 * This method will internally call checkPermission of SecurityManager 
	 * and will return if a user is authorized user or not.
	 * @param sessionDataBean
	 * @param entityName
	 * @param entityId
	 * @param permission
	 * @param privilegeType
	 */
	private Boolean checkPermission(SessionDataBean sessionDataBean, String entityName, Long entityId,
			String permission)
	{
		// To get privilegeCache through 
		// Singleton instance of PrivilegeManager, requires User LoginName		
		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(sessionDataBean.getUserName());
		
		// Call to SecurityManager.checkPermission bypassed &
		// instead, call redirected to privilegeCache.hasPrivilege		
		Boolean isAuthorisedUser = privilegeCache.hasPrivilege(entityName+"_"+entityId, permission);
		
		if (Permissions.READ_DENIED.equals(permission))
			isAuthorisedUser = !isAuthorisedUser;
		
		if(!isAuthorisedUser)
		{
			if(validator != null)
			{
				isAuthorisedUser = validator.hasPrivilegeToView(sessionDataBean, entityId.toString(), permission);
			}
		}
		return isAuthorisedUser;
	}
	
	/**
	 * To check for Authorization for Global Participants - not registereed to any CP 
	 * @param sessionDataBean
	 * @param entityName
	 * @param entityId
	 * @param permission
	 * @return
	 */
	private boolean checkPermissionOnGlobalParticipant(SessionDataBean sessionDataBean)
	{
		boolean isAuthorisedUser = false;
		validator = getValidatorInstance();
		if(validator != null)
		{
			isAuthorisedUser = validator.hasPrivilegeToViewGlobalParticipant(sessionDataBean);
		}
		return isAuthorisedUser;
	}
	
	/**
	 * Executes Query to get CP ids for given entity id on database.Results are added in List<List<String>> 
	 * and this list is returned.
	 * @param sessionDataBean
	 * @param sql
	 * @throws DAOException
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	private List<List<String>> executeQuery(SessionDataBean sessionDataBean, String sql) throws DAOException, ClassNotFoundException, SQLException
	{    
		List<List<String>> aList = new ArrayList<List<String>>();
		Statement stmt = null;
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet resultSet = null;
		resultSet = stmt.executeQuery(sql);
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();
		
		while (resultSet.next())
		{
			int i = 1;
			List<String> entityIdsList = new ArrayList<String>();
			while (i <= columnCount)
			{
				entityIdsList.add( resultSet.getObject(i).toString());
				i++;
			}
			aList.add(entityIdsList);
		}
		if(resultSet!=null)
		  resultSet.close();
		if(stmt!=null)
		  stmt.close();
		return aList;
		
	}
	
	/**
	 * This method removes data from list aList.
	 * It could be all data related to QueryResultObjectDataBean
	 * or only the identified fields depending on 
	 * the value of boolean removeOnlyIdentifiedData
	 * user
	 * @author supriya_dankh
	 * @param aList
	 * @param queryResultObjectData
	 * @param removeOnlyIdentifiedData
	 */
	private void removeUnauthorizedFieldsData(List aList,
			Vector identifiedColumnIdentifiers, Vector objectColumnIdentifiers, boolean removeOnlyIdentifiedData, boolean isSimpleSearch)
	{

		Vector objectColumnIds = new Vector();

		if (removeOnlyIdentifiedData)
		{
			objectColumnIds.addAll(identifiedColumnIdentifiers);
		}
		else
		{
			objectColumnIds.addAll(objectColumnIdentifiers);
		}
		Logger.out.debug("objectColumnIds:" + objectColumnIds);
		if (objectColumnIds != null)
		{
			for (int k = 0; k < objectColumnIds.size(); k++)
			{
				if(isSimpleSearch)
					aList.set(((Integer) objectColumnIds.get(k)).intValue()-1, Constants.hashedOut);
				else
					aList.set(((Integer) objectColumnIds.get(k)).intValue(), Constants.hashedOut);
			}
		}
	}	
	
	private void removeUnauthorizedFieldsData(List aList,boolean removeOnlyIdentifiedData, QueryResultObjectDataBean queryResultObjectDataBean)
	{
		Vector objectColumnIds = new Vector();
		boolean isAuthorizedUser = true;
		if (!removeOnlyIdentifiedData) {
			objectColumnIds.addAll(queryResultObjectDataBean
					.getObjectColumnIds());
			isAuthorizedUser = false;

		} else {
			objectColumnIds.addAll(queryResultObjectDataBean
					.getIdentifiedDataColumnIds());
			
		}
		if (objectColumnIds != null) {
			for (int k = 0; k < objectColumnIds.size(); k++) {
				aList.set(((Integer) objectColumnIds.get(k)).intValue(),
						Constants.hashedOut);
			}
		}

		if (validator != null) {
			List tqColumnMetadataList = queryResultObjectDataBean
					.getTqColumnMetadataList();
			validator.hasPrivilegeToViewTemporalColumn(tqColumnMetadataList,
					aList, isAuthorizedUser);
		}
	}	
}
