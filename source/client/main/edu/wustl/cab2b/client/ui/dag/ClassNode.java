package edu.wustl.cab2b.client.ui.dag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.IGraphNodeRenderer;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.IGraphPort;

import edu.wustl.cab2b.client.ui.util.ClientConstants;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

/**
 * This represents one node in DAG.
 * 
 * @author Mahesh Iyer
 * @author Pratibha Dhok
 */
public class ClassNode extends GenericNode {
	private IExpression expression;

	// Map holding association expression to logical operator map
	private Map<IGraphPort, String> associationToLogicalOperatorMap;

	// Ordered list of target ports
	private List<IGraphPort> targetPorts;

	// Ordered lsit of source ports
	private List<IGraphPort> sourcePorts;

	// Source Port to link map
	private Map<IGraphPort, PathLink> sourcePortToPathLinkMap;

	private String operatorBetweenAttrAndAssociation;

	private ClassNodeRenderer classNodeRenderer;

	private ClassNodeType type = ClassNodeType.ConstraintOnlyNode;

	/**
	 * Default constructor
	 */
	public ClassNode() {
		associationToLogicalOperatorMap = new HashMap<IGraphPort, String>();
		targetPorts = new ArrayList<IGraphPort>();
		sourcePorts = new ArrayList<IGraphPort>();
		sourcePortToPathLinkMap = new HashMap<IGraphPort, PathLink>();
		setOperatorBetAttrAndAss(ClientConstants.OPERATOR_AND);
	}

	/**
	 * Method to add a port to target port list.
	 * 
	 * @param port
	 *            port to be added
	 */
	public void addTargetPort(IGraphPort port) {
		targetPorts.add(port);
	}

	/**
	 * Method to get target port list.
	 * 
	 * @return List of IGraphPort
	 */
	public List<IGraphPort> getTargetPortList() {
		return targetPorts;
	}

	/**
	 * Method to add source port in source Port list.
	 * 
	 * @param port
	 *            port to add
	 */
	public int addSourcePort(IGraphPort port) {
		sourcePorts.add(port);
	//	associationToLogicalOperatorMap.put(port, ClientConstants.OPERATOR_AND);
		return (sourcePorts.size() - 1);
	}

	/**
	 * Method to source port list
	 * 
	 * @return List of IGraphPort
	 */
	public List<IGraphPort> getSourcePorts() {
		return sourcePorts;
	}

	/**
	 * Method to get source port at specified location
	 * 
	 * @param port
	 *            port at given location.
	 */
	public IGraphPort getSourcePortAt(int i) {
		return sourcePorts.get(i);
	}

	/**
	 * Method to remove given port from source port list.
	 * 
	 * @param port
	 *            Port to remove
	 */
	public void removeSourcePort(IGraphPort port) {
		associationToLogicalOperatorMap.remove(port);
		sourcePortToPathLinkMap.remove(port);
		sourcePorts.remove(port);
	}

	/**
	 * Sets given like to given source port
	 * 
	 * @param port
	 *            source port
	 * @param link
	 *            PathLink to set
	 */
	public void setLinkForSourcePort(IGraphPort port, PathLink link) {
		sourcePortToPathLinkMap.put(port, link);
	}

	/**
	 * @param port
	 *            source port
	 * @return PathLink for given source port
	 */
	public PathLink getLinkForSourcePort(IGraphPort port) {
		return sourcePortToPathLinkMap.get(port);
	}

	/**
	 * Method to remove given port from target port list.
	 * 
	 * @param port
	 *            Port to remove
	 */
	public void deleteTargetPort(IGraphPort port) {
		targetPorts.remove(port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.client.ui.dag.GenericNode#createNodeRenderer(org.netbeans.graph.api.control.GraphHelper,
	 *      org.netbeans.graph.api.model.IGraphNode, boolean)
	 */
	public IGraphNodeRenderer createNodeRenderer(GraphHelper helper, IGraphNode node,
			boolean isForView) {
		classNodeRenderer = new ClassNodeRenderer(helper, node, isForView);
		return classNodeRenderer;
	}

	/**
	 * @param expression
	 *            Expression to set
	 */
	public void setExpressionId(IExpression expression) {
		this.expression = expression;
	}

	/**
	 * @return The expression id associted with the instance.
	 */
	public IExpressionId getExpressionId() {
		return expression.getExpressionId();
	}

	/**
	 * Sets given operator for given port.
	 * 
	 * @param port
	 *            IGraphPort
	 * @param operator
	 *            to set
	 */
	public void setLogicalOperator(IGraphPort port, String operator) {
		associationToLogicalOperatorMap.put(port, operator);
	}

	/**
	 * @param port
	 *            Given port
	 * @return Logical operator for given port
	 */
	public String getLogicalOperator(IGraphPort port) {
		return associationToLogicalOperatorMap.get(port);
	}

	/**
	 * Sets given logical operator for the source port present at given index
	 * 
	 * @param associationIdx
	 *            source port index
	 * @param operator
	 *            operator to set
	 */
	public void setLogicalOperator(int associationIdx, String operator) {
		associationToLogicalOperatorMap.put(sourcePorts.get(associationIdx), operator);
	}

	/**
	 * @return
	 */
	public String getOperatorBetAttrAndAss() {
		return operatorBetweenAttrAndAssociation;
	}

	/**
	 * @param operator
	 *            Operator between attribute and association
	 */
	public void setOperatorBetAttrAndAss(String operator) {
		operatorBetweenAttrAndAssociation = operator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.netbeans.graph.api.model.ability.IDisplayable#getTooltipText()
	 */
	public String getTooltipText() {
		StringBuffer sb = new StringBuffer();
		IRule rule = null;
		if (!((IExpression) expression).containsRule()) {
			return "";
		} else {
			rule = (IRule) expression.getOperand(0);
		}
		int totalConditions = rule.size();
		String name = classNodeRenderer.getNameForToolTip();
		// Populate panels with corresponding value
		sb.append("<HTML>");
		sb.append("<P> Condition(s) on  ").append(name).append("<P>");
		for (int i = 0; i < totalConditions; i++) {
			ICondition condition = rule.getCondition(i);
			sb.append((i + 1)).append(") ");

			String formattedAttributeName = CommonUtils.getFormattedString(condition.getAttribute()
					.getName());

			sb.append("<B>").append(formattedAttributeName).append("</B>").append(" ");
			List<String> values = condition.getValues();
			RelationalOperator operator = condition.getRelationalOperator();
			sb.append(
					edu.wustl.cab2b.client.ui.query.Utility
							.displayStringForRelationalOperator(operator)).append(" ");
			int size = values.size();
			if (size > 0)// Special case for 'Not Equals and Equals
			{
				if (size == 1) {
					sb.append("<B>").append(values.get(0)).append("</B>");
				} else {
					sb.append("( <B>");
					if (values.get(0).indexOf(",") != -1) {
						sb.append("\"");
						sb.append(values.get(0));
						sb.append("\"");
					} else {
						sb.append(values.get(0));
					}
					for (int j = 1; j < size; j++) {
						sb.append(", ");
						if (values.get(j).indexOf(",") != -1) {
							sb.append("\"");
							sb.append(values.get(j));
							sb.append("\"");
						} else {
							sb.append(values.get(j));
						}
					}
					sb.append("</B> )");
				}
			}
			sb.append("<P>");
		}
		sb.append("</HTML>");
		return sb.toString();
	}

	/**
	 * @return The type of this class node
	 */
	public ClassNodeType getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type of this class node.
	 */
	public void setType(ClassNodeType type) {
		this.type = type;
	}
}
