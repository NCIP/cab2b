package edu.wustl.cab2b.admin.flex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.admin.bizlogic.SearchCategoryBizLogic;
import edu.wustl.cab2b.admin.util.AdminConstants;
import edu.wustl.cab2b.admin.util.AttributePair;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.querysuite.queryengine.impl.CommonPathFinder;
import edu.wustl.common.util.Graph;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * @author lalit_chand
 */
public class CategoryDagPanel extends DAGPanel {

    private int nodeId;

    private int editNodeId;

    private Graph<DAGNode, DAGLink> graph;

    private IPathFinder pathFinder = new CommonPathFinder();

    private HttpSession session;

    private HttpServletRequest request;

    private Map<String, IPath> idVsPathMap;

    private AttributePair attributePair;

    public CategoryDagPanel() {
        request = flex.messaging.FlexContext.getHttpRequest();
        session = request.getSession();

    }

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

    public Map getIdVsPathMap() {
        return idVsPathMap;
    }

    /**
     * it deletes the node from 
     * @param nodeId
     */
    
    public void deleteNode(long nodeId) {
        Iterator<DAGNode> iterator = getDagNodeSet().iterator();

        while (iterator.hasNext()) {

            DAGNode dagNode = iterator.next();
            if (dagNode.getNodeId() == nodeId) {
                graph.removeVertex(dagNode);

            }
        }
    }

    /**@param strToCreateQueryObject
     * @param entityID
     * This method creates the DAGNode.
     * @return DAGNode
     */

    public DAGNode createNode(String strToCreateQueryObject, String entityID) {
        String[] attributesList = strToCreateQueryObject.split(";");
        Long entityId = Long.parseLong(entityID);
        EntityInterface entity = EntityCache.getCache().getEntityById(entityId);
        String nodeName = edu.wustl.cab2b.common.util.Utility.getDisplayName(entity);
        String toolTip;
        StringBuffer sbuffer = new StringBuffer();

        for (String attribute : attributesList) {

            String[] attributeName = attribute.split(":");
            sbuffer = sbuffer.append(" ").append(CommonUtils.getFormattedString(attributeName[0])).append("\n");

        }
        toolTip = sbuffer.toString();
        DAGNode dagNode = new DAGNode();
        dagNode.setToolTip(toolTip);
        dagNode.setNodeName(nodeName);
        dagNode.setNodeId(this.nodeId);
        this.nodeId++;
        dagNode.setEntityId(entityId.intValue());
        dagNode.setAttribute(Arrays.asList(attributesList));
        graph.addVertex(dagNode);
        return dagNode;
    }

