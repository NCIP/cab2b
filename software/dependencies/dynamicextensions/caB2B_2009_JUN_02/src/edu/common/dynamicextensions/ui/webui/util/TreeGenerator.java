/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/*
 * Created on Oct 4, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.ui.webui.util;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;

/**
 * @author preeti_munot
 *
 */
public class TreeGenerator
{

	private String contextPath = null;

	/**
	 * 
	 * @return context path
	 */
	public String getContextPath()
	{
		return this.contextPath;
	}

	/**
	 * 
	 * @param contextPath  : Context path
	 */
	public void setContextPath(String contextPath)
	{
		this.contextPath = contextPath;
	}

	/**
	 * @param rootName : name of the root node
	 * @param childList : List of child nodes
	 * @return TreeData object for the given root  and list of child nodes
	 */
	public TreeData getTreeData(String rootName, ContainerInterface container)
	{
		TreeData treedata = new TreeData();
		treedata.setImagesUrl("images/");
		int i=0;
		TreeNode node = new TreeNode(rootName,i++);
		
		TreeNode subNode = null; 
		if(container!=null)
		{
			String containerName = container.getCaption();
			subNode = new TreeNode(containerName,i++);
			node.add(subNode);
		}
		if(subNode==null)
		{
			node.add(new TreeNode("New Form",i));
		}
		else
		{
			subNode.add(new TreeNode("New Form",i));
		}
		treedata.add(node);
		return treedata;
	}
}
