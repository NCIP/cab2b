/**
 * <p>Title: CDEBizLogic Class>
 * <p>Description:	This is biz Logic class for the CDEs.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 26, 2005
 */

package edu.wustl.common.bizlogic;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEImpl;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.cde.PermissibleValueImpl;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.tree.CDETreeNode;
import edu.wustl.common.tree.TreeDataInterface;
import edu.wustl.common.tree.TreeNode;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This is biz Logic class for the CDEs.
 * @author gautam_shetty
 */
/**
 * @author poornima_govindrao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * @author poornima_govindrao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CDEBizLogic extends DefaultBizLogic implements TreeDataInterface
{
    
    /**
     * Saves the storageType object in the database.
     * @param obj The storageType object to be saved.
     * @param session The session in which the object is saved.
     * @throws DAOException
     */
    protected void insert(Object obj,DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
    {
        CDEImpl cde = (CDEImpl) obj;
        
        //Delete the previous CDE data from the database.
        delete(cde, dao);
        
        //Insert the new CDE data in teh database.
        dao.insert(cde, sessionDataBean, false,false);
        Iterator iterator = cde.getPermissibleValues().iterator();
        while (iterator.hasNext())
        {
            PermissibleValueImpl permissibleValue = (PermissibleValueImpl) iterator.next();
            dao.insert(permissibleValue, sessionDataBean, false,false);
        }
    }	
    
    /**
     * Deletes the CDE and the corresponding permissible values from the database.
     * @param obj the CDE to be deleted.
     * @param dao the DAO object. 
     */
    protected void delete(Object obj, DAO dao) throws DAOException,
            UserNotAuthorizedException
    {
        CDE cde = (CDE) obj;
        List list = dao.retrieve(CDEImpl.class.getName(), "publicId", cde.getPublicId());
        if (!list.isEmpty())
        {
            CDEImpl cde1 = (CDEImpl)list.get(0);
            dao.delete(cde1);
        }
    }
    
    /**
     * (non-Javadoc)
     * @see edu.wustl.catissuecore.bizlogic.TreeDataInterface#getTreeViewData()
     */
    public Vector getTreeViewData() throws DAOException
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Vector getTreeViewData(String cdeName) throws DAOException
    {
        cdeName = URLDecoder.decode(cdeName);
//        try
//        {
//            cdeName = URLDecoder.decode(cdeName, "UTF-8");
//        }
//        catch(UnsupportedEncodingException encExp)
//        {
//            throw new DAOException("Could not generate tree : CDE name not proper.");
//        }
        
        CDE cde = CDEManager.getCDEManager().getCDE(cdeName);
        CDETreeNode root  = new CDETreeNode();
        root.setCdeName(cdeName);
        Vector vector = getTreeNodeList(root, cde.getPermissibleValues());
        
        return vector;
    }
    
	/**
     * @param cde
     * @return
     */
    private Vector getTreeNodeList(TreeNode parentTreeNode, Set permissibleValueSet)
    {
        Vector treeNodeVector = new Vector();
        if (permissibleValueSet == null)
            return null;
        
        Iterator iterator = permissibleValueSet.iterator();
        while (iterator.hasNext())
        {
            PermissibleValueImpl permissibleValueImpl = (PermissibleValueImpl) iterator.next();
            CDETreeNode treeNode = new CDETreeNode(permissibleValueImpl.getIdentifier(),
                    											 permissibleValueImpl.getValue());
            treeNode.setParentNode(parentTreeNode);
            treeNode.setCdeName(((CDETreeNode) parentTreeNode).getCdeName());
            Vector subPermissibleValues = getTreeNodeList(treeNode,
                    								permissibleValueImpl.getSubPermissibleValues());
            if (subPermissibleValues != null)
            {
            	//Bug-2717: For sorting  
            	Collections.sort(subPermissibleValues);
                treeNode.setChildNodes(subPermissibleValues);
            }
            
            treeNodeVector.add(treeNode);
            //Bug-2717: For sorting
            Collections.sort(treeNodeVector);
        }
        
        return treeNodeVector;
    }

    /**
     * Returns the CDE values without category names and only sub-categories
     * Poornima:Refer to bug 1718
     * @param permissibleValueSet - Set of permissible values  
     * @param permissibleValueList - Filtered CDEs
     */
    public void getFilteredCDE(Set permissibleValueSet,List permissibleValueList)
    {
        Iterator iterator = permissibleValueSet.iterator();
        while (iterator.hasNext())
        {
            PermissibleValue permissibleValue= (PermissibleValue) iterator.next();
            Set subPermissibleValues = permissibleValue.getSubPermissibleValues();
            //if there are no sub-permissible values, add to the list
            if (subPermissibleValues == null || 
            		subPermissibleValues.size()==0)
            {
            	permissibleValueList.add(new NameValueBean(permissibleValue.getValue(),permissibleValue.getValue()));
            }
            //else call the method for its children
            else
            {
            	getFilteredCDE(subPermissibleValues,permissibleValueList);
            }
        }
        //Bug-2717: For sorting
        Collections.sort(permissibleValueList);
    }
    
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.bizlogic.TreeDataInterface#getTreeViewData(edu.wustl.common.beans.SessionDataBean, java.util.Map)
	 */
	public Vector getTreeViewData(SessionDataBean sessionData, Map map,List list) throws DAOException {

		return null;
	}
}