    /**
     * @param strToCreateQueryObject
     * @param entityID
     * @return DAGNode representing edited DAGNode
     */
    public DAGNode editNode(String strToCreateQueryObject, String entityID) {
        String[] attributesList = strToCreateQueryObject.split(";");
        String toolTip;
        StringBuffer sbuffer = new StringBuffer();

        for (String attribute : attributesList) {

            String[] attributeName = attribute.split(":");
            sbuffer = sbuffer.append(" ").append(CommonUtils.getFormattedString(attributeName[0])).append("\n");

        }
        toolTip = sbuffer.toString();

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
     * 
     * @param nodeId Represents NodeId of editedNode.
     * @return It returns a String having all the attributes.
     */
    public String getLimitUI(int nodeId) {
        this.editNodeId = nodeId;
        SearchCategoryBizLogic bizlogic = new SearchCategoryBizLogic();
        EntityInterface entity = null;
        String result = null;
        for (DAGNode dagNode : getDagNodeSet()) {
            if (dagNode.getNodeId() == nodeId) {
                entity = EntityCache.getCache().getEntityById(new Long(dagNode.getEntityId()));
                result = bizlogic.showAttributeInformation(entity, dagNode);
            }
        }
        return result;
    }

    /**
     * Gets all path Lists between nodes
     * @param linkedNodeList
     * @return
     */
    private Map<String, List<IPath>> getPathList(List<DAGNode> linkedNodeList) {
        DAGNode sourceNode = linkedNodeList.get(0);
        DAGNode destinationNode = linkedNodeList.get(1);
        AbstractEntityCache cache = EntityCache.getCache();

        EntityInterface sourceEntity = cache.getEntityById(new Long(sourceNode.getEntityId()));
        EntityInterface destinationEntity = cache.getEntityById(new Long(destinationNode.getEntityId()));

        Set<ICuratedPath> allCuratedPaths = pathFinder.getCuratedPaths(sourceEntity, destinationEntity);
        Logger.out.debug("  getCuratedPaths() executed : " + allCuratedPaths.size());
        List<IPath> curatedPathList = new ArrayList<IPath>(allCuratedPaths.size());
        List<IPath> selectedPathList = new ArrayList<IPath>();

        for (ICuratedPath curatedPaths : allCuratedPaths) {
            Set<IPath> pathSet = curatedPaths.getPaths();
            // called pathfinder for 2 entities , so this set will have only 1
            // IPath in it
            if (!pathSet.isEmpty()) {
                IPath path = pathSet.iterator().next();
                if (curatedPaths.isSelected()) {
                    selectedPathList.add(path);
                }
                curatedPathList.add(path);
            }
        }
        List<IPath> generalPathList = pathFinder.getAllPossiblePaths(sourceEntity, destinationEntity);
        for (IPath path : generalPathList) {
            idVsPathMap.put(Long.toString(path.getPathId()), path);

        }
        Map<String, List<IPath>> allPathMap = new HashMap<String, List<IPath>>(3);
        allPathMap.put(Constants.SELECTED_PATH, selectedPathList);
        allPathMap.put(Constants.CURATED_PATH, curatedPathList);
        allPathMap.put(Constants.GENERAL_PATH, generalPathList);
        return allPathMap;
    }

    /**
     * Gets display path string
     * @param path
     * @return String representing full path.
     */
    public String getPathDisplayString(IPath path) {
        StringBuffer returner = new StringBuffer();
        String roleName;
        List<IAssociation> associationList = path.getIntermediateAssociations();
        boolean isFirstAssociation = true;
        for (IAssociation association : associationList) {
            roleName = edu.wustl.cab2b.client.ui.query.Utility.getRoleName(association);
            if (isFirstAssociation) {
                EntityInterface srcEntity = association.getSourceEntity();
                EntityInterface tarEntity = association.getTargetEntity();
                String srcEntityName = Utility.parseClassName(srcEntity.getName());
                String tarEntityName = Utility.parseClassName(tarEntity.getName());
                isFirstAssociation = false;
                returner.append(srcEntityName + " >> (" + roleName + ") >> " + tarEntityName);
            } else {
                EntityInterface tarEntity = association.getTargetEntity();
                String tarEntityName = Utility.parseClassName(tarEntity.getName());
                returner.append(" >> (" + roleName + ") >> " + tarEntityName);
            }
        }
        return returner.toString();
    }

    public Map<String, List<DAGLink>> getpaths(List linkedNodeList) {
        Map<String, List<IPath>> allPathMaps = getPathList(linkedNodeList);
        Map<String, List<DAGLink>> allDagLinkMap = new HashMap<String, List<DAGLink>>();
        Set<String> allPath = allPathMaps.keySet();

        Iterator<String> iterator = allPath.iterator();

        while (iterator.hasNext()) {
            List<DAGLink> allPathsListStr = new ArrayList<DAGLink>();
            String key = iterator.next();

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
     * @param linkedNodeList
     * @param selectedPath
     * @return it returns a DAGLink object which connects two nodes on DAG
     */

    public List<DAGLink> linkNodes(List<DAGNode> linkedNodeList, DAGLink selectedPath) {
        DAGNode sourceNode = linkedNodeList.get(0);
        DAGNode destinationNode = linkedNodeList.get(1);
        Map<String, List<IPath>> allPathMaps = getPathList(linkedNodeList);
        session.setAttribute(AdminConstants.ID_VS_PATH_MAP, getIdVsPathMap());
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

                //  checking if it is for Category or for CuratePath    
                if (session.getAttribute(AdminConstants.PAGE_IDENTIFIER) != null) {

                    if (graph.willCauseNewCycle(sourceNode, destinationNode)) {
                        return null;
                    }

                } else {
                    if (graph.willViolateTreeConstraint(sourceNode, destinationNode)) {
                        return null;
                    }

                }
                graph.putEdge(sourceNode, destinationNode, dagPath);
            }
        }

        return dagPathList;
    }

    /**
     * This gets called when user click Back button and it sends all the DAGNodes added previously.
     * @return list of DAGNode
     */
    public Map repaintDAG() {
        CategoryDagPanel categoryDagpanel = (CategoryDagPanel) session.getAttribute(AdminConstants.CATEGORY_INSTANCE);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put(AdminConstants.DAG_NODE_SET, categoryDagpanel.getDagNodeSet());
        map.put(AdminConstants.DAG_LINK_SET, categoryDagpanel.getDagPathSet());
        return map;
    }

    /**
     * This method checks for multiple roots on dag
     * @return true if there exists a multiple roots
     */
    public boolean checkMultipleRoot() {
        List<Long> sourceNodeList = new ArrayList<Long>();
        List<Long> destinationNodeList = new ArrayList<Long>();
        List<Long> multipleRootsList = new ArrayList<Long>();

        for (DAGLink dagLink : getDagPathSet()) {
            for (DAGNode dagNode : getDagNodeSet()) {

                if (dagLink.getSourceNodeId() == dagNode.getNodeId()) {
                    sourceNodeList.add(dagNode.getNodeId());
                }
                if (dagLink.getDestinatioNodeId() == dagNode.getNodeId()) {
                    destinationNodeList.add(dagNode.getNodeId());

                }
            }
        }

        for (Long nodeId : sourceNodeList) {

            if (!destinationNodeList.contains(nodeId)) {
                if (!multipleRootsList.contains(nodeId)) {
                    multipleRootsList.add(nodeId);
                }
            }
        }
        if (multipleRootsList.size() == 1)
            return false;

        return true;
    }

    /**
     * 
     * 
     * @param sourceEntityId
     * @param targetEntityId
     * @return  true if both the sourceEntityId and sourceEntityId belongs to same EntityGroup
     */
    public boolean isSameEntityGroup(int sourceEntityId, int targetEntityId) {
        AbstractEntityCache cache = EntityCache.getCache();
        EntityInterface sourceEntityInterface = cache.getEntityById(new Long(sourceEntityId));
        EntityInterface targetEntityInterface = cache.getEntityById(new Long(targetEntityId));
        EntityGroupInterface soruceEntityGroupInterFace = sourceEntityInterface.getEntityGroupCollection().iterator().next();
        EntityGroupInterface targetEntityGroupInterFace = targetEntityInterface.getEntityGroupCollection().iterator().next();

        if (soruceEntityGroupInterFace == targetEntityGroupInterFace)
            return true;
        else {
            session.setAttribute(AdminConstants.SOURCE_CLASS_ID, sourceEntityId);
            session.setAttribute(AdminConstants.TARGET_CLASS_ID, targetEntityId);
            return false;
        }
    }

    /**
     * 
     * @param sourceNodeId
     * @param targetNodeId
     * @param attributeStr
     * @return DAGLink object representing InterModelJoin Link
     */
    public DAGLink createInterModelJoin(int sourceNodeId, int targetNodeId, String attributeStr) {
        String[] attributes = attributeStr.split("_");
        String sourceAttributeId = attributes[0];
        String targetAttributeId = attributes[1];
        String matchFactor = attributes[2];
        AbstractEntityCache cache = EntityCache.getCache();
        AttributeInterface sourceAttribute = cache.getAttributeById(Long.valueOf(sourceAttributeId));
        AttributeInterface targetAttribute = cache.getAttributeById(Long.valueOf(targetAttributeId));
        attributePair = new AttributePair(sourceAttribute, targetAttribute);
        if (AttributePair.MatchFactor.CLASS_CONCEPT_CODE.getValue().equalsIgnoreCase(matchFactor)) {
            attributePair.setMatchFactor(AttributePair.MatchFactor.CLASS_CONCEPT_CODE);
        } else if (AttributePair.MatchFactor.ATTRIBUTE_CONCEPT_CODE.getValue().equalsIgnoreCase(matchFactor)) {
            attributePair.setMatchFactor(AttributePair.MatchFactor.ATTRIBUTE_CONCEPT_CODE);
        } else if (AttributePair.MatchFactor.PUBLIC_ID.getValue().equalsIgnoreCase(matchFactor)) {
            attributePair.setMatchFactor(AttributePair.MatchFactor.PUBLIC_ID);
        } else {
            attributePair.setMatchFactor(AttributePair.MatchFactor.MANUAL_CONNECT);
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
     * @return This method will return a List of List of all DagLink for Auto Connect .
     * 
     */

    public String getAutoConnectPaths(Set<DAGNode> dagNodeSet) {

        Set<EntityInterface> entitySet = new HashSet<EntityInterface>(dagNodeSet.size());
        AbstractEntityCache cache = EntityCache.getCache();
        StringBuilder toolTipForEachICurate;
        Map<List, String> map = new HashMap<List, String>();

        for (DAGNode dagNode : dagNodeSet) {
            EntityInterface entity = cache.getEntityById(dagNode.getEntityId());
            entitySet.add(entity);
        }

        Set<ICuratedPath> iPathSet = pathFinder.autoConnect(entitySet);
        if (iPathSet.isEmpty()) {
            return null;

        }

        for (ICuratedPath iCuratedPath : iPathSet) {
            List<DAGLink> dagPathList = new ArrayList<DAGLink>();
            toolTipForEachICurate = new StringBuilder();

            for (IPath path : iCuratedPath.getPaths()) {
                DAGLink dagPath = new DAGLink();
                toolTipForEachICurate.append(getPathDisplayString(path)).append("\n");
                dagPath.setToolTip(getPathDisplayString(path));
                dagPath.setPathId(Long.toString(path.getPathId()));

                for (DAGNode dagNode : dagNodeSet) {

                    long dagNodeId = dagNode.getNodeId();
                    long dagNodeEntityId = dagNode.getEntityId();
                    long pathSourceEntityId = path.getSourceEntity().getId();
                    long pathTargetEntityId = path.getTargetEntity().getId();

                    if (dagNodeEntityId == pathSourceEntityId)
                        dagPath.setSourceNodeId(dagNodeId);
                    if (dagNodeEntityId == pathTargetEntityId)
                        dagPath.setDestinationNodeId(dagNodeId);

                }
                dagPathList.add(dagPath);
            }

            map.put(dagPathList, toolTipForEachICurate.toString());

        }

        return makeXML(map);

    }

    /**
     * 
     * @param mapForToolTip
     * @return it returns the xml having aal the info of DAGLink .
     */
    public String makeXML(Map<List, String> mapForToolTip) {

        StringBuilder builder = new StringBuilder();

        for (List<DAGLink> dagLinkList : mapForToolTip.keySet()) {
            builder.append("<ICurated>").append("\n");
            for (DAGLink dagLink : dagLinkList) {
                builder.append("<DagLink>").append("\n");
                builder.append("<pathId>").append(dagLink.getPathId()).append("</pathId>").append("\n");
                builder.append("<sourceNodeId>").append(dagLink.getSourceNodeId()).append("</sourceNodeId>").append(
                                                                                                                    "\n");
                builder.append("<targetNodeId>").append(dagLink.getDestinatioNodeId()).append("</targetNodeId>").append(
                                                                                                                        "\n");
                builder.append("<toolTip>").append(handleSpecialCharacter(dagLink.getToolTip())).append(
                                                                                                        "</toolTip>").append(
                                                                                                                             "\n");
                builder.append("</DagLink>").append("\n");

            }

            builder.append("<ToolTip>").append(handleSpecialCharacter(mapForToolTip.get(dagLinkList))).append(
                                                                                                              "</ToolTip>").append(
                                                                                                                                   "\n");
            builder.append("</ICurated>").append("\n");
        }

        return builder.toString();

    }

    public String handleSpecialCharacter(String sourceString) {

        sourceString = sourceString.replaceAll(">", "&gt;");

        return sourceString;
    }

    /**
     * It updates the graph.
     * @param dagNodeList
     * @param selectedDagPathList
     */
    public void updateAutoConnect(List<DAGNode> dagNodeList, List<DAGLink> selectedDagPathList) {

        for (DAGLink dagLink : selectedDagPathList) {
            DAGNode sourceNode = null;
            DAGNode targetNode = null;
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

        return graph.getEdges();
    }

    public AttributePair getAttributePair() {
        return this.attributePair;

    }

}
