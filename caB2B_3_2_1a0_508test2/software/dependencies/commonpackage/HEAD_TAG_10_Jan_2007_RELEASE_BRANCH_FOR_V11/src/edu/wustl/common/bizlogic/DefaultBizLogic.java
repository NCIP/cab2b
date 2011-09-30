/**
 * <p>Title: DefaultBizLogic Class>
 * <p>Description:	DefaultBizLogic is a class which contains the default 
 * implementations required for all the biz logic classes.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 26, 2005
 */

package edu.wustl.common.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.domain.AuditEventDetails;
import edu.wustl.common.domain.AuditEventLog;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.querysuite.security.utility.CsmCacheManager;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * DefaultBizLogic is a class which contains the default 
 * implementations required for all the biz logic classes.
 * @author kapil_kaveeshwar
 */
public class DefaultBizLogic extends AbstractBizLogic
{
	/**
	 * This method gets called before insert method. Any logic before inserting into database can be included here.
	 * @param obj The object to be inserted.
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 * */
	protected void preInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{

	}

	public void createProtectionElement(Object currentObj) throws BizLogicException
	{
		
	}
	/**
	 * Inserts an object into the database.
	 * @param obj The object to be inserted.
	 * @throws DAOException
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		dao.insert(obj, sessionDataBean, true, true);
	}

	/**
	 * This method gets called after insert method. Any logic after insertnig object in database can be included here.
	 * @param obj The inserted object.
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws DAOException
	 * @throws UserNotAuthorizedException 
	 * */
	protected void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{

	}

	/**
	 * Deletes an object from the database.
	 * @param obj The object to be deleted.
	 * @throws DAOException
	 * @throws UserNotAuthorizedException TODO
	 */
	protected void delete(Object obj, DAO dao) throws DAOException, UserNotAuthorizedException
	{
		dao.delete(obj);
	}

	/**
	 * This method gets called before update method. Any logic before updating into database can be included here.
	 * @param dao the dao object
	 * @param currentObj The object to be updated.
	 * @param oldObj The old object.
	 * @param sessionDataBean session specific data
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 * */
	protected void preUpdate(DAO dao, Object currentObj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException,
			UserNotAuthorizedException
	{

	}

	/**
	 * Updates an objects into the database.
	 * @param obj The object to be updated into the database. 
	 * @throws DAOException
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		dao.update(obj, sessionDataBean, true, true, false);
		dao.audit(obj, oldObj, sessionDataBean, true);
	}

	/**
	 * This method gets called after update method. Any logic after updating into database can be included here.
	 * @param dao the dao object
	 * @param currentObj The object to be updated.
	 * @param oldObj The old object.
	 * @param sessionDataBean session specific data
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 * */
	protected void postUpdate(DAO dao, Object currentObj, Object oldObj, SessionDataBean sessionDataBean) throws BizLogicException,
			UserNotAuthorizedException
	{

	}

	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
	{
		return true;
	}

	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param sourceObjectName	source object name
	 * @param selectColumnName	An array of field names to be selected
	 * @param whereColumnName An array of field names.
	 * @param whereColumnCondition The comparision condition for the field values. 
	 * @param whereColumnValue An array of field values.
	 * @param joinCondition The join condition.
	 * @throws SMException
	 */
	public List retrieve(String sourceObjectName, String[] selectColumnName, String[] whereColumnName, String[] whereColumnCondition,
			Object[] whereColumnValue, String joinCondition) throws DAOException
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);

		List list = null;

		try
		{
			dao.openSession(null);

			list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
			//dao.commit();
		}
		catch (DAOException daoExp)
		{
			daoExp.printStackTrace();
			Logger.out.error(daoExp.getMessage(), daoExp);
		}
		finally
		{
			dao.closeSession();
		}

