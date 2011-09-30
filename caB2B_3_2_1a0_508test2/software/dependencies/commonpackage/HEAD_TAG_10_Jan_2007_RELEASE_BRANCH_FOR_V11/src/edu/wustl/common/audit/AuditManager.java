/*
 *
 */
package edu.wustl.common.audit;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.wustl.common.dao.DAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.domain.AuditEvent;
import edu.wustl.common.domain.AuditEventDetails;
import edu.wustl.common.domain.AuditEventLog;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.ClassRelationshipData;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.logger.Logger;


/**
 * AuditManager is an algorithm to figure out the changes with respect to database due to 
 * insert, update or delete data from/to database. 
 * @author kapil_kaveeshwar
 */
public class AuditManager 
{
	/* Instance of Audit event. 
	 * All the change under one database session are added under this event.
	 **/
	private AuditEvent auditEvent;  
	
	/**
	 * Instanciate a new instance of AuditManager
	 * */
	public AuditManager()
	{
		auditEvent = new AuditEvent();
	}
	
	/**
	 * Set the id of the logged-in user who performed the changes.
	 * @param userId System identifier of logged-in user who performed the changes.
	 * */
	public void setUserId(Long userId)
	{
		auditEvent.setUserId(userId);
	}
	
	/**
	 * Set the ip address of the machine from which the event was performed.
	 * */
	public void setIpAddress(String IPAddress)
	{
		auditEvent.setIpAddress(IPAddress);
	}
	
	/**
	 * Check whether the object type is a premitive data type or a user defined datatype.
	 * */
	private boolean isVariable(Object obj)
	{
		if(obj instanceof Number || obj instanceof String || 
				obj instanceof Boolean || obj instanceof Character || 
				obj instanceof Date)
			return true;
		return false;
	}

	/** This function checks weather given object is instance of
	 * Collection or not
	 * @param obj
	 * @return boolean 
	 */
 
	private boolean isCollection(Object obj)
	{
		if(obj instanceof Collection)
			return true;
		return false;
		
	}
	
	/**
	 * Compares the contents of two objects. 
	 * @param currentObj Current state of object.
	 * @param currentObj Previous state of object.
	 * @param eventType Event for which the comparator will be called. e.g. Insert, update, delete etc.
	 * */
	public void compare(Auditable currentObj, Auditable previousObj, String eventType) throws AuditException
	{
		if( currentObj == null )
			return;
		
		try
		{
			//An auidt event will contain many logs.
			AuditEventLog auditEventLog = new AuditEventLog();
			
			//Set System identifier if the current object.
			auditEventLog.setObjectIdentifier(currentObj.getId());
			
			//Set the table name of the current class
			auditEventLog.setObjectName(HibernateMetaData.getTableName(currentObj.getClass()));
			auditEventLog.setEventType(eventType);
			
			//An event log will contain many event details
			Set auditEventDetailsCollection = new HashSet();
			
			//Class of the object being compared
			Class currentObjClass = currentObj.getClass();
			Class previousObjClass = currentObjClass; 
			
			if(previousObj!=null)
				previousObjClass = previousObj.getClass();
			
			//check the class for both objects are equals or not.
			if(previousObjClass.equals(currentObjClass))
			{
				//Retrieve all the methods defined in the class. 
				Method[] methods = currentObjClass.getMethods();
				
				for (int i = 0; i < methods.length; i++)
				{
					//filter only getter methods.
					if(methods[i].getName().startsWith("get") && methods[i].getParameterTypes().length==0)
					{				
//						if(isManyToMany(currentObj, methods[i]))
//						{
//							Collection auditEventLogs = processManyToManyRelation(currentObj, previousObj,
//														methods[i], eventType);
//							auditEvent.getAuditEventLogCollection().addAll(auditEventLogs);
//						}
//	 					else
						{
							AuditEventDetails auditEventDetails = processField(methods[i], currentObj, previousObj);
							
							if(auditEventDetails!=null)
								auditEventDetailsCollection.add(auditEventDetails);
					
						}
					}
				}
			}
			
			if(!auditEventDetailsCollection.isEmpty())
			{
				auditEventLog.setAuditEventDetailsCollcetion(auditEventDetailsCollection);
				auditEvent.getAuditEventLogCollection().add(auditEventLog);
			}
			
	
		}
		catch(Exception ex)
		{
			Logger.out.error(ex.getMessage(),ex);
			throw new AuditException();
		}
			
	}
	
