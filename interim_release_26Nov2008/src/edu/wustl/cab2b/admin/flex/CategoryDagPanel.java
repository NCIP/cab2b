package edu.wustl.cab2b.admin.flex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.admin.bizlogic.SearchCategoryBizLogic;
import edu.wustl.cab2b.admin.util.AdminConstants;
import edu.wustl.cab2b.admin.util.AttributePair;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.path.PathBuilder;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.querysuite.queryengine.impl.CommonPathFinder;
import edu.wustl.common.util.Graph;

/**
 * @author lalit_chand
 */
public class CategoryDagPanel extends DAGPanel {

	private static final Logger logger = edu.wustl.common.util.logger.Logger
			.getLogger(PathBuilder.class);;

	private int nodeId;

	private int editNodeId;

	private Graph<DAGNode, DAGLink> graph;

	private IPathFinder pathFinder;

	private HttpSession session;

	private HttpServletRequest request;

	private Map<String, IPath> idVsPathMap;

	private AttributePair attributePair;

	/**
	 * It initializes the flex session
	 */

	public CategoryDagPanel() {
		this.request = flex.messaging.FlexContext.getHttpRequest();
		this.session = request.getSession();
		this.pathFinder = new CommonPathFinder();

	}

	/**
	 * It reinitializes the fields when flex client gets refreshed
	 */
	public void initCategoryDagPanel() {
		this.session.setAttribute(AdminConstants.CATEGORY_INSTANCE, this);
		this.graph = new Graph<DAGNode, DAGLink>();
		this.nodeId = 1;
		this.editNodeId = 0;
		this.idVsPathMap = new HashMap<String, IPath>();
	}

