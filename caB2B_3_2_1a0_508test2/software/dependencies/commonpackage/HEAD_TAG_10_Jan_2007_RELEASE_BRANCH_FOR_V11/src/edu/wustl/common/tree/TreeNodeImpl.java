/*
 * Created on Apr 27, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.tree;

import java.io.Serializable;
import java.util.Vector;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeNodeImpl implements Serializable, TreeNode
{
    
    /**
     * identifier for the node.
     */
    Long identifier;
    
    /**
     * Name of the node.
     */
    String value;
    
    /**
     * Parent node of this node.
     */
    TreeNode parentNode;
    
    /**
     * List of child nodes.
     */
    Vector childNodes = new Vector();
    
    /**
     * Default Constructor.
     */
    public TreeNodeImpl()
    {
    }
    
    /**
     * Default Constructor.
     */
    public TreeNodeImpl(Long identifier, String value)
    {
        this.identifier = identifier;
        this.value = value;
    }
    
    /**
     * @return Returns the identifier.
     */
    public Long getIdentifier()
    {
        return identifier;
    }
    
    /**
     * @param identifier The identifier to set.
     */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }
    
    /**
     * @return Returns the value.
     */
    public String getValue()
    {
        return value;
    }
    
    /**
     * @param value The value to set.
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    /**
     * @return Returns the parentNode.
     */
    public TreeNode getParentNode()
    {
        return parentNode;
    }
    
    /**
     * @param parentNode The parentNode to set.
     */
    public void setParentNode(TreeNode parentNode)
    {
        this.parentNode = parentNode;
    }
    
    /**
     * @return Returns the childNodes.
     */
    public Vector getChildNodes()
    {
        return childNodes;
    }
    
    /**
     * @param childNodes The childNodes to set.
     */
    public void setChildNodes(Vector childNodes)
    {
        this.childNodes = childNodes;
    }
    
    public boolean equals(Object obj)
    {
        boolean flag = false;
        if (obj instanceof TreeNodeImpl)
        {
            TreeNodeImpl treeNodeImpl = (TreeNodeImpl) obj;
            if (this.getIdentifier().equals(treeNodeImpl.getIdentifier()))
            {
                flag = true;
            }
        }
        
        return flag;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        int i = 0;
        if(getIdentifier() != null)
            i += getIdentifier().hashCode();
        return i;
    }
    /**
     * To display Tooltip for the Tree node. By default it will return value, override this method if need different tool tip.
     * @return The tooltip to display 
     */
    String getToolTip()
    {
    	return this.value;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        if (this.identifier.longValue() != 0)
        {
            return this.value + " : " + this.identifier;
        }
        
        return this.value;
    }
}