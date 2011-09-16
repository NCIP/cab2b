/**
 * <p>Title: HibernateDAO Class>
 * <p>Description:	HibernateDAO is default implemention of AbstractDAO through Hibernate ORM tool.
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 16, 2005
 */

package edu.wustl.common.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.audit.Auditable;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AuditException;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * Default implemention of AbstractDAO through Hibernate ORM tool.
 * @author kapil_kaveeshwar
 */
public class HibernateDAOImpl implements HibernateDAO
{
    protected Session session = null;
    protected Transaction transaction = null;
    protected AuditManager auditManager;
    private boolean isUpdated = false;
    
    /**
     * This method will be used to establish the session with the database.
     * Declared in AbstractDAO class.
     * 
     * @throws DAOException
     */
    public void openSession(SessionDataBean sessionDataBean) throws DAOException
    {
    	//Logger.out.info("Session opened:------------------------");
        try
        {
            session = DBUtil.currentSession();
           // Logger.out.info("Transaction begin:---------------------");
            transaction = session.beginTransaction();
            
            auditManager = new AuditManager();
            
            if(sessionDataBean!=null)
            {
            	auditManager.setUserId(sessionDataBean.getUserId());
            	auditManager.setIpAddress(sessionDataBean.getIpAddress());
            }
            else
            {
                auditManager.setUserId(null);
            }
        }
        catch (HibernateException dbex)
        {
           // Logger.out.error(dbex.getMessage(), dbex);
            throw handleError(Constants.GENERIC_DATABASE_ERROR, dbex);
        }
    }
    
    /**
     * This method will be used to close the session with the database.
     * Declared in AbstractDAO class.
     * @throws DAOException
     */
    public void closeSession() throws DAOException
    {
    	
        try
        {
            DBUtil.closeSession();
         //   Logger.out.info("-------------close session------------");
        }
        catch (HibernateException dx)
        {
            //Logger.out.error(dx.getMessage(), dx);
            throw handleError(Constants.GENERIC_DATABASE_ERROR, dx);
        }
        session = null;
        transaction = null;
        auditManager = null;
    }
    
    /**
     * Commit the database level changes.
     * Declared in AbstractDAO class.
     * @throws DAOException
     */
    public void commit() throws DAOException
    {
        try
        {
            auditManager.insert(this);
            
            if (transaction != null)
                transaction.commit();
        }
        catch (HibernateException dbex)
        {
            //Logger.out.error(dbex.getMessage(), dbex);
            throw handleError("Error in commit: ", dbex);
        }
    }
    
    /**
     * Rollback all the changes after last commit. 
     * Declared in AbstractDAO class. 
     * @throws DAOException
     */
    public void rollback() throws DAOException
    {
     /** 
      * the isUpdated==true is removed because if there is cascade save-update and the association collection objects 
      * is violating constaring then in insert() method session.save() is throwing exception and isUpdated 
      * is not gettting set to true. Because of this roll back is not happining on parent object
      * 
      */
    	if(isUpdated==true) 
        {
	        try
	        {
	            if (transaction != null)
	            {
	                transaction.rollback();
	            }
	        }
	        catch (HibernateException dbex)
	        {
	            //Logger.out.error(dbex.getMessage(), dbex);
	            throw handleError("Error in rollback: ", dbex);
	        }
        }
    }
    
    public void disableRelatedObjects(String tableName, String whereColumnName, Long whereColumnValues[]) throws DAOException
	{
    	try
        {
    		Statement st = session.connection().createStatement();
    		
    		StringBuffer buff = new StringBuffer();
    		for (int i = 0; i < whereColumnValues.length; i++)
			{
    			buff.append(whereColumnValues[i].longValue());
    			if((i+1)<whereColumnValues.length)
    				buff.append(",");
			}
    		
    		String sql = "UPDATE "+tableName+" SET ACTIVITY_STATUS = '"+Constants.ACTIVITY_STATUS_DISABLED+ "' WHERE "+whereColumnName+" IN ( "+buff.toString()+")";
    		//Logger.out.debug("sql "+sql);
    		int count = st.executeUpdate(sql);
    		st.close();
    		//Logger.out.debug("Update count "+count);
        }
        catch (HibernateException dbex)
        {
        	//Logger.out.error(dbex.getMessage(),dbex);
        	throw handleError("Error in JDBC connection: ",dbex);
        }
        catch (SQLException sqlEx)
        {
        	//Logger.out.error(sqlEx.getMessage(),sqlEx);
        	throw handleError("Error in disabling Related Objects: ",sqlEx);
        }
	}
    
