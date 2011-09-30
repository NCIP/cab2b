/**
 * <p>Title: AdvanceQueryTreeNode Class </p>
 * <p>Description:	AdvanceQueryTreeNode represents tree node for Advance Query result tree. </p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Prafull Kadam
 * @version 1.00
 */

package edu.wustl.common.tree;

import java.io.Serializable;

public class AdvanceQueryTreeNode extends TreeNodeImpl implements Serializable, Comparable
{

	// The actual name which wil be displayed on the Advance Query Tree. 
	private String displayName;

	public AdvanceQueryTreeNode(Long identifier, String value, String displayName)
	{
		super(identifier, value);
		this.displayName = displayName;
	}

	public AdvanceQueryTreeNode()
	{
		super();
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
	 * For Advance Query tree Node displayName will be shown as node name on the tree. 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return displayName;
	}

	/**
	 * @return Returns the toolTip.
	 */
	public String getToolTip()
	{
		return value;
	}

	/**
	 * Comparator implementation for the AdvanceQuery Tree node. The comparision will be done on the basis of the displayName attribute. 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj)
	{
		if (obj instanceof AdvanceQueryTreeNode)
		{
			AdvanceQueryTreeNode node = (AdvanceQueryTreeNode) obj;
			if (displayName != null && node.displayName != null)
			{	
				//Bug id: 3624, compareTo function is replaced with compareToIgnoreCase to sort element alphabetically with case insensetive
				return displayName.compareToIgnoreCase(node.displayName);
			}
		}
		return 0;
	}
}