package edu.wustl.cab2b.client.ui.dag;

import java.util.List;

import org.netbeans.graph.api.model.builtin.GraphLink;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.IExpressionId;

public class PathLink extends GraphLink 
{
	private IPath m_path;
	private List<IExpressionId> m_associationList;
	private IExpressionId destExpressionId;
	private IExpressionId sourceExpressionId;
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
	
	public List<IExpressionId> getAssociationExpressions()
	{
		return m_associationList;
	}
	
	public void setAssociationExpressions(List<IExpressionId> expressions)
	{
		m_associationList = expressions;
	}
	
	public void setDestinationExpressionId(IExpressionId expressionId)
	{
		destExpressionId = expressionId;
	}
	
	public IExpressionId getDestinationExpressionId()
	{
		return destExpressionId;
	}
	
	public void setSourceExpressionId(IExpressionId expressionId)
	{
		sourceExpressionId = expressionId;
	}
	
	public IExpressionId getSourceExpressionId()
	{
		return sourceExpressionId;
	}
	public IExpressionId getLogicalConnectorExpressionId()
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