    /**
     * Saves the persistent object in the database.
     * @param obj The object to be saved.
     * @param session The session in which the object is saved.
     * @throws DAOException
     * @throws HibernateException Exception thrown during hibernate operations.
     */
    public void insert(Object obj, SessionDataBean sessionDataBean,
            boolean isAuditable, boolean isSecureInsert) throws DAOException, UserNotAuthorizedException
    {
    	//Logger.out.info("inser call---------------------");
        boolean isAuthorized = true;
        
        try
        {
        /**
		* Now, Authorizations on Objects will be done in corresponding biz logic 
		* for the Object through DefaultBizLogic's 'isAuthorized' method
		* For this version, each Project will have to provide its implementation
		* for objects which require secured access 
		* By Default :: we return as 'true' i.e. user authorized
		*/   
            if(isAuthorized)
            {
                session.save(obj);
                if (obj instanceof Auditable && isAuditable)
                    auditManager.compare((Auditable) obj, null, "INSERT");
                isUpdated = true;
            }
            else
            {
                throw new UserNotAuthorizedException("Not Authorized to insert");
            }
        }
        catch (HibernateException hibExp)
        {
            throw handleError("", hibExp);
        }
        catch (AuditException hibExp)
        {
            throw handleError("", hibExp);
        }
        catch( SMException smex)
        {
            throw handleError("", smex);
        }
        
    }
    
    
   private DAOException handleError(String message, Exception hibExp)
    {
       // Logger.out.error(hibExp.getMessage(), hibExp);
        String msg = generateErrorMessage(message, hibExp);
        return new DAOException(msg, hibExp);
    }
    
    private String generateErrorMessage(String messageToAdd, Exception ex)
    {
        if (ex instanceof HibernateException)
        {
            HibernateException hibernateException = (HibernateException) ex;
            StringBuffer message = new StringBuffer(messageToAdd);
            String str[] = hibernateException.getMessages();
            if (message != null)
            {
                for (int i = 0; i < str.length; i++)
                {
                	//Logger.out.debug("str:" + str[i]);
                    message.append(str[i] + " ");
                }
            }
            else
            {
                return "Unknown Error";
            }
            return message.toString();
        }
        else
        {
            return ex.getMessage();
        }
    }
    
    /**
     * Updates the persistent object in the database.
     * @param obj The object to be updated.
     * @param session The session in which the object is saved.
     * @throws DAOException 
     * @throws HibernateException Exception thrown during hibernate operations.
     */
    public void update(Object obj, SessionDataBean sessionDataBean, boolean isAuditable, boolean isSecureUpdate, boolean hasObjectLevelPrivilege) throws DAOException, UserNotAuthorizedException
    {
    	boolean isAuthorized = true;
        try
        {
       /**
		* Now, Authorizations on Objects will be done in corresponding biz logic 
		* for the Object through DefaultBizLogic's 'isAuthorized' method
		* For this version, each Project will have to provide its implementation
		* for objects which require secured access 
		* By Default :: we return as 'true' i.e. user authorized
		*/   
            
            if(isAuthorized)
            {
                session.update(obj);
    //                Object oldObj = retrieve(obj.getClass().getName(), ((Auditable)obj).getId());
    //                if (obj instanceof Auditable && isAuditable)
    //                auditManager.compare((Auditable) obj, (Auditable)oldObj, "UPDATE");
                isUpdated = true;
                
            }
            else
            {
                throw new UserNotAuthorizedException("Not Authorized to update");
            }
        }
        catch (HibernateException hibExp)
        {
            //Logger.out.error(hibExp.getMessage(), hibExp);
            //throw new DAOException("Error in update", hibExp);
        	throw handleError("", hibExp);
        }
//        catch (AuditException hibExp)
//        {
//            throw handleError("", hibExp);
//        }
        catch (SMException smex)
        {
            //Logger.out.error(smex.getMessage(), smex);
            //throw new DAOException("Error in update", smex);
        	throw handleError("", smex);
        }
       
    }

    
    public void audit(Object obj, Object oldObj, SessionDataBean sessionDataBean, boolean isAuditable) throws DAOException
    {
        try
        {
            if (obj instanceof Auditable && isAuditable)
            {
                //Logger.out.debug("In update audit...................................");
                auditManager.compare((Auditable) obj, (Auditable)oldObj, "UPDATE");
            }
        }
        catch (AuditException hibExp)
        {
            throw handleError("", hibExp);
        }
    }
    
