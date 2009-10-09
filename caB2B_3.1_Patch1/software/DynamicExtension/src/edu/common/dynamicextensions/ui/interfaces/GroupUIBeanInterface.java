/*
 * Created on Nov 16, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.interfaces;

import java.util.List;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface GroupUIBeanInterface
{
	/**
	 * 
	 * @return group description
	 */
	public String getGroupDescription();

	/**
	 * 
	 * @param groupDescription : description for the group
	 */
	public void setGroupDescription(String groupDescription);

	/**
	 * 
	 * @return groupName 
	 */
	public String getGroupName();

	/**
	 * 
	 * @param groupName : Name of group
	 */
	public void setGroupName(String groupName);
	/**
	 * 
	 * @return Group operation performed :  Save or show next page
	 */
	public String getGroupOperation();
	/**
	 * 
	 * @param groupOperation : Group operation performed - Save or show next page
	 */
	public void setGroupOperation(String groupOperation);
	
	/**
	 * 
	 * @return create new group or use existing
	 */
	public String getCreateGroupAs();

	/**
	 * 
	 * @param createGroupAs Create group or use existing
	 */
	public void setCreateGroupAs(String createGroupAs);
	
	/**
	 * @return
	 */
	public List getGroupList();

	/**
	 * 
	 * @param groupList
	 */
	public void setGroupList(List groupList);
	
	/**
	 * 
	 * @return
	 */
	public String getGroupNameText();
	
	/**
	 * 
	 * @param groupNameText
	 */
	public void setGroupNameText(String groupNameText);
	

}
