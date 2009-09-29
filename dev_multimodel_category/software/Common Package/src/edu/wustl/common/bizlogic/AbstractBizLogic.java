/**
 * <p>Title: AbstractBizLogic Class>
 * <p>Description:	AbstractBizLogic is the base class of all the Biz Logic classes.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 26, 2005
 */
package edu.wustl.common.bizlogic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import titli.controller.Name;
import titli.controller.RecordIdentifier;
import titli.controller.interfaces.IndexRefresherInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.exceptionformatter.ExceptionFormatter;
import edu.wustl.common.exceptionformatter.ExceptionFormatterFactory;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
  
/**
 * AbstractBizLogic is the base class of all the Biz Logic classes.
 * @author gautam_shetty
 */
public abstract class AbstractBizLogic implements IBizLogic
{           
	private static org.apache.log4j.Logger logger =Logger.getLogger(AbstractBizLogic.class);
	/**
     * This method gets called before insert method. Any logic before inserting into database can be included here.
     * @param obj The object to be inserted.
     * @param dao the dao object
     * @param sessionDataBean session specific data
     * @throws DAOException This will get thrown if application failed to
     *  communicate with database objects
     * @throws UserNotAuthorizedException 
     * */
	protected abstract void preInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException;
	
    /**
     * Inserts an object into the database.
     * @param obj The object to be inserted.
     * @param sessionDataBean TODO
     * @throws DAOException
     * @throws UserNotAuthorizedException TODO
     */
    protected abstract void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException;
    
    protected abstract void insert(Object obj, DAO dao) throws DAOException, UserNotAuthorizedException;
    
    /**
     * This method gets called after insert method. Any logic after insertnig object in database can be included here.
     * @param obj The inserted object.
     * @param dao the dao object
     * @param sessionDataBean session specific data
     * @throws DAOException
     * @throws UserNotAuthorizedException 
     * */
    protected abstract void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException;
    