    public void addAuditEventLogs(Collection auditEventDetailsCollection)
    {
        auditManager.addAuditEventLogs(auditEventDetailsCollection);
    }
    
    /**
     * Deletes the persistent object from the database.
     * @param obj The object to be deleted.
     */
    public void delete(Object obj) throws DAOException
    {
        try
        {
            session.delete(obj);
            
            //            if(isAuditable)
            //        		auditManager.compare((AbstractDomainObject)obj,null,"INSERT");
        }
        catch (HibernateException hibExp)
        {
           // Logger.out.error(hibExp.getMessage(), hibExp);
            throw new DAOException("Error in delete", hibExp);
        }
    }
    
    /**
     * Retrieves all the records for class name in sourceObjectName.
     * @param sourceObjectName Contains the classname whose records are to be retrieved.
     */
    public List retrieve(String sourceObjectName) throws DAOException
    {
        return retrieve(sourceObjectName, null, null, null, null, null);
    }
    
    public List retrieve(String sourceObjectName, String whereColumnName,
            Object whereColumnValue) throws DAOException
    {
        String whereColumnNames[] = {whereColumnName};
        String colConditions[] = {"="};
        Object whereColumnValues[] = {whereColumnValue};

        return retrieve(sourceObjectName, null, whereColumnNames,
                colConditions, whereColumnValues, Constants.AND_JOIN_CONDITION);
    }
    
    /**
     * (non-Javadoc)
     * @see edu.wustl.common.dao.AbstractDAO#retrieve(java.lang.String, java.lang.String[])
     */
    public List retrieve(String sourceObjectName, String[] selectColumnName)
            throws DAOException
    {
        String[] whereColumnName = null;
        String[] whereColumnCondition = null;
        Object[] whereColumnValue = null;
        String joinCondition = null;
        return retrieve(sourceObjectName, selectColumnName, whereColumnName,
                whereColumnCondition, whereColumnValue, joinCondition);
    }
    