		return list;
	}

	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param whereColumnName An array of field names.
	 * @param whereColumnCondition The comparision condition for the field values. 
	 * @param whereColumnValue An array of field values.
	 * @param joinCondition The join condition.
	 * @throws SMException
	 */
	public List retrieve(String sourceObjectName, String[] whereColumnName, String[] whereColumnCondition, Object[] whereColumnValue,
			String joinCondition) throws DAOException
	{
		return retrieve(sourceObjectName,null, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
	}

	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param colName Contains the field name.
	 * @param colValue Contains the field value.
	 */
	public List retrieve(String className, String colName, Object colValue) throws DAOException
	{
		String colNames[] = {colName};
		String colConditions[] = {"="};
		Object colValues[] = {colValue};

		return retrieve(className, colNames, colConditions, colValues, Constants.AND_JOIN_CONDITION);
	}

	/**
	 * Retrieves all the records for class name in sourceObjectName.
	 * @param sourceObjectName Contains the classname whose records are to be retrieved.
	 */
	public List retrieve(String sourceObjectName) throws DAOException
	{
		return retrieve(sourceObjectName, null, null, null, null);
	}

	/**
	 * Retrieves all the records for class name in sourceObjectName.
	 * @param sourceObjectName Contains the classname whose records are to be retrieved.
	 * @param selectColumnName An array of the fields that should be selected
	 */
	public List retrieve(String sourceObjectName, String[] selectColumnName) throws DAOException
	{
		return retrieve(sourceObjectName, selectColumnName, null, null, null, null);
	}
	
	public Object retrieve(String sourceObjectName, Long id)  throws DAOException
	{
		
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);

		Object object = null;
		
		try
		{
			dao.openSession(null);

			object  = dao.retrieve(sourceObjectName, id);
			//dao.commit();
		}
		catch (DAOException daoExp)
		{
			daoExp.printStackTrace();
			Logger.out.error(daoExp.getMessage(), daoExp);
		}
		finally
		{
			dao.closeSession();
		}

		return object;

	}

	public List getList(String sourceObjectName, String[] displayNameFields, String valueField, boolean isToExcludeDisabled) throws DAOException
	{
		String[] whereColumnName = null;
		String[] whereColumnCondition = null;
		Object[] whereColumnValue = null;
		String joinCondition = null;
		String separatorBetweenFields = ", ";

		if (isToExcludeDisabled)
		{
			whereColumnName = new String[]{"activityStatus"};
			whereColumnCondition = new String[]{"!="};
			whereColumnValue = new String[]{Constants.ACTIVITY_STATUS_DISABLED};
		}

		return getList(sourceObjectName, displayNameFields, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
				separatorBetweenFields);
	}

	/**
	 * Returns collection of name value pairs.
	 * @param sourceObjectName
	 * @param displayNameFields
	 * @param valueField
	 * @param whereColumnName
	 * @param whereColumnCondition
	 * @param whereColumnValue
	 * @param joinCondition
	 * @param separatorBetweenFields
	 * @return
	 * @throws DAOException
	 */
	public List getList(String sourceObjectName, String[] displayNameFields, String valueField, String[] whereColumnName,
			String[] whereColumnCondition, Object[] whereColumnValue, String joinCondition, String separatorBetweenFields, boolean isToExcludeDisabled)
			throws DAOException
	{
		if (isToExcludeDisabled)
		{
			whereColumnName = (String[]) Utility.addElement(whereColumnName, "activityStatus");
			whereColumnCondition = (String[]) Utility.addElement(whereColumnCondition, "!=");
			whereColumnValue = Utility.addElement(whereColumnValue, Constants.ACTIVITY_STATUS_DISABLED);
		}

		return getList(sourceObjectName, displayNameFields, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
				separatorBetweenFields);
	}

	//Mandar : 12-May-06 : bug id 863 : Sorting of ID columns
	private List getList(String sourceObjectName, String[] displayNameFields, String valueField, String[] whereColumnName,
			String[] whereColumnCondition, Object[] whereColumnValue, String joinCondition, String separatorBetweenFields) throws DAOException
	{
		//Logger.out.debug("in get list");
		Vector nameValuePairs = new Vector();

		nameValuePairs.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

		String[] selectColumnName = new String[displayNameFields.length + 1];
		for (int i = 0; i < displayNameFields.length; i++)
		{
			selectColumnName[i] = displayNameFields[i];
		}
		selectColumnName[displayNameFields.length] = valueField;

		List results = retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

		NameValueBean nameValueBean;
		Object[] columnArray = null;

		/**
		 * For each row in the result a vector will be created.Vector will contain all the columns
		 * other than the value column(last column). 
		 * If there is only one column in the result it will be set as the Name for the NameValueBean.
		 * When more than one columns are present, a string representation will be set. 
		 */
		if (results != null)
		{
			for (int i = 0; i < results.size(); i++)
			{
				columnArray = (Object[]) results.get(i);
				Object tmpObj = null;
				Vector tmpBuffer = new Vector();
				StringBuffer nameBuff = new StringBuffer();
				if (columnArray != null)
				{
					for (int j = 0; j < columnArray.length - 1; j++)
					{
						if (columnArray[j] != null && (!columnArray[j].toString().equals("")))
						{
							tmpBuffer.add(columnArray[j]);
						}
					}
					nameValueBean = new NameValueBean();

					//create name data
					if (tmpBuffer.size() == 1)//	only one column
					{
						tmpObj = tmpBuffer.get(0);

						//Logger.out.debug("nameValueBean Name : : " + tmpObj);
						//Logger.out.debug("NameClass : : " + tmpObj.getClass().getName());
						nameValueBean.setName(tmpObj);
					}
					else
					// multiple columns
					{
						for (int j = 0; j < tmpBuffer.size(); j++)
						{
							nameBuff.append(tmpBuffer.get(j).toString());
							if (j < tmpBuffer.size() - 1)
							{
								nameBuff.append(separatorBetweenFields);
							}
						}

						//Logger.out.debug("nameValueBean Name : : " + nameBuff.toString());
						nameValueBean.setName(nameBuff.toString());
					}

					int valueID = columnArray.length - 1;
					nameValueBean.setValue(columnArray[valueID].toString());

					nameValuePairs.add(nameValueBean);
				}
			}
		}
		Collections.sort(nameValuePairs);
		return nameValuePairs;
	}

	protected List disableObjects(DAO dao, Class sourceClass, String classIdentifier, String tablename, String colName, Long objIDArr[])
			throws DAOException
	{
		dao.disableRelatedObjects(tablename, colName, objIDArr);
		List listOfSubElement = getRelatedObjects(dao, sourceClass, classIdentifier, objIDArr);
		auditDisabledObjects(dao, tablename, listOfSubElement);
		return listOfSubElement;
	}
	
	protected List disableObjects(DAO dao, String tablename, Class sourceClass, 
			String classIdentifier, Long objIDArr[]) throws DAOException
	{
		List listOfSubElement = getRelatedObjects(dao, sourceClass, classIdentifier, objIDArr);
		disableAndAuditObjects(dao, sourceClass.getName(), tablename, listOfSubElement);
		return listOfSubElement;
	}
	
	/**
	 * 
	 * @param dao
	 * @param sourceClass
	 * @param tablename
	 * @param listOfSubElement
	 * @throws DAOException
	 */
	protected void disableAndAuditObjects(DAO dao, String sourceClass, String tablename, 
			List listOfSubElement) throws DAOException
	{
		Iterator iterator = listOfSubElement.iterator();
		Collection auditEventLogsCollection = new HashSet();

		try
		{
			while (iterator.hasNext())
			{
				Long objectId = (Long) iterator.next();
				IActivityStatus object  =(IActivityStatus) dao.retrieve( sourceClass, objectId);
				object.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
				dao.update(object, null, false, false, false);
				addAuditEventstoColl(tablename, auditEventLogsCollection, objectId);
			}
	
			HibernateDAO hibDAO = (HibernateDAO) dao;
			hibDAO.addAuditEventLogs(auditEventLogsCollection);
		}
		catch (UserNotAuthorizedException ex)
		{
			throw new DAOException(ex); //TODO: should throw bizlogicexception
		}
	}
	/**
	 * @param tablename
	 * @param auditEventLogsCollection
	 * @param objectId
	 */
	private void addAuditEventstoColl(String tablename, Collection auditEventLogsCollection,
			Long objectId)
	{
		AuditEventLog auditEventLog = new AuditEventLog();
		auditEventLog.setObjectIdentifier(objectId);
		auditEventLog.setObjectName(tablename);
		auditEventLog.setEventType(Constants.UPDATE_OPERATION);
	
		Collection auditEventDetailsCollection = new HashSet();
		AuditEventDetails auditEventDetails = new AuditEventDetails();
		auditEventDetails.setElementName(Constants.ACTIVITY_STATUS_COLUMN);
		auditEventDetails.setCurrentValue(Constants.ACTIVITY_STATUS_DISABLED);
	
		auditEventDetailsCollection.add(auditEventDetails);
	
		auditEventLog.setAuditEventDetailsCollcetion(auditEventDetailsCollection);
		auditEventLogsCollection.add(auditEventLog);
	}	

	/**
	 * @param tablename
	 * @param listOfSubElement
	 */
	protected void auditDisabledObjects(DAO dao, String tablename, List listOfSubElement)
	{
		Iterator iterator = listOfSubElement.iterator();
		Collection auditEventLogsCollection = new HashSet();

		while (iterator.hasNext())
		{
			Long objectIdentifier = (Long) iterator.next();
			addAuditEventstoColl(tablename, auditEventLogsCollection, objectIdentifier);
		}
		HibernateDAO hibDAO = (HibernateDAO) dao;
		hibDAO.addAuditEventLogs(auditEventLogsCollection);
	}

	public List getRelatedObjects(DAO dao, Class sourceClass, String classIdentifier, Long objIDArr[]) throws DAOException
	{
		String sourceObjectName = sourceClass.getName();
		String selectColumnName[] = {Constants.SYSTEM_IDENTIFIER};

		String[] whereColumnName = {classIdentifier + "." + Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnCondition = {"in"};
		Object[] whereColumnValue = {objIDArr};
		String joinCondition = Constants.AND_JOIN_CONDITION;

		List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		list = Utility.removeNull(list);
		Logger.out.debug(sourceClass.getName() + " Related objects to " + edu.wustl.common.util.Utility.getArrayString(objIDArr) + " are " + list);
		return list;
	}

	//Aarti: Overloaded to let selectColumnName and whereColumnName also be
	// parameters to method and are not hardcoded
	public List getRelatedObjects(DAO dao, Class sourceClass, String[] selectColumnName, String[] whereColumnName, Long objIDArr[])
			throws DAOException
	{
		String sourceObjectName = sourceClass.getName();
		String[] whereColumnCondition = {"in"};
		Object[] whereColumnValue = {objIDArr};
		String joinCondition = Constants.AND_JOIN_CONDITION;

		List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		Logger.out.debug(sourceClass.getName() + " Related objects to " + edu.wustl.common.util.Utility.getArrayString(objIDArr) + " are " + list);
		list = Utility.removeNull(list);
		return list;
	}

	public List getRelatedObjects(DAO dao, Class sourceClass, String[] whereColumnName, String[] whereColumnValue, String[] whereColumnCondition)
			throws DAOException
	{
		String sourceObjectName = sourceClass.getName();
		String joinCondition = Constants.AND_JOIN_CONDITION;
		String selectColumnName[] = {Constants.SYSTEM_IDENTIFIER};
		List list = dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

		list = Utility.removeNull(list);
		return list;
	}

	/**
	 * @author aarti_sharma
	 * Method allows assigning of privilege privilegeName to user identified by userId 
	 * or role identified by roleId
	 * on objects ids objectIds of type objectType.
	 * Privilege is to be assigned to user or a role is identified by boolean assignToUser
	 * 
	 */
	protected void setPrivilege(DAO dao, String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser,
			boolean assignOperation) throws SMException, DAOException
	{
		Logger.out.debug(" privilegeName:" + privilegeName + " objectType:" + objectType + " objectIds:"
				+ edu.wustl.common.util.Utility.getArrayString(objectIds) + " userId:" + userId + " roleId:" + roleId + " assignToUser:"
				+ assignToUser);
		
		edu.wustl.common.security.PrivilegeUtility privilegeUtility = new edu.wustl.common.security.PrivilegeUtility();
		
		// To get privilegeCache through 
		// Singleton instance of PrivilegeManager, requires User LoginName
		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
				
		if (assignToUser)
		{
//			SecurityManager.getInstance(this.getClass()).assignPrivilegeToUser(privilegeName, objectType, objectIds, userId, assignOperation);
				
			try 
			{
				String userName = privilegeUtility.getUserById(userId.toString()).getLoginName();
				PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(userName);
				privilegeCache.updateUserPrivilege(privilegeName, objectType, objectIds, userId, assignOperation);
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
//			SecurityManager.getInstance(this.getClass()).assignPrivilegeToGroup(privilegeName, objectType, objectIds, roleId, assignOperation);
			
			try 
			{
				privilegeManager.updateGroupPrivilege(privilegeName, objectType, objectIds, roleId, assignOperation);
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected Object getCorrespondingOldObject(Collection objectCollection, Long id)
	{
		Iterator iterator = objectCollection.iterator();
		while (iterator.hasNext())
		{
			AbstractDomainObject abstractDomainObject = (AbstractDomainObject) iterator.next();
			
			if (id != null && id.equals(abstractDomainObject.getId()))			
			{
				return abstractDomainObject;
			}
		}
		return null;
	}

	protected DAOException handleSMException(SMException e)
	{
		Logger.out.error("Exception in Authorization: " + e.getMessage(), e);
		String message = "Security Exception: " + e.getMessage();
		if (e.getCause() != null)
			message = message + " : " + e.getCause().getMessage();
		return new DAOException(message, e);
	}

	/**
	 *  Method to check the ActivityStatus of the given identifier
	 * @param dao
	 * @param identifier of the Element
	 * @param className of the Element
	 * @param errorName Dispaly Name of the Element
	 * @throws DAOException
	 */
	protected void checkStatus(DAO dao, IActivityStatus ado, String errorName) throws DAOException
	{
		if (ado != null)
		{
			Long identifier = ((AbstractDomainObject)ado).getId();
			if (identifier != null)
			{
				String className = ado.getClass().getName();
				String activityStatus = ado.getActivityStatus();
				if(activityStatus==null)
				{
					activityStatus = getActivityStatus(dao, className, identifier);
				}
				if (activityStatus.equals(Constants.ACTIVITY_STATUS_CLOSED))
				{
					throw new DAOException(errorName + " " + ApplicationProperties.getValue("error.object.closed"));
				}
			}
		}
	}

	public String getActivityStatus(DAO dao, String sourceObjectName, Long indetifier) throws DAOException
	{
		String whereColumnNames[] = {Constants.SYSTEM_IDENTIFIER};
		String colConditions[] = {"="};
		Object whereColumnValues[] = {indetifier};
		String[] selectColumnName = {Constants.ACTIVITY_STATUS};
		List list = dao
				.retrieve(sourceObjectName, selectColumnName, whereColumnNames, colConditions, whereColumnValues, Constants.AND_JOIN_CONDITION);

		if (!list.isEmpty())
		{
			Object obj = list.get(0);
			Logger.out.debug("obj Class " + obj.getClass());
			//Object[] objArr = (String)obj
			return (String) obj;
		}
		return "";
	}

	protected void insert(Object obj, DAO dao) throws DAOException, UserNotAuthorizedException
	{
		dao.insert(obj,null,false,false);
	}

	protected void update(DAO dao, Object obj) throws DAOException, UserNotAuthorizedException
	{
		dao.update(obj,null,false,false,false);
	}

    public Object retrieveAttribute(Class objClass, Long id, String attributeName) throws DAOException
    {
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);

		Object attribute = null;

		try
		{
			dao.openSession(null);
			attribute = dao.retrieveAttribute(objClass, id, attributeName);
		}
		catch (DAOException daoExp)
		{
			daoExp.printStackTrace();
			Logger.out.error(daoExp.getMessage(), daoExp);
		}
		finally
		{
			dao.closeSession();
		}
		return attribute;
    }
	/**
	 * To retrieve the attribute value for the given source object name & Id.
	 * @param sourceObjectName Source object in the Database. 
	 * @param id Id of the object.
	 * @param attributeName attribute name to be retrieved. 
	 * @return The Attribute value corresponding to the SourceObjectName & id.
	 * @throws DAOException
	 * @see edu.wustl.common.bizlogic.IBizLogic#retrieveAttribute(java.lang.String, java.lang.Long, java.lang.String)
	 */
	public Object retrieveAttribute(String sourceObjectName, Long id, String attributeName) throws DAOException 
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);

		Object attribute = null;

		try
		{
			dao.openSession(null);
			attribute = dao.retrieveAttribute(sourceObjectName, id, attributeName);
		}
		catch (DAOException daoExp)
		{
			daoExp.printStackTrace();
			Logger.out.error(daoExp.getMessage(), daoExp);
		}
		finally
		{
			dao.closeSession();
		}
		return attribute;
	}
	
	/**
	 * This method gets called before populateUIBean method. Any logic before updating uiForm can be included here.
	 * @param domainObj object of type AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 */
	protected void prePopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm) throws BizLogicException
	{
		
	}
	
	
	/**
	 * This method gets called after populateUIBean method. Any logic after populating  object uiForm can be included here.
	 * @param domainObj object of type AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 */
	protected void postPopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm)throws BizLogicException
	{
		
	}
	
	
	/**
	 * This method is called from LoginAction (after successful User Login)
	 * & here, privilegeCache object gets created for logged user 
	 * & this object is stored in Cache through PrivilegeManager 
	 * 
	 * @throws Exception 
	 * @author ravindra_jain
	 */
	public void cachePrivileges(String loginName) throws Exception
	{
		// A privilegeCache object is created for a user during Login & 
		// this cache contains the Classes, objects,... & corresponding Privileges
		// which user has on these classes, objects, etc.
		// All later Security checks are done through the cache & no call to the database is made
		PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(loginName);
					
	}

	@Override
	protected void postInsert(Collection<AbstractDomainObject> objCollection, DAO dao,
			SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#isAuthorized(edu.wustl.common.dao.AbstractDAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */
	public boolean isAuthorized(AbstractDAO dao, Object domainObject, SessionDataBean sessionDataBean) throws UserNotAuthorizedException, DAOException
	{
		boolean isAuthorized = false;
		String protectionElementName = null;
		// Customize check for DE, return true if sessionDataBean is NULL
		if(sessionDataBean == null)
		{
			return true;
		}
		if(sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}
		//	Get the base object id against which authorization will take place 
		if(domainObject instanceof List)
		{
		    List list = (List) domainObject;
			for(Object domainObject2 : list)
			{
				protectionElementName = getObjectId(dao, domainObject2);
			}
		}
		else	
		{
			protectionElementName = getObjectId(dao, domainObject);
		}
		//TODO To revisit this piece of code --> Vishvesh
		if(Constants.allowOperation.equals(protectionElementName))
		{
			return true;
		}
		//Get the required privilege name which we would like to check for the logged in user.
		String privilegeName = getPrivilegeName(domainObject);
		PrivilegeCache privilegeCache = getPrivilegeCache(sessionDataBean);
		//Checking whether the logged in user has the required privilege on the given protection element
		if(! protectionElementName.equalsIgnoreCase("ADMIN_PROTECTION_ELEMENT"))
		{
			String [] prArray = protectionElementName.split("_");
			String baseObjectId = prArray[0];
			String objId = "";
			for (int i = 1 ; i < prArray.length;i++)
			{
				objId = baseObjectId + "_" + prArray[i];
				isAuthorized = privilegeCache.hasPrivilege(objId, privilegeName);
				if (!isAuthorized)
				{
					break;
				}
			}
		}
		else
		{
			isAuthorized = privilegeCache.hasPrivilege(protectionElementName,privilegeName);
		}
		
        if (!isAuthorized)
        {
            UserNotAuthorizedException ex = new UserNotAuthorizedException();
            ex.setPrivilegeName(privilegeName);
            if (protectionElementName != null && (protectionElementName.contains("Site") || protectionElementName.contains("CollectionProtocol")))
            {
                String [] arr = protectionElementName.split("_");
                String [] nameArr = arr[0].split("\\.");
                String baseObject = nameArr[nameArr.length-1];
                ex.setBaseObject(baseObject);
                ex.setBaseObjectIdentifier(arr[1]);
            }
            throw ex;
            //ex.setBaseObject()
        }
		return isAuthorized;		
	}
	
	/**This method gets the instance of privilege cache which is used for authorization.
	 * @param sessionDataBean
	 * @return
	 */
	private PrivilegeCache getPrivilegeCache(SessionDataBean sessionDataBean)
	{
		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(sessionDataBean.getUserName());
		return privilegeCache;
	}

	/**
	 * Gets privilege name depending upon operation performed
	 * @param domainObject Object on which authorization is reqd.
	 * @return Privilege Name
	 */
	protected String getPrivilegeName(Object domainObject)
    {
    	String privilegeName = Variables.privilegeDetailsMap.get(getPrivilegeKey(domainObject));
    	return privilegeName;
    }
	
	/**
	 * @param domainObject
	 * @return
	 */
	protected String getPrivilegeKey(Object domainObject)
    {
    	return null;
    }
	
	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#getObjectId(edu.wustl.common.dao.AbstractDAO, java.lang.Object)
	 */
	public String getObjectId(AbstractDAO dao, Object domainObject) 
	{
		return Constants.allowOperation;
	}
	
	/**
     * Executes the HQL query.
     * @param query HQL query to execute.
     * @throws ClassNotFoundException Class not fount Exception
     * @throws BizLogicException BizLogic exception
     */
    public List executeQuery(String query) throws ClassNotFoundException, DAOException
    {
    	List returner = null;
    	Session session = null;
    	try
		{
        	session = DBUtil.getCleanSession();
    		Query hibernateQuery = session.createQuery(query);
			returner = hibernateQuery.list();
		}
		catch (HibernateException e)
		{
			throw (new DAOException(e));
		}
		catch (BizLogicException e)
		{
			throw new DAOException("Failed to create Session Object"+ e.getMessage());
		} 
		finally
		{
			session.close();
		}
		return returner;
    }

	@Override
	public boolean isReadDeniedTobeChecked() 
	{
		return false;
	}
	
	@Override
	public String getReadDeniedPrivilegeName()
	{
		return null;
	}
	
	public boolean hasPrivilegeToView(String objName, Long identifier, SessionDataBean sessionDataBean)
	{
		if(sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}
		
		List cpIdsList = new ArrayList();
		Set<Long> cpIds = new HashSet<Long>();
		
		cpIdsList = Utility.getCPIdsList(objName, identifier, sessionDataBean, cpIdsList);
		
		if(cpIdsList == null)
		{
			return false;
		}
		
		for(Object cpId : cpIdsList)
		{
			cpId = cpIdsList.get(0);
			cpIds.add(Long.valueOf(cpId.toString()));
		}
		
		PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());
		StringBuffer sb = new StringBuffer();
		sb.append(Constants.COLLECTION_PROTOCOL_CLASS_NAME).append("_");
		boolean isPresent = false;
		
		for (Long cpId : cpIds)
		{
			String privilegeName = getReadDeniedPrivilegeName();
			
			String [] privilegeNames = privilegeName.split(",");
			if(privilegeNames.length > 1)
			{	
				if((privilegeCache.hasPrivilege(sb.toString()+cpId.toString(), privilegeNames[0])))
				{
					isPresent = privilegeCache.hasPrivilege(sb.toString()+cpId.toString(), privilegeNames[1]);
					isPresent = !isPresent;
				}
			}
			else
			{
				isPresent = privilegeCache.hasPrivilege(sb.toString()+cpId.toString(), privilegeName);
			}
				
			if (privilegeName != null && privilegeName.equalsIgnoreCase(Permissions.READ_DENIED))
			{
				isPresent = !isPresent;
			}
			if (!isPresent)
			{
				return false;
			} 
		}
    	return true;
	}
}