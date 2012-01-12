/*
 * Created on Jul 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.tree;

import java.io.Serializable;

import edu.wustl.common.util.global.Constants;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated storageContainerType comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StorageContainerTreeNode extends TreeNodeImpl implements Serializable, Comparable
{
    private static final long serialVersionUID = 1234567890L;
    
    /**
     * Type of storage container.
     */
    private String type;
    
    private String toolTip="";

    private String activityStatus = Constants.ACTIVITY_STATUS_ACTIVE;
    
    /**
     * Default constructor. 
     */
    public StorageContainerTreeNode()
    {
    }
    
    /**
     * Parameterized constructor. 
     */
    public StorageContainerTreeNode(Long identifier, String value, String type)
    {
        super(identifier, value);
        this.type = type;
    }

    //constructor with tooltip 
    public StorageContainerTreeNode(Long identifier, String value, String type, String toolTip, String activityStatus)
    {
        super(identifier, value);
        this.type = type;
        this.toolTip = toolTip; 
        this.activityStatus = activityStatus;
    }
    
	/**
	 * @return Returns the toolTip.
	 */
	public String getToolTip() {
		return toolTip;
	}

    /**
     * @return Returns the type.
     */
    public String getType()
    {
        return type;
    }
    
    /**
     * @param type The type to set.
     */
    public void setType(String type)
    {
        this.type = type;
    }
    
    
	/**
	 * @return Returns the activityStatus.
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}
	/**
	 * @param activityStatus The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String nodeName = value;
        return nodeName;
    }
    
    public int compareTo(Object tmpobj)
	{
    	StorageContainerTreeNode treeNode = (StorageContainerTreeNode) tmpobj;
    	return this.identifier.compareTo(treeNode.identifier);
	}
 }