    /**
     * Retrieves the records for class name in sourceObjectName according to field values passed in the passed session.
     * @param whereColumnName An array of field names.
     * @param whereColumnCondition The comparision condition for the field values. 
     * @param whereColumnValue An array of field values.
     * @param joinCondition The join condition.
     * @param The session object.
     */
    public List retrieve(String sourceObjectName, String[] selectColumnName,
            String[] whereColumnName, String[] whereColumnCondition,
            Object[] whereColumnValue, String joinCondition)
            throws DAOException
    {
        List list = null;
        try
        {
            StringBuffer sqlBuff = new StringBuffer();
            
            String className = Utility.parseClassName(sourceObjectName);
            
            //Logger.out.debug("***********className:"+className);
            if (selectColumnName != null && selectColumnName.length > 0)
            {
                sqlBuff.append("Select ");
                
                //Added by Aarti 
                //--------------------------------
//                if(sourceObjectName.equals(Site.class.getName()))
//                {
//                    sqlBuff.append(className + "." + Constants.SYSTEM_IDENTIFIER);
//                    sqlBuff.append(", ");
//                }
                //---------------------------------
                for (int i = 0; i < selectColumnName.length; i++)
                {
                    sqlBuff.append(Utility.createAttributeNameForHQL(className, selectColumnName[i]));
                    if (i != selectColumnName.length - 1)
                    {
                        sqlBuff.append(", ");
                    }
                }
                sqlBuff.append(" ");
            }
            //Logger.out.debug(" String : "+sqlBuff.toString());
            
            Query query = null;
            sqlBuff.append("from " + sourceObjectName
                    + " " + className);
            //Logger.out.debug(" String : "+sqlBuff.toString());
            
            if ((whereColumnName != null && whereColumnName.length > 0)
                    && (whereColumnCondition != null && whereColumnCondition.length == whereColumnName.length)
                    && (whereColumnValue != null))
            {
                if (joinCondition == null)
                    joinCondition = Constants.AND_JOIN_CONDITION;
                
                sqlBuff.append(" where ");
                
                //Adds the column name and search condition in where clause. 
                for (int i = 0; i < whereColumnName.length; i++)
                {
                    sqlBuff.append(className + "." + whereColumnName[i] + " ");
                    if(whereColumnCondition[i].indexOf("in")!=-1)
                    {
                    	sqlBuff.append(whereColumnCondition[i] + "(  ");
                    	Object valArr[] = (Object [])whereColumnValue[i];
                    	for (int j = 0; j < valArr.length; j++)
						{
                    		//Logger.out.debug(sqlBuff);
                    		sqlBuff.append("? ");
                    		if((j+1)<valArr.length)
                    			sqlBuff.append(", ");
						}
                    	sqlBuff.append(") ");
                    }
                    else if(whereColumnCondition[i].indexOf("is not null")!=-1)
                    {
                    	sqlBuff.append(whereColumnCondition[i]);
                    }
                    else if(whereColumnCondition[i].indexOf("is null")!=-1)
                    {
                        sqlBuff.append(whereColumnCondition[i]);
                    }
                    else
                    {
                    	sqlBuff.append(whereColumnCondition[i] + " ? ");
                    }
                    
                    if (i < (whereColumnName.length - 1))
                        sqlBuff.append(" " + joinCondition + " ");
                }
                
                //Logger.out.debug(sqlBuff.toString());
                
                query = session.createQuery(sqlBuff.toString());
                
                int index = 0;
                //Adds the column values in where clause
                for (int i = 0; i < whereColumnValue.length; i++)
                {
                    //Logger.out.debug("whereColumnValue[i]. " + whereColumnValue[i]);
                    if(whereColumnCondition[i].equals("is null") || whereColumnCondition[i].equals("is not null") )
                    {}
                    else
                    { 
                    
                        Object obj = whereColumnValue[i];
                        if(obj instanceof Object[])
                        {
                        	Object[] valArr = (Object[])obj;
                        	for (int j = 0; j < valArr.length; j++)
    						{
                        		query.setParameter(index, valArr[j]);
                        		index++;
    						}
                        }
                        else
                        {
                        	query.setParameter(index, obj);
                        	index++;
                        }
                    }
                }
            }
            else
            {
                query = session.createQuery(sqlBuff.toString());
            }
            
            list = query.list();
            
        }
        catch (HibernateException hibExp)
        {
            //Logger.out.error(hibExp.getMessage(), hibExp);
            throw new DAOException("Error in retrieve " + hibExp.getMessage(),
                    hibExp);
        }
        catch (Exception exp)
        {
            //Logger.out.error(exp.getMessage(), exp);
            throw new DAOException("Logical Erroe in retrieve method "
                    + exp.getMessage(), exp);
        }
        return list;
    }
    
    public Object retrieve(String sourceObjectName, Long id)
            throws DAOException
    {
        try
        {
        	Object object = session.load(Class.forName(sourceObjectName), id); 
            return HibernateMetaData.getProxyObjectImpl(object);
        }
        catch (ClassNotFoundException cnFoundExp)
        {
           // Logger.out.error(cnFoundExp.getMessage(), cnFoundExp);
            throw new DAOException("Error in retrieve "
                    + cnFoundExp.getMessage(), cnFoundExp);
        }
        catch (HibernateException hibExp)
        {
           // Logger.out.error(hibExp.getMessage(), hibExp);
            throw new DAOException("Error in retrieve " + hibExp.getMessage(),
                    hibExp);
        }
    }
    