	/** This function checks weather object has Many-Many relationship with object returned by Method
	 *	@param obj Object for which Many-To-Many relation is to be checked
	 *	@param method Method which returns the RoleClass
	 *	@return boolean if true then object has Many-To-Many relation othersise not
	 */
	private boolean isManyToMany(Auditable obj, Method method) throws Exception
	{
		boolean isManyToMany = false;
		Class roleClass = getRoleClass(obj, method);
		
		String attName = Utility.parseAttributeName(method.getName());
		
		//Checking roleClass for null because there might be case when there is no roleClass for 
		// given object and for given method. In that case getRoleClass returns null.	
		if(roleClass != null)
		{
			String roleAttributeName=HibernateMetaData.getFullyQualifiedRoleAttName(obj.getClass(),roleClass,attName);
			isManyToMany = HibernateMetaData.isRelationManyToMany(obj.getClass(), roleClass,roleAttributeName);
		
		}
		return isManyToMany;
	}
	

	/** This function gets the Role Class of a given object for given method
	 * 	@param obj
	 * 	@param method
	 * 	@return Role Class
	 */
	private Class getRoleClass(Auditable obj, Method method) throws Exception
	{
		Class roleClass=null;
		
		Object obj1 =  getValue(obj,method);
		
		if(obj1!=null && obj1 instanceof Collection )
		{
			String attName = Utility.parseAttributeName(method.getName());
			roleClass = HibernateMetaData.getRoleClass(attName);
		}
		return roleClass;
	}
	
	/**
	 * This Function checks for Many-To-Many Relations and adds the entry in audit table.. 
	 * @param currentObj Current state of object.
	 * @param previousObj  Previous state of object.
	 * @param eventType Event for which the comparator will be called. e.g. Insert, update, delete etc.
	 * */
	
	private Collection processManyToManyRelation(Auditable currentObj, Auditable previousObj, 
									Method method, String eventType) throws Exception
	{
		Collection auditEventLogCollection = new HashSet();
		
		Class roleClass = getRoleClass(currentObj, method);
		String attName = Utility.parseAttributeName(method.getName());
		String roleAttributeName = HibernateMetaData.getFullyQualifiedRoleAttName(currentObj.getClass(),roleClass,attName);
		
		ClassRelationshipData crd=HibernateMetaData.getClassRelationshipData(currentObj.getClass(),roleClass,roleAttributeName);
		
		String roleId=crd.getRoleId();
		String keyId=crd.getKeyId();
		
		//Getting the tableName
		String tableName=crd.getRelationTable();
		
		//getting the collection values and copying it in another collection.
		// This is done because original values should not get affected
		Collection values=(Collection)getValue(currentObj,method);
		Collection currCollection=duplicateCollection(values);
		
		values=(Collection)getValue(previousObj,method);
		Collection prevCollection=duplicateCollection(values);
		
		//removing the common values from current collection and prev collection as for common values
		//there is no audit required
		removeCommonEntry(currCollection,prevCollection);
		
		Iterator currColIterator = currCollection.iterator();
		Iterator prevColIterator = prevCollection.iterator();
		
		
		// Iterating through both the collection i'e currCollection and prevCollection until
		// one of the collection ends.
		// By doing this we are directly auditing the values as setting the prev value to the previous collection value
		// and current values to the current collection value
		while(currColIterator.hasNext() && prevColIterator.hasNext())
		{
			Set auditEventDetailsCollection = new HashSet();
			Auditable curr = (Auditable)currColIterator.next();
			Auditable prev = (Auditable)prevColIterator.next();
		
			AuditEventDetails auditEventDetails = setAuditEventDetails(null,roleId,
					Utility.toString(prev.getId()),
					Utility.toString(curr.getId()));			
			auditEventDetailsCollection.add(auditEventDetails);
				
			AuditEventLog auditEventLog=setAuditEventLog(auditEventDetailsCollection,tableName,eventType);
			auditEventLogCollection.add(auditEventLog);
			
		}
		//After iterating if currCollection has not finished then this means user has entered more values than previous collection
		// then audit the values as setting the prev value to null
		// and current values to the current collection value
		while(currColIterator.hasNext())
		{
			Set auditEventDetailsCollection = new HashSet();
			Auditable curr=(Auditable)currColIterator.next();
			AuditEventDetails auditEventDetails1 = setAuditEventDetails(null,keyId,null,
						Utility.toString(currentObj.getId()));
			auditEventDetailsCollection.add(auditEventDetails1);
			
			AuditEventDetails auditEventDetails2=setAuditEventDetails(null,roleId,null,
						curr.getId().toString());
			auditEventDetailsCollection.add(auditEventDetails2);
			
			AuditEventLog auditEventLog=setAuditEventLog(auditEventDetailsCollection,tableName,eventType);
			auditEventLogCollection.add(auditEventLog);

		}
		//After iterating if prevCollection has not finished then this means user has deleted some values from previous collection
		// then audit the values as setting the prev value to the previous collection value
		// and current values to null

		while(prevColIterator.hasNext())
		{
			Set auditEventDetailsCollection = new HashSet();
			AbstractDomainObject prev=(AbstractDomainObject)prevColIterator.next();
			
			AuditEventDetails auditEventDetails1=setAuditEventDetails(null,keyId,
					Utility.toString(currentObj.getId()),null);
			auditEventDetailsCollection.add(auditEventDetails1);
		
			AuditEventDetails auditEventDetails2=setAuditEventDetails(null,roleId,
					Utility.toString(prev.getId()),null);
			auditEventDetailsCollection.add(auditEventDetails2);
			
			AuditEventLog auditEventLog=setAuditEventLog(auditEventDetailsCollection,tableName,eventType);
			auditEventLogCollection.add(auditEventLog);

		}
		return auditEventLogCollection; 
	}
	
