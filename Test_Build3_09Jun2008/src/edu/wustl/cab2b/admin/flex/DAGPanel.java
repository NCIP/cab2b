package edu.wustl.cab2b.admin.flex;

import java.util.List;
import java.util.Map;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 *  This abstract class represents the basic methods for creating UI on DAG   
 * @author lalit_chand
 *
 */

public abstract class DAGPanel {

    public abstract DAGNode createNode(String strToCreateQueryObject, String entityName);

    public abstract Map<String, List<DAGLink>> getpaths(List linkedNodeList);

    public abstract String getPathDisplayString(IPath ipath);

    public abstract List<DAGLink> linkNodes(List<DAGNode> linkedNodeList, DAGLink selectedPath);

    public abstract void deleteLink(List<DAGNode> linkedNodeList , String pathName);

}
