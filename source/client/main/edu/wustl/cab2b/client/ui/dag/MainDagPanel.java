package edu.wustl.cab2b.client.ui.dag;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.netbeans.graph.api.GraphFactory;
import org.netbeans.graph.api.model.GraphEvent;
import org.netbeans.graph.api.model.IGraphLink;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.IGraphPort;
import org.netbeans.graph.api.model.builtin.GraphDocument;
import org.netbeans.graph.api.model.builtin.GraphLink;
import org.netbeans.graph.api.model.builtin.GraphPort;

import edu.wustl.cab2b.client.ui.IUpdateAddLimitUIInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.dag.ambiguityresolver.AmbiguityObject;
import edu.wustl.cab2b.client.ui.dag.ambiguityresolver.ResolveAmbiguity;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImageConstants;
import edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.IConstraintEntity;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;

public class MainDagPanel extends Cab2bPanel
{
	// view to be added to this panel
	private JComponent m_view;
	private GraphDocument m_document;
	private DagControlPanel m_controlPanel;
	private IClientQueryBuilderInterface m_queryObject;
	private EventHandler m_eventHandler;
	private ViewController m_viewController;
	private DocumentRenderer m_documentRenderer;
	private IUpdateAddLimitUIInterface m_addLimitPanel;
	private Map<DagImageConstants, Image> m_dagImageMap;
	private List<ClassNode> m_currentNodeList;
	private ExpressionPanel m_expressionPanel;
	private IPathFinder m_pathFinder;
	
	/**
	 * Constructor method
	 * @param addLimitPanel 
	 * @param dagImageMap
	 * @param pathFinder
	 */
	public MainDagPanel(IUpdateAddLimitUIInterface addLimitPanel, Map<DagImageConstants, Image> dagImageMap, IPathFinder pathFinder)
	{
		m_dagImageMap = dagImageMap;
		m_currentNodeList = new ArrayList<ClassNode>();
		m_document = new GraphDocument();
		m_documentRenderer = new DocumentRenderer(m_dagImageMap.get(DagImageConstants.DocumentPaperIcon), m_dagImageMap.get(DagImageConstants.PortImageIcon));
		m_viewController = new ViewController(this);
		m_eventHandler = new EventHandler(this);
		m_addLimitPanel = addLimitPanel;
		m_pathFinder = pathFinder;
		initGUI();
	}
	
	public void setQueryObject (IClientQueryBuilderInterface queryObject)
	{
		m_queryObject = queryObject;
	}
	
