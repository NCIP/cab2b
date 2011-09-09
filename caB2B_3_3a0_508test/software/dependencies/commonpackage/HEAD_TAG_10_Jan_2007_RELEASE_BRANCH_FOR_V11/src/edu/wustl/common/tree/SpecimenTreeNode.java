package edu.wustl.common.tree;

import java.io.Serializable;

/**
 * @author ramya_nagraj
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class SpecimenTreeNode extends TreeNodeImpl implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * No-Args Constructor.
	 */
	public SpecimenTreeNode()
	{
		
	}
	
	/**
	 * Parametrized Construstor.
	 * @param identifier
	 * @param value
	 */
	public SpecimenTreeNode(Long identifier,String value)
	{
	    super(identifier, value);
	}
	
	/**
	 * String containing the type of the specimen node.
	 */
	private String type;
	
	/**
	 * String containing the class of the specimen node.
	 */
	private String specimenClass;
	
	/**
	 * String containing Id of the parent node
	 */
	private String parentIdentifier;
	
	/**
	 * String containing value of the parent node
	 */
	private String parentValue;
	
	/**
	 * @return String containing the type of specimen node.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Returns String containing the class of specimen.
	 * @return specimenClass
	 */
	public String getSpecimenClass() {
		return specimenClass;
	}

	/**
	 * Sets the class of specimen.
	 * @param specimenClass
	 */
	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}
	
	    
	/* (non-Javadoc)
	 * @see edu.wustl.common.tree.TreeNodeImpl#toString()
	 */
	public String toString()
	{
	    return this.value;
	}

	/**
	 * Returns the parentIdentifier of the tree node.
	 * @return parentIdentifier
	 */
	public String getParentIdentifier() {
		return parentIdentifier;
	}

	/**
	 * Sets the parentIdentifier of the tree node.
	 * @param parentIdentifier
	 */
	public void setParentIdentifier(String parentIdentifier) {
		this.parentIdentifier = parentIdentifier;
	}

	/**
	 * Returns the parentValue of the tree node. 
	 * @return parentValue
	 */
	public String getParentValue() {
		return parentValue;
	}

	/**
	 *Sets the parentValue of the tree node. 
	 * @param parentValue
	 */
	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}
}