    /**
     * This method gets called after insert method. Any logic after insertnig object in database can be included here.
     * @param objCollection Collection of object to be inserted
     * @param dao the dao object
     * @param sessionDataBean session specific data
     * @throws DAOException
     * @throws UserNotAuthorizedException 
     * */
    protected abstract void postInsert(Collection<AbstractDomainObject> objCollection, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException;
    /**
     * Deletes an object from the database.
     * @param obj The object to be deleted.
     * @throws DAOException
     * @throws UserNotAuthorizedException TODO
     */
    protected abstract void delete(Object obj, DAO dao) throws DAOException, UserNotAuthorizedException;
    
    /**
     * This method gets called before update method. Any logic before updating into database can be included here.
     * @param dao the dao object
     * @param currentObj The object to be updated.
     * @param oldObj The old object.
     * @param sessionDataBean session specific data
     * @throws DAOException
     * @throws UserNotAuthorizedException
     * */
    protected abstract void preUpdate(DAO dao,Object currentObj,Object oldObj , SessionDataBean sessionDataBean) throws BizLogicException, UserNotAuthorizedException;
    
    /**
     * Updates an objects into the database.
     * @param obj The object to be updated into the database. 
     * @param sessionDataBean TODO
     * @throws DAOException
     * @throws UserNotAuthorizedException TODO
     */
    protected abstract void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException;
    
    /**
     * This method gets called after update method. Any logic after updating into database can be included here.
     * @param dao the dao object
     * @param currentObj The object to be updated.
     * @param oldObj The old object.
     * @param sessionDataBean session specific data
     * @throws DAOException
     * @throws UserNotAuthorizedException
     * */
    protected abstract void postUpdate(DAO dao,Object currentObj,Object oldObj , SessionDataBean sessionDataBean) throws BizLogicException, UserNotAuthorizedException;
    
    protected abstract void update(DAO dao, Object obj) throws DAOException, UserNotAuthorizedException;
    
    /**
     * Validates the domain object for enumerated values.
     * @param obj The domain object to be validated. 
     * @param dao The DAO object
     * @param operation The operation(Add/Edit) that is to be performed.
     * @return True if all the enumerated value attributes contain valid values
     * @throws DAOException
     */
    protected abstract boolean validate(Object obj, DAO dao, String operation) throws DAOException;
    
    protected abstract void setPrivilege(DAO dao,String privilegeName, Class objectType, Long[] objectIds, Long userId, String roleId, boolean assignToUser, boolean assignOperation) throws SMException,DAOException;
    
    /**
     * Deletes an object from the database.
     * @param obj The object to be deleted.
     * @throws DAOException
     * @throws UserNotAuthorizedException TODO
     * @throws BizLogicException
     */
    public void delete(Object obj, int daoType) throws UserNotAuthorizedException, BizLogicException
    {
        AbstractDAO dao = DAOFactory.getInstance().getDAO(daoType);
		try
		{
	        dao.openSession(null);
	        delete(obj, dao);
	        dao.commit();
	        //refresh the index for titli search
//			refreshTitliSearchIndex(Constants.TITLI_DELETE_OPERATION, obj);
		}
		catch(DAOException ex)
		{
			String errMsg = getErrorMessage(ex,obj,"Deleting");
			if(errMsg==null)
			{
				errMsg=ex.getMessage();
			}
			try
			{
				dao.rollback();				
			}
			catch(DAOException daoEx)
			{
				throw new BizLogicException(daoEx.getMessage(), daoEx);
			}
			logger.debug("Error in delete");
			throw new BizLogicException(errMsg, ex);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				//TODO ERROR Handling
				throw new BizLogicException();
			}
		}
    }
    
  
    /**
     * This method gives the error message.
     * This method should be overrided for customizing error message
     * @param ex - Exception
     * @param obj - Object
     * @return - error message string
     */
    public String getErrorMessage(DAOException ex, Object obj, String operation)
    {
    	String errMsg;
    	
    	if (ex.getWrapException() == null)
    	{
    		errMsg = ex.getMessage();
    	}
    	else
    	{
    		errMsg = formatException(ex.getWrapException(),obj,operation);
    	}

        return errMsg;
    }
    /**
     * 
     * @param obj The object to be inserted
     * @param sessionDataBean  session specific data
     * @param daoType Type of dao (Hibernate or JDBC)
     * @param isInsertOnly If insert only is true then insert of Defaultbiz logic is called
     * @throws UserNotAuthorizedException User Not Authorized Exception
     * @throws BizLogicException Wrapping DAO exception into Bizlogic exception
     */
    private void insert(Object obj, SessionDataBean sessionDataBean, int daoType, boolean isInsertOnly) throws UserNotAuthorizedException, BizLogicException
    {
    	long startTime = System.currentTimeMillis();
    	AbstractDAO dao = DAOFactory.getInstance().getDAO(daoType);
		try
		{
	        dao.openSession(sessionDataBean);
	        // Authorization to ADD object checked here
	        if (isAuthorized(dao, obj, sessionDataBean))
            {
	        	validate(obj, dao, Constants.ADD);	        
	        	preInsert(obj, dao, sessionDataBean);
	        	insert(obj, sessionDataBean, isInsertOnly, dao);
		        dao.commit();
		        //refresh the index for titli search
		        //refreshTitliSearchIndex(Constants.TITLI_INSERT_OPERATION, obj);
		        postInsert(obj, dao, sessionDataBean);
            }	        
		}
		catch(DAOException ex)
		{
			String errMsg = getErrorMessage(ex,obj,"Inserting");
			if(errMsg==null)
			{
				errMsg=ex.getMessage();
			}
			try
			{
				dao.rollback();
			}
			catch(DAOException daoEx)
			{
				throw new BizLogicException(daoEx.getMessage(), daoEx);
			}
			logger.debug("Error in insert");
			throw new BizLogicException(errMsg, ex);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				//TODO ERROR Handling
				throw new BizLogicException();
			}
			long endTime = System.currentTimeMillis();		
			logger.info("EXECUTE TIME FOR ACTION - " + this.getClass().getSimpleName() + " : " + (endTime - startTime));
		}
    }
    /**
     * 
     * @param objCollection Collection of objects to be inserted
     * @param sessionDataBean  session specific data
     * @param daoType Type of dao (Hibernate or JDBC)
     * @param isInsertOnly If insert only is true then insert of Defaultbiz logic is called
     * @throws UserNotAuthorizedException User Not Authorized Exception
     * @throws BizLogicException Wrapping DAO exception into Bizlogic exception
     */
    public final void insert(Collection<AbstractDomainObject> objCollection, SessionDataBean sessionDataBean, int daoType, boolean isInsertOnly) throws BizLogicException, UserNotAuthorizedException
    {
    	AbstractDAO dao = DAOFactory.getInstance().getDAO(daoType);
    	try
    	{
    		dao.openSession(sessionDataBean);
    		
	    	preInsert(objCollection, dao, sessionDataBean);
	    	insertMultiple(objCollection,dao,sessionDataBean);
			dao.commit();
			postInsert(objCollection, dao, sessionDataBean);
		}
		catch (DAOException ex)
		{
			String errMsg = getErrorMessage(ex,objCollection,"Inserting");
			if(errMsg==null)
			{
				errMsg=ex.getMessage();
			}
			try
			{
				dao.rollback();
			}
			catch(DAOException daoEx)
			{
				throw new BizLogicException(daoEx.getMessage(), daoEx);
			}
			logger.debug("Error in insert");
			throw new BizLogicException(errMsg, ex);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				String errMsg = getErrorMessage(daoEx,objCollection,"Session Close");
				errMsg=daoEx.getMessage();
				throw new BizLogicException(errMsg,daoEx);
			}
		}
    }
    /**
     * 
     * @param objCollection Collection of objects to be inserted
     * @param sessionDataBean  session specific data
     * @param dao Type of dao (Hibernate or JDBC)
     * @throws UserNotAuthorizedException User Not Authorized Exception
     * @throws BizLogicException Wrapping DAO exception into Bizlogic exception
     */
    public final void insertMultiple(Collection<AbstractDomainObject> objCollection, AbstractDAO dao,SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
    	//  Authorization to ADD multiple objects (e.g. Aliquots) checked here
		for(AbstractDomainObject obj : objCollection)
		{
    		if (!isAuthorized(dao, obj, sessionDataBean))
	        {
	        	throw new UserNotAuthorizedException();
	        } 
    		else
    		{	
			    validate(obj, dao, Constants.ADD);	
			}
		}    	
		for(AbstractDomainObject obj : objCollection)
		{
		    insert(obj, sessionDataBean, false, dao);
		}
    }
	/**
	 * @param obj object to be inser and validate
	 * @param sessionDataBean
	 * @param isInsertOnly
	 * @param dao
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private void insert(Object obj, SessionDataBean sessionDataBean,
			boolean isInsertOnly, AbstractDAO dao) throws DAOException, UserNotAuthorizedException
	{
		if(isInsertOnly)
		{
			insert(obj,dao);	        	
		}
		else
		{
			insert(obj, dao, sessionDataBean);
		}
	}
    
    public final void insert(Object obj,SessionDataBean sessionDataBean, int daoType) throws BizLogicException, UserNotAuthorizedException
	{
		insert(obj, sessionDataBean, daoType, false);
	}
    
    public final void insert(Object obj, int daoType) throws BizLogicException, UserNotAuthorizedException
    {
    	insert(obj, null, daoType, true);
    }
  
    private void update(Object currentObj,Object oldObj,int daoType, SessionDataBean sessionDataBean, boolean isUpdateOnly) throws BizLogicException, UserNotAuthorizedException
    {                     
    	long startTime = System.currentTimeMillis();
    	AbstractDAO dao = DAOFactory.getInstance().getDAO(daoType);
		try
		{
	        dao.openSession(sessionDataBean);
	        
	        // Authorization to UPDATE object checked here
	        if (!isAuthorized(dao, currentObj, sessionDataBean))
	        {
	        	throw new UserNotAuthorizedException();
	        } 
	        else
	        {
		        validate(currentObj, dao, Constants.EDIT);
		        preUpdate(dao, currentObj, oldObj, sessionDataBean);
		        if(isUpdateOnly)
		        {
		        	//update(currentObj,daoType);
	                update(dao, currentObj);
		        }else{
		        	update(dao, currentObj, oldObj, sessionDataBean);
		        }  
		        dao.commit();
		        //refresh the index for titli search
	//			refreshTitliSearchIndex(Constants.TITLI_UPDATE_OPERATION, currentObj);
		        postUpdate(dao, currentObj, oldObj, sessionDataBean);
	        }
		}
		catch(DAOException ex)
		{
			//added to format constrainviolation message
			String errMsg = getErrorMessage(ex,currentObj,"Updating");
			if(errMsg==null)
			{
				errMsg=ex.getMessage();
			}
			try
			{
				dao.rollback();				
			}
			catch(DAOException daoEx)
			{
				//TODO ERROR Handling
				throw new BizLogicException(daoEx.getMessage(), daoEx);
				//throw new BizLogicException(ex.getMessage(), ex);
			}
			//TODO ERROR Handling
			throw new BizLogicException(errMsg, ex);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				//TODO ERROR Handling
				throw new BizLogicException();
			}
			long endTime = System.currentTimeMillis();		
			logger.info("EXECUTE TIME FOR ACTION - " + this.getClass().getSimpleName() + " : " + (endTime - startTime));
		}
    }
    
    /**
     * 
     */
    public final void update(Object currentObj,Object oldObj,int daoType, SessionDataBean sessionDataBean) throws BizLogicException, UserNotAuthorizedException
	{
		update(currentObj, oldObj, daoType, sessionDataBean, false);
	}
    
    /**
     * 
     */
    public final void update(Object currentObj, int daoType) throws BizLogicException, UserNotAuthorizedException
	{
    	update(currentObj, null, daoType, null, true);
	}
    
    public final void setPrivilege(int daoType,String privilegeName, Class objectType, Long[] objectIds, Long userId, SessionDataBean sessionDataBean, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, BizLogicException
    {
        AbstractDAO dao = DAOFactory.getInstance().getDAO(daoType);
		try
		{
		    logger.debug(" privilegeName:"+privilegeName+" objectType:"+objectType+" objectIds:"+edu.wustl.common.util.Utility.getArrayString(objectIds)+" userId:"+userId+" roleId:"+roleId+" assignToUser:"+assignToUser);
	        dao.openSession(sessionDataBean);
	        setPrivilege(dao, privilegeName,objectType,objectIds,userId, roleId, assignToUser, assignOperation);
	        dao.commit();
		}
		catch(DAOException ex)
		{
			try
			{
				dao.rollback();
			}
			catch(DAOException daoEx)
			{
				//TODO ERROR Handling
				throw new BizLogicException(daoEx.getMessage(), daoEx);
				//throw new BizLogicException(ex.getMessage(), ex);
			}
			//TODO ERROR Handling
			throw new BizLogicException(ex.getMessage(), ex);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				//TODO ERROR Handling
				throw new BizLogicException("Unknown Error");
			}
		}
    }
    
	public String formatException(Exception ex, Object obj, String operation)
	{
		String errMsg="";
		if(ex==null)
		{
			return null;
		}
		String roottableName = null;
		String tableName = null;
    	try
    	{   				
    	    // Get ExceptionFormatter
        	ExceptionFormatter ef = ExceptionFormatterFactory.getFormatter(ex);
			// call for Formating Message
			if(ef!=null)
			{
				roottableName=HibernateMetaData.getRootTableName(obj.getClass());
				tableName=HibernateMetaData.getTableName(obj.getClass());
				Object[] arguments = {roottableName,DBUtil.currentSession().connection(),tableName};
				errMsg = ef.formatMessage(ex,arguments);
			}
			else
			{
				// if ExceptionFormatter not found Format message through Default Formatter 
				//String arg[]={operation,tableName};
	            //errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.SMException.01",arg);
				errMsg=ex.getMessage();
			}
    	}
    	catch(Exception e)
    	{
    		logger.error(ex.getMessage(),ex);
    		// if Error occured while formating message then get message
    		// formatted through Default Formatter
    		String arg[]={operation,tableName};
            errMsg = new DefaultExceptionFormatter().getErrorMessage("Err.SMException.01",arg);   
    	}
    	return errMsg;
	}
		
	

	/**
	 * refresh the titli search index to reflect the changes in the database
	 * @param operation the operation to be performed : "insert", "update" or "delete"
	 * @param obj the object correspondig to the record to be refreshed
	 */
	private void refreshTitliSearchIndex(String operation, Object obj) 
	{            
		try
		{ 
			TitliInterface titli = Titli.getInstance();
			Name dbName = (titli.getDatabases().keySet().toArray(new Name[0]))[0]; 
			String tableName = HibernateMetaData.getTableName(obj.getClass()).toLowerCase();
			System.out.println("tableName: "+tableName);
			String id= ((AbstractDomainObject) obj).getId().toString();
			System.out.println("id: "+id);
						
			Map<Name, String> uniqueKey = new HashMap<Name, String>();
			uniqueKey.put(new Name(Constants.IDENTIFIER), id);
			
			RecordIdentifier recordIdentifier = new RecordIdentifier(dbName,	new Name(tableName), uniqueKey);
		
			IndexRefresherInterface indexRefresher = titli.getIndexRefresher();
			
			if (operation != null && operation.equalsIgnoreCase(Constants.TITLI_INSERT_OPERATION)) 
			{
				indexRefresher.insert(recordIdentifier);
			}
			else if (operation != null	&& operation.equalsIgnoreCase(Constants.TITLI_UPDATE_OPERATION)) 
			{   
				indexRefresher.update(recordIdentifier);
			}
			else if (operation != null	&& operation.equalsIgnoreCase(Constants.TITLI_DELETE_OPERATION)) 
			{
				indexRefresher.delete(recordIdentifier);
			}
		} 
		catch (TitliException e) 
		{
			logger.error("Titli search index cound not be refreshed for opeartion "+operation, e);
		}
		
	}
	
	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param colName Contains the field name.
	 * @param colValue Contains the field value.
	 */
	public boolean populateUIBean(String className, Long identifier, IValueObject uiForm) throws DAOException, BizLogicException
	{
		long startTime = System.currentTimeMillis();
		boolean isSuccess = false;
		
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);

			//List list= dao.retrieve(className, Constants.SYSTEM_IDENTIFIER, identifier);
			Object object = dao.retrieve(className, identifier);
						
	        if (object!=null)
	        {
	            /* 
	              If the record searched is present in the database,
	              populate the formbean with the information retrieved.
	             */
	        	AbstractDomainObject abstractDomain = (AbstractDomainObject)object;
	        	
	        	prePopulateUIBean(abstractDomain,  uiForm);
	        	uiForm.setAllValues(abstractDomain);
	        	postPopulateUIBean(abstractDomain, uiForm);
	            isSuccess = true;
	        }
		}
		catch (DAOException daoExp)
		{
			logger.error(daoExp.getMessage(),daoExp);
			String errMsg = daoExp.getMessage();
			throw new BizLogicException(errMsg, daoExp);	
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				throw new BizLogicException();
			}
		}
		
		String simpleClassName = Utility.parseClassName(className);
		
		long endTime = System.currentTimeMillis();
		logger.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR UI - "+ simpleClassName + " : " + (endTime - startTime));

		return isSuccess;
	}
	
	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param colName Contains the field name.
	 * @param colValue Contains the field value.
	 */
	public AbstractDomainObject populateDomainObject(String className, Long identifier, IValueObject uiForm) throws DAOException,BizLogicException,AssignDataException
	{
		long startTime = System.currentTimeMillis();
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		AbstractDomainObject abstractDomain = null;
		
		try
		{
			dao.openSession(null);

			//List list = dao.retrieve(className,Constants.SYSTEM_IDENTIFIER,identifier);
			Object object = dao.retrieve(className, identifier);
									
			if (object!=null)
	        {
	            /* 
	              If the record searched is present in the database,
	              populate the formbean with the information retrieved.
	             */
	        	abstractDomain = (AbstractDomainObject)object;
	        	if( abstractDomain != null )
	        	{
	        		abstractDomain.setAllValues(uiForm);
	        	}
	        }
			//dao.commit();
		}
		catch (DAOException daoExp)
		{
			logger.error(daoExp.getMessage(),daoExp);
			String errMsg=daoExp.getMessage();
			throw new BizLogicException(errMsg, daoExp);
		}
		catch (AssignDataException daoExp)
		{
			logger.error(daoExp.getMessage(),daoExp);
			throw daoExp;
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				throw new BizLogicException();
			}
		}
		
		String simpleClassName = Utility.parseClassName(className);
		
		long endTime = System.currentTimeMillis();
		logger.info("EXECUTE TIME FOR RETRIEVE IN EDIT FOR DB - "+	simpleClassName +" : "+ (endTime - startTime));

		return abstractDomain;
	}
	
	/**
	 * This method gets called before populateUIBean method. Any logic before updating uiForm can be included here.
	 * @param domainObj object of type AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 */
	protected abstract void prePopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm) throws BizLogicException;
	
	/**
	 * This method gets called after populateUIBean method. Any logic after populating  object uiForm can be included here.
	 * @param domainObj object of type AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 */
	protected abstract void postPopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm) throws BizLogicException;	
	
	public abstract boolean isReadDeniedTobeChecked();
	
	public abstract String getReadDeniedPrivilegeName();
}