	private void initGUI()
	{				
		m_controlPanel = new DagControlPanel(this, m_dagImageMap);
		m_controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BorderLayout());
		m_view = GraphFactory.createView(m_document, m_documentRenderer, m_viewController, m_eventHandler);
		m_view.setBackground(Color.WHITE);
		add(m_controlPanel, BorderLayout.NORTH);
		add(new JScrollPane(m_view), BorderLayout.CENTER);
		m_expressionPanel = new ExpressionPanel("Current expression : ");
		add(m_expressionPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Method to add node to a graph
	 * @throws MultipleRootsException 
	 *
	 */
	public void updateGraph(IExpressionId expressionId) throws MultipleRootsException
	{
		IExpression expression = m_queryObject.getQuery().getConstraints().getExpression(expressionId);
		IConstraintEntity constraintEntity = expression.getConstraintEntity();
		ClassNode node = new ClassNode();

		//set proper display class name 
		node.setDisplayName( edu.wustl.cab2b.common.util.Utility.getDisplayName(constraintEntity.getDynamicExtensionsEntity()));		
		//node.setDisplayName(constraintEntity.getDynamicExtensionsEntity().getName());
		
		node.setExpressionId(expression);
		node.setID(String.valueOf(expressionId.getInt()));
		m_currentNodeList.add(node);
		m_queryObject.addExressionIdToVisibleList(expressionId);
		m_document.addComponents(GraphEvent.createSingle(node));
		m_expressionPanel.setText(getExprssionString());
	}
	
	public void updateGraph(String expressionIdString) throws MultipleRootsException
	{
		int expressionIdInt = Integer.parseInt(expressionIdString);
		IExpressionId expressionId = Cab2bQueryObjectFactory.createExpressionId(expressionIdInt);
		updateGraph(expressionId);
	}
	
	/**
	 * Method to invoke a DAG view
	 * @param args
	 */
	public static void main(String[] args)
	{
		/*JFrame frame = new JFrame("The diagramtaic view of Query interface");
		MainDagPanel dagPanel = new MainDagPanel(null);
		frame.getContentPane().add(dagPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setVisible(true);*/
	}

	public GraphDocument getDocument() 
	{
		return m_document;
	}
	
	public boolean isLinkingValid()
	{
		List<ClassNode> selectedNodes = m_viewController.getCurrentNodeSelection();
		String errorMessage="";
		boolean status = true;
		if(2 != selectedNodes.size())
		{
			errorMessage = "Please select two nodes for linking";
			status = false;
		}
		else
		{
			ClassNode sourceNode = selectedNodes.get(0);
			ClassNode destinationNode = selectedNodes.get(1);
			if((sourceNode.getAssociations().contains(destinationNode.getExpressionId())) ||
						(destinationNode.getAssociations().contains(sourceNode.getExpressionId())))
			{
				errorMessage = "The selected categories are already linked";
				status = false;
			}
		}
		if(false == status)
		{
			JOptionPane.showMessageDialog(this, errorMessage, "Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
		}
		return status;
	}
	/**
	 * Method to link selected nodes
	 *
	 */
	public void linkNodes()
	{
		List<ClassNode> selectedNodes = m_viewController.getCurrentNodeSelection();
		// If number of nodes to connect is not 2, set warning user
		if(true == isLinkingValid())
		{
		
			// 1. Get Expression Ids from selected nodes
			// 2. Get paths for selected nodes
			// 3. For multiple paths show link with red color and ask user to resolve ambiguity
			// 4. Else show link with green color
			// 5. Change source nodes to show added association
			// 6. Update query interface accordingly
			ClassNode sourceNode = selectedNodes.get(0);
			ClassNode destinationNode =  selectedNodes.get(1);
			linkNode(sourceNode, destinationNode);
		}
		m_expressionPanel.setText(getExprssionString());
	}
	
	private List<IPath> getPaths(ClassNode sourceNode, ClassNode destNode)
	{
		/**
		 * Get IEntityInterface objects from source and destination nodes
		 */
		IExpression expression = m_queryObject.getQuery().getConstraints().getExpression(sourceNode.getExpressionId());
		IConstraintEntity sourceEntity = expression.getConstraintEntity();
		expression = m_queryObject.getQuery().getConstraints().getExpression(destNode.getExpressionId());
		IConstraintEntity destinationEntity = expression.getConstraintEntity();
		
		AmbiguityObject obj = new AmbiguityObject(sourceEntity.getDynamicExtensionsEntity(), destinationEntity.getDynamicExtensionsEntity());
        ResolveAmbiguity resolveAmbigity = new ResolveAmbiguity(obj,m_pathFinder);
        Map<AmbiguityObject, List<IPath>> map = resolveAmbigity.getPathsForAllAmbiguities();
        return map.get(obj);
        
    }
	
	/**
	 * Method to clone the node and all its links
	 * @param node to clone
	 * @return list of Class Nodes generated by clonning ClassNodes
	 */
	private List<ClassNode> createNodeCopies(ClassNode node, int numOfCopies)
	{
		List<ClassNode> nodeList = new ArrayList<ClassNode>();
		for(int i=0; i<numOfCopies; i++)
		{
			IExpressionId  expressionId = node.getExpressionId(); // Get the expressId of node to clone
			IExpression sourceExpression = m_queryObject.getQuery().getConstraints().getExpression(expressionId);
			
			//Create copy of this node and add it to graph document
			IExpressionId newExpressionId = m_queryObject.createExpressionCopy(sourceExpression);
			IConstraintEntity constraintEntity = sourceExpression.getConstraintEntity();
			ClassNode newNode = new ClassNode();
			
			//set proper class display name 
			newNode.setDisplayName(edu.wustl.cab2b.common.util.Utility.getDisplayName(constraintEntity.getDynamicExtensionsEntity()));
			//newNode.setDisplayName(constraintEntity.getDynamicExtensionsEntity().getName());
			
			newNode.setExpressionId(m_queryObject.getQuery().getConstraints().getExpression(newExpressionId));
			newNode.setID(String.valueOf(newExpressionId.getInt()));
			newNode.setOperatorBetAttrAndAss(node.getOperatorBetAttrAndAss());
			m_document.addComponents(GraphEvent.createSingle(newNode));
			m_currentNodeList.add(newNode);
			
			// Add associations for which the current node is 
			// acting as a source node
			List associationList = node.getAssociations();
			for(int assIdx=0; assIdx < associationList.size(); assIdx++)
			{
				IExpressionId associationExpId = (IExpressionId)associationList.get(assIdx);
							
				// Get the port associated with is expression
				IGraphPort port = node.getAssociationPort(associationExpId);
				
				// Get all the links associated with this port
				IGraphLink[] links = port.getLinks();
				
				//get the destination node for this port
				IGraphNode destNode = links[0].getTargetPort().getNode();
				
				// Now add link between source and destination node
				LinkTwoNode(newNode, (ClassNode)destNode.getLookup().lookup(ClassNode.class), node.getAssociationPath(associationExpId)); // TODO actual Path Object
				newNode.setLogicalOperator(associationExpId, node.getLogicalOperator(associationExpId));
			}
			
			// Add associations for which the current node is 
			// acting as a destination node
			List<IGraphPort> ports = node.getTargetPortList();
			for(int targetPortIdx=0; targetPortIdx<ports.size(); targetPortIdx++)
			{
				// Get all the links associated with this port
				IGraphLink[] links = ports.get(targetPortIdx).getLinks();
				
				//get the destination node for this port
				ClassNode sourceNode = (ClassNode)links[0].getSourcePort().getNode().getLookup().lookup(ClassNode.class);
				
				// Now add link between source and destination node
				LinkTwoNode(sourceNode, newNode, sourceNode.getAssociationPath(expressionId)); // TODO actual Path Object
			}
			nodeList.add(newNode);
		}
		return nodeList;
	}
	
	
	/**
	 * This method link nodes if association is available between them
	 * @param sourceNode
	 * @param destNode
	 */
	private void linkNode(ClassNode sourceNode, ClassNode destNode)
	{
		// Get all the available paths between source and destination node
		List<IPath> paths = getPaths(sourceNode, destNode);
		if(paths == null || paths.size() == 0)
		{
			JOptionPane.showMessageDialog(this, "No path available/selected between source and destination categories", "Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(false == m_queryObject.isPathCreatesCyclicGraph(sourceNode.getExpressionId(), destNode.getExpressionId(), paths.get(0)))
		{
			// If user selected paths are more than 1, then makecopies of the destination node
			// and add them to UI
			List<ClassNode> destinationNodes = new ArrayList<ClassNode>();
			if(paths.size()>1)
			{
				destinationNodes = createNodeCopies(destNode, paths.size()-1);
			}
			destinationNodes.add(destNode); // put the current node also into the list 
			for(int i=0; i<destinationNodes.size(); i++)
			{
				LinkTwoNode(sourceNode, destinationNodes.get(i), paths.get(i));
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Cannot connect selected nodes as it creates cycle in the query graph", "Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	/**
	 * Method to link two nodes with given path
	 * @param sourceNode
	 * @param destNode
	 * @param path
	 */
	private void LinkTwoNode(ClassNode sourceNode, ClassNode destNode, IPath path) 
	{
		//   Update query object to have this association path set
        List<IExpressionId> intermediateExpressions = null;
		try 
		{
			intermediateExpressions = m_queryObject.addPath(sourceNode.getExpressionId(), destNode.getExpressionId(), path);
		}
		catch (CyclicException e)
		{
			JOptionPane.showMessageDialog(this, "Cannot connect nodes as it creates cycle in graph", "Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		//	From the query object get list of associations between these two node
		GraphPort sourcePort = new GraphPort();
		sourceNode.addPort(sourcePort);
		GraphPort targetPort = new GraphPort();
		destNode.addPort(targetPort);
		sourceNode.setAssociationPort(destNode.getExpressionId(), sourcePort);
		destNode.addTargetPort(targetPort);
		
		//	now create the visual link..
        GraphLink link = new GraphLink();
        link.setSourcePort((GraphPort) sourcePort);
        link.setTargetPort((GraphPort) targetPort);
        link.setTooltipText(edu.wustl.cab2b.client.ui.query.Utility.getPathDisplayString(path));
        m_document.addComponents(GraphEvent.createSingle(link));
        
        // Add association Id to the source list
        int assPosition = sourceNode.addAssociation(destNode.getExpressionId()); 
        sourceNode.setAssociationPath(destNode.getExpressionId(), path); // Set the association path object
        
        // ===================== IMPORTANT QUERY UPDATION STARTS HERE ==========
        sourceNode.setIntermediateExpressionIdsForAssociation(destNode.getExpressionId(), intermediateExpressions);
          
        // If the first association is added, put operator between attribute condition and association
        String operator;
        if(assPosition == 0)
        {
        	operator = sourceNode.getOperatorBetAttrAndAss();
        }
        else // Get the logical operator associated with previous association
        {
        	operator = sourceNode.getLogicalOperator(assPosition-1); 
        }
      
        IExpressionId destId = sourceNode.getLogicalConnectorExpressionForAssociation(destNode.getExpressionId());
        m_queryObject.setLogicalConnector(sourceNode.getExpressionId(), destId  , edu.wustl.cab2b.client.ui.query.Utility.getLogicalOperator(operator));
        
        // Put appropriate parenthesis
        if(assPosition != 0)
        {
        	IExpressionId previousExpId = sourceNode.getLogicalConnectorExpressionForAssociation((IExpressionId)(sourceNode.getAssociations().get(0)));
        	m_queryObject.addParantheses(sourceNode.getExpressionId(), previousExpId, destId);
        }
        //      ==================== IMPORTANT QUERY UPDATION ENDS HERE ===========
        // Update user interface
        m_viewController.getHelper().scheduleNodeToLayout(sourceNode);
        m_viewController.getHelper().scheduleNodeToLayout(destNode);
        m_viewController.getHelper().scheduleLinkToLayout(link);
        m_viewController.getHelper().recalculate();
	}
	
	/**
	 * Method to display details of selected node in the upper panel
	 * @param expressionId
	 */
	public void showNodeDetails(ClassNode node)
	{
		m_addLimitPanel.editAddLimitUI( m_queryObject.getQuery().getConstraints().getExpression(node.getExpressionId()));
	}
	
	/**
	 * Method to clear all the paths (Clear all the assoications)
	 */
	public void clearAllPaths()
	{
		m_viewController.clearAllPaths();
		m_expressionPanel.setText(getExprssionString());
	}
	
	/**
	 * Delete selected expression from query object
	 * @param expressionId
	 */
	public void deleteExpression(IExpressionId expressionId)
	{
		m_addLimitPanel.clearAddLimitUI(m_queryObject.getQuery().getConstraints().getExpression(expressionId));
		m_queryObject.removeExpression(expressionId);
		m_queryObject.removeExressionIdFromVisibleList(expressionId);
		//Remove node with this expressionId from the list
		for(int i=0; i<m_currentNodeList.size(); i++)
		{
			if(m_currentNodeList.get(i).getExpressionId() == expressionId)
			{
				m_currentNodeList.remove(i);
				break;
			}
		}
		m_expressionPanel.setText(getExprssionString());
	}
		
	/**
	 * Method to delete linking path between source and destination node
	 * @param sourceNode Source entity of path
	 * @param destinationNode target entity of path
	 */
	public void deletePath(ClassNode sourceNode, ClassNode destinationNode)
	{
		List<IExpressionId> expressionIds = sourceNode.getIntermediateExpressionIdsForAssociation(destinationNode.getExpressionId());
		// If the association is direct association, remove the respective association 
		if(0 == expressionIds.size())
		{
			m_queryObject.removeAssociation(sourceNode.getExpressionId(), destinationNode.getExpressionId());
		}
		else
		{
			for(int i=0; i<expressionIds.size(); i++)
			{
				m_queryObject.removeExpression(expressionIds.get(i));
			}
		}
		sourceNode.removeIntermediateExpressionIdsForAssociation(destinationNode.getExpressionId());
		m_expressionPanel.setText(getExprssionString());
	}
	
	/**
	 * Method to change logical operator of association
	 */
	public void updateLogicalOperatorForAssociation(ClassNode sourceNode, IExpressionId destinationNodeId, String operator)
	{
		IExpressionId destinationId = sourceNode.getLogicalConnectorExpressionForAssociation(destinationNodeId);
		m_queryObject.setLogicalConnector(sourceNode.getExpressionId(), destinationId, edu.wustl.cab2b.client.ui.query.Utility.getLogicalOperator(operator));
		m_expressionPanel.setText(getExprssionString());
	}
	
	/**
	 * Method to change logical operator of association
	 */
	public void updateLogicalOperatorForAttributes(ClassNode sourceNode, String operator)
	{
		IExpressionId destinationId = sourceNode.getLogicalConnectorExpressionForAssociation((IExpressionId)(sourceNode.getAssociations().get(0)));
		m_queryObject.setLogicalConnector(sourceNode.getExpressionId(), destinationId, edu.wustl.cab2b.client.ui.query.Utility.getLogicalOperator(operator));
		m_expressionPanel.setText(getExprssionString());
	}
	
	/**
	 * Cleares dag panel. Removes all the expression and associations
	 * from query object and all the objects on dag panel are cleared accordingly
	 */
	public void clearDagPanel()
	{
		m_viewController.deleteAllNodes();
		m_currentNodeList.clear();
		m_expressionPanel.setText("Current expression : ");
	}
	
	/**
	 * Method to convert expression into string representation 
	 */
	public String getExprssionString()
	{
		HashMap<IExpressionId, String> expressionToStringMap = new HashMap<IExpressionId, String>();
		HashSet<IExpressionId> expressionsCovered = new HashSet<IExpressionId>();
		for(int i=0; i<m_currentNodeList.size(); i++)
		{
			if(null == expressionToStringMap.get(m_currentNodeList.get(i).getExpressionId()))
			{
				FormExpression(expressionToStringMap, expressionsCovered, m_currentNodeList.get(i));
			}
		}
	    StringBuffer sb = new StringBuffer();
	    sb.append("<HTML>Current expression : ");
	    String nonConnectedExpressions = "";
	    int totalNonConnectedExpressions = 0;
	    for(int i=0; i<m_currentNodeList.size(); i++)
		{
	    	IExpressionId expressionId = m_currentNodeList.get(i).getExpressionId();
	    	if(expressionsCovered.contains(expressionId) == false)
	    	{
	    		if(m_currentNodeList.get(i).getAssociations().size() == 0)
	    		{
	    			nonConnectedExpressions+=expressionId.getInt();
	    			nonConnectedExpressions+=", ";
	    			totalNonConnectedExpressions++;
	    		}
	    		else
	    		{
	    			sb.append(expressionToStringMap.get(expressionId)).append("<P>");
	    		}
	    	}
		}
	    if(totalNonConnectedExpressions > 0)
	    {
	    	
	    	if(totalNonConnectedExpressions == 1)
	    	{
	    		sb.append("Expression ").append(nonConnectedExpressions.subSequence(0, nonConnectedExpressions.length()-2));
	    		sb.append(" is not connected");
	    	}
	    	else
	    	{
	    		sb.append("Expressions ").append(nonConnectedExpressions.subSequence(0, nonConnectedExpressions.length()-2));
	    		sb.append(" are not connected");
	    	}
	    }
	    sb.append("</HTML>");
	   	return sb.toString();
	}
	/**
	 * Form expression for given expression Id
	 * @param sb
	 * @param node
	 */
	private String FormExpression(HashMap<IExpressionId, String> expressionToStringMap, HashSet<IExpressionId> expressionsCovered,
			ClassNode node)
	{
		StringBuffer sb = new StringBuffer();
		int expressionId = node.getExpressionId().getInt();
		List<IExpressionId> associatedNodes = (List<IExpressionId>)node.getAssociations();
		if(associatedNodes.size() > 0)
		{
			sb.append("( [").append(expressionId).append("] ").append(node.getOperatorBetAttrAndAss().toString()).append(" ");
			if(associatedNodes.size()>1)
			{
				sb.append("( ");
			}
			for(int i=0; i<associatedNodes.size(); i++)
			{
				if(i > 0)
				{
					sb.append(" ").append(node.getLogicalOperator(i-1)).append(" ");
				}
				if(null != expressionToStringMap.get(associatedNodes.get(i)))
				{
					sb.append(expressionToStringMap.get(associatedNodes.get(i)));
				}
				else
				{
					sb.append( FormExpression(expressionToStringMap, expressionsCovered, getNode(associatedNodes.get(i))));
				}
				expressionsCovered.add(associatedNodes.get(i));
			}
			if(associatedNodes.size()>1)
			{
				sb.append(") ");
			}
			sb.append(") ");
		}
		else
		{
			sb.append("[" + expressionId + "] ");
		}
		expressionToStringMap.put(node.getExpressionId(), sb.toString());
		return sb.toString();
	}
	
	/**
	 * Get the class node object associated with this expressionId
	 * @param expressionId The expression Id for which to get object
	 * @return classNode object associated with this expression Id 
	 */
	private ClassNode getNode(IExpressionId expressionId)
	{
		for(int i=0; i<m_currentNodeList.size(); i++)
		{
			if(m_currentNodeList.get(i).getExpressionId() == expressionId)
			{
				
				return m_currentNodeList.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Method to perform auto-connect functionality
	 */
	public void performAutoConnect()
	{
		System.out.println("User clicked auto-connect functionality");
	}
}
