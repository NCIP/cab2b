package edu.wustl.cab2b.client.ui.dag;

import java.awt.Image;

import org.netbeans.graph.api.control.IGraphLinkRenderer;
import org.netbeans.graph.api.control.IGraphNodeRenderer;
import org.netbeans.graph.api.control.IGraphPortRenderer;
import org.netbeans.graph.api.model.IGraphLink;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.IGraphPort;

public class DocumentRenderer extends SimpleDocumentRenderer 
{
	public DocumentRenderer(Image image, Image portImage)
	{
		super(image, portImage);
	}
	public IGraphNodeRenderer getNodeRenderer (IGraphNode node)
	{
	        return ((GenericNode) node.getLookup ().lookup (GenericNode.class)).createNodeRenderer (helper, node);
	}

	public IGraphPortRenderer getPortRenderer (IGraphPort port) 
	{
		return super.getPortRenderer (port); // TODO
	}
	    
	 // sanjeev
	 public IGraphLinkRenderer getLinkRenderer (IGraphLink link) 
	 {
		 return super.getLinkRenderer(link);
	 }
	
}
