package edu.wustl.cab2b.admin.flex;

import java.util.List;
import java.util.Map;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * This abstract class represents the basic methods for creating UI on DAG
 * 
 * @author lalit_chand
 * 
 */

public abstract class DAGPanel {
	/**
	 * @param strToCreateQueryObject
	 * @param entityName
	 * @return DAGNode
	 */
	public abstract DAGNode createNode(String strToCreateQueryObject,
			String entityName);

	/**
	 * 
	 * @param linkedNodeList
	 * @return Map<String, List<DAGLink>>
	 */
	public abstract Map<String, List<DAGLink>> getpaths(List linkedNodeList);

	/**
	 * 
	 * @param ipath
	 * @return String representing toolTip
	 */
	public abstract String getPathDisplayString(IPath ipath);

	/**
	 * @param linkedNodeList
	 * @param selectedPath
	 * @return List<DAGLink>
	 */
	public abstract List<DAGLink> linkNodes(List<DAGNode> linkedNodeList,
			DAGLink selectedPath);

	/**
	 * It deletes the path between two nodes
	 * 
	 * @param linkedNodeList
	 * @param pathName
	 */
	public abstract void deleteLink(List<DAGNode> linkedNodeList,
			String pathName);

}
