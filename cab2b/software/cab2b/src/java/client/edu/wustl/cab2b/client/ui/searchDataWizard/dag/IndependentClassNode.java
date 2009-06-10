package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

/**
 * This class is a subclass of ClassNode written to create a node which is
 * independent of query or expression
 * 
 * @author Hrishikesh Rajpathak
 * 
 */
public class IndependentClassNode extends ClassNode {

	private Long entityId;

	public IndependentClassNode() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.client.ui.searchDataWizard.dag.ClassNode#getTooltipText()
	 */
	@Override
	public String getTooltipText() {
		return ("toolTip");
	}

	/**
	 * @return Returns the entityId.
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId
	 *            The entityId to set.
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

}
