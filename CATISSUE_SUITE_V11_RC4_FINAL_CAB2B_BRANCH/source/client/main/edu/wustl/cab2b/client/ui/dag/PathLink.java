package edu.wustl.cab2b.client.ui.dag;

import java.util.List;

import org.netbeans.graph.api.model.builtin.GraphLink;

import edu.wustl.common.querysuite.metadata.path.IPath;

public class PathLink extends GraphLink 
{
	private IPath m_path;
	private List<Integer> m_associationList;
	private Integer destExpressionId;
	private Integer sourceExpressionId;
	public PathLink()
	{
		super();
	}
	
	public void setPath(IPath path)
	{
		m_path = path;
	}
	
	public IPath getPath()
	{
		return m_path;
	}
	
	public List<Integer> getAssociationExpressions()
	{
		return m_associationList;
	}
	
	public void setAssociationExpressions(List<Integer> expressions)
	{
		m_associationList = expressions;
	}
	
	public void setDestinationExpressionId(Integer expressionId)
	{
		destExpressionId = expressionId;
	}
	
	public Integer getDestinationExpressionId()
	{
		return destExpressionId;
	}
	
	public void setSourceExpressionId(Integer expressionId)
	{
		sourceExpressionId = expressionId;
	}
	
	public Integer getSourceExpressionId()
	{
		return sourceExpressionId;
	}
	public Integer getLogicalConnectorExpressionId()
	{
		if(m_associationList.size() > 0)
		{
			return m_associationList.get(0);
		}
		else
		{
			return destExpressionId;
		}
	}
}
