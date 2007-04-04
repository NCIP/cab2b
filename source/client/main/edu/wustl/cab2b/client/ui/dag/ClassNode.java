package edu.wustl.cab2b.client.ui.dag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.IGraphNodeRenderer;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.IGraphPort;

import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

public class ClassNode extends GenericNode 
{
	private IExpressionId m_expressionId;
	private IExpression m_expression;
	private List<IExpressionId> m_associationList;
	// Map holding association expression to logical operator map
	private HashMap<IExpressionId, String> m_associationToLogicalOperatorMap;
	// Map holding association expression to port map
	private HashMap<IExpressionId, IGraphPort> m_associationToPortMap;
	// Map to hold association expression to association Path object
	private HashMap<IExpressionId, IPath> m_associationToPathMap;
	//	Map to hold association expression to association Path object
	private HashMap<IExpressionId, List<IExpressionId>> m_associationToIntermediateExpressionIdsMap;
	
	private List<IGraphPort> targetPorts;
	private String m_OperatorBetweenAttrAndAss;
	private static final String ANDOPERATOR = "AND";
	private static final String OROPERATOR = "OR";
	
	public ClassNode()
	{
		m_associationList = new ArrayList<IExpressionId>();
		m_associationToLogicalOperatorMap = new HashMap<IExpressionId, String>();
		m_associationToPortMap = new HashMap<IExpressionId, IGraphPort>();
		targetPorts = new ArrayList<IGraphPort> ();
		m_associationToPathMap = new HashMap<IExpressionId, IPath> ();
		m_associationToIntermediateExpressionIdsMap = new HashMap<IExpressionId, List<IExpressionId>>();
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
	
	public void deleteTargetPortFromList(IGraphPort port)
	{
		targetPorts.remove(port);
	}
	public void setAssociationPort(IExpressionId expressionId, IGraphPort port)
	{
		m_associationToPortMap.put(expressionId, port);
	}
	
	public IGraphPort getAssociationPort(IExpressionId expressionId)
	{
		return m_associationToPortMap.get(expressionId);
	}
	public IGraphNodeRenderer createNodeRenderer(GraphHelper helper, IGraphNode node) 
	{
		 return new ClassNodeRenderer (helper, node); // sanjeev
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
	/**
	 * Method to add association expression to list and returns the postion of
	 * added association in list
	 * @param expressionId
	 * @return
	 */
	public int addAssociation(IExpressionId expressionId)
	{
		m_associationList.add(expressionId);
		m_associationToLogicalOperatorMap.put(expressionId, OROPERATOR);
		return (m_associationList.size()-1);
	}
	
	public void removeAssociation(IExpressionId expressionId)
	{
		m_associationToLogicalOperatorMap.remove(expressionId);
		m_associationToPortMap.remove(expressionId);
		m_associationToPathMap.remove(expressionId);
		m_associationList.remove(expressionId);
	}
	
	public List getAssociations()
	{
		return m_associationList;
	}
	
	public void setLogicalOperator(IExpressionId expressionId, String operator)
	{
		m_associationToLogicalOperatorMap.put(expressionId, operator);
	}
	
	public String getLogicalOperator(IExpressionId expressionId)
	{
		return m_associationToLogicalOperatorMap.get(expressionId);
	}
	
	public void setLogicalOperator(int associationIdx, String operator)
	{
		m_associationToLogicalOperatorMap.put(m_associationList.get(associationIdx), operator);
	}
	
	public String getLogicalOperator(int associationIdx)
	{
		return m_associationToLogicalOperatorMap.get(m_associationList.get(associationIdx));
	}
	public String getOperatorBetAttrAndAss()
	{
		return m_OperatorBetweenAttrAndAss;
	}
	
	public void setOperatorBetAttrAndAss(String operator)
	{
		m_OperatorBetweenAttrAndAss = operator;
	}
	
	public void setAssociationPath(IExpressionId expressionId, IPath path)
	{
		m_associationToPathMap.put(expressionId, path);
	}
	
	public IPath getAssociationPath(IExpressionId expressionId)
	{
		return m_associationToPathMap.get(expressionId);
	}
	/**
	 * Method to set intermediate exprssion Ids for a path 
	 * @param expressionId
	 * @param expressionIdList
	 */
	public void setIntermediateExpressionIdsForAssociation(IExpressionId expressionId, List<IExpressionId> expressionIdList)
	{
		m_associationToIntermediateExpressionIdsMap.put(expressionId, expressionIdList);
	}
	
	public List<IExpressionId> getIntermediateExpressionIdsForAssociation(IExpressionId expressionId)
	{
		return m_associationToIntermediateExpressionIdsMap.get(expressionId);
	}
	
	public IExpressionId getLogicalConnectorExpressionForAssociation(IExpressionId expressionId)
	{
		List<IExpressionId> list = m_associationToIntermediateExpressionIdsMap.get(expressionId);
		if(list.size() == 0)
			return expressionId;
		else
			return list.get(0);
	}
	public void removeIntermediateExpressionIdsForAssociation(IExpressionId expressionId)
	{
		m_associationToIntermediateExpressionIdsMap.remove(expressionId);
	}
	
	public String getTooltipText()
	{
		StringBuffer sb = new StringBuffer();
		IRule rule = (IRule)m_expression.getOperand(0);
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
}
