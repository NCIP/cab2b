/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.flex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This Class represents basic DAG Object
 * 
 * @author lalit_chand
 */

public abstract class DAGObject implements Externalizable {

	/** Node is of DAG */
	public long nodeId;

	/** Node name of DAG */
	public String nodeName;

	/**
	 * Default constructor
	 */
	public DAGObject() {

	}

	/**
	 * 
	 * @return id
	 */
	public long getNodeId() {
		return nodeId;
	}

	/**
	 * 
	 * @return name
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @return
	 */
	public abstract String getToolTip();

	/**
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {

	}

	/**
	 * 
	 * @param nodeId
	 */
	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 
	 * @param nodeName
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * 
	 * @param objectOutput
	 * @throws IOException
	 */
	public void writeExternal(ObjectOutput objectOutput) throws IOException {

	}

}