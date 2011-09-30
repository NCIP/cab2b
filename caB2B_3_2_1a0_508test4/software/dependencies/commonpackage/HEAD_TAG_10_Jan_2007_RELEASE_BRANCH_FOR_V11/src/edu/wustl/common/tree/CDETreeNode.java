/*
 * Created on Aug 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.tree;

import java.io.Serializable;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CDETreeNode extends TreeNodeImpl implements Serializable, Comparable
{
	private static final long serialVersionUID = 1234567890L;
	
    private String cdeName;
    
    public CDETreeNode()
    {
    }
    
    public CDETreeNode(Long identifier,String value)
    {
        super(identifier, value);
    }
    
    /**
     * @return Returns the cdeName.
     */
    public String getCdeName()
    {
        return cdeName;
    }
    
    /**
     * @param cdeName The cdeName to set.
     */
    public void setCdeName(String cdeName)
    {
        this.cdeName = cdeName;
    }
    
    /* (non-Javadoc)
     * @see edu.wustl.common.tree.TreeNodeImpl#toString()
     */
    public String toString()
    {
        return this.value;
    }
    
    //Bug-2717: For sorting
    public int compareTo(Object tmpobj)
	{
    	CDETreeNode treeNode = (CDETreeNode) tmpobj;
    	return value.compareTo(treeNode.getValue());
	}
}