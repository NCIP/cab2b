package edu.wustl.common.tree;

import java.util.Date;

/**
 * A tree node to represent an experiment and an experiment group
 * in a hierarchical view using tree data structure.
 * 
 * @author chetan_bh
 */
public class ExperimentTreeNode extends TreeNodeImpl
{
	/**
	 * Name of the experiment/experiment group.
	 */
	String name;
	
	/**
	 * Description for the experiment/experiment group.
	 */
	String desc;
	
	/**
	 * Date of creation.
	 */
	Date createdOn;
	
	/**
	 * Date last updated.
	 */
	Date lastUpdatedOn;
	
	/**
	 * boolean value to distinguish between experiment and experiment group.
	 */
	boolean isExperimentGroup = false;
	
	
	
	public ExperimentTreeNode()
	{
		super();
	}	
	
	
	public ExperimentTreeNode(Long id)
	{
		this(id, null);
	}


	public ExperimentTreeNode(Long id, String name)
	{
		setIdentifier(id);
		setName(name);
	}


	/**
	 * Gets creation date.
	 * @return
	 */
	public Date getCreatedOn()
	{
		return createdOn;
	}


	/**
	 * Sets creation date.
	 * @param createdOn
	 */
	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}


	/**
	 * Gets description.
	 * @return
	 */
	public String getDesc()
	{
		return desc;
	}


	/**
	 * Sets Description.
	 * @param desc
	 */
	public void setDesc(String desc)
	{
		this.desc = desc;
	}


	/**
	 * Sets experiment Name.
	 * @return
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * Sets experiment name.
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	/**
	 * Returns true if the node is an experiment group node else false.
	 * @return
	 */
	public boolean isExperimentGroup()
	{
		return isExperimentGroup;
	}


	/**
	 * Sets node type to experiment group if parameter passed is true, else experiment node.
	 * @param isExperimentGroup
	 */
	public void setExperimentGroup(boolean isExperimentGroup)
	{
		this.isExperimentGroup = isExperimentGroup;
	}
	
	
	/**
	 * Gets last updated date.
	 * @return
	 */
	public Date getLastUpdatedOn()
	{
		return lastUpdatedOn;
	}


	/**
	 * Sets last updated date.
	 * @param lastUpdatedOn
	 */
	public void setLastUpdatedOn(Date lastUpdatedOn)
	{
		this.lastUpdatedOn = lastUpdatedOn;
	}

	
	public String toString()
	{
		return name;
	}
	
}