	/**
	 * This function copies the values of one collection into another collection
	 * @param values
	 * @return duplicate collection
	 */
	private Collection duplicateCollection(Collection values)
	{
		Collection duplicateCol=new HashSet();
		if(values!=null)
		{
			duplicateCol.addAll(values);
		}
		return duplicateCol;
	
	}
	
	/**
	 * This function sets the AuditEventDetails object's values 
	 * @param id
	 * @param elementName
	 * @param previousValue
	 * @param currentValue
	 * @return AuditEvenDetails
	 */
	private AuditEventDetails setAuditEventDetails(Long id,String elementName,String previousValue,String currentValue)
	{
		AuditEventDetails auditEventDetails=new AuditEventDetails();
		auditEventDetails.setId(id);
		auditEventDetails.setElementName(elementName);
		auditEventDetails.setPreviousValue(previousValue);
		auditEventDetails.setCurrentValue(currentValue);
		return auditEventDetails;
	}

	/**
	 * This function sets the AuditEventLog object's values 
	 * @param auditEventDetailsCollection
	 * @param objectName
	 * @param eventType
	 * @return AuditEvenLog
	 */
	private AuditEventLog setAuditEventLog(Collection auditEventDetailsCollection,String objectName,String eventType)
	{
		AuditEventLog auditEventLog=new AuditEventLog();
		auditEventLog.setAuditEventDetailsCollcetion(auditEventDetailsCollection);
		auditEventLog.setObjectName(objectName);
		auditEventLog.setEventType(eventType);
		return auditEventLog;

	}
	

	/**
	 * This function removes the common entries of currCollection and prevcollection
	 * @param currCollection 
	 * @param prevCollection
	 */
	private void removeCommonEntry(Collection currCollection,Collection prevCollection)
	{
		Iterator prevColIterator = prevCollection.iterator();
	    while(prevColIterator.hasNext())
		{
	    	Auditable prev = (Auditable)prevColIterator.next();
	    	Iterator currColIterator=currCollection.iterator();
			
			while(currColIterator.hasNext())
			{
				Auditable curr= (Auditable) currColIterator.next();
				
				if(prev!=null && curr!=null && 
						prev.getId().equals(curr.getId()))
				{
					prevColIterator.remove();
					currColIterator.remove();
					break;
				}
			}
		}
	}
	

	/**
	 * Process each field to find the change from previous value to current value.
	 * @param method referance of getter method object access the current and previous value of the object.
	 * 
	 * @param currentObj instance of current object
	 * @param previousObj instance of previous object.
	 * */
	private AuditEventDetails processField(Method method, Object currentObj, Object previousObj) throws Exception
	{
		//Get the old value of the attribute from previousObject
		Object prevVal = getValue(previousObj, method);
		
		//Get the current value of the attribute from currentObject
		Object currVal = getValue(currentObj, method);
		
		//Compare the old and current value
		AuditEventDetails auditEventDetails = compareValue(prevVal, currVal);
		
		if(auditEventDetails!=null)
		{
			//Parse the attribute name from getter method.
			String attributeName = Utility.parseAttributeName(method.getName());
			
			//Find the currosponding column in the database
			String columnName = HibernateMetaData.getColumnName(currentObj.getClass(),attributeName);
		
			//Case of transient object
			if(columnName.equals(""))
				return null;
			
			auditEventDetails.setElementName(columnName);
		}
		return auditEventDetails;
	}
	
