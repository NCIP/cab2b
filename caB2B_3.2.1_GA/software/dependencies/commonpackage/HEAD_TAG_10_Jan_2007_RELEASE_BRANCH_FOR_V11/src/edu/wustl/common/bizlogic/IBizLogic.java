/*
 * Created on Jul 3, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.bizlogic;

import java.util.List;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IBizLogic
{
	/**
     * Deletes an object from the database.
     * @param obj The object to be deleted.
     * @throws DAOException
     * @throws UserNotAuthorizedException TODO
     * @throws BizLogicException
     */
	public abstract void delete(Object obj, int daoType) throws UserNotAuthorizedException, BizLogicException;
	
	public void insert(Object obj,SessionDataBean sessionDataBean, int daoType) throws BizLogicException, UserNotAuthorizedException;
	
	public void insert(Object obj, int daoType) throws BizLogicException, UserNotAuthorizedException;
    
    public void update(Object currentObj,Object oldObj,int daoType, SessionDataBean sessionDataBean) throws BizLogicException, UserNotAuthorizedException;
	
    public void update(Object currentObj, int daoType) throws BizLogicException, UserNotAuthorizedException;
    public void createProtectionElement(Object currentObj) throws BizLogicException;
	public abstract List retrieve(String sourceObjectName, String[] selectColumnName, String[] whereColumnName,
            String[] whereColumnCondition, Object[] whereColumnValue,
            String joinCondition) throws DAOException;
	/**
	 * This method checks for a particular privilege on a particular Object_Id
	 * Gets privilege name as well as Object_Id from appropriate BizLogic 
	 * depending on the context of the operation
	 * @throws DAOException 
	 * @see edu.wustl.common.bizlogic.IBizLogic#isAuthorized(edu.wustl.common.dao.AbstractDAO, java.lang.Object, edu.wustl.common.beans.SessionDataBean)
	 */	
	public  boolean isAuthorized(AbstractDAO dao,Object domainObject, SessionDataBean sessionDataBean) throws UserNotAuthorizedException, DAOException;
	
	/**
	 * This method returns the protection element name which should be used to authorize.
	 * Default Implementation
	 * If call is through some bizLogic which does not require authorization, 
	 * let that operation be allowed for ALL
	 */
	public String getObjectId(AbstractDAO dao, Object domainObject);
    
    /**
     * Retrieves the records for class name in sourceObjectName according to field values passed.
     * @param whereColumnName An array of field names.
     * @param whereColumnCondition The comparision condition for the field values. 
     * @param whereColumnValue An array of field values.
     * @param joinCondition The join condition.
     */
    public abstract List retrieve(String sourceObjectName, String[] whereColumnName,
            String[] whereColumnCondition, Object[] whereColumnValue,
            String joinCondition) throws DAOException;    
    
    /**
     * Retrieves the records for class name in sourceObjectName according to field values passed.
     * @param colName Contains the field name.
     * @param colValue Contains the field value.
     */
    public abstract List retrieve(String className, String colName, Object colValue)
            throws DAOException;    
    
    /**
     * Retrieves all the records for class name in sourceObjectName.
     * @param sourceObjectName Contains the classname whose records are to be retrieved.
     */
    public abstract List retrieve(String sourceObjectName) throws DAOException;
    
    public Object retrieve(String sourceObjectName, Long id)  throws DAOException;
    
    public abstract List getList(String sourceObjectName, String[] displayNameFields, String valueField, String[] whereColumnName,
            String[] whereColumnCondition, Object[] whereColumnValue,
            String joinCondition, String separatorBetweenFields,  boolean isToExcludeDisabled) throws DAOException;
    
    public abstract List getList(String sourceObjectName, String[] displayNameFields, String valueField, boolean isToExcludeDisabled) 
    			throws DAOException;
    
    public abstract List getRelatedObjects(DAO dao, Class sourceClass, String classIdentifier,Long objIDArr[])throws DAOException;

    public void setPrivilege(int daoType,String privilegeName, Class objectType, Long[] objectIds, Long userId, SessionDataBean sessionDataBean, String roleId, boolean assignToUser, boolean assignOperation) throws SMException, BizLogicException;
    
    /**
	 * To retrieve the attribute value for the given source object name & Id.
	 * @param sourceObjectName Source object in the Database. 
	 * @param id Id of the object.
	 * @param attributeName attribute name to be retrieved. 
	 * @return The Attribute value corresponding to the SourceObjectName & id.
	 * @throws DAOException
	 */
    public Object retrieveAttribute(String sourceObjectName, Long id, String attributeName) throws DAOException;

	/**
	 * This is a wrapper function to retrieves attribute  for given class 
	 * name and identifier using dao.retrieveAttribute().
	 * @param objClass source Class object
	 * @param id identifer of the source object
	 * @param attributeName attribute to be retrieved
	 * @return
	 * @throws DAOException
	 */
    public Object retrieveAttribute(Class objClass, Long id, String attributeName) throws DAOException;
    public boolean populateUIBean(String className, Long identifier, IValueObject uiForm)throws DAOException,BizLogicException;
    
    public AbstractDomainObject populateDomainObject(String className, Long identifier, IValueObject uiForm) throws DAOException,BizLogicException,AssignDataException;
	
    public boolean isReadDeniedTobeChecked();
	
    public boolean hasPrivilegeToView(String objName, Long identifier, SessionDataBean sessionDataBean);
	
    public String getReadDeniedPrivilegeName();
}