	/**
	 * @param nodeId
	 */
	public void setNodeID(int nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Returns Id Vs Path Map
	 * 
	 * @return
	 */
	public Map<String, IPath> getIdVsPathMap() {
		return idVsPathMap;
	}

	/**
	 * It deletes the node from Flex UI
	 * 
	 * @param nodeId
	 */
	public void deleteNode(long nodeId) {
		for (DAGNode dagNode : getDagNodeSet()) {
			if (dagNode.getNodeId() == nodeId) {
				graph.removeVertex(dagNode);
			}
		}
	}

	/**
	 * This method creates the DAGNode.
	 * 
	 * @param strToCreateQueryObject
	 * @param entityID
	 * @return DAGNode
	 */
	public DAGNode createNode(String strToCreateQueryObject, String entityID) {
		Long entityId = Long.parseLong(entityID);
		EntityInterface entity = EntityCache.getCache().getEntityById(entityId);
		String nodeName = edu.wustl.cab2b.common.util.Utility
				.getDisplayName(entity);

		StringBuffer sbuffer = new StringBuffer();
		String[] attributesList = strToCreateQueryObject.split(";");
		for (String attribute : attributesList) {
			String[] attributeName = attribute.split(":");
			sbuffer = sbuffer.append(" ").append(
					Utility.getFormattedString(attributeName[0])).append("\n");
		}
		String toolTip = sbuffer.toString();

		DAGNode dagNode = new DAGNode();
		dagNode.setToolTip(toolTip);
		dagNode.setNodeName(nodeName);
		dagNode.setNodeId(this.nodeId++);
		dagNode.setEntityId(entityId.intValue());
		dagNode.setAttribute(Arrays.asList(attributesList));
		graph.addVertex(dagNode);

		return dagNode;
	}

	/**
	 * Edits the DagNode
	 * 
	 * @param strToCreateQueryObject
	 * @param entityID
	 * @return DAGNode representing edited DAGNode
	 */
	public DAGNode editNode(String strToCreateQueryObject, String entityID) {
		String[] attributesList = strToCreateQueryObject.split(";");
		StringBuffer sbuffer = new StringBuffer();
		for (String attribute : attributesList) {
			String[] attributeName = attribute.split(":");
			sbuffer = sbuffer.append(
					Utility.getFormattedString(attributeName[0])).append("\n");
		}
		String toolTip = sbuffer.toString();

		DAGNode editedNode = null;
		for (DAGNode dagNode : getDagNodeSet()) {
			if (dagNode.getNodeId() == this.editNodeId) {
				editedNode = dagNode;
				dagNode.setAttribute(Arrays.asList(attributesList));
				dagNode.setToolTip(toolTip);
			}
		}

		return editedNode;
	}

	/**
	 * Called from flex while editing dagNode
	 * 
	 * @param nodeId
	 *            Represents NodeId of editedNode.
	 * @return It returns a String having all the attributes.
	 */
	public String getLimitUI(int nodeId) {
		this.editNodeId = nodeId;
		SearchCategoryBizLogic bizlogic = new SearchCategoryBizLogic();
		EntityInterface entity = null;
		String result = null;
		for (DAGNode dagNode : getDagNodeSet()) {
			if (dagNode.getNodeId() == nodeId) {
				entity = EntityCache.getCache().getEntityById(
						dagNode.getEntityId());
				result = bizlogic.showAttributeInformation(entity, dagNode);
			}
		}

		return result;
	}

	/**
	 * Gets all path Lists between nodes
	 * 
	 * @param linkedNodeList
	 * @return Map<String, List<IPath>>
	 */
	private Map<String, List<IPath>> getPathList(List<DAGNode> linkedNodeList) {
		DAGNode sourceNode = linkedNodeList.get(0);
		DAGNode destinationNode = linkedNodeList.get(1);
		AbstractEntityCache cache = EntityCache.getCache();

		EntityInterface sourceEntity = cache.getEntityById(sourceNode
				.getEntityId());
		EntityInterface destinationEntity = cache.getEntityById(destinationNode
				.getEntityId());

		Set<ICuratedPath> allCuratedPaths = pathFinder.getCuratedPaths(
				sourceEntity, destinationEntity);
		logger
				.debug("  getCuratedPaths() executed : "
						+ allCuratedPaths.size());
		List<IPath> curatedPathList = new ArrayList<IPath>(allCuratedPaths
				.size());
		List<IPath> selectedPathList = new ArrayList<IPath>();

		for (ICuratedPath curatedPaths : allCuratedPaths) {
			Set<IPath> pathSet = curatedPaths.getPaths();
			// called pathfinder for 2 entities , so this set will have only 1
			// IPath in it
			addPath(pathSet, curatedPaths, selectedPathList, curatedPathList);
			/*
			 * if (!pathSet.isEmpty()) { IPath path = pathSet.iterator().next();
			 * if (curatedPaths.isSelected()) { selectedPathList.add(path); }
			 * curatedPathList.add(path); }
			 */
		}

		List<IPath> generalPathList = pathFinder.getAllPossiblePaths(
				sourceEntity, destinationEntity);
		for (IPath path : generalPathList) {
			idVsPathMap.put(Long.toString(path.getPathId()), path);

		}

		Map<String, List<IPath>> allPathMap = new HashMap<String, List<IPath>>(
				3);
		allPathMap.put(Constants.SELECTED_PATH, selectedPathList);
		allPathMap.put(Constants.CURATED_PATH, curatedPathList);
		allPathMap.put(Constants.GENERAL_PATH, generalPathList);

		return allPathMap;
	}

	private void addPath(Set<IPath> pathSet, ICuratedPath curatedPaths,
			List<IPath> selectedPathList, List<IPath> curatedPathList) {
		IPath path = pathSet.iterator().next();
		if (curatedPaths.isSelected()) {
			selectedPathList.add(path);
		}
		curatedPathList.add(path);
	}

	/**
	 * Gets display path string
	 * 
	 * @param path
	 * @return String representing full path.
	 */
	public String getPathDisplayString(IPath path) {
		StringBuffer returner = new StringBuffer();
		String roleName = null;
		List<IAssociation> associationList = path.getIntermediateAssociations();
		boolean isFirstAssociation = true;
		for (IAssociation association : associationList) {
			roleName = edu.wustl.cab2b.client.ui.query.Utility
					.getRoleName(association);
			if (isFirstAssociation) {
				EntityInterface srcEntity = association.getSourceEntity();
				EntityInterface tarEntity = association.getTargetEntity();
				String srcEntityName = edu.wustl.common.util.Utility
						.parseClassName(srcEntity.getName());
				String tarEntityName = edu.wustl.common.util.Utility
						.parseClassName(tarEntity.getName());
				isFirstAssociation = false;
				returner.append(srcEntityName + " >> (" + roleName + ") >> "
						+ tarEntityName);
			} else {
				EntityInterface tarEntity = association.getTargetEntity();
				String tarEntityName = edu.wustl.common.util.Utility
						.parseClassName(tarEntity.getName());
				returner.append(" >> (" + roleName + ") >> " + tarEntityName);
			}
		}

		return returner.toString();
	}

	/**
	 * @param linkedNodeList
	 *            List of linked nodes
	 * @see edu.wustl.cab2b.admin.flex.DAGPanel#getpaths(java.util.List)
	 * @return Map<String, List<DAGLink>>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<DAGLink>> getpaths(List linkedNodeList) {
		Map<String, List<IPath>> allPathMaps = getPathList(linkedNodeList);
		Map<String, List<DAGLink>> allDagLinkMap = new HashMap<String, List<DAGLink>>();

		Set<String> allPath = allPathMaps.keySet();
		for (String key : allPath) {
			List<DAGLink> allPathsListStr = new ArrayList<DAGLink>();

			for (IPath iPath : allPathMaps.get(key)) {
				Path path = (Path) iPath;
				DAGLink pathLink = new DAGLink();
				pathLink.setToolTip(getPathDisplayString((IPath) iPath));
				pathLink.setPathId(Long.toString(path.getPathId()));
				allPathsListStr.add(pathLink);
			}
			allDagLinkMap.put(key, allPathsListStr);
		}

		return allDagLinkMap;
	}

	/**
	 * Links two nodes
	 * 
	 * @param linkedNodeList
	 * @param selectedPath
	 * @return it returns a DAGLink object which connects two nodes on DAG
	 */
	public List<DAGLink> linkNodes(List<DAGNode> linkedNodeList,
			DAGLink selectedPath) {

		session.setAttribute(AdminConstants.ID_VS_PATH_MAP, getIdVsPathMap());

		Map<String, List<IPath>> allPathMaps = getPathList(linkedNodeList);
		List<IPath> pathsList = allPathMaps.get(Constants.GENERAL_PATH);

		List<IPath> selectedList = new ArrayList<IPath>();
		for (int i = 0; i < pathsList.size(); i++) {
			IPath path = pathsList.get(i);
			long pathId = path.getPathId();

			String selectedPathId = selectedPath.getPathId();
			if (Long.toString(pathId).equals(selectedPathId)) {
				selectedList.add(path);
			}
		}

		List<DAGLink> dagPathList = null;
		if (!selectedList.isEmpty()) {
			DAGNode sourceNode = linkedNodeList.get(0);
			DAGNode destinationNode = linkedNodeList.get(1);

			dagPathList = new ArrayList<DAGLink>();
			for (int k = 0; k < selectedList.size(); k++) {
				IPath path = selectedList.get(k);
				String pathStr = Long.toString(path.getPathId());

				DAGLink dagPath = new DAGLink();
				dagPath.setToolTip(getPathDisplayString(path));
				dagPath.setPathId(pathStr);
				dagPath.setSourceNodeId(sourceNode.getNodeId());
				dagPath.setDestinationNodeId(destinationNode.getNodeId());
				dagPathList.add(dagPath);

				// checking if it is for Category or for CuratePath
				if (session.getAttribute(AdminConstants.PAGE_IDENTIFIER) != null
						&& graph.willCauseNewCycle(sourceNode, destinationNode)) {
					return null;
				} else if (willViolateTreeConstraint(sourceNode,
						destinationNode)) {
					return null;
				}

				graph.putEdge(sourceNode, destinationNode, dagPath);
			}
		}

		return dagPathList;
	}

	/**
	 * It checks the violation of tree constraint
	 * 
	 * @param src
	 * @param target
	 * @return boolean
	 */
	private boolean willViolateTreeConstraint(DAGNode src, DAGNode target) {
		if (!graph.containsVertex(target)) {
			return false;
		}
		if (graph.containsEdge(src, target)) {
			return false;
		}
		return !(graph.getInNeighbours(target).isEmpty() && graph
				.getVertexPaths(target, src).isEmpty());
	}

	/**
	 * This gets called when user click Back button and it sends all the
	 * DAGNodes added previously.
	 * 
	 * @return Map of all the dagNodes and dagLink present on dag
	 */
	@SuppressWarnings("unchecked")
	public Map repaintDAG() {
		CategoryDagPanel categoryDagpanel = (CategoryDagPanel) session
				.getAttribute(AdminConstants.CATEGORY_INSTANCE);
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put(AdminConstants.DAG_NODE_SET, categoryDagpanel.getDagNodeSet());
		map.put(AdminConstants.DAG_LINK_SET, categoryDagpanel.getDagPathSet());

		return map;
	}

	/**
	 * This method checks for multiple roots on dag
	 * 
	 * @return true if there exists a multiple roots
	 */
	public boolean areMultipleRoots() {
		List<Long> sourceNodeList = new ArrayList<Long>();
		List<Long> destinationNodeList = new ArrayList<Long>();
		List<Long> multipleRootsList = new ArrayList<Long>();

		for (DAGLink dagLink : getDagPathSet()) {
			setNodeList(dagLink, sourceNodeList, destinationNodeList);
		}

		for (Long nodeId : sourceNodeList) {
			if (!destinationNodeList.contains(nodeId)
					&& !multipleRootsList.contains(nodeId)) {
				multipleRootsList.add(nodeId);
			}
		}

		boolean hasMultipleRoots = true;
		if (multipleRootsList.size() == 1) {
			hasMultipleRoots = false;
		}

		return hasMultipleRoots;
	}

	/**
	 * This method separate all sourceNodes and destination Nodes into different
	 * list
	 * 
	 * @param dagLink
	 * @param sourceNodeList
	 * @param destinationNodeList
	 */
	private void setNodeList(DAGLink dagLink, List<Long> sourceNodeList,
			List<Long> destinationNodeList) {

		for (DAGNode dagNode : getDagNodeSet()) {
			if (dagLink.getSourceNodeId() == dagNode.getNodeId()) {
				sourceNodeList.add(dagNode.getNodeId());
			}

			if (dagLink.getDestinatioNodeId() == dagNode.getNodeId()) {
				destinationNodeList.add(dagNode.getNodeId());
			}
		}

	}

	/**
	 * 
	 * 
	 * @param sourceEntityId
	 * @param targetEntityId
	 * @return true if both the sourceEntityId and sourceEntityId belongs to
	 *         same EntityGroup
	 */
	public boolean isSameEntityGroup(Long sourceEntityId, Long targetEntityId) {
		AbstractEntityCache cache = EntityCache.getCache();
		EntityInterface sourceEntityInterface = cache
				.getEntityById(sourceEntityId);
		EntityInterface targetEntityInterface = cache
				.getEntityById(targetEntityId);
		EntityGroupInterface soruceEntityGroupInterFace = sourceEntityInterface
				.getEntityGroupCollection().iterator().next();
		EntityGroupInterface targetEntityGroupInterFace = targetEntityInterface
				.getEntityGroupCollection().iterator().next();

		boolean isSameEntityGroup = false;
		if (soruceEntityGroupInterFace == targetEntityGroupInterFace) {
			isSameEntityGroup = true;
		} else {
			session
					.setAttribute(AdminConstants.SOURCE_CLASS_ID,
							sourceEntityId);
			session
					.setAttribute(AdminConstants.TARGET_CLASS_ID,
							targetEntityId);

			isSameEntityGroup = false;
		}

		return isSameEntityGroup;
	}

	/**
	 * 
	 * @param sourceNodeId
	 * @param targetNodeId
	 * @param attributeStr
	 * @return DAGLink object representing InterModelJoin Link
	 */
	public DAGLink createInterModelJoin(int sourceNodeId, int targetNodeId,
			String attributeStr) {
		String[] attributes = attributeStr.split("_");
		String sourceAttributeId = attributes[0];
		String targetAttributeId = attributes[1];
		String matchFactor = attributes[2];

		AbstractEntityCache cache = EntityCache.getCache();
		AttributeInterface sourceAttribute = cache.getAttributeById(Long
				.valueOf(sourceAttributeId));
		AttributeInterface targetAttribute = cache.getAttributeById(Long
				.valueOf(targetAttributeId));

		attributePair = new AttributePair(sourceAttribute, targetAttribute);
		if (AttributePair.MatchFactor.CLASS_CONCEPT_CODE.getValue()
				.equalsIgnoreCase(matchFactor)) {
			attributePair
					.setMatchFactor(AttributePair.MatchFactor.CLASS_CONCEPT_CODE);
		} else if (AttributePair.MatchFactor.ATTRIBUTE_CONCEPT_CODE.getValue()
				.equalsIgnoreCase(matchFactor)) {
			attributePair
					.setMatchFactor(AttributePair.MatchFactor.ATTRIBUTE_CONCEPT_CODE);
		} else if (AttributePair.MatchFactor.PUBLIC_ID.getValue()
				.equalsIgnoreCase(matchFactor)) {
			attributePair.setMatchFactor(AttributePair.MatchFactor.PUBLIC_ID);
		} else {
			attributePair
					.setMatchFactor(AttributePair.MatchFactor.MANUAL_CONNECT);
		}

		DAGLink dagLink = new DAGLink();
		dagLink.setPathId("");
		dagLink.setSourceNodeId(sourceNodeId);
		dagLink.setDestinationNodeId(targetNodeId);
		dagLink.setToolTip(attributePair.toString());

		return dagLink;
	}

	/**
	 * 
	 * @param dagNodeSet
	 * @return This method will return a List of List of all DagLink Objects for
	 *         Auto Connect .
	 * 
	 */

	public String getAutoConnectPaths(Set<DAGNode> dagNodeSet) {
		AbstractEntityCache cache = EntityCache.getCache();
		Set<EntityInterface> entitySet = new HashSet<EntityInterface>(
				dagNodeSet.size());
		for (DAGNode dagNode : dagNodeSet) {
			EntityInterface entity = cache.getEntityById(dagNode.getEntityId());
			entitySet.add(entity);
		}

		Set<ICuratedPath> iPathSet = pathFinder.autoConnect(entitySet);
		if (iPathSet.isEmpty()) {
			return null;
		}

		Map<List<DAGLink>, String> map = new HashMap<List<DAGLink>, String>();
		setToolTipForCurate(iPathSet, dagNodeSet, map);
		return makeXML(map);
	}

	private void setToolTipForCurate(Set<ICuratedPath> iPathSet,
			Set<DAGNode> dagNodeSet, Map<List<DAGLink>, String> map) {
		for (ICuratedPath iCuratedPath : iPathSet) {
			List<DAGLink> dagPathList = new ArrayList<DAGLink>();
			StringBuilder toolTipForEachICurate = new StringBuilder();

			for (IPath path : iCuratedPath.getPaths()) {
				DAGLink dagPath = new DAGLink();

				toolTipForEachICurate.append(getPathDisplayString(path))
						.append("\n");
				dagPath.setToolTip(getPathDisplayString(path));
				dagPath.setPathId(Long.toString(path.getPathId()));
				setDagPath(dagNodeSet, path, dagPath);
				dagPathList.add(dagPath);
			}

			map.put(dagPathList, toolTipForEachICurate.toString());
		}

	}

	private void setDagPath(Set<DAGNode> dagNodeSet, IPath path, DAGLink dagPath) {
		for (DAGNode dagNode : dagNodeSet) {
			long dagNodeId = dagNode.getNodeId();
			long dagNodeEntityId = dagNode.getEntityId();
			long pathSourceEntityId = path.getSourceEntity().getId();
			long pathTargetEntityId = path.getTargetEntity().getId();

			if (dagNodeEntityId == pathSourceEntityId) {
				dagPath.setSourceNodeId(dagNodeId);
			}

			if (dagNodeEntityId == pathTargetEntityId) {
				dagPath.setDestinationNodeId(dagNodeId);
			}
		}
	}

	/**
	 * 
	 * @param mapForToolTip
	 * @return it returns the xml having all the info of DAGLink .
	 */
	private String makeXML(Map<List<DAGLink>, String> mapForToolTip) {
		StringBuilder builder = new StringBuilder();

		for (List<DAGLink> dagLinkList : mapForToolTip.keySet()) {
			builder.append("<ICurated>").append("\n");
			for (DAGLink dagLink : dagLinkList) {
				builder.append("<DagLink>").append("\n");
				builder.append("<pathId>").append(dagLink.getPathId()).append(
						"</pathId>").append("\n");
				builder.append("<sourceNodeId>").append(
						dagLink.getSourceNodeId()).append("</sourceNodeId>")
						.append("\n");
				builder.append("<targetNodeId>").append(
						dagLink.getDestinatioNodeId())
						.append("</targetNodeId>").append("\n");
				builder.append("<toolTip>").append(
						handleSpecialCharacter(dagLink.getToolTip())).append(
						"</toolTip>").append("\n");
				builder.append("</DagLink>").append("\n");
			}
			builder.append("<ToolTip>").append(
					handleSpecialCharacter(mapForToolTip.get(dagLinkList)))
					.append("</ToolTip>").append("\n");
			builder.append("</ICurated>").append("\n");
		}

		return builder.toString();
	}

	private String handleSpecialCharacter(String sourceString) {
		return sourceString.replaceAll(">", "&gt;");
	}

	/**
	 * It updates the graph.
	 * 
	 * @param dagNodeList
	 * @param selectedDagPathList
	 */
	public void updateAutoConnect(List<DAGNode> dagNodeList,
			List<DAGLink> selectedDagPathList) {
		for (DAGLink dagLink : selectedDagPathList) {
			DAGNode sourceNode = null, targetNode = null;

			for (DAGNode dagNode : dagNodeList) {
				if (dagLink.getSourceNodeId() == dagNode.getNodeId()) {
					sourceNode = dagNode;
				}

				if (dagLink.getDestinatioNodeId() == dagNode.getNodeId()) {
					targetNode = dagNode;
				}
			}

			this.graph.putEdge(sourceNode, targetNode, dagLink);
		}
	}

	/**
	 * Deletes the link between the nodes on UI
	 * 
	 * @param linkedNodeList
	 * @param pathName
	 */
	public void deleteLink(List<DAGNode> linkedNodeList, String pathName) {
		graph.removeEdge(linkedNodeList.get(0), linkedNodeList.get(1));
	}

	/**
	 * @return Set<DAGNode> - it represents all the nodes object on dag
	 */
	public Set<DAGNode> getDagNodeSet() {

		return graph.getVertices();
	}

	/**
	 * @return Set<DAGLink> - it represents all the path object on dag
	 */
	public Set<DAGLink> getDagPathSet() {
		return graph.getAllWeights();
	}

	/**
	 * @return AttributePair
	 */

	public AttributePair getAttributePair() {
		return this.attributePair;
	}

}