	/** This function gets the value returned by the method invoked my given object
	 * @param obj Object for which method should be invoked
	 * @param method This is the method for which we have to find out the return value
	 * @return Object return value
	 */
	private Object getValue(Object obj,Method method) throws Exception
	{
		if(obj!=null)
		{
			Object val = Utility.getValueFor(obj,method);
			
			if(val instanceof Auditable)
			{
				Auditable auditable = (Auditable)val;
				return auditable.getId();
			}
			if(isVariable(val))
				return val; 
			
			 
//			if(isCollection(val))
//			{
//				return val;
//			}
		
		}
		return null;
	}
	
	
	/** This function compares the prevVal object and currval object
	 * and if there is any change in value then create the AuditEvenDetails object
	 * @param preVal previous value
	 * @param currVal current value
	 * @return AuditEventDetails  
	 */
	private AuditEventDetails compareValue(Object prevVal, Object currVal) 
	{
		//Logger.out.debug("prevVal <"+prevVal+">");
		//Logger.out.debug("currVal <"+currVal+">");
		
		if(prevVal==null && currVal==null)
		{
			return null;
		}
		
		if(prevVal==null || currVal==null)
		{
			if(prevVal==null && currVal!=null)
			{
				AuditEventDetails auditEventDetails = new AuditEventDetails();
				auditEventDetails.setPreviousValue(null);
				auditEventDetails.setCurrentValue(currVal.toString());
				return auditEventDetails;
			}
			else if(prevVal!=null && currVal==null)
			{
				AuditEventDetails auditEventDetails = new AuditEventDetails();
				auditEventDetails.setPreviousValue(prevVal.toString());
				auditEventDetails.setCurrentValue(null);
				return auditEventDetails;
			}
		}
		else if(!prevVal.equals(currVal))
		{
			AuditEventDetails auditEventDetails = new AuditEventDetails();
			auditEventDetails.setPreviousValue(prevVal.toString());
			auditEventDetails.setCurrentValue(currVal.toString());
			return auditEventDetails;
		}
		return null;
	}
	
	public void insert(DAO dao) throws DAOException 
	{
		if(auditEvent.getAuditEventLogCollection().isEmpty())
			return;
		
		try
		{
			dao.insert(auditEvent,null, false, false);
			Iterator auditLogIterator = auditEvent.getAuditEventLogCollection().iterator();
			while(auditLogIterator.hasNext())
			{
				AuditEventLog auditEventLog = (AuditEventLog)auditLogIterator.next();
				auditEventLog.setAuditEvent(auditEvent);
				dao.insert(auditEventLog,null, false, false);
				
	  			Iterator auditEventDetailsIterator = auditEventLog.getAuditEventDetailsCollcetion().iterator();
	  			while(auditEventDetailsIterator.hasNext())
	  			{
	  				AuditEventDetails auditEventDetails = (AuditEventDetails)auditEventDetailsIterator.next();
	  				auditEventDetails.setAuditEventLog(auditEventLog);
	  				dao.insert(auditEventDetails,null, false, false);
	  			}
			}
			auditEvent = new AuditEvent();
		}
		catch(UserNotAuthorizedException sme)
		{
		    Logger.out.debug("Exception:"+sme.getMessage(),sme);
		}
		
		
	}
	public void addAuditEventLogs(Collection auditEventLogsCollection)
	{
	    auditEvent.getAuditEventLogCollection().addAll(auditEventLogsCollection);
	}
	
	public static void main(String[] args)  throws IllegalAccessException, Exception
	{
//		Variables.catissueHome = System.getProperty("user.dir");
//		PropertyConfigurator.configure(Variables.catissueHome+"\\WEB-INF\\src\\"+"ApplicationResources.properties");
//		Logger.out = org.apache.log4j.Logger.getLogger("A");
//		DBUtil.currentSession();
//		Logger.out.info("here");
//		
//		AuditManager aAuditManager = new AuditManager();

		
//		HibernateDAO dao = new HibernateDAO();
//		dao.openSession(null);
//		Department deptCurr = (Department)dao.retrieve(Department.class.getName(),new Long(2));
//		dao.closeSession();
//
//		dao.openSession(null);
//		dao.closeSession();
		
		/*AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		StorageContainer storageContainerCurr = (StorageContainer)(bizLogic.retrieve(StorageContainer.class.getName(),Constants.SYSTEM_IDENTIFIER,new Long(1))).get(0);
		StorageContainer storageContainerOld = (StorageContainer)(bizLogic.retrieve(StorageContainer.class.getName(),Constants.SYSTEM_IDENTIFIER,new Long(1))).get(0);*/
		
		
		//storageContainerCurr.setTempratureInCentigrade(new Double(80));
		

		
		/*aAuditManager.compare(storageContainerCurr,storageContainerOld,"UPDATE");
		Logger.out.debug(aAuditManager.auditEvent.getAuditEventLogCollection());*/
		
		
		/*Institution a = new Institution();
		a.setName("AA");
		aAuditManager.compare(a,null, "INSERT");*/
		/*Biohazard b=new Biohazard();
		b.setName("sdfdf");
		b.setType("asdfas");
		b.setComments("sdfsdf");
		aAuditManager.compare(b,null, "INSERT");
		//Specimen s=new Specimen();
		
		
		Logger.out.debug(aAuditManager.auditEvent.getAuditEventLogCollection());*/
	}
	
	
}