	public Object loadCleanObj(String sourceObjectName, Long id) throws Exception
	{
		Object obj = retrieve(sourceObjectName, id);
		session.evict(obj);
		return obj;
	}
	
	
	/**
     * Executes the HQL query.
     * @param query HQL query to execute.
     * @param sessionDataBean TODO
     * @param isSecureExecute TODO
     * @param columnIdsMap
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List executeQuery(String query, SessionDataBean sessionDataBean, boolean isSecureExecute, Map queryResultObjectDataMap) throws ClassNotFoundException, DAOException
    {
    	List returner = null;
    	
    	try
		{
			Query hibernateQuery = session.createQuery(query);
			returner = hibernateQuery.list();
			
		}
		catch (HibernateException e)
		{
			throw (new DAOException(e));
		}
    	
		return returner;
    }
    
    /**
     * Executes the HQL query.
     * @param query
     * @param sessionDataBean TODO
     * @param isSecureExecute TODO
     * @param columnIdsMap
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List executeQuery(String query, SessionDataBean sessionDataBean, boolean isSecureExecute, boolean hasConditionOnIdentifiedField, Map queryResultObjectDataMap) throws ClassNotFoundException, DAOException
    {
    	
		return null;
    }

    public Object retrieveAttribute(Class<AbstractDomainObject> objClass,Long id,
    								String attributeName)
    								throws DAOException{
    	
    	String objClassName =objClass.getName();
    	String simpleName =objClass.getSimpleName();
    	attributeName= Utility.createAttributeNameForHQL(simpleName,attributeName);    	
		
    	StringBuffer queryStringBuffer = new StringBuffer();
		queryStringBuffer.append("Select ").append(simpleName)
						.append(".").append(attributeName)
						.append(" from ")
						.append(objClassName).append(" ").append(simpleName)
						.append(" where ")
						.append(simpleName).append(".").append(Constants.SYSTEM_IDENTIFIER)
						.append("=").append(id);
		try
		{
			return session.createQuery(queryStringBuffer.toString()).list();
		}
		catch(HibernateException exception)
		{
			throw new DAOException(exception.getMessage(),exception);
		}
    }
    /**
	 * To retrieve the attribute value for the given source object name & Id.
	 * @param sourceObjectName Source object in the Database. 
	 * @param id Id of the object.
	 * @param attributeName attribute name to be retrieved. 
	 * @return The Attribute value corresponding to the SourceObjectName & id.
	 * @throws DAOException
	 * @see edu.wustl.common.dao.DAO#retrieveAttribute(java.lang.String, java.lang.Long, java.lang.String)
	 * @deprecated This function is deprecated use retrieveAttribute(Class className,Long id, String attributeName)
	 */
	public Object retrieveAttribute(String sourceObjectName, Long id, String attributeName) throws DAOException 
	{
		String[] selectColumnNames = {attributeName};
		String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER}; 
		String[] whereColumnCondition = {"="};
		Object[] whereColumnValue = {id};
		
		List result = retrieve(sourceObjectName, selectColumnNames, whereColumnName, whereColumnCondition, whereColumnValue, null);
		Object attribute = null;
		
		/*
		 * if the attribute is of type collection, then it needs to be returned as Collection(HashSet)
		 */
		if (Utility.isColumnNameContainsElements(attributeName))
		{
			Collection collection = new HashSet();
			attribute = collection;
			for(int i=0;i<result.size();i++)
			{
				/**
				 * Name: Prafull
				 * Calling HibernateMetaData.getProxyObject() because it could be proxy object.
				 */
				collection.add(HibernateMetaData.getProxyObjectImpl(result.get(i)));
			}
		}
		else
		{
			if (!result.isEmpty())
			{
				/**
				 * Name: Prafull
				 * Calling HibernateMetaData.getProxyObject() because it could be proxy object.
				 */
				attribute = HibernateMetaData.getProxyObjectImpl(result.get(0));
			}
		}
		
		return attribute;	
	}
    
}