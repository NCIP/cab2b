/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.flex;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.wustl.cab2b.admin.util.AdminConstants;

/**
 * This class represents the DAGNode class from Flex Client.
 * 
 * @author lalit_chand
 */
public class DAGNode extends DAGObject {
	private static final long serialVersionUID = -4803914964658460609L;

	private final HttpSession session;

	private int xCordinate;

	private int yCordinate;

	private String toolTip;

	private String operatorBetweenAttrAndAssociation;

	private String nodeType;

	private List<DAGNode> associationList;

	private List<String> operatorList;

	private List<DAGLink> dagpathList;

	private String errorMsg;

	private long entityId;

	private List<String> attributesList;

	/**
	 * Default constructor .
	 */
	public DAGNode() {
		this.session = flex.messaging.FlexContext.getHttpRequest().getSession(
				false);
		this.associationList = new ArrayList<DAGNode>();
		this.operatorList = new ArrayList<String>();
		this.dagpathList = new ArrayList<DAGLink>();
		this.toolTip = "";
		this.operatorBetweenAttrAndAssociation = "";
		this.nodeType = "";
		this.errorMsg = "";

	}

	/**
	 * @return xCordinate
	 */
	public int getXCordinate() {
		return xCordinate;
	}

	/**
	 * @return yCordinate
	 */
	public int getYCordinate() {
		return yCordinate;
	}

	/**
	 * @return toolTip of DAGNode
	 */
	public String getToolTip() {
		return toolTip;
	}

	/**
	 * Sets toolTip
	 * 
	 * @param toolTip
	 */

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	/**
	 * @return entityId
	 */

	public long getEntityId() {
		return entityId;
	}

	/**
	 * Sets entityId
	 * 
	 * @param entityId
	 */
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return attributesList
	 */

	public List<String> getAttributeList() {
		return attributesList;
	}

	/**
	 * Sets attributesList
	 * 
	 * @param attributeList
	 */
	public void setAttribute(List attributeList) {
		this.attributesList = attributeList;
	}

	/**
	 * It reads serialized data coming from flex client
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		nodeName = in.readUTF();
		toolTip = in.readUTF();
		nodeId = in.readInt();
		operatorBetweenAttrAndAssociation = in.readUTF();
		nodeType = in.readUTF();
		associationList = (List<DAGNode>) in.readObject();
		operatorList = (List<String>) in.readObject();
		dagpathList = (List<DAGLink>) in.readObject();
		errorMsg = in.readUTF();
		xCordinate = in.readInt();
		yCordinate = in.readInt();
		entityId = in.readInt();
		attributesList = (List<String>) in.readObject();

		CategoryDagPanel categoryDagPanel = (CategoryDagPanel) session
				.getAttribute(AdminConstants.CATEGORY_INSTANCE);
		for (DAGNode dagNode : categoryDagPanel.getDagNodeSet()) {
			if (dagNode.getNodeId() == nodeId) {
				dagNode.xCordinate = xCordinate;
				dagNode.yCordinate = yCordinate;
			}
		}
	}

	/**
	 * It serializes DAGNode for communication with flex client
	 * 
	 * @param out
	 * @throws IOException
	 */

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(nodeName);
		out.writeUTF(toolTip);
		out.writeInt((int) nodeId);
		out.writeUTF(operatorBetweenAttrAndAssociation);
		out.writeUTF(nodeType);
		out.writeObject(associationList);
		out.writeObject(operatorList);
		out.writeObject(dagpathList);
		out.writeUTF(errorMsg);
		out.writeInt(xCordinate);
		out.writeInt(yCordinate);
		out.writeInt((int) entityId);
		out.writeObject(attributesList);
	}

	/**
	 * Equals on getNodeId().
	 * @param obj
	 * @return
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DAGNode)) {
			return false;
		}

		DAGNode o = (DAGNode) obj;
		return getNodeId() == o.getNodeId();
	}

	/**
	 * Hash on getNodeId().
	 * @return hashCode
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(getNodeId()).toHashCode();
	}

}
