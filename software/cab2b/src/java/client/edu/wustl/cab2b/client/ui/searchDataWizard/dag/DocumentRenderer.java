/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import java.awt.Image;

import org.netbeans.graph.api.control.IGraphLinkRenderer;
import org.netbeans.graph.api.control.IGraphNodeRenderer;
import org.netbeans.graph.api.control.IGraphPortRenderer;
import org.netbeans.graph.api.model.IGraphLink;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.IGraphPort;

public class DocumentRenderer extends SimpleDocumentRenderer 
{
	private boolean isForView= false;
	public DocumentRenderer(Image image, Image portImage, boolean forView)
	{
		super(image, portImage);
		isForView = forView;
	}
	public IGraphNodeRenderer getNodeRenderer (IGraphNode node)
	{
	        return ((GenericNode) node.getLookup ().lookup (GenericNode.class)).createNodeRenderer (helper, node, isForView);
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
