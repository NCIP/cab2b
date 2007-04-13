package edu.wustl.cab2b.client.ui.dag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.IGraphNodeRenderer;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.IGraphPort;

import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

public class ClassNode extends GenericNode 
{
	private IExpressionId m_expressionId;
	private IExpression m_expression;

	// Map holding association expression to logical operator map
	private HashMap<IGraphPort, String> m_associationToLogicalOperatorMap;
	
	// Ordered list of target ports
	private List<IGraphPort> targetPorts;
	
	// Ordered lsit of source ports
	private List<IGraphPort> sourcePorts;
	
	// Source Port to link map
	private HashMap<IGraphPort, PathLink> m_sourcePortToPathLinkMap;
	
	private String m_OperatorBetweenAttrAndAss;
	public static final String ANDOPERATOR = "AND";
	public static final String OROPERATOR = "OR";
	private ClassNodeType type = ClassNodeType.ConstraintOnlyNode;
	
	public ClassNode()
	{
	
		m_associationToLogicalOperatorMap = new HashMap<IGraphPort, String>();
		targetPorts = new ArrayList<IGraphPort> ();
		sourcePorts = new ArrayList<IGraphPort> ();
		m_sourcePortToPathLinkMap =  new HashMap<IGraphPort, PathLink>();
		
		m_OperatorBetweenAttrAndAss = ANDOPERATOR;
	}
	
	public void addTargetPort(IGraphPort port)
	{
		targetPorts.add(port);
	}
	
	public List<IGraphPort> getTargetPortList()
	{
		return targetPorts;
	}
	
	/**
	 * Method to added source port in source Port list
	 * @param port
	 */
	public int addSourcePort(IGraphPort port)
	{
		sourcePorts.add(port);
		m_associationToLogicalOperatorMap.put(port, OROPERATOR);
		return (sourcePorts.size()-1);
	}
	
	/**
	 * Method to get source Ports added to this list
	 * @return
	 */
	public List<IGraphPort> getSourcePorts()
	{
		return sourcePorts;
	}
	
	/**
	 * Method to get source port at specified location
	 * @param port
	 */
	public IGraphPort getSourcePortAt(int i)
	{
		return sourcePorts.get(i);
	}
	
	public void removeSourcePort(IGraphPort port)
	{
		m_associationToLogicalOperatorMap.remove(port);
		m_sourcePortToPathLinkMap.remove(port);
		sourcePorts.remove(port);
	}
	
	public void setLinkForSourcePort(IGraphPort port, PathLink link)
	{
		m_sourcePortToPathLinkMap.put(port, link);
	}
	
	public PathLink getLinkForSourcePort(IGraphPort port)
	{
		return m_sourcePortToPathLinkMap.get(port);
	}
	
	public void deleteTargetPort(IGraphPort port)
	{
		targetPorts.remove(port);
	}
	public IGraphNodeRenderer createNodeRenderer(GraphHelper helper, IGraphNode node, boolean isForView) 
	{
		 return new ClassNodeRenderer (helper, node, isForView);
	}
	
	public void setExpressionId(IExpression expression)
	{
		m_expression = expression;
		m_expressionId = expression.getExpressionId();
	}
	
	public IExpressionId getExpressionId()
	{
		return m_expressionId;
	}
	
	public void setLogicalOperator(IGraphPort port, String operator)
	{
		m_associationToLogicalOperatorMap.put(port, operator);
	}
	
	public String getLogicalOperator(IGraphPort port)
	{
		return m_associationToLogicalOperatorMap.get(port);
	}
	
	public void setLogicalOperator(int associationIdx, String operator)
	{
		m_associationToLogicalOperatorMap.put(sourcePorts.get(associationIdx), operator);
	}
	/*
	public String getLogicalOperator(int associationIdx)
	{
		return m_associationToLogicalOperatorMap.get(sourcePorts.get(associationIdx));
	}*/
	public String getOperatorBetAttrAndAss()
	{
		return m_OperatorBetweenAttrAndAss;
	}
	
	public void setOperatorBetAttrAndAss(String operator)
	{
		m_OperatorBetweenAttrAndAss = operator;
	}
	
	public String getTooltipText()
	{
		StringBuffer sb = new StringBuffer();
		IRule rule = null;
		if(!((IExpression)m_expression).containsRule())
		{
			return "";
		}
		else
		{
			rule = (IRule)m_expression.getOperand(0);
		}
		int totalConditions = rule.size();
		// Populate panels with corresponding value
		sb.append("<HTML>");
		sb.append("<P> Attribute Conditions : <P>");
		for(int i=0; i<totalConditions; i++)
		{
			ICondition condition = rule.getCondition(i);
			sb.append((i+1)).append(") ");
			
			String formattedAttributeName =  CommonUtils.getFormattedString(condition.getAttribute().getName());
			
			sb.append("<B>").append(formattedAttributeName).append("</B>").append(" ");
			List<String> values = condition.getValues();
			RelationalOperator operator = condition.getRelationalOperator();
			sb.append(edu.wustl.cab2b.client.ui.query.Utility.displayStringForRelationalOperator(operator)).append(" ");
			int size = values.size();
			if(size>0)// Special case for 'Not Equals and Equals
			{
				if(size == 1)
				{
					sb.append("<B>").append(values.get(0)).append("</B>");
				}
				else
				{
					sb.append("( <B>");
					if(values.get(0).indexOf(",") != -1)
					{
						sb.append("\"");
						sb.append(values.get(0));
						sb.append("\"");
					}
					else
					{	
						sb.append(values.get(0));
					}
					for(int j=1; j<size; j++)
					{
						sb.append(", ");
						if(values.get(j).indexOf(",") != -1)
						{
							sb.append("\"");
							sb.append(values.get(j));
							sb.append("\"");
						}
						else
						{	
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
	public ClassNodeType getType()
	{
		return type;
	}
	public void setType(ClassNodeType type)
	{
		this.type = type;
	}
}
