package edu.wustl.cab2b.client.ui.dag;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.netbeans.graph.api.GraphFactory;
import org.netbeans.graph.api.model.GraphEvent;
import org.netbeans.graph.api.model.IGraphPort;
import org.netbeans.graph.api.model.builtin.GraphDocument;
import org.netbeans.graph.api.model.builtin.GraphPort;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.IUpdateAddLimitUIInterface;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.dag.ambiguityresolver.AmbiguityObject;
import edu.wustl.cab2b.client.ui.dag.ambiguityresolver.AutoConnectAmbiguityResolver;
import edu.wustl.cab2b.client.ui.dag.ambiguityresolver.ResolveAmbiguity;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.client.ui.query.Utility;
import edu.wustl.cab2b.client.ui.util.ClientConstants;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImages;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IJoinGraph;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Expression;
import edu.wustl.common.util.logger.Logger;

/**
 * Class for creating main dag panel 
 * @author deepak_shingan
 *
 */
public class MainDagPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /**
     * view to be added to this panel
     */
    protected JComponent view;

    /**
     * Graph document required as base container for generated nodes
     */
    protected GraphDocument graphDocument;

    /**
     * Dag panel having control buttons like Auto Connect, Manual connect, Reset  etc 
     */
    protected DagControlPanel dagControlPanel;

    /**
     * IClientQueryBuilderInterface for dag query object
     */
    protected IClientQueryBuilderInterface dagQueryObject; //  @jve:decl-index=0:

    /**
     * For handling reset button and other events
     */
    protected EventHandler eventHandler;

    /**
     * Component having all NODE controls like connect, delete node etc.  
     */
    protected ViewController viewController;

    /**
     * For rendering nodes on GraphDocument canvas
     */
    protected DocumentRenderer documentRenderer;

    /**
     * Interface having dag controlling methods  
     */
    protected IUpdateAddLimitUIInterface mainDagAddLimitPanel;

    /**
     * Map of images required on DAG panel
     */
    protected Map<DagImages, Image> dagImages;

    /**
     * List of current nodes
     */
    protected List<ClassNode> currentNodeList;

    /**
     * Panel for showing current expressions and linking of nodes in DAG query, 
     * appears at the bottom of DAG panel 
     */
    protected ExpressionPanel expressionPanel;

    /**
     * IPathFinder 
     */
    protected IPathFinder dagPathFinder;

    /**
     * Flag indication for checking visibility of Nodes with ViewController
     */
    protected boolean isDAGForView = false;

    /**
     * Entity name to be displayed on DAG node
     */
    protected DisplayNameInterafce entityNameDisplayer;

    /**
     * Constructor method
     * @param addLimitPanel 
     * @param dagImageMap
     * @param pathFinder
     */
    public MainDagPanel(
            IUpdateAddLimitUIInterface addLimitPanel,
            Map<DagImages, Image> dagImageMap,
            IPathFinder pathFinder,
            boolean isDAGForView,
            DisplayNameInterafce entityNameDisplayer) {
        dagImages = dagImageMap;
        this.entityNameDisplayer = entityNameDisplayer;
        currentNodeList = new ArrayList<ClassNode>();
        graphDocument = new GraphDocument();
        this.isDAGForView = isDAGForView;
        documentRenderer = new DocumentRenderer(dagImages.get(DagImages.DocumentPaperIcon),
                dagImages.get(DagImages.PortImageIcon), this.isDAGForView);
        viewController = new ViewController(this);
        eventHandler = new EventHandler(this);
        mainDagAddLimitPanel = addLimitPanel;
        dagPathFinder = pathFinder;
        initGUI();
    }

    /**
     * Constructor method
     * @param addLimitPanel 
     * @param dagImageMap
     * @param pathFinder
     */
    public MainDagPanel(
            IUpdateAddLimitUIInterface addLimitPanel,
            Map<DagImages, Image> dagImageMap,
            IPathFinder pathFinder,
            boolean isDAGForView) {

        // constructor with default entity name displayer.
        this(addLimitPanel, dagImageMap, pathFinder, isDAGForView, new DisplayNameInterafce() {
            public String getEntityDisplayName(EntityInterface entity) {
                return edu.wustl.cab2b.common.util.Utility.getDisplayName(entity);
            }
        });
    }

    /**
     * Set the query object 
     * @param queryObject
     */
    public void setQueryObject(IClientQueryBuilderInterface queryObject) {
        dagQueryObject = queryObject;
    }

    /**
     * Initialize user interface
     */
    private void initGUI() {
        dagControlPanel = new DagControlPanel(this, dagImages);

        dagControlPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
        dagControlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new BorderLayout());
        view = GraphFactory.createView(graphDocument, documentRenderer, viewController, eventHandler);
        view.setBackground(Color.WHITE);
        add(dagControlPanel, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(view);
        add(jScrollPane, BorderLayout.CENTER);
        expressionPanel = new ExpressionPanel("Current expression : ");
        add(expressionPanel, BorderLayout.SOUTH);
    }

    /**
     * Method to get number of expression ID's present in query
     * @return
     */
    public int getExpressionCount() {
        return dagQueryObject.getQuery().getConstraints().size();
    }

    /**
     * Method to get first expression ID's in query 
     * @return
     */
    public IExpression getFirstExpression() {
        IConstraints v = dagQueryObject.getQuery().getConstraints();
        int size = v.size();
        if (size == 0) {
            return null;
        }
        return v.iterator().next();
    }

    /**
     *  CaB2B does not use this method for use of any functionality. 
     *  This method is used in querysuit model for updating the main dag graph panel.      
     * 
     */
    public void updateGraph() {
        try {
            IQuery query = dagQueryObject.getQuery();
            IConstraints constraints = query.getConstraints();

            //            Enumeration<IExpressionId> exprEnumeration = constraints.getExpressionIds();
            //            IExpressionId expressionId;
            for (IExpression expression : constraints) {
                //expressionId = exprEnumeration.nextElement();
                //IExpression expression = constraints.getExpression(expressionId);
                ClassNodeType nodeType = getNodeType(expression.getExpressionId());
                /**
                 * An Expression should be added to DAG only when its visible.
                 * If DAG is for view then all nodes should be shown in DAG 
                 * else only the ones that have constraints should be shown
                 */
                if (expression.isVisible()) {
                    if (isDAGForView) {
                        this.updateGraph(expression.getExpressionId());
                    } else if (!nodeType.equals(ClassNodeType.ViewOnlyNode)) {
                        this.updateGraph(expression.getExpressionId());
                    }

                }
            }
            IJoinGraph joinGraph = constraints.getJoinGraph();
            List<IExpression> roots = joinGraph.getAllRoots();
            for (IExpression rootExpressionId : roots) {
                if (rootExpressionId != null) {
                    addAssociations(constraints, joinGraph, rootExpressionId);
                }
            }
            expressionPanel.setText(getExprssionString());
        } catch (MultipleRootsException exception) {
            CommonUtils.handleException(exception, NewWelcomePanel.getMainFrame(), true, true, true, false);
        }
    }

    /**
     *  CaB2B does not use this method for use of any functionality. 
     *  This method is used in querysuit model for updating the main dag graph panel.      
     * 
     */
    private List<IAssociation> addAssociations(IConstraints constraints, IJoinGraph joinGraph,
                                               IExpression parentExpr) {
        List<IAssociation> associations = null;
        IAssociation association = null;
        List<IExpression> intermediateExpressions = new ArrayList<IExpression>();
        List<IExpression> childList = joinGraph.getChildrenList(parentExpr);
        if (childList.isEmpty()) {
            return associations;
        }
        IPath path;
        int childSize = childList.size();
        for (int index = 0; index < childSize; index++) {
            IExpression childExpression = childList.get(index);

            associations = new ArrayList<IAssociation>();
            association = joinGraph.getAssociation(parentExpr, childExpression);
            associations.add(association);
            if (!childExpression.isVisible()) {

                IExpression sourceId;
                while (!childExpression.isVisible()) {
                    intermediateExpressions.add(childExpression);
                    sourceId = childExpression;
                    childExpression = joinGraph.getChildrenList(childExpression).get(0);
                    association = joinGraph.getAssociation(sourceId, childExpression);
                    associations.add(association);
                }
            }

            EntityInterface source = parentExpr.getQueryEntity().getDynamicExtensionsEntity();
            EntityInterface target = childExpression.getQueryEntity().getDynamicExtensionsEntity();
            path = new Path(source, target, associations);

            /*
             * A link should be added to DAG  when either DAG is for view 
             * or when the target node has constarints
             */
            List<Integer> intermediateExpressionsIds = new ArrayList<Integer>(intermediateExpressions.size());
            for (IExpression exp : intermediateExpressions) {
                intermediateExpressionsIds.add(exp.getExpressionId());
            }
            if (isDAGForView) {
                linkTwoNode(getNode(parentExpr.getExpressionId()), getNode(childExpression.getExpressionId()),
                            path, intermediateExpressionsIds, false);
                addAssociations(constraints, joinGraph, childExpression);
            } else if (!getNodeType(childExpression.getExpressionId()).equals(ClassNodeType.ViewOnlyNode)) {
                int childId = childExpression.getExpressionId();

                linkTwoNode(getNode(parentExpr.getExpressionId()), getNode(childId), path,
                            intermediateExpressionsIds, false);
                addAssociations(constraints, joinGraph, childExpression);
            }

        }
        return associations;
    }

    /**
     * Method for selecting nodes of given expression ID
     * @param exprId
     */
    public void selectNode(int exprId) {
        viewController.selectNode(exprId);
    }

    /**
     * Method to add node to a graph
     * @throws MultipleRootsException 
     */
    public void updateGraph(int expressionId) throws MultipleRootsException {
        IExpression expression = getExpression(expressionId);
        IQueryEntity constraintEntity = expression.getQueryEntity();

        ClassNode node = new ClassNode();
        node.setDisplayName(entityNameDisplayer.getEntityDisplayName(constraintEntity.getDynamicExtensionsEntity()));
        //node.setDisplayName(edu.wustl.cab2b.common.util.Utility.getDisplayName(constraintEntity.getDynamicExtensionsEntity()));
        node.setExpressionId(expression);
        node.setID(Integer.toString(expression.getExpressionId()));
        node.setType(getNodeType(expression.getExpressionId()));
        currentNodeList.add(node);
        dagQueryObject.addExressionIdToVisibleList(expression.getExpressionId());
        graphDocument.addComponents(GraphEvent.createSingle(node));
        expressionPanel.setText(getExprssionString());
    }

    /**
     * Method to update the graph when new expression is added to the query
     * @param expressionIdString
     * @throws MultipleRootsException
     */
    public void updateGraph(String expressionIdString) throws MultipleRootsException {
        updateGraph(Integer.parseInt(expressionIdString));
    }

    /**
     * Method to update the graph when new expression is added to the query
     * @param expressionId
     * @throws MultipleRootsException
     */
    public void updateGraphForViewExpression(int expressionId) throws MultipleRootsException {
        updateGraph(expressionId);
    }

    /**
     * Get instance of GraphDocument 
     * @return
     */
    public GraphDocument getDocument() {
        return graphDocument;
    }

    /**
     *  Set instance of GraphDocument 
     * @param document
     */
    public void setDocument(GraphDocument document) {
        graphDocument = document;
    }

    /**
     * Checks paths between two nodes and if available return true else false
     * @param selectedNodes
     * @return boolean
     */
    private boolean isLinkingValid(List<ClassNode> selectedNodes) {
        boolean status = true;
        if (currentNodeList.size() < 2 || selectedNodes.size() != 2 || selectedNodes == null) {
            String errorMessage = "Please select two nodes for linking";
            JOptionPane.showMessageDialog(this, errorMessage, "Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
            status = false;
        }
        return status;
    }

    /**
     * Method to link selected nodes
     *
     */
    public void linkNodes() {
        List<ClassNode> selectedNodes = viewController.getCurrentNodeSelection();
        // If number of nodes to connect is not 2, set warning user
        if (isLinkingValid(selectedNodes)) {
            ClassNode sourceNode = selectedNodes.get(0);
            ClassNode destinationNode = selectedNodes.get(1);
            linkNode(sourceNode, destinationNode);
            if (!(selectedNodes.get(0) instanceof IndependentClassNode)) {
                expressionPanel.setText(getExprssionString());
            }
        }

    }

    /**
     * Method to get list of paths between source and destination entity
     * @param sourceNode The source node to connect
     * @param destNode The taget node to connect
     * @return List of selected paths between source and destination
     */
    protected List<IPath> getPaths(ClassNode sourceNode, ClassNode destNode) {

        IQuery query = dagQueryObject.getQuery();
        IConstraints constraints = query.getConstraints();
        /**
         * Get IEntityInterface objects from source and destination nodes
         */
        IExpression expression = constraints.getExpression(sourceNode.getExpressionId());
        IQueryEntity sourceEntity = expression.getQueryEntity();
        expression = constraints.getExpression(destNode.getExpressionId());
        IQueryEntity destinationEntity = expression.getQueryEntity();

        AmbiguityObject ambiguityObject = new AmbiguityObject(sourceEntity.getDynamicExtensionsEntity(),
                destinationEntity.getDynamicExtensionsEntity());
        ResolveAmbiguity resolveAmbigity = new ResolveAmbiguity(ambiguityObject, dagPathFinder);
        Map<AmbiguityObject, List<IPath>> map = resolveAmbigity.getPathsForAllAmbiguities();
        return map.get(ambiguityObject);
    }

    /**
     * This method link nodes if association is available between them
     * @param sourceNode Source node to connect
     * @param destNode Target node to connect
     */
    protected void linkNode(final ClassNode sourceNode, final ClassNode destNode) {
        List<IPath> paths = getPaths(sourceNode, destNode);
        if (paths == null || paths.isEmpty()) {
            JOptionPane.showMessageDialog(MainDagPanel.this,
                                          "No path available/selected between source and destination categories",
                                          "Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!dagQueryObject.isPathCreatesCyclicGraph(sourceNode.getExpressionId(), destNode.getExpressionId(),
                                                     paths.get(0))) {
            for (int i = 0; i < paths.size(); i++) {
                linkTwoNode(sourceNode, destNode, paths.get(i), new ArrayList<Integer>(), true);
            }
        } else {
            JOptionPane.showMessageDialog(MainDagPanel.this,
                                          "Cannot connect selected nodes as it creates cycle in the query graph",
                                          "Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Method to link two nodes with given path
     * @param sourceNode The source node to connect
     * @param destNode The destination  node to connect
     * @param path The path of connection
     */
    protected void linkTwoNode(final ClassNode sourceNode, final ClassNode destNode, final IPath path,
                               List<Integer> intermediateExpressionsIds, final boolean updateQueryRequired) {
        Logger.out.debug("Linking nodes: " + sourceNode.getID() + " and " + destNode.getID()
                + "; Intermediate exps: " + intermediateExpressionsIds.toString());

        // Update query object to have this association path set
        if (updateQueryRequired) {
            try {
                intermediateExpressionsIds = dagQueryObject.addPath(sourceNode.getExpressionId(),
                                                                    destNode.getExpressionId(), path);
            } catch (CyclicException e) {
                JOptionPane.showMessageDialog(this, "Cannot  connect nodes as it creates cycle in graph",
                                              "Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // From the query object get list of associations between these two node
        GraphPort sourcePort = new GraphPort();
        sourceNode.addPort(sourcePort);
        GraphPort targetPort = new GraphPort();
        destNode.addPort(targetPort);
        int assPosition = sourceNode.addSourcePort(sourcePort); // The location where node is added now
        destNode.addTargetPort(targetPort);

        //  now create the visual link..
        PathLink link = new PathLink();
        link.setSourcePort((GraphPort) sourcePort);
        link.setTargetPort((GraphPort) targetPort);
        link.setAssociationExpressions(intermediateExpressionsIds);
        link.setDestinationExpressionId(destNode.getExpressionId());
        link.setSourceExpressionId(sourceNode.getExpressionId());
        link.setPath(path);
        sourceNode.setLinkForSourcePort(sourcePort, link);
        link.setTooltipText(edu.wustl.cab2b.common.util.Utility.getPathDisplayString(path));
        graphDocument.addComponents(GraphEvent.createSingle(link));

        // ===================== IMPORTANT QUERY UPDATION STARTS HERE ==========
        if (updateQueryRequired) {
            if (assPosition == 0) {
                updateQueryObject(link, sourceNode, destNode, null);
            } else {
                updateQueryObject(link, sourceNode, destNode, sourcePort);
            }
        } else {
            IConstraints constraints = MainDagPanel.this.dagQueryObject.getQuery().getConstraints();
            int sourceExpId = sourceNode.getExpressionId();
            IExpression sourceExp = constraints.getExpression(sourceExpId);
            int nextExpId;
            if (intermediateExpressionsIds.size() > 0) {
                nextExpId = intermediateExpressionsIds.get(0);
            } else {
                nextExpId = destNode.getExpressionId();
            }
            if (assPosition != 0) {
                IExpression nextExp = constraints.getExpression(nextExpId);
                IConnector<LogicalOperator> logicalConnector = sourceExp.getConnector(
                                                                                      sourceExp.indexOfOperand(nextExp) - 1,
                                                                                      sourceExp.indexOfOperand(nextExp));
                LogicalOperator logicalOperator = logicalConnector.getOperator();
                if (logicalOperator.equals(LogicalOperator.And)) {
                    sourceNode.setLogicalOperator(sourcePort, ClientConstants.OPERATOR_AND);
                } else {
                    sourceNode.setLogicalOperator(sourcePort, ClientConstants.OPERATOR_OR);
                }

            } else {
                if (((Expression) sourceExp).containsRule()) {
                    IConnector<LogicalOperator> logicalConnector = sourceExp.getConnector(0, 1);
                    LogicalOperator logicalOperator = logicalConnector.getOperator();
                    if (logicalOperator.equals(LogicalOperator.And)) {
                        sourceNode.setOperatorBetAttrAndAss(ClientConstants.OPERATOR_AND);
                    } else {
                        sourceNode.setOperatorBetAttrAndAss(ClientConstants.OPERATOR_OR);
                    }
                }
            }
        }

        // Update user interface
        viewController.getHelper().scheduleNodeToLayout(sourceNode);
        viewController.getHelper().scheduleNodeToLayout(destNode);
        viewController.getHelper().scheduleLinkToLayout(link);
        viewController.getHelper().recalculate();
    }

    /**
     * Updates query object with passed parameters
     * @param link
     * @param sourceNode
     * @param destNode
     * @param sourcePort
     */
    private void updateQueryObject(PathLink link, ClassNode sourceNode, ClassNode destNode, GraphPort sourcePort) {
        // If the first association is added, put operator between attribute condition and association
        String operator = null;
        if (sourcePort == null) {
            operator = sourceNode.getOperatorBetAttrAndAss();
        } else { // Get the logical operator associated with previous association
            operator = sourceNode.getLogicalOperator(sourcePort);
        }

        // Get the expressionId between which to add logical operator
        int destId = link.getLogicalConnectorExpressionId();
        dagQueryObject.setLogicalConnector(sourceNode.getExpressionId(), destId,
                                           Utility.getLogicalOperator(operator), false);

        // Put appropriate parenthesis
        if (sourcePort != null) {
            int previousExpId = sourceNode.getLinkForSourcePort(sourceNode.getSourcePortAt(0)).getLogicalConnectorExpressionId();
            dagQueryObject.addParantheses(sourceNode.getExpressionId(), previousExpId, destId);
        }
    }

    /**
     * Method to display details of selected node in the upper panel
     * @param expressionId
     */
    public void showNodeDetails(ClassNode node) {
        IQuery query = dagQueryObject.getQuery();
        IConstraints constraints = query.getConstraints();
        mainDagAddLimitPanel.editAddLimitUI(constraints.getExpression(node.getExpressionId()));
    }

    /**
     * Method to clear all the paths (Clear all the assoications)
     */
    public void clearAllPaths() {
        viewController.clearAllPaths();
        expressionPanel.setText(getExprssionString());
    }

    /**
     * Delete selected expression from query object
     * @param expressionId
     */
    public void deleteExpression(int expressionId) {
        mainDagAddLimitPanel.clearAddLimitUI();
        dagQueryObject.removeExpression(expressionId);
        dagQueryObject.removeExressionIdFromVisibleList(expressionId);
        //Remove node with this expressionId from the list
        for (int i = 0; i < currentNodeList.size(); i++) {
            if (currentNodeList.get(i).getExpressionId() == expressionId) {
                currentNodeList.remove(i);
                break;
            }
        }
        expressionPanel.setText(getExprssionString());
    }

    /**
     * Method to delete linking path between source and destination node
     * @param sourceNode Source entity of path
     * @param destinationNode target entity of path
     */
    public void deletePath(PathLink link) {
        List<Integer> expressionIds = link.getAssociationExpressions();
        // If the association is direct association, remove the respective association 
        if (0 == expressionIds.size()) {
            dagQueryObject.removeAssociation(link.getSourceExpressionId(), link.getDestinationExpressionId());
        } else {
            for (int i = 0; i < expressionIds.size(); i++) {
                dagQueryObject.removeExpression(expressionIds.get(i));
            }
        }
        expressionPanel.setText(getExprssionString());
    }

    /**
     * Method to change logical operator of association
     */
    public void updateLogicalOperatorForAssociation(ClassNode sourceNode, PathLink link, String operator) {
        Integer destinationId = link.getLogicalConnectorExpressionId();
        dagQueryObject.setLogicalConnector(sourceNode.getExpressionId(), destinationId,
                                           Utility.getLogicalOperator(operator), true);
        expressionPanel.setText(getExprssionString());
    }

    /**
     * Method to change logical operator of association
     */
    public void updateLogicalOperatorForAttributes(ClassNode sourceNode, String operator) {
        PathLink link = sourceNode.getLinkForSourcePort(sourceNode.getSourcePortAt(0));
        Integer destinationId = link.getLogicalConnectorExpressionId();
        dagQueryObject.setLogicalConnector(sourceNode.getExpressionId(), destinationId,
                                           Utility.getLogicalOperator(operator), true);
        expressionPanel.setText(getExprssionString());
    }

    /**
     * Cleares dag panel. Removes all the expression and associations
     * from query object and all the objects on dag panel are cleared accordingly
     */
    public void clearDagPanel() {
        viewController.deleteAllNodes();
        currentNodeList.clear();
        expressionPanel.setText("Current expression : ");
    }

    /**
     * Method to convert expression into string representation 
     */
    public String getExprssionString() {
        HashMap<Integer, String> expressionToStringMap = new HashMap<Integer, String>();
        HashSet<Integer> expressionsCovered = new HashSet<Integer>();
        for (int i = 0; i < currentNodeList.size(); i++) {
            if (null == expressionToStringMap.get(currentNodeList.get(i).getExpressionId())) {
                formExpression(expressionToStringMap, expressionsCovered, currentNodeList.get(i));
            }
        }

        StringBuffer expressionString = new StringBuffer();
        expressionString.append("<HTML>Current expression : ");
        String nonConnectedExpressions = "";
        int totalNonConnectedExpressions = 0;
        for (int i = 0; i < currentNodeList.size(); i++) {
            int expressionId = currentNodeList.get(i).getExpressionId();
            if (expressionsCovered.contains(expressionId) == false) {
                if (currentNodeList.get(i).getSourcePorts().size() == 0) {
                    nonConnectedExpressions += expressionId;
                    nonConnectedExpressions += ", ";
                    totalNonConnectedExpressions++;
                } else {
                    expressionString.append(expressionToStringMap.get(expressionId)).append("<P>");
                }
            }
        }

        if (totalNonConnectedExpressions > 0) {
            expressionString.append("Expression "
                    + nonConnectedExpressions.subSequence(0, nonConnectedExpressions.length() - 2));
            if (totalNonConnectedExpressions == 1) {
                expressionString.append(" is not connected");
            } else {
                expressionString.append(" are not connected");
            }
        }

        expressionString.append("</HTML>");
        return expressionString.toString();
    }

    /**
     * Form expression for given expression Id
     * @param sb
     * @param node
     */
    private String formExpression(HashMap<Integer, String> expressionToStringMap,
                                  HashSet<Integer> expressionsCovered, ClassNode node) {
        StringBuffer expressionString = new StringBuffer();

        /**
         * If node is only in view and does not have constraints, its should not be a part of the expression string
         */
        ClassNodeType nodeType = getNodeType(node.getExpressionId());
        if (nodeType.equals(ClassNodeType.ViewOnlyNode)) {
            expressionToStringMap.put(node.getExpressionId(), expressionString.toString());
            return expressionString.toString();
        }

        int expressionId = node.getExpressionId();
        List<IGraphPort> ports = node.getSourcePorts();
        if (ports.size() > 0) {
            expressionString.append("( [").append(expressionId).append("] ").append(
                                                                                    node.getOperatorBetAttrAndAss().toString()).append(
                                                                                                                                       " ");
            if (ports.size() > 1) {
                expressionString.append("( ");
            }

            for (int i = 0; i < ports.size(); i++) {
                int associationNode = node.getLinkForSourcePort(ports.get(i)).getDestinationExpressionId();
                if (i > 0 && !getNodeType(associationNode).equals(ClassNodeType.ViewOnlyNode)) {
                    expressionString.append(" ").append(node.getLogicalOperator(ports.get(i))).append(" ");
                }

                if (expressionToStringMap.get(associationNode) != null) {
                    expressionString.append(expressionToStringMap.get(associationNode));
                } else {
                    expressionString.append(formExpression(expressionToStringMap, expressionsCovered,
                                                           getNode(associationNode)));
                }

                expressionsCovered.add(associationNode);
            }

            if (ports.size() > 1) {
                expressionString.append(") ");
            }

            expressionString.append(") ");
        } else {
            expressionString.append("[" + expressionId + "] ");
        }

        expressionToStringMap.put(node.getExpressionId(), expressionString.toString());
        return expressionString.toString();
    }

    /**
     * Get the class node object associated with this expressionId
     * @param expressionId The expression Id for which to get object
     * @return classNode object associated with this expression Id 
     */
    private ClassNode getNode(int expressionId) {
        ClassNode classNode = null;
        for (int i = 0; i < currentNodeList.size(); i++) {
            if (currentNodeList.get(i).getExpressionId() == expressionId) {
                classNode = currentNodeList.get(i);
                break;
            }
        }
        return classNode;
    }

    /**
     * Method to perform auto-connect functionality
     */
    public void performAutoConnect() {
        if (currentNodeList.size() < 2) {
            // Cannot perform connect all functionality
            JOptionPane.showMessageDialog(this.getParent().getParent().getParent(),
                                          "Please add atleast two nodes.", "Auto Connect Warning",
                                          JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<ClassNode> selectedNodes = viewController.getCurrentNodeSelection();
        if (selectedNodes == null || selectedNodes.size() <= 1) {
            // Cannot perform connect all functionality
            JOptionPane.showMessageDialog(this.getParent().getParent().getParent(),
                                          "Please select atleast two nodes", "Auto Connect Warning",
                                          JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (false == isAutoConnectValid(selectedNodes)) {
            // Cannot perform connect all functionality
            JOptionPane.showMessageDialog(
                                          this.getParent().getParent().getParent(),
                                          "Auto Connect failed. Some of the selected nodes are already connected.",
                                          "Auto Connect Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Set<EntityInterface> entitySet = new HashSet<EntityInterface>();
        for (int i = 0; i < selectedNodes.size(); i++) {
            IExpression expression = getExpression(selectedNodes.get(i).getExpressionId());
            EntityInterface entity = expression.getQueryEntity().getDynamicExtensionsEntity();
            entitySet.add(entity);
        }
        checkPathValidity(entitySet, selectedNodes);
    }

    /**
     * Method for checking validity of paths between selected nodes and their entities
     * @param entitySet
     */
    protected void checkPathValidity(Set<EntityInterface> entitySet, List<ClassNode> selectedNodes) {
        Set<ICuratedPath> paths = dagPathFinder.autoConnect(entitySet);
        if (paths == null || paths.size() <= 0) {
            // Cannot perform connect all functionality
            JOptionPane.showMessageDialog(this, "No curated path available between selected nodes.",
                                          "Auto-Connect warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ICuratedPath path = getSelectedCuratedPath(paths);
        if (path == null) {
            // Show ambiguity resolver to get a curated path
            AutoConnectAmbiguityResolver childPanel = new AutoConnectAmbiguityResolver(paths);
            childPanel.setParentWindow(WindowUtilities.showInDialog(NewWelcomePanel.getMainFrame(), childPanel,
                                                                    "Available curated paths Panel",
                                                                    Constants.WIZARD_SIZE2_DIMENSION, true, false));
            path = childPanel.getUserSelectedPath();
        }

        if (path != null) {
            autoConnectNodes(selectedNodes, path);
        }

    }

    /**
     * Method to connect selected nodes with the selected curated path
     * @param selectedNodes The node list selected for auto-connection
     * @param path The curated path with which to connect a list
     */
    protected void autoConnectNodes(List<ClassNode> selectedNodes, ICuratedPath curatedPath) {
        Set<IPath> paths = curatedPath.getPaths();
        for (IPath path : paths) {
            List<ClassNode> sourceNodes = getNodesWithEntity(selectedNodes, path.getSourceEntity());
            List<ClassNode> destinationNodes = getNodesWithEntity(selectedNodes, path.getTargetEntity());
            for (int i = 0; i < sourceNodes.size(); i++) {
                for (int j = 0; j < destinationNodes.size(); j++) {
                    linkTwoNode(sourceNodes.get(i), destinationNodes.get(j), path, new ArrayList<Integer>(), true);
                }
            }
        }
        expressionPanel.setText(getExprssionString());
    }

    /**
     * The to validate auto-connect event
     * @param selectedNodes Nodes to auto-connect
     * @return true if auto-connect is valid, else return false
     */
    private boolean isAutoConnectValid(List<ClassNode> selectedNodes) {
        for (int i = 0; i < selectedNodes.size(); i++) {
            ClassNode sourceNode = selectedNodes.get(i);
            List<IGraphPort> ports = sourceNode.getSourcePorts();
            for (int j = 0; j < ports.size(); j++) {
                int id = sourceNode.getLinkForSourcePort(ports.get(j)).getDestinationExpressionId();
                for (int k = 0; k < selectedNodes.size(); k++) {
                    if (id == selectedNodes.get(k).getExpressionId()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Method to get a IGraph node associated with given entityInterface
     * @param classNodes The list of IGraph Nodes 
     * @param entity The Entity to match
     * @return List of Nodes matching to given entity interface
     */
    private List<ClassNode> getNodesWithEntity(List<ClassNode> classNodes, EntityInterface entity) {
        List<ClassNode> entityNodes = new ArrayList<ClassNode>();

        for (int i = 0; i < classNodes.size(); i++) {
            ClassNode node = classNodes.get(i);
            IExpression expression = getExpression(node.getExpressionId());
            EntityInterface currentEntity = expression.getQueryEntity().getDynamicExtensionsEntity();
            if (true == entity.equals(currentEntity)) {
                entityNodes.add(node);
            }
        }
        return entityNodes;
    }

    /**
     * Method to return selected curated path from a set of curated paths
     * @param paths Set of curated paths 
     * @return The deafult selected path if any from a set of curated paths else returns false
     */
    protected ICuratedPath getSelectedCuratedPath(Set<ICuratedPath> curratedPathSet) {
        ICuratedPath requiredCurratedPath = null;
        if (curratedPathSet.size() == 1) {
            for (ICuratedPath curratedPath : curratedPathSet) {
                requiredCurratedPath = curratedPath;
            }
        }

        if (requiredCurratedPath == null) {
            for (ICuratedPath curratedPath : curratedPathSet) {
                if (true == curratedPath.isSelected()) {
                    requiredCurratedPath = curratedPath;
                    break;
                }
            }
        }

        return requiredCurratedPath;
    }

    /**
     * Returns node type for given expressionID
     * @param expressionId
     * @return ClassNodeType
     */
    public ClassNodeType getNodeType(int expressionId) {
        IExpression expression = getExpression(expressionId);
        if (expression.containsRule()) {
            if (expression.isInView()) {
                return ClassNodeType.ConstraintViewNode;
            } else {
                return ClassNodeType.ConstraintOnlyNode;
            }
        } else {
            return ClassNodeType.ViewOnlyNode;
        }
    }

    /**
     * Gets IExpression for given expressionId
     * @param expressionId
     * @return
     */
    public IExpression getExpression(int expressionId) {
        return dagQueryObject.getQuery().getConstraints().getExpression(expressionId);
    }

    /**
     * Adds expression to view
     * @param expressionId
     */
    public void addExpressionToView(int expressionId) {
        Expression expression = (Expression) dagQueryObject.getQuery().getConstraints().getExpression(expressionId);
        expression.setInView(true);
    }

    /**
     * Remove expression to view
     * @param expressionId
     */
    public void removeExpressionFromView(int expressionId) {
        Expression expression = (Expression) dagQueryObject.getQuery().getConstraints().getExpression(expressionId);
        expression.setInView(false);
    }

    /**
     * Returns value of isDAGForView
     * @return isDAGForView  
     */
    public boolean isDAGForView() {
        return isDAGForView;
    }

    /**
     * Set value for isDAGForView
     * @param forView
     */
    public void setDAGForView(boolean forView) {
        isDAGForView = forView;
    }

    /**
     * This method enables or disables the AutoConnect button of the DagControlPanel
     * @param enable
     */
    public void enableAutoConnectButton(boolean enable) {
        dagControlPanel.enableAutoConnectButton(enable);
    }

    /**
     * Returns number of visible nodes in DAG
     * @return int
     */
    public int getVisibleNodeCount() {
        return currentNodeList.size();
    }
}
