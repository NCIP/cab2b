/**
 * <p>Title: QueryTreeNodeData Class>
 * <p>Description: QueryTreeNodeData represents the node in the query result view tree.</p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Poornima Govindrao
 * @version 1.00
 */
package edu.wustl.common.tree;

import java.io.Serializable;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.common.util.global.Constants;


/**
 * QueryTreeNodeData represents the node in the query result view tree.
 * @author poornima_govindrao
 */
public class QueryTreeNodeData implements QueryTreeNode, Serializable
{
	private String identifier;
	
    private String objectName;
    
    // This will be Containe the actual name display label of the node
    private String displayName;
    
    private String parentObjectIdentifier;
    
    private String parentObjectName;
    
    private String combinedParentIdentifier;
    
    private String combinedParentObjectName;
    
    private String toolTipText;
    
    /**
     * Initializes an empty node.
     */
    public QueryTreeNodeData()
    {
        identifier = null;
        objectName = null;
        parentObjectIdentifier = null;
        parentObjectName = null;
        combinedParentIdentifier = null;
		combinedParentObjectName = null;
		toolTipText = null;
    }

   
    /**
     * Sets the id of the data this node represents.
     * @param identifier the id.
     * @see #getId()
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }
    
    public void initialiseRoot(String rootName)
    {
        initialiseRoot();
    }
    
    /**
     * Returns the id of the data this node represents.
     * @return the id of the data this node represents.
     * @see #setId(long)
     */
    public Object getIdentifier()
    {
        return identifier;
    }

	/**
	 * @return Returns the objectName.
	 */
	public String getObjectName() {
		return objectName;
	}
	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName()
	{
		return displayName;
	}
	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	/**
	 * @param objectName The objectName to set.
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	/**
	 * @return Returns the parentObjectName.
	 */
	public String getParentObjectName() {
		return parentObjectName;
	}
	/**
	 * @param parentObjectName The parentObjectName to set.
	 */
	public void setParentObjectName(String parentObjectName) {
		this.parentObjectName = parentObjectName;
	}
	/**
	 * @param parentIdentifier The parentIdentifier to set.
	 */
	public void setParentIdentifier(String parentIdentifier) {
		this.parentObjectIdentifier = parentIdentifier;
	}

	public void initialiseRoot() {
		this.setObjectName(Constants.ROOT);
		
	}
	public QueryTreeNode getParentTreeNode()
	{
		QueryTreeNodeData node = new QueryTreeNodeData();
		node.setIdentifier(this.combinedParentIdentifier);
		node.setObjectName(this.getCombinedParentObjectName());
		
		return node;
	}

	public boolean isChildOf(QueryTreeNode treeNode) 
	{
		QueryTreeNodeData node = (QueryTreeNodeData)treeNode;
		if ((this.getCombinedParentIdentifier() != null) && (this.getParentIdentifier() != null))
		{
		    return ((this.parentObjectIdentifier.equals(node.getIdentifier()) && this.parentObjectName.equals(node.getObjectName()))
		            && (this.combinedParentIdentifier.equals(node.getCombinedParentIdentifier()) && this.combinedParentObjectName.equals(node.getCombinedParentObjectName())));
		}
		
		return (this.parentObjectIdentifier.equals(node.getIdentifier()) && this.parentObjectName.equals(node.getObjectName()));
	}
	public boolean hasEqualParents(QueryTreeNode treeNode) 
	{
		QueryTreeNodeData node = (QueryTreeNodeData) treeNode;
		return this.getIdentifier().equals(node.getCombinedParentIdentifier());
	}

	public Object getParentIdentifier()
	{
		return this.parentObjectIdentifier;
	}
 
	/**
	 * @return Returns the combinedParentIdentifier.
	 */
	public String getCombinedParentIdentifier() {
		return combinedParentIdentifier;
	}
	/**
	 * @param combinedParentIdentifier The combinedParentIdentifier to set.
	 */
	public void setCombinedParentIdentifier(String combinedParentIdentifier) {
		this.combinedParentIdentifier = combinedParentIdentifier;
	}
	/**
	 * @return Returns the combinedParentObjectName.
	 */
	public String getCombinedParentObjectName() {
		return combinedParentObjectName;
	}
	/**
	 * @param combinedParentObjectName The combinedParentObjectName to set.
	 */
	public void setCombinedParentObjectName(String combinedParentObjectName) {
		this.combinedParentObjectName = combinedParentObjectName;
	}
	
	/* (non-Javadoc)
	 * @see edu.wustl.common.vo.QueryTreeNode#isPresentIn(javax.swing.tree.DefaultMutableTreeNode)
	 */
	public boolean isPresentIn(DefaultMutableTreeNode parentNode) 
	{
		for (int i = 0; i < parentNode.getChildCount(); i++)
        {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			QueryTreeNodeData node = (QueryTreeNodeData)childNode.getUserObject();
			
			if (this.identifier.equals(node.getIdentifier()) )
			{
				return true;
			}
        }
		
		return false;
	}
	
	public String toString()
	{
		String nodeName = this.objectName;
		if (this.identifier != null)
			nodeName = nodeName  + ":" + this.identifier;
		
		return nodeName;
	}


	public String getToolTipText() {
		return toolTipText;
	}


	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}
}
